USE intertrain;

-- 1.	A empresa contém um conjunto de clientes que fazem reservas de viagens de comboios.
SELECT C.NrCliente, C.Nome, V.idViagem AS 'Ref da viagem', V.LocalidadeOrigem AS 'Origem', V.LocalidadeDestino AS 'Destino'
	FROM cliente C
	INNER JOIN reserva R ON C.NrCliente = R.Cliente
	INNER JOIN viagem V ON R.Viagem = V.idViagem
    ORDER BY C.NrCliente,V.idViagem;

-- 2.	Um cliente para fazer uma reserva, tem que se autenticar com o email e a sua password na aplicação de reservas da empresa.

SELECT NrCliente, Email, Password
	FROM cliente C;
        
-- 3.	Cada cliente paga uma reserva para uma viagem. Um cliente é sempre passageiro da viagem que compra.

SELECT Cliente, Viagem, Valor
	FROM reserva AS R
	ORDER BY R.Cliente, R.Viagem;

-- 4.	Para efeitos de faturação, a empresa deverá saber o número de contribuinte e número do CC de cada um dos seus clientes,
-- 		a data em que cada reserva foi emitida e o respetivo preço.

SELECT C.NrCliente, C.NIF, C.CC, R.DataEmissao AS 'Data de emissão da reserva', R.Valor
	FROM cliente C
    INNER JOIN reserva R ON R.Cliente = C.NrCliente
    ORDER BY C.NrCliente,R.DataEmissao;

-- 5.	A empresa deseja ter a possibilidade de avisar os seus clientes sobre viagens que têm reservadas que se realizarão num futuro próximo.
--      Para tal, deve ser possível gerar uma lista das próximas viagens e avisar todos os clientes com reservas para essa viagem. 
--      Para a notificação ao cliente das viagens a empresa deseja saber o nome do cliente de forma a poder personalizar as mensagens de 
--      comunicação. A empresa deseja notificar os seus clientes por telemóvel e email.

SELECT C.Nome, C.Telefone, C.Email, R.DataEmissao, V.idViagem, V.Data
	FROM cliente C
    INNER JOIN reserva R ON C.NrCliente = R.Cliente
    INNER JOIN viagem V ON R.Viagem = V.idViagem
    WHERE DATEDIFF(V.Data, CURDATE()) BETWEEN 0 AND 120;
    
-- 6.	A empresa deseja ainda saber um pouco mais sobre os seus clientes por forma a poder melhorar os seus serviços. 
--      Para tal a empresa considera importante saber a idade dos seus clientes e o sexo.

SELECT NrCliente, Sexo, TIMESTAMPDIFF(YEAR, DN, CURDATE()) AS 'Idade'
	FROM cliente;

-- 7.	A empresa deseja poder saber em que alturas do ano mais reservas de viagens são emitidas.

SELECT MONTH(R.DataEmissao) AS 'Mês de emissão', (COUNT(*)) AS 'Número de reservas'
	FROM reserva R
    GROUP BY MONTH(R.DataEmissao)
    ORDER BY COUNT(*) DESC;

-- 8.	O preço de uma reserva é dado pelo preço base que todas as viagens têm associado, eventualmente com a possibilidade de um desconto.

SELECT R.idReserva, C.Nome, R.Valor, V.Preco, R.Desconto, L.Classe, R.Viagem
	FROM reserva R
    INNER JOIN cliente C ON R.Cliente = C.NrCliente
    INNER JOIN viagem V ON R.Viagem = V.idViagem
    INNER JOIN lugar L ON (R.NrLugar = L.Nr AND R.Carruagem = L.Carruagem AND L.Comboio = V.Comboio)
    ORDER BY R.idReserva;

-- 9.	Uma viagem tem uma localidade de origem e destino, uma duração e é sempre feita por um comboio.

SELECT V.idViagem, V.LocalidadeOrigem AS 'Origem', V.LocalidadeDestino 'Destino', V.Duracao
	FROM viagem V;

-- 10.	A informação dos locais de origem e destino da viagem assim como a hora de partida e chegada deverão constar nas informações 
--      de cada viagem.

SELECT idViagem, LocalidadeOrigem AS 'Origem', HoraOrigem AS 'Partida', LocalidadeDestino 'Destino', HoraDestino AS 'Chegada', Duracao
	FROM viagem;

-- 11.	A empresa deseja poder reajustar o conjunto de viagens que oferece com base em alguns dados estatísticos sobre as viagens.
--      Para isso é necessário gerar dados sobre o número de viagens para cada localidade.

SELECT LocalidadeDestino AS 'Destino', COUNT(*) AS 'Número de viagens'
	FROM viagem
    GROUP BY LocalidadeDestino
    ORDER BY COUNT(*) DESC, LocalidadeDestino DESC;

-- 12.	A empresa deseja ter informação sobre o tipo de comboio, por forma a escolher o comboio que melhor se adapta às necessidades
--      de cada viagem.

SELECT idComboio, Tipo
	FROM comboio C;

-- 13.	O cliente deve ter a capacidade de poder escolher o lugar que mais lhe agrada para uma determinada viagem. Para tal deve ter
--      acesso à lista de lugares que ainda não foram reservados numa dada viagem e escolher um desses lugares. O mais importante para
--      os clientes na escolha do lugar é a classe em que vão viajar assim como saber se esse lugar se encontra à janela, se tem 
--      tomada elétrica disponível e se tem mesa.

CALL lugares_livres(1);

-- 14.	É necessário que haja a possibilidade de obter uma lista com os clientes que reservaram uma viagem e o número do lugar,
--      carruagem e comboio destinado a cada um;

SELECT R.Cliente, V.Comboio, R.NrLugar, R.Carruagem
	FROM viagem V
    INNER JOIN reserva R ON V.idViagem = R.Viagem
    WHERE V.idViagem = 1;

-- 15.	A empresa deseja poder saber quanto faturou até ao momento com o total das reservas e em cada viagem;

SELECT Viagem, ROUND(SUM(Valor), 2) AS 'Total faturado (€)'
	FROM reserva
    GROUP BY Viagem;

-- 16.	Gerar estatísticas sobre a taxa de ocupação de cada viagem, relativamente à capacidade de um comboio;

SELECT V.idViagem, V.LocalidadeOrigem AS 'Origem', V.LocalidadeDestino AS 'Destino', COUNT(*) AS 'Número de Reservas', 
	   ROUND(COUNT(*)/(SELECT COUNT(*) FROM lugar L WHERE L.Comboio = V.Comboio) * 100) AS 'Taxa de ocupação (%)'
	FROM viagem V
    INNER JOIN reserva R ON V.idViagem = R.Viagem
    GROUP BY V.idViagem;
    
SELECT X.idViagem, X.Origem, X.Destino, NR AS 'Número de Reservas', Lot AS 'Lotação do Comboio', ROUND(NR/Lot * 100) AS 'Ocupação (%)'
	FROM
    (SELECT V.idViagem, V.LocalidadeOrigem AS 'Origem', V.LocalidadeDestino AS 'Destino', COUNT(*) AS 'NR'
		FROM viagem V
        INNER JOIN reserva R ON V.idViagem = R.Viagem
        GROUP BY V.idViagem) AS X
	INNER JOIN
	(SELECT V.idViagem, COUNT(*) AS 'Lot'
		FROM viagem V
        INNER JOIN comboio C ON V.Comboio = C.idComboio
        INNER JOIN lugar L ON C.idComboio = L.Comboio
        GROUP BY V.idViagem) AS Y
	ON X.idViagem = Y.idViagem;  
    
-- 17.	Saber o número de reservas que obtiveram um desconto.
SELECT COUNT(*) AS 'Nº Reservas'
	FROM reserva
	WHERE Desconto > 0;
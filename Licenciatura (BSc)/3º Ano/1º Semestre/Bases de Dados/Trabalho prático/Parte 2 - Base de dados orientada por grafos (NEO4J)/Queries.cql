// 1. A empresa contém um conjunto de clientes que fazem reservas de viagens de comboios.

MATCH (c:Cliente) -[:FEZ]-> (r:Reserva) -[:RELATIVA]-> (v:Viagem)
RETURN c.NrCliente, c.Nome, v.idViagem AS `Ref da viagem`, v.LocalidadeOrigem AS Origem, v.LocalidadeDestino AS Destino
ORDER BY c.NrCliente, v.idViagem;

// 2. Um cliente para fazer uma reserva, tem que se autenticar com o email e a sua password na aplicação de reservas da empresa.

MATCH (c:Cliente)
RETURN c.NrCliente, c.Email, c.Password;

// 3. Cada cliente paga uma reserva para uma viagem. Um cliente é sempre passageiro da viagem que compra.

MATCH (c:Cliente) -[:FEZ]-> (r:Reserva) -[:RELATIVA]-> (v:Viagem)
RETURN c.NrCliente, v.idViagem AS `Ref da viagem`, r.Valor
ORDER BY c.NrCliente, v.idViagem;

// 4. Para efeitos de faturação, a empresa deverá saber o número de contribuinte e número do CC de cada um dos seus clientes,
//    a data em que cada reserva foi emitida e o respetivo preço.

MATCH (c:Cliente) -[:FEZ]-> (r:Reserva)
RETURN c.NrCliente, c.NIF, c.CC, r.DataEmissao AS `Data de emissão da Reserva`, r.Valor
ORDER BY c.NrCliente, r.DataEmissao;

// 5. A empresa deseja ter a possibilidade de avisar os seus clientes sobre viagens que têm reservadas que se realizarão num futuro próximo. 
//    Para tal, deve ser possível gerar uma lista das próximas viagens e avisar todos os clientes com reservas para essa viagem. 
//    Para a notificação ao cliente das viagens a empresa deseja saber o nome do cliente de forma a poder personalizar as mensagens de comunicação. 
//    A empresa deseja notificar os seus clientes por telemóvel e email.

MATCH (c:Cliente) -[:FEZ]-> (r:Reserva) -[:RELATIVA]-> (v:Viagem)
WITH v.Data - timestamp() AS timediff, c, r, v
WHERE timediff > 0 AND timediff < 120*24*60*60*1000
RETURN c.Nome, c.Telefone, c.Email, r.DataEmissao, v.idViagem, v.Data
ORDER BY c.Nome;

// 6. A empresa deseja ainda saber um pouco mais sobre os seus clientes por forma a poder melhorar os seus serviços. 
//    Para tal a empresa considera importante saber a idade dos seus clientes e o sexo.

MATCH (c:Cliente)
WITH timestamp() - c.DN AS timediff, c
RETURN c.NrCliente, c.Sexo, timediff/(1000*60*60*24*365) AS Idade;

// 7. A empresa deseja poder saber em que alturas do ano mais reservas de viagens são emitidas.

MATCH (r:Reserva)
RETURN r.DataEmissao;
/////////// A Aplicação terá que fazer a conversão e contar

// 8. O preço de uma reserva é dado pelo preço base que todas as viagens têm associado, eventualmente com a possibilidade de um desconto.

MATCH (l:Lugar) -[:RESERVADO]-> (r:Reserva) -[:RELATIVA]-> (v:Viagem)
MATCH (r:Reserva) <-[:FEZ]- (c:Cliente)
RETURN r.idReserva, c.Nome,  r.Valor, v.Preco, r.Desconto, l.Classe, v.idViagem
ORDER BY (r.idReserva) ASC;

// 9. Uma viagem tem uma localidade de origem e destino, uma duração e é sempre feita por um comboio. 

MATCH (v:Viagem)
RETURN v.idViagem, v.LocalidadeOrigem AS Origem, v.LocalidadeDestino AS Destino, v.Duracao;

// 10. A informação dos locais de origem e destino da viagem assim como a hora de partida e chegada deverão constar nas informações de cada viagem.

MATCH (v:Viagem)
RETURN v.idViagem, v.LocalidadeOrigem AS Origem, v.HoraOrigem AS Partida, v.LocalidadeDestino AS Destino, v.HoraDestino AS Chegada, v.Duracao;

// 11. A empresa deseja poder reajustar o conjunto de viagens que oferece com base em alguns dados estatísticos sobre as viagens. 
//     Para isso é necessário gerar dados sobre o número de viagens para cada localidade.

MATCH (v:Viagem)
RETURN v.LocalidadeDestino AS Destino, count(v) AS `Número de Viagens`
ORDER BY `Número de Viagens` DESC ;

// 12. A empresa deseja ter informação sobre o tipo de comboio, por forma a escolher o comboio que melhor se adapta às necessidades de cada viagem.

MATCH (c:Comboio)
RETURN c.idComboio, c.Tipo;

// 13. O cliente deve ter a capacidade de poder escolher o lugar que mais lhe agrada para uma determinada viagem. 
//     Para tal deve ter acesso à lista de lugares que ainda não foram reservados numa dada viagem e escolher um desses lugares. 
//     O mais importante para  os clientes na escolha do lugar é a classe em que vão viajar assim como saber se esse lugar se encontra à janela,
//     se tem tomada elétrica disponível e se tem mesa.

MATCH (l:Lugar) -[:RESERVADO]-> (r:Reserva) -[:RELATIVA]-> (v:Viagem {idViagem: 1})
WITH collect(l) AS lugaresReservados
MATCH (l:Lugar) -[:PERTENCE]-> (c:Comboio) <-[:REALIZADA]- (v:Viagem {idViagem: 1})
WHERE NOT l IN lugaresReservados
RETURN l.Nr, l.Carruagem, l.Classe, l.Janela, l.Tomada, l.Mesa;

// 14. É necessário que haja a possibilidade de obter uma lista com os clientes que reservaram uma viagem e o número do lugar, 
//     carruagem e comboio destinado a cada um; (Para a Viagem 1 apenas)
MATCH (cli:Cliente) -[:FEZ]-> (r:Reserva) <-[:RESERVADO]- (l:Lugar)
MATCH (c:Comboio) <-[:REALIZADA]- (v:Viagem {idViagem:1}) <-[:RELATIVA]- (r:Reserva)
RETURN cli.NrCliente, v.idViagem, c.idComboio, l.Nr AS Lugar, l.Carruagem
ORDER BY Cliente ASC;

// 15. A empresa deseja poder saber quanto faturou até ao momento com o total das reservas e em cada viagem;

MATCH (r:Reserva) -[:RELATIVA]-> (v:Viagem)
WITH sum(r.Valor) AS value, v
RETURN v.idViagem, round(100 * value) / 100 AS `Total Facturado (€)`
ORDER BY v.idViagem;

// 16. Gerar estatísticas sobre a taxa de ocupação de cada viagem, relativamente à capacidade de um comboio; 

MATCH (l:Lugar) -[:PERTENCE]-> (c:Comboio) <-[:REALIZADA]- (v:Viagem)
WITH count(l) AS Capacidade, v
MATCH (r:Reserva) -[:RELATIVA]-> (v:Viagem)
WITH count(r) AS Reservados, v, Capacidade
RETURN v.idViagem, v.LocalidadeOrigem AS Origem, v.LocalidadeDestino AS Destino, Reservados, Capacidade, round(((Reservados*1.0)/Capacidade)*100) AS `Ocupacao (%)`
ORDER BY v.idViagem;

// 17. Saber o número de reservas que obtiveram um desconto.

MATCH (c:Cliente) -[:FEZ]-> (r:Reserva) -[:RELATIVA]-> (v:Viagem)
WHERE r.Desconto > 0
RETURN r.idReserva, c.Nome AS Cliente, c.NrCliente, v.idViagem, r.Valor, r.Desconto
ORDER BY r.idReserva;

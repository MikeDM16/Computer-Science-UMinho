use cadernetacromos;

/*1) Quais os cromos que fazem parte da caderneta? Ordene-os por equipa, posição e
nome do jogador.*/
SELECT * FROM cromo
JOIN jogador ON (jogador.Nr = cromo.jogador)
JOIN equipa ON (jogador.Equipa = equipa.Id)
JOIN posicao ON (posicao.Id = jogador.posicao)
WHERE cromo.adquirido = 'S'
ORDER BY equipa.Designacao, posicao.Designacao, jogador.Nome;

/*2) Quais são os nomes dos jogadores cujos cromos ainda não foram adquiridos?*/
SELECT jogador.Nome FROM cromo
JOIN jogador ON (jogador.Nr = cromo.Jogador)
WHERE cromo.Adquirido = 'N';

/*3) Quantos cromos faltam adquirir para cada uma das equipas da coleção?*/
SELECT COUNT(jogador.nome) AS 'Nr', equipa.Designacao AS 'Equipa' FROM jogador
JOIN equipa ON (equipa.Id = jogador.Equipa)
JOIN cromo ON (cromo.jogador = jogador.Nr AND cromo.Adquirido = 'N')
GROUP BY equipa.Designacao;

SELECT COUNT(*) AS 'Em falta', Equipa.Designacao AS 'Equipa' FROM cromo
JOIN jogador ON (jogador.Nr = cromo.Jogador)
JOIN Equipa ON (equipa.Id = jogador.Equipa)
WHERE (cromo.Adquirido = 'N')
GROUP BY equipa.Designacao;

/*4) Quais são os números dos cromos que têm jogadores do ‘Clube de Futebol os
Belenenses’ e do ‘Vitória Sport Clube’?*/
SELECT cromo.Nr, Equipa.Designacao AS 'Equuipa' FROM cromo
JOIN jogador ON (jogador.Nr = cromo.Jogador)
JOIN equipa ON (Equipa.Id = jogador.Equipa)
WHERE (equipa.Designacao = 'Clube de Futebol os Belenenses' OR
		equipa.Designacao = 'Vitória Sport Clube');

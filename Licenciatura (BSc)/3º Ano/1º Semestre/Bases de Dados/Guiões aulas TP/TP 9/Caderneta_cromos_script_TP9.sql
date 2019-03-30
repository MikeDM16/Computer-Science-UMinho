use cadernetacromos;

/*1. Em que páginas da caderneta estão os jogadores do ‘Sporting Clube de Braga’ e do ‘Rio Ave
Futebol Clube’ que jogam na posição ‘Defesa’?*/
SELECT cromo.PagCaderneta FROM cromo
JOIN jogador ON (jogador.Nr = cromo.jogador)
JOIN equipa ON (equipa.Id = jogador.Equipa)
JOIN posicao ON (posicao.Id = jogador.Posicao)
WHERE (Equipa.Designacao IN ('Sporting Clube de Braga', 'Rio Ave Futebol Clube') 
		AND posicao.Designacao = 'Defesa');
        
/*2. Quais os números dos cromos dos jogadores que não jogam como ‘Médio’ ou ‘Defesa’, cujos
treinadores são ‘Jorge Jesus’ e ‘Nuno Espírito Santo’. Apresente a lista ordenada, de forma
crescente, por número de cromo.*/
SELECT cromo.Nr FROM cromo
JOIN jogador ON (jogador.Nr = cromo.jogador)
JOIN posicao ON (posicao.id = jogador.Posicao)
JOIN equipa ON (equipa.Id = jogador.Equipa)
WHERE ( (posicao.Designacao NOT IN ('Defesa','Médio')) AND
		Equipa.Treinador IN ('Jorge Jesus','Nuno Espírito Santo'))
ORDER BY (cromo.Nr) ASC;        

/*3. Definir uma vista (view) que permita apresentar a lista dos cromos em falta, apresentando o
número do cromo, nome do jogador e nome da equipa à qual pertencem.*/
CREATE VIEW cromos_em_falta AS
	SELECT cromo.Nr, jogador.Nome, Equipa.Designacao AS 'Equipa' FROM cromo
    JOIN jogador ON (jogador.Nr = cromo.Jogador)
    JOIN equipa ON (equipa.Id = jogador.Equipa)
    WHERE cromo.Adquirido = 'N';

SELECT * FROM cromos_em_falta;

DELIMITER $$
/*4. Implementar um procedimento (procedure) que, dado o nome de uma equipa apresente a
lista completa dos cromos que a ela dizem respeito, ordenando-a por número de página e
número do cromo.*/
CREATE PROCEDURE cromos_equipa(IN nome_equipa VARCHAR(45))
	BEGIN
		SELECT * from cromo
        JOIN jogador ON (jogador.Nr = cromo.jogador)
        JOIN equipa ON (equipa.Id = jogador.Equipa AND equipa.Designacao = nome_equipa)
        ORDER BY cromo.PagCaderneta, cromo.Nr ASC; 
	END
$$

CALL cromos_equipa("Boavista Futebol Clube");

DROP PROCEDURE IF EXISTS colecao_atual;
DELIMITER $$
/*5) Implementar um procedimento que apresente a caderneta completa da coleção de cromos,
indicando o número do cromo, o tipo do cromo, o nome do jogador, o nome da equipa e se
o cromo já foi ou não adquirido.*/
CREATE PROCEDURE colecao_atual ()
BEGIN
	SELECT cromo.Nr, 
		   tipocromo.Descricao AS 'Tipo Cromo', 
           jogador.Nome, 
           Equipa.Designacao AS 'Equipa',
           cromo.Adquirido FROM cromo
	JOIN tipocromo ON (tipocromo.Nr = cromo.Tipo)
    LEFT JOIN jogador ON (jogador.Nr = cromo.Jogador )
    JOIN equipa ON (equipa.Id = jogador.Equipa);
END    
$$
CALL colecao_atual;

SELECT * FROM CROMO
JOIN tipocromo ON (tipocromo.Nr = cromo.Tipo)

DROP FUNCTION IF EXISTS repetidos
/*6. Implementar uma função (function) que, dado o número de um cromo, indique se o cromo é
ou não repetido.*/
DELIMITER $$
CREATE FUNCTION Repetidos(numero_cromo INT)
RETURNS CHAR(1)
BEGIN
	DECLARE r CHAR(1);
    SET r = (SELECT * FROM cromo 
			 WHERE cromo.Nr = numero_cromo);
	RETURN r;
END
$$

DELIMITER $$
SELECT Repetidos(1);
$$


/*7. Implementar uma função (function) que, dado o número de um cromo, devolva o tipo do
cromo, o nome do jogador e o nome da equipa.*/
DROP FUNCTION informacao
DELIMITER $$
CREATE FUNCTION informacao( numero_cromo INT)
RETURNS VARCHAR(200)
BEGIN
	DECLARE tipo VARCHAR(45);
    DECLARE jogador VARCHAR(45);
    DECLARE equipa VARCHAR(45);
    
    SELECT tipocromo.Descricao INTO tipo
		FROM tipocromo
        JOIN cromo ON (cromo.Tipo = tipocromo.Nr)
			WHERE cromo.Nr = numero_cromo;
	
    SET jogador = (SELECT jogador.Nome FROM jogador
					JOIN cromo ON (cromo.Jogador = jogador.Nr)
                    WHERE cromo.Nr = numero_cromo);
	SET equipa = (SELECT equipa.Designacao FROM equipa
					JOIN jogador ON (jogador.Equipa = equipa.Id)
                    JOIN cromo ON (cromo.Jogador = jogador.Nr)
                    WHERE cromo.Nr = numero_cromo);
	RETURN tipo+equipa+jogador; 
END
$$

SELECT informacao(1);
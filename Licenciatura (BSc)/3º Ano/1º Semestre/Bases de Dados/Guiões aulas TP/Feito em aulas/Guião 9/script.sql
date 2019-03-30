use caderneta;
select * from jogador;

SELECT PagCaderneta FROM cromo
JOIN jogador ON cromo.jogador = jogador.Nr
JOIN posicao ON posicao.id = jogador.Posicao
JOIN equipa ON jogador.Equipa = equipa.Id
WHERE (equipa.Designacao = 'Sporting Clube de Braga' OR equipa.Designacao = 'Rio Ave Futebol Clube')
	AND (posicao.Designacao = 'Defesa')

-- -------------------------------------------------------

SELECT cromo.Nr, jogador.Nome FROM cromo
JOIN jogador ON jogador.Nr = cromo.Jogador
JOIN posicao ON posicao.id = jogador.Posicao
JOIN equipa ON equipa.id = jogador.Equipa
WHERE (posicao.Designacao != 'Defesa' AND posicao.Designacao != 'Médio')
	AND (equipa.Treinador = 'Jorge Jesus' OR equipa.Treinador = 'Nuno Espírito Santo')
ORDER BY (cromo.Nr) ASC

/*
CREATE PROCEDURE p_name([IN|OUT|INOUT] par_name type))
	BEGIN
    (...)
    END

CREATE FUNCTION f_name (param_name type)
RETURN type
BEGIN
(...)
END


CREATE VIEW view_name AS 
[select statement]

*/
DROP VIEW cromosFaltam

CREATE VIEW cromosFaltam AS
SELECT cromo.Nr, jogador.Nome, equipa.Designacao FROM cromo
LEFT JOIN jogador ON cromo.jogador = jogador.Nr
JOIN equipa ON jogador.Equipa = equipa.Id
WHERE (cromo.Adquirido = 'N');

SELECT * FROM cromosFaltam

DROP PROCEDURE cromosEquipa;

-- -------------------------------------------------------

DELIMITER $$
CREATE PROCEDURE cromosEquipa (IN nomeEquipa VARCHAR(75))
BEGIN
	SELECT cromo.Nr, cromo.PagCaderneta FROM cromo
    JOIN jogador ON jogador.Nr = cromo.Jogador
    JOIN equipa ON equipa.id = jogador.Equipa
    WHERE (equipa.Designacao = nomeEquipa )
    ORDER BY cromo.Nr ASC, cromo.PagCaderneta ASC;
END
$$

CALL cromosEquipa("Boavista Futebol Clube");

-- -------------------------------------------------------

DROP PROCEDURE IF EXISTS colecaoTotal;
DELIMITER $$
CREATE PROCEDURE colecaoTotal ()
BEGIN
	SELECT cromo.Nr, cromo.Tipo, jogador.Nome, equipa.Designacao, cromo.Adquirido FROM cromo
    JOIN jogador ON jogador.Nr = cromo.Jogador
    JOIN equipa ON equipa.Id = jogador.Equipa;
END
$$
CALL colecaoTotal;

-- -------------------------------------------------------

DELIMITER $$
CREATE FUNCTION `cromoAdquirido` (cromo_id INT)
RETURNS CHAR(1)
BEGIN
	DECLARE r CHAR(1);
    
    SET r = (SELECT cromo.Adquirido FROM cromo WHERE cromo.Nr = cromo_id );
   -- SELECT cromo.Adquirido INTO r
    -- FROM cromo WHERE cromo.Nr = cromo_id
    RETURN r;
END
$$
-- -------------------------------------------------------
DELIMITER $$
CREATE FUNCTION tipoCromo (cromo_id INT)
RETURNS VARCHAR(200)
BEGIN
	DECLARE tipo VARCHAR(20);
    DECLARE equipa VARCHAR(75);
    DECLARE nome VARCHAR(75);
    
    SET tipo = (SELECT tipoCromo.Descricao FROM TipoCromo
					JOIN cromo ON TipoCromo.Nr = cromo.Nr
					WHERE cromo.Nr = cromo_id);
	SET nome = (SELECT jogador.Nome FROM jogador
				JOIN cromo ON cromo.jogador = jogador.Nr
                WHERE cromo.Nr = cromo_id);
	SET equipa = (SELECT equipa.Designacao FROM equipa
				JOIN jogador ON jogador.Equipa)
END
$$

-- -------------------------------------------------------
CREATE TABLE caderneta-audCromos (
	data_registo DATETIME NOT NULL,
    cromo INT NOT NULL,
    PRIMARY KEY (data_registo, cromo))

DELIMITER $$
CREATE TRIGGER adquirido BEFORE UPDATE ON cromo
FOR EACH ROW 
BEGIN

	IF (OLD.Adquirido = 'N' AND NEW.Adquirido = 'S') 
		THEN INSERT INTO caderneta.audCromos(data_registo, cromo) VALUES
				(sysdate(), New.Nr);
	END IF;
END
$$
USE intertrain;

-- ----------------------------------------------------------------------------------
delimiter $$
CREATE PROCEDURE alterar_hora_chegada_viagem(IN p_viagem INT, IN p_novaHora TIME)
BEGIN

DECLARE novaDuracao TIME;
DECLARE horaPartida TIME;

START TRANSACTION;

SET horaPartida = (SELECT V.HoraOrigem
				       FROM viagem V
                       WHERE V.idViagem = p_viagem);

IF (p_novaHora > horaPartida) THEN SET novaDuracao = TIMEDIFF(p_novaHora, horaPartida);
							  ELSE SET novaDuracao = ADDTIME(TIMEDIFF('24:00:00',horaPartida), p_novaHora);
END IF;

UPDATE viagem V
  SET V.HoraDestino = p_novaHora, V.Duracao = novaDuracao
  WHERE V.idViagem = p_viagem;

END $$

-- ----------------------------------------------------------------------------------
delimiter $$
CREATE PROCEDURE fazer_reserva(IN viagem INT, IN cliente INT, IN desconto INT, IN nrLugar INT, IN carruagem INT)
BEGIN

DECLARE lugarOcupado INT;
DECLARE mustRollback INT DEFAULT 0;
DECLARE Erro BOOL DEFAULT 0;
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET Erro = 1;

SET autocommit = 0;
SET SQL_SAFE_UPDATES = 0;

START TRANSACTION;

SELECT COUNT(*) INTO lugarOcupado
	FROM reserva R
    WHERE R.Viagem = viagem AND R.Carruagem = carruagem AND R.NrLugar = nrLugar;

IF (lugarOcupado = 1) THEN SET mustRollback = 1; END IF;
	
INSERT INTO reserva ( `DataEmissao`, `Desconto`, `NrLugar`, `Carruagem`, `Viagem`,`Cliente`)
VALUES (CURDATE(), desconto, nrLugar, carruagem, viagem, cliente);

IF Erro OR (mustRollback = 1) THEN ROLLBACK; ELSE COMMIT; END IF;

END $$

-- ----------------------------------------------------------------------------------
delimiter $$
CREATE PROCEDURE registar_cliente(IN nome VARCHAR(64), IN sexo CHAR(1), IN dn DATE, IN cc VARCHAR(8), IN nif VARCHAR(9),
								  IN telefone VARCHAR(15), IN email VARCHAR(128), IN password VARCHAR(15))
BEGIN

START TRANSACTION;

INSERT INTO cliente VALUES (nome, sexo, dn, cc, nif, telefone, email, password);

END $$

-- ----------------------------------------------------------------------------------
delimiter &&
CREATE PROCEDURE lugares_livres(IN viagem INT)
BEGIN

SELECT L.Nr, L.Carruagem, L.Classe, IF(L.Janela,'Sim','Não') AS 'Janela', IF(L.Tomada,'Sim','Não') AS 'Tomada', IF(L.Mesa,'Sim','Não') AS 'Mesa'
	FROM lugar L
    INNER JOIN comboio C ON L.Comboio = C.idComboio
    INNER JOIN viagem V ON C.idComboio = V.Comboio
    WHERE V.idViagem = viagem AND 
		(L.Nr, L.Carruagem) NOT IN (SELECT R.NrLugar, R.Carruagem
										FROM reserva R
                                        INNER JOIN viagem V ON R.Viagem = V.idViagem
                                        WHERE V.idViagem = viagem);
                                        
END;
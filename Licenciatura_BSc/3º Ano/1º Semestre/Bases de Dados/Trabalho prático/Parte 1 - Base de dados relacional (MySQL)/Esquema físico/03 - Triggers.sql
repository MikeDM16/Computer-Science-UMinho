USE intertrain;

delimiter &&

CREATE TRIGGER define_duracao BEFORE INSERT ON viagem
FOR EACH ROW

BEGIN

DECLARE duracao TIME;

SET SQL_SAFE_UPDATES = 0;

IF (NEW.HoraDestino > NEW.HoraOrigem) THEN SET duracao = TIMEDIFF(NEW.HoraDestino, NEW.HoraOrigem);
									  ELSE SET duracao = ADDTIME(TIMEDIFF('24:00:00',NEW.HoraOrigem), NEW.HoraDestino);
END IF;

SET NEW.Duracao = duracao;

END; &&

-- ----------------------------------------------------------------------------------
delimiter &&

CREATE TRIGGER define_preco BEFORE INSERT ON reserva
FOR EACH ROW

BEGIN

DECLARE precoV FLOAT;
DECLARE classeV INT;
DECLARE comboioV INT;

SET SQL_SAFE_UPDATES = 0;

SET precoV = (SELECT V.Preco
			     FROM viagem V
                 WHERE V.idViagem = NEW.Viagem);

SET comboioV = (SELECT C.idComboio
					FROM comboio C
					INNER JOIN viagem V ON V.Comboio = C.idComboio
                    WHERE V.idViagem = NEW.Viagem);

SET classeV = (SELECT L.Classe
					FROM lugar L
                    WHERE L.Comboio = comboioV AND L.Carruagem = NEW.Carruagem AND L.Nr = NEW.NrLugar);
                  
IF classeV = 1 THEN SET precoV = precoV * 1.30;
END IF;

SET NEW.Valor = precoV * (1 - (NEW.Desconto/100));

END; &&

-- ----------------------------------------------------------------------------------
delimiter $$

CREATE TRIGGER valida_cliente BEFORE INSERT ON cliente
FOR EACH ROW
BEGIN

IF NEW.Sexo NOT IN ('M', 'F') 
	THEN SIGNAL SQLSTATE '12345' SET MESSAGE_TEXT = 'Sexo inválido';
END IF;

END; $$

-- ----------------------------------------------------------------------------------
delimiter $$

CREATE TRIGGER valida_lugar BEFORE INSERT ON lugar
FOR EACH ROW
BEGIN

DECLARE nrCarruagens INT;

IF NEW.Classe NOT IN (1, 2) 
	THEN SIGNAL SQLSTATE '12345' SET MESSAGE_TEXT = 'Classe inválida';
END IF;

SET nrCarruagens = (SELECT Carruagens
						FROM comboio C
                        WHERE C.idComboio = NEW.Comboio);
                        
IF NEW.Carruagem > nrCarruagens
	THEN SIGNAL SQLSTATE '12345' SET MESSAGE_TEXT = 'Carruagem inválida';
END IF;

END; $$

-- ----------------------------------------------------------------------------------
delimiter $$

CREATE TRIGGER valida_comboio BEFORE INSERT ON comboio
FOR EACH ROW
BEGIN

IF NEW.Tipo NOT IN ('Intercidades', 'Alfa Pendular') 
	THEN SIGNAL SQLSTATE '12345' SET MESSAGE_TEXT = 'Tipo do comboio inválido';
END IF;

END; $$
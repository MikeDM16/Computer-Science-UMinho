--
-- Unidade Curricular de Bases de Dados.
-- Sistemas de Dados Relacionais.
--
-- I N D I C E S 
-- Operações de Descrição de Dados.
-- Criação, alteração e remoção de indices.
--
-- Exemplos de Aplicação.
-- Belo, O., 2016.
--

-- Base de Dados de Trabalho.
USE Sakila;

-- Criacao de um indice sobre o atributo "rental_date" da tabela "rental".
CREATE INDEX idx_RentalDate 
	ON rental (rental_date);

-- Criacao de um indice único sobre o atributo "email" da tabela "customer".
CREATE UNIQUE INDEX idx_eMail 
	ON customer (email);

-- Criacao de um indice composto sobre a tabela "".
CREATE INDEX idx_CustomerRental
	ON payment (customer_id, rental_id);

-- Visualizacao dos inndices definidos sobre a tabela 'rental'.
SHOW INDEX FROM rental;
SHOW INDEX FROM customer;
SHOW INDEX FROM payment;

-- Remocao de todos os indices criados anteriormente.
DROP INDEX idx_RentalDate ON rental;
DROP INDEX idx_eMail ON customer;
DROP INDEX idx_CustomerRental ON payment;

-- Visualizacao de todos os indices criados sobre as tabelas da base de dados "Sakila".
SELECT DISTINCT TABLE_NAME, INDEX_NAME
	FROM INFORMATION_SCHEMA.STATISTICS
	WHERE TABLE_SCHEMA = 'Sakila';

--
-- <fim>







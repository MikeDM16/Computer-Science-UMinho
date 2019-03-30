--
-- Unidade Curricular de Bases de Dados.
-- Sistemas de Dados Relacionais.
--
-- Q U E R I E S
-- Operações de Manipulacao de Dados.
-- Queries simples e complexas.
-- Exemplos de Aplicação.
-- Exercícios apresentados nas aulas.
-- Belo, O., 2016.
--

-- Indicação da base de dados de trabalho.
USE Sakila;

-- Consulta de todo o conteúdo da tabela "Customer"
SELECT *
	FROM Customer;

-- Consulta de todo o conteúdo da tabela "Customer", com indicação do esquema
-- à qual pertence.
SELECT *
	FROM Sakila.Customer;

--
SELECT *
	FROM Country;

--
SELECT *
	FROM Address;

-- Visualização de uma constante.
SELECT 1;

-- Realização do calculo de uma expressao matemática.
SELECT 1 + 2 * 3;

-- Utilização da funcao PI(), que nos fornece o valor da constante matemática pi.
SELECT PI();

-- Utilização da funcao SIN(), que nos fornece o seno de um dado valor.
SELECT SIN(90);

-- Visualização da string °A Linguagem SQL é um mundo!".
SELECT 'A Linguagem SQL é um mundo!' AS Obs;

-- Utilização da funcao NOW(), que nos fornece a data e a hora atuais do sistema.
SELECT NOW();

-- Utilização da funcao CURDATE(), que nos fornece a data atual do sistema.
SELECT CURDATE();

-- Uma query com seleção de atributos e critério de filtragem.
SELECT customer_id, first_name, last_name, email
	FROM Customer
	WHERE customer_id = 1;

--
SELECT first_name, last_name, email
	FROM Customer
	WHERE customer_id = 1 OR customer_id =2;

-- Utilização do operador IN
SELECT first_name, last_name, email
	FROM Customer
	WHERE customer_id IN (1,2,3,4,5);

--
SELECT customer_id, first_name, last_name, email
	FROM Customer
	WHERE customer_id >= 1 AND customer_id <= 5;

-- Utilização do operador BETWEEN
SELECT customer_id, first_name, last_name, email
	FROM Customer
	WHERE customer_id BETWEEN 1 AND 5;

--
SELECT customer_id, first_name, last_name, email, address_id
	FROM Customer
	WHERE customer_id NOT IN (1,5,22) OR
		address_id = 1;

-- Consulta de uma lista de eMails de clientes - "customer"
SELECT customer_id, first_name, last_name, email
	FROM Customer;

-- Consulta de uma lista de eMails de funcionários - "staff"
SELECT staff_id, first_name, last_name, email
	FROM Staff
	ORDER BY last_name ASC;

-- Consulta de uma lista de eMails de funcionários - "staff", com ordenação
-- dos seus resultados.
SELECT staff_id, first_name, last_name, email
	FROM Staff
	ORDER BY last_name, first_name DESC;

-- Consulta de uma lista de eMails de clientes - "customer" -  e 
-- funcionários - "staff" simultneamente.
SELECT customer_id, first_name, last_name, email
	FROM Customer
UNION
SELECT staff_id, first_name, last_name, email
	FROM Staff;

-- Complemento da query anterior com alguma "cosmética" para realce dos resultados.
SELECT 'C' AS Tipo, customer_id AS Nr, 
	first_name AS Nome, last_name AS Apelido, 
	email, '______' AS Observacoes
	FROM Customer
UNION
SELECT 'F', staff_id, first_name, last_name, email, '______'
	FROM Staff;

-- Utilização da query anterior como sub-query de uma outra, com ordenação
-- dos resultados.
SELECT *
FROM (
	SELECT 'C' AS Tipo, customer_id AS Nr, 
		first_name AS Nome, last_name AS Apelido, 
		email, '______' AS Observacoes
		FROM Customer AS C
	UNION
	SELECT 'F', staff_id, first_name, last_name, email, '______'
		FROM Staff AS S) AS T
ORDER BY Nome, Apelido ASC;

-- Utilização do operador LIKE.
SELECT *
	FROM customer
	WHERE email LIKE 'P__A%'
	ORDER BY email;

--
SELECT *
	FROM City
	WHERE city LIKE 'P%';


-- Análise de valores nulos numa query.
SELECT *
	FROM customer
	WHERE email IS NULL;
--
SELECT *
	FROM customer
	WHERE email IS NOT NULL;

-- Utilização da função de agregação COUNT().
SELECT COUNT(*)
	FROM customer;

--
SELECT COUNT(customer_id)
	FROM customer;

-- Utilização da função de agregação COUNT(), SUM(), MAX(), MIN() e AVG().
SELECT COUNT(*), SUM(amount), MAX(amount), 
	MIN(amount), AVG(amount)
	FROM payment;

--  Isto não dá!!!!! :-)
SELECT COUNT(*), SUM(amount), MAX(amount), 
	MIN(amount), AVG(amount)
	FROM payment
	WHERE customer_id IN (1,2,3);

-- Utilização da clausula GROUP BY, com limitação de valores e ordenação 
-- de resultados.
SELECT customer_id AS NrCliente, COUNT(*) AS NrPagamentos, 
	SUM(amount) AS TotalPagamentos, 
	MAX(amount) AS MaiorPagamento, 
	MIN(amount) AS MenorPagamento, 
	AVG(amount) AS MédiaPagaments
	FROM payment
	WHERE customer_id IN (1,2,3,4,5)
	GROUP BY customer_id
		HAVING SUM(amount) > 130
	ORDER BY SUM(amount) DESC
	LIMIT 3;

	
-- Manipulacao de valores do tipo DATE e DATETIME
--
SELECT *
	FROM rental
	WHERE rental_date = '2005-05-24';

--
SELECT *
	FROM rental
	WHERE DATE(rental_date) <> '2005-05-24';

-- 
SELECT DISTINCT YEAR(payment_date) AS Ano
	FROM payment
	ORDER BY YEAR(payment_date) ASC;

--
SELECT DISTINCT MONTH(payment_date) AS Mês
	FROM payment
	ORDER BY MONTH(payment_date) ASC;

-- 
SELECT *
	FROM rental
	WHERE DATE(rental_date) = CURDATE();

-- Alugueres realizados em 2005 duramte o mês de Dezembro.
SELECT COUNT(*)
	FROM rental
	WHERE YEAR(rental_date) = 2005
		AND MONTH(rental_date) = 12;

-- 
SELECT ADDDATE(CURDATE(), INTERVAL 1 DAY) as Amanha;

--
SELECT ADDDATE(CURDATE(), INTERVAL -7 DAY) as SemanaPassada;

--
SELECT WEEK(CURDATE());

--
SELECT WEEK(ADDDATE(CURDATE(), INTERVAL -7 DAY)) as SemanaPassada;

--
SELECT ADDDATE(CURDATE(), INTERVAL -1 YEAR) as HaUmAnoAtras;

--
SELECT CURTIME();

--
SELECT ADDTIME(CURTIME(),CURTIME());

--
SELECT ADDTIME(CURTIME(),'1:00');

-- Formatação de valores do tipo DATE nos resultados de saída de uma query. 
-- %W	Weekday - Nome do dia da semana (Sunday..Saturday).
-- %M	Month - Nome do mês (January..December).
-- %Y	Year - Ano, numérico, quatro dígitos.
SELECT DATE_FORMAT(CURDATE(), '%W %M %Y');

--
SELECT NOW();
-- %D	Day - Dia do mês com sufixo inglês (0th, 1st, 2nd, 3rd, ‚Ä¶)
-- %y	Year - Ano, numèrico, dois dígitos.
-- %a	Nome abreviado do dia da semana (Sun..Sat).
-- %d	Dia do mês, numérico (00..31)
-- %m	Mês, numérico (00..12)
-- %b	Nome do mês abreviado (Jan..Dec)
-- %j	Dia do ano (001..366)
SELECT DATE_FORMAT(NOW(),'%D %y %a %d %m %b %j');

--
SELECT DATEDIFF(NOW(),'2014-10-12');

--
SELECT DAY(rental_date) AS Dia,
	DAYNAME(rental_date) AS NomeDia,
	DAYOFMONTH(rental_date) AS DiaDoMes,
	MONTHNAME(rental_date) AS DiaDoMes,
	DAYOFWEEK(rental_date) AS DiaDaSemana,
	DAYOFYEAR(rental_date) AS DiasDoAno
	FROM rental
	WHERE rental_date = '2005-05-24 22:53:30';

-- Quais foram os alugueres realizados nos meses de Maio, Junho e Julho de 2005.
--
SELECT Rental_id, rental_date
	FROM rental
	WHERE MONTHNAME(Rental_date) IN ('May','June', 'July') AND 
		YEAR(rental_date) = 2005;

-- Quais foram os valores dos alugueres realizados entre nos meses de Maio, Junho e 
-- Julho de 2005.
--
SELECT RE.Rental_id, RE.rental_date, PA.amount
	FROM rental AS RE INNER JOIN Payment AS PA
		ON RE.Rental_id = PA.Rental_id
	WHERE MONTHNAME(RE.Rental_date) IN ('May','June', 'July') AND 
		YEAR(RE.rental_date) = 2005;


-- Manipulacao de valores do tipo string - CHAR() e VARCHAR().
--
SELECT LENGTH('Os Cursos MIEI e LCC');
--
SELECT CONCAT('O SGBD My', 'S', 'QL');
--
SELECT CONCAT_WS(' ', first_name, last_name) 
	FROM customer;
--
SELECT RTRIM('Os Cursos MIEI e LCC       ');
--
SELECT LTRIM('       Os Cursos MIEI e LCC');
--
SELECT TRIM(leading 'O' from 'Os Cursos MIEI e LCC');
--
SELECT LOCATE('MI', 'Os Cursos MIEI e LCC');
--
SELECT SUBSTRING('Os Cursos MIEI e LCC',5,5);
--
SELECT LEFT('Os Cursos MIEI e LCC',3);
--
SELECT RIGHT('Os Cursos MIEI e LCC', 3);
--
SELECT UCASE(lastname) 
	from table_name;
--
SELECT REPLACE('O curso de LEI', 'LEI', 'MIEI');

-- Operacoes com funcoes de manipulacao de strngs
--
SELECT staff_id AS Nr, UCASE(CONCAT(first_name, ' ',last_name)) AS Nome, 
	email AS eMail
	FROM Staff;

-- Juncoes externas
-- Exemplo de uma junção externa à esquerda
SELECT *
	FROM customer AS C LEFT OUTER JOIN rental AS R
		ON C.customer_id=R.customer_id;

-- Exemplo de uma junção externa à direita
SELECT *
	FROM film_actor AS FA RIGHT OUTER JOIN film AS F
		ON FA.film_id=F.film_id;

-- Realização de queries um pouco mais complexas. 
--
-- Utilização de uma operação de junção (intera) entre duas tabelas.
SELECT *
	FROM payment AS P INNER JOIN customer AS C
		ON P.customer_id=C.customer_id;

-- Utilização de operações encadeadas de junções internas.
SELECT I.City, 
	COUNT(*) AS NrPagamentos, 
	SUM(P.amount) AS TotalPagamentos, 
	MAX(P.amount) AS MaiorPagamento, 
	MIN(P.amount) AS MenorPagamento, 
	AVG(P.amount) AS MédiaPagaments
	FROM payment AS P INNER JOIN customer AS C
		ON P.customer_id=C.customer_id
		INNER JOIN address AS A
			ON C.address_id=A.address_id
			INNER JOIN City AS I
				ON A.city_id=I.city_id
	WHERE I.City = 'Po';

--
SELECT I.City, C.Customer_id, 
	COUNT(*) AS NrPagamentos, 
	SUM(P.amount) AS TotalPagamentos, 
	MAX(P.amount) AS MaiorPagamento, 
	MIN(P.amount) AS MenorPagamento, 
	AVG(P.amount) AS MédiaPagaments
	FROM payment AS P INNER JOIN customer AS C
		ON P.customer_id=C.customer_id
		INNER JOIN address AS A
			ON C.address_id=A.address_id
			INNER JOIN City AS I
				ON A.city_id=I.city_id
	WHERE I.City = 'Po' OR I.City = 'Boa Vista'
	GROUP BY I.City, C.Customer_id;

--
SELECT *
	FROM Customer AS C INNER JOIN address AS A
		ON C.address_id=A.address_id
		INNER JOIN City AS I
			ON A.city_id=I.city_id 
	WHERE I.city_id IN (38,39,40);

-- Utilização de operações de junção com sub-queries.
SELECT *
	FROM Customer AS C INNER JOIN address AS A
		ON C.address_id=A.address_id
		INNER JOIN City AS I
			ON A.city_id=I.city_id 
	WHERE A.city_id IN 
		(SELECT city_id
			FROM City
			WHERE City LIKE 'AT%');

-- 
SELECT 	C.customer_id AS Nr, C.first_name AS Nome, 
		C.last_name AS Apelido, 
		C.email, '______' AS Observacoes
	FROM Customer AS C INNER JOIN address AS A
		ON C.address_id=A.address_id
--		INNER JOIN City AS I
--			ON A.city_id=I.city_id 
	WHERE A.city_id NOT IN 
		(SELECT city_id
			FROM City
			WHERE City LIKE 'AT%');

--
SELECT COUNT(*)
	FROM film
	WHERE film_id NOT IN (
	SELECT DISTINCT I.film_id
		FROM rental AS R INNER JOIN inventory AS I
			ON R.inventory_id=I.inventory_id);

--
SELECT *
	FROM film
	WHERE film_id NOT IN (
	SELECT DISTINCT I.film_id
		FROM rental AS R INNER JOIN inventory AS I
			ON R.inventory_id=I.inventory_id);

--
SELECT F.film_id, F.title, R.rental_date
	FROM film AS F LEFT OUTER JOIN inventory AS I
			ON F.film_id=I.film_id
			LEFT OUTER JOIN rental AS R
				ON I.inventory_id=R.inventory_id;

--
SELECT *
	FROM film AS F INNER JOIN inventory AS I
		ON F.film_id=I.film_id
		INNER JOIN rental AS R
			ON I.inventory_id=R.inventory_id;

-- Quais os clientes que até hohe não realizaram qualquer aluguer.
SELECT *
	FROM customer
	WHERE customer_id NOT IN (
		SELECT DISTINCT customer_id
			FROM rental);
 
-- Criação de um novo registo na tabela "customer".
INSERT INTO customer 
	(customer_id, store_id, first_name, last_name, email, 
	address_id, active)
	VALUE 
	(600, 1,'João','Casinhas','casinhas@sakila.com',605,1),
	(601, 1,'Ana','Aninhos','aninhos@sakila.com',605,1);

-- Remoçãodo dos registos criados anteriormente.
DELETE FROM customer
	WHERE customer_id = 600 OR customer_id = 601;

--
SELECT C.customer_id AS "Cliente", COUNT(R.rental_id) NrAlugueres, 
	MAX(R.rental_date) AS "DtUlAluguere"
	FROM Customer AS C LEFT OUTER JOIN rental AS R 
		ON C.customer_id=R.customer_id
	WHERE C.customer_id >= 595
	GROUP BY C.customer_id;

--
SELECT C.customer_id AS "Cliente", COUNT(P.payment_id) NrPagamentos, 
	SUM(P.amount) AS "ValorPago", IFNULL(MAX(P.payment_date),'?') AS "DtUltPagamento"
	FROM payment AS P RIGHT OUTER JOIN Customer AS C  
		ON P.customer_id=C.customer_id
	WHERE C.customer_id >= 595
	GROUP BY C.customer_id;

-- Quais os nomes dos atores dosfilmes que foram alugados por clientes
-- do país 'Peru' durante o mês de 'Maio' de 2006.
SELECT DISTINCT CONCAT(AC.first_name,' ', AC.last_name) AS NomeAtor 
	FROM country as C 
		INNER JOIN city AS I
		ON C.country_id=I.country_id
			INNER JOIN address AS A
			ON I.city_id=A.city_id
				INNER JOIN customer AS U
				ON U.address_id=A.address_id
					INNER JOIN rental AS R
					ON U.customer_id=R.customer_id
						INNER JOIN inventory AS N
						ON N.inventory_id=R.inventory_id
							INNER JOIN film AS F
							ON N.film_id=F.film_id
								INNER JOIN film_actor AS FA
								ON FA.film_id=F.film_id
									INNER JOIN actor AS AC
									ON AC.actor_id=FA.actor_id

	WHERE C.country = 'Peru'
		AND YEAR(R.rental_date) = '2006'
		AND MONTHNAME(R.rental_date) = 'May';

--
-- <fim>

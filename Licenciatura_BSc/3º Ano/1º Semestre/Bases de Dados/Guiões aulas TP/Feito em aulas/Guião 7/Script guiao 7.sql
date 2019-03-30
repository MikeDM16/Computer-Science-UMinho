use pizzaria; 

/*Questao b.1*/
SELECT * FROM pizzas;

/*Questão b.2*/
SELECT matériaprima.Designação FROM matériaprima
ORDER BY Designação ASC;

/*Questão b.3*/
SELECT pizzas.Designação FROM pizzas
ORDER BY pizzas.Preço DESC
LIMIT 3

/*Questão b.4*/
SELECT matériaprima.Designação FROM matériaprima
JOIN matériaprimapizzas 
	ON matériaprimapizzas.MatériaPrima = matériaprima.Nr
JOIN pizzas ON pizzas.Nr = matériaprimapizzas.Pizza
			WHERE pizzas.Designação = 'Bela Napolitana'
            
/*questão b.5*/
processostarefasSELECT * FROM matériaprima
JOIN matériaprimapizzas 
		ON matériaprimapizzas.MatériaPrima = matériaprima.Nr
JOIN pizzas ON matériaprimapizzas.Pizza = pizzas.Nr
WHERE matériaprima.Preço > 5

/*Questão b.6*/
SELECT matériaprima.Preço, Pizzas.Designação FROM matériaprima
JOIN matériaprimapizzas 
		ON matériaprimapizzas.MatériaPrima = matériaprima.Nr
JOIN pizzas ON pizzas.Nr = matériaprimapizzas.Pizza
		WHERE pizzas.Designação = 'Pizza Queijo' 
		   OR pizzas.Designação = 'Pizza Cogumelos'
           
/*Questão b.7*/
SELECT funcionários.Nome FROM funcionários
JOIN processos ON processos.Gestor = funcionários.Nr
JOIN produçãopizzas ON processos.Nr = produçãopizzas.processo


/*Questão b.8*/
SELECT pizzas.Designação FROM pizzas
JOIN produçãopizzas ON produçãopizzas.Pizza = pizzas.Nr
	WHERE produçãopizzas.DataHoraProdução > '30-07-2016 00:00:00' 
	AND produçãopizzas.DataHoraProdução < '2016-09-30 00:00:00'

/*Questão b.9*/
SELECT máquinas.Designação FROM máquinas
JOIN produçãopizzas ON produçãopizzas.Máquina = máquinas.Nr
JOIN pizzas ON produçãopizzas.Pizza = pizzas.Nr
	WHERE pizzas.Nr = 1 OR pizzas.Nr = 2 OR pizzas.Nr = 5
	AND Month(produçãopizzas.DataHoraProdução) = 9
  /*produçãopizzas.DataHoraProdução > '2016-09-01 00:00:00 
	AND produçãopizzas.DataHoraProdução < '2016-09-30 00:00:00'*/

/*Questão b.10*/
SELECT COUNT(pizzasprodução.Quantidade) AS Quantidade FROM pizzas
JOIN pizzasprodução ON pizzasprodução.Nr = pizzas.Nr
	WHERE pizzasprodução.Ano = (YEAR(NOW())-1)
		OR pizzasprodução.Ano = (YEAR(NOW())-2)
        
/*Questão b.11 */
SELECT AVG(Produçãopizzas.Duração) AS 'Tempo médio produção' FROM Produçãopizzas
JOIN pizzas ON produçãopizzas.Pizza = pizzas.Nr
			WHERE pizzas.Designação = 'Pizza Queijo'
            
/*Questão b.12*/
SELECT tarefas.Designação FROM tarefas
JOIN produçãopizzas ON produçãopizzas.Tarefa = tarefas.Nr
JOIN funcionários ON funcionários.Nr = produçãopizzas.Funcionário
JOIN funções ON funções.Nr = funcionários.Função AND funcionários.Função = 'Gestor'
JOIN processostarefas ON processostarefas.Tarefa = tarefas.Nr
ORDER BY (processostarefas.NrSequência)

/*Questão b.13*/
SELECT SUM(produçãopizzas.Duração) AS 'Tempo Uso', 
	   SUM(produçãopizzas.Duração)*máquinas.CustoHora AS 'Custo'
FROM máquinas JOIN produçãopizzas ON produçãopizzas.Máquina = 1

/*Questão b.14*/
SELECT AVG(produçãopizzas.Duração) FROM produçãopizzas
JOIN tarefas ON tarefas.Nr = produçãopizzas.Tarefa
WHERE produçãopizzas.Pizza = 1 OR produçãopizzas.Pizza = 3

/*Questão b.15 REVER*/
SELECT matériaprima.Designação FROM matériaprima
JOIN matériaprimapizzas ON matériaprimapizzas.MatériaPrima = matériaprima.Nr
ORDER BY matériaprimapizzas.Quantidade DESC
Limit 5;

/*Questoa b.16*/
SELECT SUM(matériaprima.Preço) AS 'Preço matéria-prima',
	   SUM(produçãopizzas.Duração)*máquinas.CustoHora AS 'Preço produçao',
       SUM(produçãopizzas.Duração)*máquinas.CustoHora + SUM(matériaprima.Preço) AS 'Preço Total'
FROM matériaprima
JOIN matériaprimapizzas ON matériaprimapizzas.MatériaPrima = matériaprima.Nr
JOIN produçãopizzas ON produçãopizzas.Pizza = 1
JOIN máquinas ON máquinas.Nr = produçãopizzas.Máquina
WHERE matériaprimapizzas.pizza = 1;

/*Questão b.17*/
SELECT SUM(matériaprima.Preço) AS 'Preço matéria-prima',
	   SUM(produçãopizzas.Duração)*máquinas.CustoHora AS 'Preço produçao',
       SUM(produçãopizzas.Duração)*máquinas.CustoHora + SUM(matériaprima.Preço) AS 'Preço Total'
FROM matériaprima
JOIN matériaprimapizzas ON matériaprimapizzas.MatériaPrima = matériaprima.Nr
JOIN produçãopizzas ON produçãopizzas.Pizza = matériaprimapizzas.Pizza
JOIN pizzasprodução ON pizzasprodução.Nr = matériaprimapizzas.Pizza
JOIN máquinas ON máquinas.Nr = produçãopizzas.Máquina
WHERE matériaprimapizzas.pizza = 1;

/*Questão b.19*/
UPDATE pizzas
	SET pizzas.Observações = 'Nada a assinalar'
    WHERE pizzas.Observações = NULL;
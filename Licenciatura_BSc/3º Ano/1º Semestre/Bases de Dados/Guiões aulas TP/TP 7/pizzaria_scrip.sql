USE pizzaria;

/*1) Qual a informação que temos disponível na base de dados sobre pizzas?*/
SELECT * FROM pizzas; 

/*2) Quais são os nomes das matérias-primas definidas na base de dados? Apresente-as
ordenadas alfabeticamente.*/
SELECT matériaprima.Designação FROM matériaprima
ORDER BY matériaprima.Designação ASC;

/*3) Quais os nomes das 5 pizzas mais caras?*/
SELECT pizzas.Designação FROM pizzas
ORDER BY pizzas.Preço DESC
LIMIT 5;

/*4) Quais as matérias primas que entram na produção da pizza ‘Bela Napolitana’?*/
SELECT matériaprima.Designação FROM matériaprima
JOIN matériaprimapizzas ON (matériaprimapizzas.MatériaPrima = matériaprima.Nr)
JOIN pizzas ON (matériaprimapizzas.pizza = pizzas.Nr)
WHERE pizzas.Designação = 'Bela Napolitana';

/*5) Quais as quantidades das matérias-primas que são utilizadas em pizzas com um
custo superior a 10.00€?*/
SELECT matériaprima.Designação, matériaprimapizzas.Quantidade, pizzas.Designação FROM matériaprima
JOIN matériaprimapizzas ON (matériaprimapizzas.MatériaPrima = matériaprima.Nr)
JOIN pizzas ON (matériaprimapizzas.Pizza = pizzas.Nr)
WHERE pizzas.Preço > 10;

/*6) Quais os preços das matérias-primas que são utilizadas no fabrico das pizzas
‘Supremo Aroma de Itália’ e ‘Delícia Campestre’.*/
SELECT matériaprima.Designação, matériaprima.Preço FROM matériaprima
JOIN matériaprimapizzas ON (matériaprimapizzas.MatériaPrima = MatériaPrima.Nr)
JOIN pizzas ON (matériaprimapizzas.Pizza = pizzas.Nr)
WHERE pizzas.Designação = 'Bela Napolitana';
/*WHERE pizzas.Designação = 'Delícia Campestre' OR pizzas.Designação = 'Supremo Aroma de Itália'*/

/*7) Quais os nomes dos funcionários que são gestores de processos das pizzas que têm
como matéria-prima ‘Trufas’?*/
SELECT funcionários.Nome FROM funcionários
JOIN processos ON (processos.Gestor = funcionários.Nr)
JOIN produçãopizzas ON (processos.Nr = produçãopizzas.Processo)
JOIN pizzas ON (pizzas.Nr = produçãopizzas.Pizza)
JOIN matériaprimapizzas ON (pizzas.Nr = matériaprimapizzas.Pizza)
JOIN matériaprima ON (matériaprima.Nr = matériaprimapizzas.MatériaPrima)
WHERE matériaprima.Designação = 'Trufas'; 

/*8) Quais foram as pizzas fabricadas entre ’30-07-2016’ e ’30-09-2016’?*/
SELECT pizzas.Designação FROM pizzas
JOIN produçãopizzas ON (produçãopizzas.Pizza = pizzas.Nr)
WHERE (DATE(produçãopizzas.DataHoraProdução) > '30-07-2016' AND
	   DATE(produçãopizzas.DataHoraProdução) < '30-09-2016');
       
/*9) Quais os nomes das máquinas que foram utilizadas na produção das pizzas ‘1’, ‘3’ e
‘13’, durante o mês de Setembro de 2016?*/
SELECT máquinas.Designação FROM máquinas
JOIN produçãopizzas ON (produçãopizzas.Máquina = máquinas.Nr)
JOIN pizzas ON (pizzas.Nr = produçãopizzas.Pizza)
WHERE ( (pizzas.Nr = 1 OR pizzas.NR = 2 OR pizzas.Nr = 13)  AND 
	  (YEAR(produçãopizzas.DataHoraProdução)= 2016 AND 
       MONTH(produçãopizzas.DataHoraProdução)= 8));
     
/*10) Quantas pizzas foram produzidas durante os dois últimos anos?*/
SELECT SUM(pizzasprodução.Quantidade) FROM pizzasprodução
JOIN pizzas ON (pizzas.Nr = pizzasprodução.Nr)
JOIN produçãopizzas ON (produçãopizzas.Pizza = pizzas.Nr)
WHERE (pizzasprodução.Ano > (YEAR(current_date())-1) AND
	   pizzasprodução.Ano > (YEAR(current_date())+1) );
       
/*11) Usualmente, qual é o tempo que se leva a produzir uma pizza ‘Especial Quatro
Estações’?*/
SELECT SUM(produçãopizzas.Duração)/(pizzasprodução.Quantidade) AS 'tempo médio' 
FROM produçãopizzas
JOIN pizzas ON (pizzas.Nr = produçãopizzas.Pizza)
JOIN pizzasprodução ON (pizzasprodução.Nr = pizzas.Nr)
WHERE pizzas.Designação = 'Especial Quatro Estações'; 

SELECT AVG(Produçãopizzas.Duração) AS 'Tempo médio produção' FROM Produçãopizzas
JOIN pizzas ON produçãopizzas.Pizza = pizzas.Nr
			WHERE pizzas.Designação = 'Especial Quatro Estações';

/*12) Quais as tarefas que são realizadas nos processos de produção do gestor ‘1’?
Ordene-as pela sua ordem de execução no processo.*/
SELECT tarefas.Designação FROM tarefas
JOIN processostarefas ON (processostarefas.Tarefa = tarefas.Nr)
JOIN processos ON (processostarefas.Processo = processos.Nr)
WHERE processos.Gestor = 1
ORDER BY processostarefas.NrSequência ASC;

/*13) Quanto tempo trabalhou a máquina ‘1’, e qual o seu custo de operação, durante os
meses de ‘Agosto’ e ‘Setembro’ de 2016?*/
SELECT SUM(produçãopizzas.Duração) As 'Tempo utilização', 
	   'Tempo utilização'*máquinas.CustoHora AS 'Custo total'
       FROM Máquinas
JOIN produçãopizzas ON (produçãopizzas.Máquina = máquinas.Nr)
WHERE (máquinas.Nr = 1 AND YEAR(produçãopizzas.DataHoraProdução) = 2016 AND
	  (MONTH(produçãopizzas.DataHoraProdução) > 8 AND 
       MONTH(produçãopizzas.DataHoraProdução) < 9) );
       
/*14) Em média, quanto tempo duram as tarefas dos processos de produção das pizzas ‘1’
e ‘7’?*/
SELECT SUM(produçãopizzas.Duração)/(COUNT(*)) AS 'Tempo duração Tarefas' FROM produçãopizzas
JOIN pizzas ON (pizzas.Nr = produçãopizzas.Pizza)
JOIN processos ON (processos.Nr = produçãopizzas.Processo)
JOIN processostarefas ON (processostarefas.Processo = processos.Nr)
JOIN tarefas ON (processostarefas.Tarefa = Tarefas.Nr)
WHERE (pizzas.Nr = 1 OR pizzas.Nr = 7);

/*15) Quais são os nomes das cinco matérias-primas mais utilizadas na produção de
pizzas?*/
SELECT matériaprima.Designação, matériaprimapizzas.Quantidade FROM matériaprima
JOIN matériaprimapizzas ON (matériaprimapizzas.MatériaPrima = matériaprima.Nr)
ORDER BY ((matériaprimapizzas.Quantidade)) DESC
LIMIT 5;
/*REVER tem que ser a soma das quantidades para todos os produtos. Assim nao reune 
produtos iguais numa so entrada*/

/*16) Quanto custa produzir a pizza ‘1’? Apresente o seu custo total, de produção e de
matérias-primas.*/
SELECT (matériaprima.Preço * SUM(matériaprimapizzas.Quantidade)) AS 'Custo matéria prima',
	   (máquinas.CustoHora * SUM(produçãopizzas.Duração)) AS 'Custo produção',
       ('Custo matéria prima' + 'Custo produção') AS 'Custo total',
       pizzas.Preço AS 'Preço Venda',
       ('Preço Venda' - 'Custo total') AS 'Lucro final'
FROM pizzas
JOIN matériaprimapizzas ON (matériaprimapizzas.Pizza = pizzas.Nr)
JOIN matériaprima ON (matériaprima.Nr = matériaprimapizzas.MatériaPrima)
JOIN produçãopizzas ON (produçãopizzas.Pizza = pizzas.Nr)
JOIN máquinas ON (máquinas.Nr = produçãopizzas.Máquina)
WHERE pizzas.Nr = 6; 

/*17) Desenvolva uma query semelhante à da alínea anterior, mas que, desta vez,
apresente a informação solicitada para todas as pizzas que foram produzidas até
hoje.*/
SELECT (matériaprima.Preço * SUM(matériaprimapizzas.Quantidade)) AS 'Custo matéria prima',
	   (máquinas.CustoHora * SUM(produçãopizzas.Duração)) AS 'Custo produção',
       ('Custo matéria prima' + 'Custo produção') AS 'Custo total',
       pizzas.Preço AS 'Preço Venda',
       ('Preço Venda' - 'Custo total') AS 'Lucro final'
FROM pizzas
JOIN matériaprimapizzas ON (matériaprimapizzas.Pizza = pizzas.Nr)
JOIN matériaprima ON (matériaprima.Nr = matériaprimapizzas.MatériaPrima)
JOIN produçãopizzas ON (produçãopizzas.Pizza = pizzas.Nr)
JOIN máquinas ON (máquinas.Nr = produçãopizzas.Máquina);

/*18) Atualize em +10% o preço de todas as matérias-primas que entram na produção de
pizzas com preço inferior a 6.50€ e que envolveram na sua produção as máquinas
’10 e ‘11’.*/

UPDATE matériaprima
	JOIN matériaprimapizzas ON (matériaprimapizzas.MatériaPrima = matériaprima.Nr)
	JOIN pizzas ON (pizzas.Nr = matériaprimapizzas.Pizza)
	JOIN produçãopizzas ON (produçãopizzas.Pizza = pizzas.Nr)
	JOIN máquinas ON (máquinas.Nr = produçãopizzas.Máquina)

SET matériaprima.Preço = 1.10*(matériaprima.Preço)

WHERE (pizzas.Preço < 6.5 AND (máquinas.Nr = 10 OR máquinas.Nr = 11));

 
 /*19) Atualize o campo de observações de todas as pizzas que tenham o valor nulo,
substituindo-o por 'Nada a assinalar'.*/
UPDATE pizzas

SET pizzas.Observações = 
	IF(pizzas.Observações = NULL, 'Nada a assinalar', pizzas.Observações);
    






        
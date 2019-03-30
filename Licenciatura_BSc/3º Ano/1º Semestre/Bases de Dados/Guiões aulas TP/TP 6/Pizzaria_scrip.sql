use pizzaria;

/*a. Quais são as pizzas que têm um preço inferior a 8.50€?*/
SELECT pizzas.Designação FROM pizzas
WHERE pizzas.Preço <= 8.5;

/*b. Quais são as matérias-primas que são necessárias para produzir as pizzas ‘88’ e ‘99’?*/
SELECT matériaprima.Designação FROM matériaprima
JOIN matériaprimapizzas ON (matériaprimapizzas.Pizza = matériaprima.Nr)
WHERE matériaprimapizzas.Pizza = 88 OR matériaprimapizzas.Pizza = 99;

/*c. Quais os nomes e funções dos funcionários que são gestores de processos?*/
SELECT funcionários.Nome, funções.Designação FROM funcionários
JOIN processos ON (processos.Gestor = funcionários.Nr)
JOIN funções ON (funções.Nr = funcionários.Função);


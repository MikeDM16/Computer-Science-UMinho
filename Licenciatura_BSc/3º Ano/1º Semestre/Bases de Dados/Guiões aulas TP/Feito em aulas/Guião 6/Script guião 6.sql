/*Questão 2.a*/
SELECT * 
	FROM pizzas
    WHERE pizzas.Preço < 5
    
/*Questão 2.b*/
SELECT matériaprima.Designação FROM matériaprima 
INNER JOIN matériaprimapizzas ON matériaprima.Nr = matériaprimapizzas.MatériaPrima 
WHERE matériaprimapizzas.Pizza = 1 OR matériaprimapizzas.Pizza = 6

/*Questão 2.c*/
SELECT funcionários.Nome, funções.Designação FROM 
funcionários JOIN processos ON funcionários.Nr = processos.Gestor
JOIN funções ON funções.Nr = funcionários.função

SELECT funcionários.nome, funções.designação FROM
processos JOIN funcionários ON processos.Gestor = funcionários.Nr
JOIN funções ON funções.Nr = funcionários.função
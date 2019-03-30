use mydb;

INSERT INTO `mydb`.`pizzas`
(`Nr`,`Designação`,`Apresentação`,`Preço`,`Observações`)
VALUES
(1, "Pizza Queijo", "Pizza saborosa com 4 queijos", 4.5, " " ),
(2, "Pizza Cogumelos", "Pizza com cogumelos frescos e naturais", 5.0 , " "),
(3, "Pizza almondegas", "Pizza saborosa com carne picada", 6.0, " "),
(4, "Pizza tropial", "Pizza com ananás", 5.0 , "");

INSERT INTO `mydb`.`pizzas`
(`Nr`,`Designação`,`Apresentação`,`Preço`,`Observações`)
VALUES
(6, "Bela Napolitana", "Pizza saborosa", 4.5, " " );

SELECT * FROM pizzas

INSERT INTO `mydb`.`funções`
(`Nr`,`Designação`)
VALUES
(1, "Gerente"),
(2, "Cozinheiro"),
(3, "Limpezas"),
(4, "Servente"),
(5, "Entregas");

INSERT INTO `mydb`.`funcionários`
(`Nr`,`Nome`,`Função`,`Observações`)
VALUES
(1,"Alberto", 1," "),
(2,"Maria", 3," "),
(3,"João", 2," "),
(4,"Frederica", 2," ");

SELECT * FROM funcionários
	Where Função = 1;
    
INSERT INTO `mydb`.`matériaprima`
(`Nr`,`Designação`,`Referência`,`Unidade`,`Preço`,`Observações`)
VALUES
(1, "Queijo", "Queijo frances", 50, 3.5, " "),
(2, "Fiambre", "Porco", 50, 5.5, " "),
(3, "Chouriço", "Enchidos", 50, 4.5, " "),
(4, "Farinha", "Fina", 70, 3.5, " "),
(5, "Oregãos", "Frasco", 10, 6.5, " ");

INSERT INTO `mydb`.`matériaprimapizzas`
(`Pizza`,`MatériaPrima`,`Quantidade`,`Incorporação`)
VALUES
(1, 4, 100, "gramas"), (2, 4, 100, "gramas"),
(3, 4, 100, "gramas"), (4, 4, 100, "gramas"),
(5, 4, 100, "gramas"), (6, 4, 100, "gramas"),

(1, 1, 200, "gramas"), (2, 1, 200, "gramas"),
(3, 1, 200, "gramas"), (4, 1, 200, "gramas"),
(5, 1, 100, "gramas"), (6, 1, 100, "gramas"),

(1, 2, 500, "gramas"), (2, 2, 100, "gramas"),
(3, 2, 500, "gramas"), (4, 2, 100, "gramas"),
(5, 2, 500, "gramas"), (6, 2, 100, "gramas");

INSERT INTO `mydb`.`matériaprimapizzas`
(`Pizza`,`MatériaPrima`,`Quantidade`,`Incorporação`)
VALUES
(6, 5, 100, "gramas"), 
(6, 4, 100, "gramas");

/*Pregunta 1 Ficha 6
	SELECT * FROM pizzas
		Where Preço < 8.5;
*/
/*Pregunta 2 Ficha 6

SELECT matériaprima.Designação FROM matériaprima
INNER JOIN matériaprimapizzas
	ON matériaprima.Nr = matériaprimapizzas.MatériaPrima
    Where matériaprimapizzas.Pizza = 1 and matériaprimapizzas.Pizza = 2;
*/

INSERT INTO `mydb`.`processos`
(`Nr`,`Designação`,`Descrição`,`Gestor`,`Observações`)
VALUES
(1, 'Produçao Pizza Cogumelos','Faz pizza', 3 ,'nada');

SELECT * from pizzas

INSERT INTO `mydb`.`produçãopizzas`
(`Pizza`,`Processo`,`Tarefa`,`Funcionário`,`Máquina`,`DataHoraProdução`,
`Duração`,`Quantidade`)
VALUES
(1, 1,<{Tarefa: }>,<{Funcionário: }>,<{Máquina: }>,
<{DataHoraProdução: }>,<{Duração: }>,<{Quantidade: }>);

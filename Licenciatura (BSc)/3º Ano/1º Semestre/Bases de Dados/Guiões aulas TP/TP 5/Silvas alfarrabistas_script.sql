USE silvas_alfarrabista; 

/*a. Quais são os livros que estão atualmente disponíveis para troca? */
SELECT livro.Titulo FROM livro
WHERE livro.Troca = true;

/*b. Quais são os livros dos clientes de ‘Braga’ que estão catalogados no sítio para troca?*/
SELECT livro.Titulo FROM livro
JOIN cLiente ON cliente.idCliente = livro.idLivro
WHERE cliente.Endereço = 'Braga'; 

/*c. Quais foram os livros do género ‘Policial’ que foram trocados entre o cliente ‘X’ e o
cliente ‘Y’?*/
SELECT livro.Titulo FROM Livro
JOIN troca 
ON ((troca.idCliente_IniciaTroca = 'X' AND troca.idCliente_ValidaTroca = 'Y') OR (troca.idCliente_IniciaTroca = 'Y' AND troca.idCliente_ValidaTroca = 'X'))
JOIN livrotroca ON livrotroca.idTroca = troca.idTroca
WHERE Livro.idLivro = livrotroca.idLivro;

/*d. Quais foram os clientes que até hoje não trocaram qualquer livro?*/
SELECT cliente.Nome FROM cliente
WHERE NrTrocas = 0;

/*e. Quais são os processo de troca que não foram validados até ao momento?*/
SELECT * FROM troca
WHERE troca.estado = false; 

/*f. Quais foram os livros trocados durante o passado mês de ‘Agosto’?*/
SELECT Livro.Titulo FROM Livro
JOIN troca ON (MONTH(troca.Data) = 'Agosto')
JOIN livrotroca ON (livrotroca.idTroca = troca.idTroca)
WHERE (livrotroca.idlivro = livro.idLivro);

/*Quanto faturaram os ‘Silvas’ alfarrabistas durante o ano de ‘2013’ em processo de
troca realizados por clientes de ‘Lisboa’?*/

SELECT SUM(Troca.Custo) AS Facturado FROM troca
JOIN cliente ON (troca.idCliente_IniciaTroca = cliente.idCliente OR troca.idCliente_ValidaTroca = cliente.idCliente)
WHERE YEAR(troca.DATA) = 2013 AND cliente.Endereço = 'Lisboa'; 

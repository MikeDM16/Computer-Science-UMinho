// CRIAR CLIENTES
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/cliente.csv" AS row
CREATE (:Cliente {NrCliente: toInt(row.NrCliente),
                  Nome: row.Nome,
                  Sexo: row.Sexo,
                  DN: toInt(row.DN),
                  CC: row.CC,
                  NIF: row.NIF,
                  Telefone: row.Telefone,
                  Email: row.Email,
                  Password: row.Password});

// CRIAR COMBOIOS
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/comboio.csv" AS row
CREATE (:Comboio {idComboio: toInt(row.idComboio),
                  Tipo: row.Tipo,
                  Carruagens: toInt(row.Carruagens)});

// CRIAR LUGARES
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/lugar.csv" AS row
CREATE (:Lugar {Nr: toInt(row.Nr),
                Carruagem: toInt(row.Carruagem),
                Comboio: toInt(row.Comboio),
                Classe: toInt(row.Classe),
                Mesa: row.Mesa,
                Janela: row.Janela,
                Tomada: row.Tomada});

// CRIAR RESERVAS
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/reserva.csv" AS row
CREATE (:Reserva {idReserva: toInt(row.idReserva),
                  Valor: toFloat(row.Valor),
                  DataEmissao: toInt(row.DataEmissao),
                  Desconto: toInt(row.Desconto)});

// CRIAR VIAGENS
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/viagem.csv" AS row
CREATE (:Viagem {idViagem: toInt(row.idViagem),
                 Data: toInt(row.Data),
                 Preco: toFloat(row.Preco),
                 HoraOrigem: row.HoraOrigem,
                 LocalidadeOrigem: row.LocalidadeOrigem,
                 HoraDestino: row.HoraDestino,
                 LocalidadeDestino: row.LocalidadeDestino,
                 Duracao: row.Duracao});


// RELACIONAR LUGAR COM COMBOIO
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/lugar.csv" AS row
MATCH (lugar:Lugar {Nr: toInt(row.Nr), Carruagem: toInt(row.Carruagem), Comboio: toInt(row.Comboio)})
MATCH (comboio:Comboio {idComboio: toInt(row.Comboio)})
MERGE (lugar)-[p:PERTENCE]->(comboio);

// RELACIONAR VIAGEM COM COMBOIO
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/viagem.csv" AS row
MATCH (viagem:Viagem {idViagem: toInt(row.idViagem)})
MATCH (comboio:Comboio {idComboio: toInt(row.Comboio)})
MERGE (viagem)-[r:REALIZADA]->(comboio);

// RELACIONAR RESERVA COM CLIENTE, VIAGEM
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/reserva.csv" AS row
MATCH (reserva:Reserva {idReserva: toInt(row.idReserva)})
MATCH (cliente:Cliente {NrCliente: toInt(row.Cliente)})
MATCH (viagem:Viagem {idViagem: toInt(row.Viagem)})
MERGE (reserva)-[r:RELATIVA]->(viagem)
MERGE (cliente)-[f:FEZ]->(reserva);

// RELACIONAR RESERVA COM LUGAR
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/reserva.csv" AS row
MATCH (viagem:Viagem {idViagem: toInt(row.Viagem)})-[:REALIZADA]->(comboio:Comboio)
WITH comboio.idComboio AS c, row
MATCH (reserva:Reserva {idReserva: toInt(row.idReserva)})
MATCH (lugar:Lugar {Nr: toInt(row.NrLugar), Carruagem: toInt(row.Carruagem), Comboio: c})
MERGE (lugar)-[d:RESERVADO]->(reserva);
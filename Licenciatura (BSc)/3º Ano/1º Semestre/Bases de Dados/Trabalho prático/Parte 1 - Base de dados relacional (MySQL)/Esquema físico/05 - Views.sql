CREATE VIEW viewViagensLugares AS

SELECT V.Data, V.Preco, V.HoraOrigem AS 'Partida', V.LocalidadeOrigem AS 'Origem', V.HoraDestino AS 'Chegada', V.LocalidadeDestino 'Destino', V.Comboio, V.Duracao,
	   L.Nr, L.Carruagem, L.Classe, IF(L.Janela,'Sim','Não') AS 'Janela', IF(L.Tomada,'Sim','Não') AS 'Tomada', IF(L.Mesa,'Sim','Não') AS 'Mesa'
	FROM viagem V
	
    INNER JOIN Comboio C ON V.Comboio = C.idComboio
    INNER JOIN Lugar L ON C.idComboio = L.Comboio;
    
-- ----------------------------------------------------------------------------------
CREATE VIEW viewClienteReserva AS

SELECT C.NrCliente AS 'Cliente', C.Nome, C.CC, C.NIF, C.Telefone, C.Email, R.idReserva AS 'Reserva', R.Valor, R.DataEmissao, R.Desconto, R.NrLugar, R.Carruagem, R.Viagem
	FROM Cliente C
    INNER JOIN Reserva R ON C.NrCliente = R.Cliente;
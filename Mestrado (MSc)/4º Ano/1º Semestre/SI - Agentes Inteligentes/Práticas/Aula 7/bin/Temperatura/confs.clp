;---------------------------------------------------------------------------------------------------------
; Mapeamento da estrutura de uma mensagem ACL, vinda do JADE, para um 
; template JESS com os mesmos parâmetros
(deftemplate ACLMessage
    (slot performative)
    (slot sender)
 	(slot content)
 	(slot lida (default 0))
    (slot time-to-live); usado para manter apenas a mensagem mais recente de um dado remetente
    (multislot receiver)
	(slot encoding)
    (slot reply-with)
    (slot in-reply-to)
    (slot envelope)
    (slot conversation-id)
    (slot protocol)
    (slot language)
    (slot ontology)
    (slot reply-by)
    (multislot reply-to)
)
;---------------------------------------------------------------------------------------------------------
; Template para um sensor de um dado departamento
(deftemplate sensor 
	(slot id_name)
	(slot temperature) 
	(slot time)
)

;---------------------------------------------------------------------------------------------------------
; Template para um contador, utilizado para dar uma ordem temporal ás mensagens ACL recebidas
(deftemplate contador
	(slot ttl)
)
(deffacts contador (contador (ttl 1)))	; contador inicializado a 1 

;---------------------------------------------------------------------------------------------------------
; Template de um Agent. Assim que o agente MonitorAgent arranca, adiciona-se como um facto na
;sua base de factos jess.
; Util para posteriormente enviar mensagens dentro do sistema jess.
(deftemplate Agent 
	(slot agent))

;---------------------------------------------------------------------------------------------------------
; Templante para estatisticas, onde são guardados os parametros a serem listados a cada 10seg
(deftemplate stats
	(slot nr_coolings  (default 0) )
	(slot nr_heatings  (default 0) )
	(slot soma_temps   (default 0.0) )
	(slot nr_pedidos   (default 0) )
	(slot stand_dev    (default 0.0) )
)
(deffacts stats (stats (nr_coolings 0))) ; Inserçao de um facto do tipo estatistica, que será atualizado ao 
;longo do tempo

;---------------------------------------------------------------------------------------------------------
; Regra utilizada para criar um facto em jess, sempre que for recebida uma mensagem ACL.
; Cria um facto com o template ACLMessage acima descrito.
(defrule jadeACL_para_jessACL
	?msg <- (Message ?message)
	?c <- (contador (ttl ?count) ) 
	=>
	(assert (ACLMessage
				(performative (call jade.lang.acl.ACLMessage getPerformative (?message getPerformative)))
				(sender 	 	 (?message getSender) 			)
				(content 	 	 (?message getContent) 			)
 				(encoding 	  	 (?message getEncoding) 		)
   	 			(reply-with 	 (?message getReplyWith) 		)
   	 			(in-reply-to 	 (?message getInReplyTo)		)
    			(envelope 		 (?message getEnvelope) 		)
    			(conversation-id (?message getConversationId) 	)
    			(protocol 		 (?message getProtocol) 		)
    			(language 	 	 (?message getLanguage) 		)
    			(ontology 		 (?message getOntology) 		)
    			(reply-by 		 (?message getReplyBy) 			)
    			(time-to-live   	?count						)
			)
	)
	(modify ?c (ttl (++ ?count))); incrementa o contador de mensagens recebidas
	;(printout t "M_jess: Criou facto com a mensagem do " ((?message getSender) getName) "." crlf)
	
	; A estrutura "?msg" inicial é apagada, porque na base de factos só fica a entrada correspondente
	;ao template ACLMessagem preenchido com os campos de ?msg 
	(retract ?msg)	
)

;---------------------------------------------------------------------------------------------------------
; Regra com o sumo de todo o sistema, pois sempre que é
(defrule ativa_refrigeração
	(Agent (agent ?agent) ) 
	(ACLMessage { performative == "INFORM" } (sender ?s) )
	(sensor (id_name ?n) (temperature ?t) )
	?dados <- (stats (nr_coolings ?c) (nr_heatings ?h) (soma_temps ?soma) (nr_pedidos ?pedidos) (stand_dev ?d) )
	=>
	; As condições dos ifs avaliam a temperantura e se o nome do agente (?n) é igual ao nome do sender (?s) da mensagem ACL
	;como forma de garantir que comunicamos com a agent sobre o qual analisamos a temperatura. 
	; Na base de factos só é mantida a ultima mensagem e registos de cada agente, logo isto lá resulta... 
	
	(if (and (> ?t 30) (eq ?n ?s) )  then 
		(bind ?content "Arrefecer" )
		(bind ?message (new jade.lang.acl.ACLMessage (get-member jade.lang.acl.ACLMessage REQUEST)))
		(?message setSender (?agent getAID) )
		(?message addReceiver ?s )
		(?message setContent ?content)
		(?agent send ?message)
		(printout t "M_jess: Enviou pedido de arrefecimento ao " (?s getName) "." crlf)
		;(modify ?dados (nr_coolings (++ ?c)) )		; incrementar nr de pedidos de arrefecimento
	else
		(if (and (< ?t 20) (eq ?n ?s) ) then 
			(bind ?content "Aquecer" )
			(bind ?message (new jade.lang.acl.ACLMessage (get-member jade.lang.acl.ACLMessage REQUEST)))
			(?message setSender (?agent getAID) )
			(?message addReceiver ?s )
			(?message setContent ?content)
			(?agent send ?message)
			(printout t "M_jess: Enviou pedido de aquecimento ao " (?s getName) "." crlf)
			;(modify ?dados (nr_heatings (++ ?h)) )	; incrementar nr de pedidos de aquecimento
		)
	)
	;(modify ?dados (nr_pedidos (++ ?pedidos)))
	;(printout t "Dados atualizados " ?dados.nr_pedidos "." crlf)
)

;---------------------------------------------------------------------------------------------------------
; Coisas para testar 
(defrule teste
	(Agent (agent ?agent) ) 
	=>
	(printout t "recebeu  pedido ." ?agent crlf)
)

;---------------------------------------------------------------------------------------------------------
; Regra onde a partir do conteudo de um facto ACLMessage, atualiza o conhecimento 
;associado ao sensor que enviou essa mensagem

; Esta regra criar uma entrada duplicada do agente sensor, mas depois existe uma regra 
;que mantem apenas a entrada mais recente, analisando o slot time. 

; A tentativa com modify em lugar do assert corrigia teoricamente este problema mas nao funcionava...
(defrule Messagem_Sensor_para_facto 
	?msg <- (ACLMessage {performative == "INFORM"} { lida == 0} (content ?c))
	=>
	(bind ?tokens (?c split " ") )  
	(modify ?msg (lida 1))
	(assert
		(sensor
			(id_name 		 ?msg.sender 	 )
			(temperature 	(nth$ 4 ?tokens) )
			(time 			(nth$ 6 ?tokens) )
		)
	)
)

;---------------------------------------------------------------------------------------------------------
; Regra para garantir que quando se faz o update (assert) de um sensor, ele nao é replicado na base de 
:conhecimento. 
(defrule limpa_Sensores_Replicados
	?s1 <- (sensor (id_name ?n1) (time ?t1))
	?s2 <- (sensor (id_name ?n2) (time ?t2))
	=>
	(if (and (< ?t1 ?t2 ) (eq ?n1 ?n2)) then 
		(retract ?s1)

	)
	(if (and (> ?t1 ?t2 ) (eq ?n1 ?n2)) then 
		(retract ?s2)

	)
)

;---------------------------------------------------------------------------------------------------------
; De forma semelhante, regra utilizaa para manter apenas a ultima mensagem de um dados agente sensor, 
;utilizando para isso o slot time-to-live acrescentado ao contexto. 
(defrule limpa_Mensagens
	?m1 <- (ACLMessage (sender ?s1) (time-to-live ?t1) )
	?m2 <- (ACLMessage (sender ?s2) (time-to-live ?t2) )
	=>
	(if (and (eq ?s1 ?s2) (< ?t1 ?t2)) then
		(retract ?m1)
	)
		(if (and (eq ?s1 ?s2) (> ?t1 ?t2)) then
		(retract ?m2)
	)
)

;---------------------------------------------------------------------------------------------------------
; Funçao chamada quando a cada 10segundos se quer imprimir as informações do agente Monitor
(deffunction informacoes ()
	(printout t "------------------ Estado atual ---------------------" crlf) 
	(facts)
	(printout t "-----------------------------------------------------" crlf) 
)
;---------------------------------------------------------------------------------------------------------
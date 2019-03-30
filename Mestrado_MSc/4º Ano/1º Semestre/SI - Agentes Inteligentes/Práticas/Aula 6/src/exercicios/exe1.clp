; jess exercicio 1: 

(deffacts ages
	(person Rosa 30)
	(person Maria 5)
	(person tiago 33))

; Item 1

(defrule separateInfo
	(person ?n ?a) =>
	(assert (name ?n))
	(assert (age  ?a))
	(printout t "exe1.1: Name " ?n " and age " ?a "." crlf))


; Item 2
(defrule printNameAge
	(person ?n ?a) =>
	(printout t "exe1.2: Person " ?n " has " ?a " years." crlf))
/*
; Item 3

(defrule rule3
	(person ?first ?age) =>
	(printout t ?first " escreva o seu último nome: "crlf)
	(bind ?last (read t) ) 
	(assert (nameandsurname ?first ?last))
	(printout t "exe1.3: Added name: " ?first " " ?last "." crlf))

*/
; Item 4

(deffacts names
	(fullname Rosa Araujo Pereira)
	(fullname Maria Sampaio Melo)
	(fullname Tiago Jose da Fonseca Silva))

;(defrule rule4
;	(fullname $?name) =>
;	(bind ?tokens (?name split [ ]) )  
; ?name is a list, not a java objet :(
;	(printout t "exe1.4: Nome: " ?name "." crlf)
;)

(defrule rule4
	(fullname ?first /[a-zA-Z]*/ ?last) =>
	(printout t "exe1.4: Último nome da " ?first " é " ?last crlf))

; Item 5
(defrule rule5
	(and (person ?p ?a) (fullname ?p /[a-zA-Z]*/ ?last)) =>
	(printout t "exe1.5: Name " ?p ", surname "  ?last ", age " ?a crlf) )
(defrule rule5Aux
	(person ?n ?i)
	(fullname ?n $? ?s)
	=>
	(printout t "Person " ?n " " ?s " has " ?i " years." crlf)
)
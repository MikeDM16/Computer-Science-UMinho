; jess exercicio 2: 

(deftemplate person 
	(slot hair-color)
	(multislot name) 
	(slot eye-color) 
	(slot age) 
	(slot abstraction))
	
(deffacts persons
	(person 
		(hair-color black)
		(eye-color blue)
		(name Joao Marsupilami Silva)
		(age 23))
	(person 
		(hair-color brown)
		(eye-color blue)
		(name Gonçalo Rodrigues Conceição)
		(age 38))
	(person 
		(hair-color red)
		(eye-color brown)
		(name Antonio Carvalho Deus)
		(age 23))
	(person 
		(hair-color blond)
		(eye-color green)
		(name Ronaldo Martins Pereira)
		(age 88))
	(person 
		(hair-color brown)
		(eye-color black)
		(name Francisco dos Santos Pereira)
		(age 2)))

; Item 1
(defrule rule1
	(person (name $?name) (age ?age) ) =>
	(bind ?size (length$ ?name) )
	(bind ?last (nth$ ?size ?name))
	(printout t "exe2.1: " ?last ", has " ?age " years." crlf))	

; Item 2
(defrule rule2
	?p <- (person (name ?n $? ?l){hair-color == brown || hair-color == black}) =>
	(printout t "exe2.2: " ?l " tem cabelo escuro." crlf))

; Item 3
(defrule rule3
	?p <- (person {eye-color == blue || eye-color == green}) =>
	(printout t "exe2.3: " ?p.name " tem olhos de cor " ?p.eye-color "." crlf)	
)

(defrule rule3aux		
	(person (name ?n) (eye-color ?ec) ) =>
	(if (or (eq ?ec blue) (eq ?ec green)) then 
		(printout t "exe2.3AUX: " ?n " tem olhos de cor " ?p.eye-color "." crlf)	
	)
)



; Item 4
(defrule rule4
	?p <- (person {eye-color != blue && eye-color != green}) =>
	(printout t "exe2.4: " ?p.name " tem olhos de cor " ?p.eye-color "." crlf))

; Item 5
(defrule rule5
	?p1 <- (person (name $?name1)) 
	?p2 <- (person (name $?name2)) =>
	(bind ?n1 (nth$ 1 ?name1) )
	(bind ?n2 (nth$ 1 ?name2) )
	(if (and (= ?p1.age ?p2.age) (neq ?n1 ?n2)) then 
		(printout t "exe2.5: " ?p1.name " e " ?p2.name " tem a mesma idade." crlf)
	)
	;else 
	;	(printout t "exe2.5: não tem a mesma idade." crlf))
)

; Item 6
(defrule rule6
	(person (eye-color ?e)) =>  
	(if (eq ?e blue) then 	
		(printout t "exe2.6: Existe pelo menos uma pessoa com olhos azuis" crlf)
	) )
	
; Item 7
/*
(defrule rule71
	?p <- (person (age ?a)) =>
	(if (< ?a 12)  then 
		(modify ?p (abstraction child))
		(printout t "exe2.7.1: " ?p.name " is a child." crlf)
	)
)
(defrule rule72
	?p <- (person (age ?a)) =>
	(if (and (> ?a 12) (< ?a 65))  then 
		(modify ?p (abstraction adult))
		(printout t "exe2.7.2: " ?p.name " is an adult." crlf)
	)
)
(defrule rule71
	?p <- (person (age ?a)) =>
	(if (> ?a 65)  then 
		(modify ?p (abstraction third age))
		(printout t "exe2.7.3: " ?p.name " is in third age." crlf)
	)
)
*/

/*
; Item 8
(defrule rule8
	(bind ?x 0)
	(foreach ?p (get-all-template-facts person)
		(if (neq ?p.abstraction adult) then (bind ?x 1)) ) =>
	(if (eq ?x 1) then
		(printout t "exe2.8: Não são todos adultos" crlf)
	else 
		(printout t "exe2.8: São todos adultos." crlf)
	)
)
*/
; Item 9
(defrule rule9
	?p <- (person (age ?a)) =>
	(if (< ?a 12)  then 
		(modify ?p (abstraction child))
		(printout t "exe2.9: " ?p.name " is a child." crlf)
	)
	(if (and (> ?a 12) (< ?a 65))  then 
		(modify ?p (abstraction adult) )
		(printout t "exe2.9: " ?p.name " is an adult." crlf)
	)
	(if (> ?a 65) then 
		(modify ?p (abstraction third-age))
		(printout t "exe2.9: " ?p.name " is in third-age." crlf)
	)
)

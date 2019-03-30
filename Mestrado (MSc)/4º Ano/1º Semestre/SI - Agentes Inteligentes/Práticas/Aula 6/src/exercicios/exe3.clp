; jess execicios 3

	
(deftemplate fruit
	(slot name (default "nd"))
	(slot form (default "nd"))
	(slot color (default "nd"))
	(slot diameter (default 0.0))
	(slot seedsnumber (default 0))
	(slot seedsclass (default "nd"))
	(slot fruitorigin (default "nd"))
	(slot surface (default "nd"))	
)

(defrule testar
	=>
	(printout t "What is the shape of the fruit?" crlf)
	(bind ?f (read t))
	(printout t "What is the diameter of the fruit?" crlf)
	(bind ?d (read t))
	(printout t "What is the color of the fruit?" crlf)
	(bind ?c (read t))
	(assert (fruit (diameter ?d) (color ?c) (form ?f)))
	(facts)
)

(defrule rule1
	?f <- (fruit {form == long} (color ?c) )
	(or (eq ?c green) (eq ?c yellow)) 
	=>
	(bind ?f.name banana)
	(printout t "r1:The fruit is an banana." crlf)
)

(defrule rule2
	(fruit (fruitorigin ?o) (form ?f) (diameter ?d) )
	(> ?d 4)
	(or (eq ?f round) (eq ?f oblong)) =>
	(assert ?o plant-crawling)
	(printout t "r2: Origin: plant-crawling." crlf)
)

(defrule rule3
	(fruit (fruitorigin ?o) {form == round} (diameter ?d) )
	(< ?d 4) =>
	(assert ?o plant-crawling)
	(printout t "r3: Origin: tree." crlf)
)

(defrule rule4_5
	(fruit (seedsnumber ?sn) (seedsclass ?sc) ) =>
	(if (eq ?sn 1) then 
		(assert ?sc lump)
		(printout t "r4_5: Class: lump." crlf)
	 else (if (> ?sn 1) then 
	 		(assert ?sc multiple)
			(printout t "r4_5: Class: multiple." crlf)
		 )
	)
)

(defrule rule6_7_8
	?f <- (fruit {fruitorigin == plant-crawling} (surface ?s) (color ?c)) 
	=>
	(if (eq ?c green) then (assert ?f.name watermelon) )
	(if (and (eq ?c yelow) (eq ?s soft)) then (assert ?f.name melon))
	(if (and (eq ?c brownish) (eq ?s rough)) then (assert ?f.name cantaloupemelon))
)

/*
(defrule rule6
	?f <- (fruit {fruitorigin == plant-crawling} {color == green}) =>
	(assert ?f.name watermelon)
)

(defrule rule7
	?f <- (fruit {fruitorigin == plant-crawling} {color == yellow} 
												 {surface == soft}) 
	=>	(assert ?f.name melon)
)

(defrule rule8
	?f <- (fruit {fruitorigin == plant-crawling} {color == brownish} 
												 {surface == rough}) 
	=>	(assert ?f.name cantaloupemelon)
)
*/

(defrule rule9_10_12
	?f <- (fruit {fruitorigin == tree} {color == orange} 
				 (seedsclass ?sc) (diameter ?d)) 
	=>	
	(if (eq ?sc multiple) then 
		(assert ?f.name orange)
		(printout t "r9_10_12: Name: Orange " crlf) )
	(if (and (eq ?sc lump) (< ?d 3)) then 
			(assert ?f.name apricot)
			(printout t "r9_10_12: Name: apricot" crlf) )	
	(if (and (eq ?sc lump) (>= ?d 3)) then 
			(assert ?f.name peach)
			(printout t "r9_10_12: Name: peach" crlf) )	
)


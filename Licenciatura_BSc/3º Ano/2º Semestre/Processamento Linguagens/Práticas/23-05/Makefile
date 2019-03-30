c = calculadora

all: 
	yacc $c.y
	flex $c.fl
	gcc y.tab.c -o $c -lm
	
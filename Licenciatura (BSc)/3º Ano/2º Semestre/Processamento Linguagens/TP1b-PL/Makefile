testarA: exe1A.fl
	flex exe1A.fl
	cc lex.yy.c -o exe1A
	cat hello.txt	
	./exe1A hello.txt
	cat out.txt


testarB: exe1B.fl
	flex exe1B.fl
	cc lex.yy.c -o exe1B	
	./exe1B shakespeare.txt
	firefox verbos.html

removeA: 
	rm out.txt exe1A
removeB: 
	rm verbos.html exe1B
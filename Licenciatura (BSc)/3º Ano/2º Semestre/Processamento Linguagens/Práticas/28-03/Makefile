V=lyr2tex
$V: $V.fl
	flex $V.fl
	cc lex.yy.c -o $V

test: $V
	  cat musicas/* | ./lyr2abc
	  -abcm2ps out.abc
	  ps2pdf Out.ps
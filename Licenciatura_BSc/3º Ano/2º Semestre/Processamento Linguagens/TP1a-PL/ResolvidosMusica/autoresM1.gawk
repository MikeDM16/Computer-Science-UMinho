BEGIN{ 
	print "---------- Autores Musicais ----------"
	print "Listagem de cantores\n"
	RS = "\n"
	FS = " "
}

$1 ~ /singer|author|music/{ 
			gsub(/(singer|author|music):[ \t]*/, "")
			n = split($0, nome, " ")
			cantor = ""
			for(i in nome){
				if (nome[i]== "?"){
					cantor = "Nao identificado"
				}else {
					if( nome[i] != "(?)"){ 
						cantor = cantor " " nome[i]
					}
				}
			}
			listSinger[cantor]++

}

END{
	n = asorti(listSinger, novo);
	printf("Numero total de cantores encontrados: %s\n", length(novo))
	print "----------------------------------------------------------------------"
	printf( "%64s", "Número de\n")
	printf("%25s %43s", "Autores", "Produções musicais\n")
	for(i=1; i!=n; i++){
		printf("%47s %12s\n", novo[i], listSinger[novo[i]]) | "sort";
	}
	print "----------------------------------------------------------------------" 
}




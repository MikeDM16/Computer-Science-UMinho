BEGIN{ 
	print "---------- Autores Musicais ----------"
	print "Listagem das canções dos diversos cantores\n"
	RS = "\n"
	FS = " "
}

$1 ~ /title/{
			gsub("title:", "")
			n = split($0, titleAux, " ")
			titulo = ""
			for(i in titleAux){
				if( titleAux[i] != "(?)"){ 
					titulo = titulo " " titleAux[i]
				}
			}
}

$1 ~ /author/{ 
			gsub("author:", "")
			n = split($0, nome, " ")
			autor = ""
			for(i in nome){
				if (nome[i]== "?"){
					autor = "Nao identificado"
				}else {
					if( nome[i] != "(?)"){ 
						autor = autor " " nome[i]
					}
				}
			}
			listMusicas[autor][titulo]
}

END{
	for(i in listMusicas){
		printf("Nome do autor: %s\n", i)
		printf("Número de canções: %s \n", length(listMusicas[i]))
		printf("Lista de canções\n")
		for(j in listMusicas[i]){
			if( length(listMusicas[i]) == 1 ){
				printf("%s ", j)
			}else{ 

			if( j ==  length(listMusicas[i]) ){
				printf("%s. ",j)
			}
			else{ 
				printf ("%s, ",j)
			}
		}
		}
		print "\n"

	}
}






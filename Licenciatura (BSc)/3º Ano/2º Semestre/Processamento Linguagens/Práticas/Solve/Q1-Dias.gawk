
BEGIN { print "---- Tratamento Dados ViaVerde -----"
		print "Relatório entradas por dia\n"
}

$1 ~ /<DATA_ENTRADA>/{ 
					gsub("<?DATA_ENTRADA>", "")
					
					FS = "-"
					dia = $1
					mes = $2
					if( (dia!=null) && (mes!=null)){ entradas[dia]++ }
} 
END{
	printf("%7s %15s", "Dias", "Nr Entradas\n")
	print "------------------------------------"
	for(i in entradas){
		printf("%6s %12s\n", i, entradas[i])
	}
	print "-------------------------------------"
}


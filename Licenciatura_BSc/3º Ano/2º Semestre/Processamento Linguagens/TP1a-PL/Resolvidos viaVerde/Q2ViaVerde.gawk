
BEGIN { print "--------- Tratamento Dados ViaVerde -----------"
		print "Lista de Locais de Saida\n"
}

$1 ~ /<SAIDA>/{
					gsub("</?SAIDA>", "")
					FS = "\n"
					local = $1
					if( local ){ saidas[local]++ }
} 
END{
	printf ("%20s %18s","Locais de saida","Nº de saídas\n")
	print "-----------------------------------------------"
	for(i in saidas){
		printf("%20s %12s \n", i, saidas[i])
	}
	print "-----------------------------------------------"
}
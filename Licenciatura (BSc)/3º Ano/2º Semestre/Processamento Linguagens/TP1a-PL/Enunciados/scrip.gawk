BEGIN{ 
	print "---------- Tratamento Dados ViaVerde ----------"
	print "Registos gastos Mensais\n"
}

$1 ~ /singer/{ 
			gsub("singer:", "")
			FS = "\n"
			listSinger[$1]++;

}

}
END{

	print "-----------------------------------------------------------------"
	for(i in listSinger){
		print listSinger[i];
	}
	print "-----------------------------------------------------------------"
}


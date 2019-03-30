
BEGIN { print "---- Tratamento Dados ViaVerde ----"
		print "Relatório mensal de entradas"
}

$1 ~ /<DATA_ENTRADA>/{ 
					gsub("</?DATA_ENTRADA>", "")

					FS = "-"
					dia = $1
					mes = $2
					if( (dia!=null) && (mes!=null)){ 
						entradas[mes][dia]++ 
						regMensal[mes]++
					}
} 
END{
	for(i in entradas){
			print "----------------------------------------"		
		if(i == 1){
			printf("%10s %10s registos entradas\n","Janeiro", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 2){
			printf("%10s %10s registos entradas\n","Fevereiro", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 3){
			printf("%10s %10s registos entradas\n","Março", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 4){
			printf("%10s %10s registos entradas\n","Abril", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 5){
			printf("%10s %10s registos entradas\n","Maio", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 6){
			printf("%10s %10s registos entradas\n","Junho", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 7){
			printf("%10s %10s registos entradas\n","Julho", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 8){
			printf("%10s %10s registos entradas\n","Agosto", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 9){
			printf("%10s %10s registos entradas\n","Setembro", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 10){
			printf("%10s %10s registos entradas\n","Outubro", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i == 11){
			printf("%10s %10s registos entradas\n","Novembro", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
		if(i==12){ 
			printf("%10s %10s registos entradas\n","Dezembro", regMensal[i])
			printf("%6s %16s", "Dia", "Nr Entradas\n")
		}
        for(j in entradas[i]){
				printf("%6s %12s\n", j, entradas[i][j])			
			}
			print "----------------------------------------"
	}
}


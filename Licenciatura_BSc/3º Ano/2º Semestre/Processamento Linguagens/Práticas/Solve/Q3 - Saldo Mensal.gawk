
BEGIN { print "---- Tratamento Dados ViaVerde ----"
		print "Registos gastos Mensais\n"

}

$1 ~ /<DATA_ENTRADA>/{ 
			gsub("</?DATA_ENTRADA>", "")
			split($0, datas, "-" )
			mes = datas[2]

}

$1 ~ /<IMPORTANCIA>/{
			gsub("</?IMPORTANCIA>", "")
			split($0, preco, ",")
			valor = preco[1] + preco[2]/100
}

$1 ~ /<VALOR_DESCONTO>/{
			gsub("</?VALOR_DESCONTO>", "")
			desc = $1
}

$1 ~ /<TAXA_IVA>/{
			gsub("</?TAXA_IVA>", "")
			iva = $1 / 100
		contri = (valor - (valor*desc)/100) * (1 + iva)
		if(mes) { saldoMensal[mes] += contri }
			else { semReg+= contri}
}


END{
	print "----------------------------------------"
	printf("%7s %25s", "Mes", "Valores Gastos\n")
	for(i in saldoMensal){
		switch(i){

			case 1: printf("%8s %19s\n", "Janeiro", saldoMensal[i])
					break;
			case 2: printf("%8s %19s\n", "Fevereiro", saldoMensal[i])
					break;
			case 3: printf("%8s %19s\n", "Março", saldoMensal[i])
					break;
			case 4: printf("%8s %19s\n", "Abril", saldoMensal[i])
					break;
			case 5: printf("%8s %19s\n", "Maio", saldoMensal[i])
					break;
			case 6: printf("%8s %19s\n", "Junho", saldoMensal[i])
					break;
			case 7: printf("%8s %19s\n", "Julho", saldoMensal[i])
					break;
			case 8: printf("%8s %19s\n", "Agosto", saldoMensal[i])
					break;
			case 9: printf("%8s %19s\n", "Setembro", saldoMensal[i])
					break;
			case 10: printf("%8s %19s\n", "Outubro", saldoMensal[i])
					break;
			case 11: printf("%8s %19s\n", "Novembro", saldoMensal[i])
					break;
			case 12: printf("%8s %19s\n", "Dezembro", saldoMensal[i])
					break;
			default: printf("%8s %19s\n", "Sem Registo", semReg)
					break;
		}
	}
	printf("%8s %16s\n", "Sem Registo", semReg)
	print "----------------------------------------"
}




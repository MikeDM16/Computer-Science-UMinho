
BEGIN { print "---------- Tratamento Dados ViaVerde ----------"
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
$1 ~ /<TIPO>/{
			gsub("</?TIPO>", "")
			FS = "\n"
			if( $1 ~ /[Pp]arque/){ 
				contri = (valor - (valor*desc)/100) * (1 + iva)
				if(mes) {saldoMensalP[mes] += contri}
					else{ saldoPsReg += contri}
			}
}
END{
	print "-----------------------------------------------------------------"
	printf("%7s %25s %25s", "Mes", "Valores Gastos", "Gastos em Parques\n")
	for(i in saldoMensal){
		switch(i){

			case 1: printf("%8s %19s %23s\n", "Janeiro", saldoMensal[i], saldoMensalP[i])
					break;
			case 2: printf("%8s %19s %23s\n", "Fevereiro", saldoMensal[i], saldoMensalP[i])
					break;
			case 3: printf("%8s %19s %23s\n", "Março", saldoMensal[i], saldoMensalP[i])
					break;
			case 4: printf("%8s %19s %23s\n", "Abril", saldoMensal[i], saldoMensalP[i])
					break;
			case 5: printf("%8s %19s %23s\n", "Maio", saldoMensal[i], saldoMensalP[i])
					break;
			case 6: printf("%8s %19s %23s\n", "Junho", saldoMensal[i], saldoMensalP[i])
					break;
			case 7: printf("%8s %19s %23s\n", "Julho", saldoMensal[i], saldoMensalP[i])
					break;
			case 8: printf("%8s %19s %23s\n", "Agosto", saldoMensal[i], saldoMensalP[i])
					break;
			case 9: printf("%8s %19s %23s\n", "Setembro", saldoMensal[i], saldoMensalP[i])
					break;
			case 10: printf("%8s %19s %23s\n", "Outubro", saldoMensal[i], saldoMensalP[i])
					break;
			case 11: printf("%8s %19s %23s\n", "Novembro", saldoMensal[i], saldoMensalP[i])
					break;
			case 12: printf("%8s %19s %23s\n", "Dezembro", saldoMensal[i], saldoMensalP[i])
					break;
			default: printf("%8s %19s %23s\n", "Sem Registo", semReg, saldoPsReg)
					break;
		}
	}
	printf("%8s %16s\n", "Sem Registo", semReg)
	print "-----------------------------------------------------------------"
}


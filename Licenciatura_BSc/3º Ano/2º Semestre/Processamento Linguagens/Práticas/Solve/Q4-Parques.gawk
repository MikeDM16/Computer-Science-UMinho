
BEGIN { print "---- Tratamento Dados ViaVerde ----"
		print "Registos gastos Mensais relativos a Parques\n"

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
}
$1 ~ /<TIPO>/{
			gsub("</?TIPO>", "")
			FS = "\n"
			if( $1 ~ /[Pp]arque/){ 
				contri = (valor - (valor*desc)/100) * (1 + iva)
				saldoMensalP[mes] += contri
			}
}
END{
	print "-----------------------------------------------"
	printf("%7s %25s", "Mes", "Valores Gastos\n")
	for(i in saldoMensalP){
		switch(i){

			case 1: printf("%8s %19s\n", "Janeiro", saldoMensalP[i])
					break;
			case 2: printf("%8s %19s\n", "Fevereiro", saldoMensalP[i])
					break;
			case 3: printf("%8s %19s\n", "Março", saldoMensalP[i])
					break;
			case 4: printf("%8s %19s\n", "Abril", saldoMensalP[i])
					break;
			case 5: printf("%8s %19s\n", "Maio", saldoMensalP[i])
					break;
			case 6: printf("%8s %19s\n", "Junho", saldoMensalP[i])
					break;
			case 7: printf("%8s %19s\n", "Julho", saldoMensalP[i])
					break;
			case 8: printf("%8s %19s\n", "Agosto", saldoMensalP[i])
					break;
			case 9: printf("%8s %19s\n", "Setembro", saldoMensalP[i])
					break;
			case 10: printf("%8s %19s\n", "Outubro", saldoMensalP[i])
					break;
			case 11: printf("%8s %19s\n", "Novembro", saldoMensalP[i])
					break;
			case 12: printf("%8s %19s\n", "Dezembro", saldoMensalP[i])
					break;
			default: break;
		}
	}
	print "-----------------------------------------------"
}


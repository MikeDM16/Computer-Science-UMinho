#!/usr/bin/env python3
import Traducao
import Text_Analysis
import ANN_Keras
import csv

def save_csv(M_data, file_name):
	with open(file_name, "w", newline='', encoding='utf-8') as f:
		writer = csv.writer(f, delimiter=";")
		falhou = 0;
		for row in range(0,len(M_data)):
				try:
					writer.writerow(M_data[row])
				except Exception as e:
					falhou += 1
		print("Save CSV falhou " + str(falhou) + " linhas.")

def main(): 
	file_name = "labels.csv"
	# Executados apenas na fase inicial de pre processamento 
	# módulo tradução texto 
	#T = Traducao.Traducao(file_name)
	#dataset = T.read_csv_file()
	# Para salvar ficheiro com traducoes
	# save_csv(dataset, "traduzido.csv")
	
	# Executados apenas na fase inicial de pre processamento 
	# Módulo análise sentimental de texto 
	#T = Text_Analysis.Text_Analysis(dataset)
	# dataset + o valor de sentimento e objetividade de cada frase -> output ANN
	#labels = T.analyse_text()
	#save_csv(dataset, "labelsTrad.csv")
	
	T = ANN_Keras.ANN_Keras()
	T.treino()
	
if __name__ == "__main__": main()
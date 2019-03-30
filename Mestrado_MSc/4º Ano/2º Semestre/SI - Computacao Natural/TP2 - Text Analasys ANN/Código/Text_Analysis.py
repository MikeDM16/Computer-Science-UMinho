from textblob import TextBlob
import numpy as np
# installation of the package: pip install textblob

class Text_Analysis():
	def __init__(self, detaset):
		self.dataset = detaset

	def analyse_text(self):
		''' labels = matriz com duas colunas, com o valor do sentimento e objetividade da frase.
			Nivel sentimento varia entre [-1, 1]
			Nivel objetividade varia entre [0, 1] 
				(subjetividade vista como o nivel de "confiança da medição" --> Aka teoria do prof Neves)
			Serão os dois outputs que a ANN deve prever para cada caso	'''

		# id das colunas que têm frases a analisar
		cols_to_analyse = [2]

		rows = len(self.dataset)
		labels = np.zeros((rows, 2))
		
		erros = 0
		for row in range(0, rows):
			for col in cols_to_analyse:
				try: 	
					frase = TextBlob(self.dataset[row][col])
					labels[row, 0] = frase.sentiment.polarity
					labels[row, 1] = frase.sentiment.subjectivity
				except Exception as e:
					erros += 1
		print("Análise deu erro em " + str(erros) + " linhas.")

		# Adicionar o nome das novas colunas
		self.dataset[0].append("Polarity")
		self.dataset[0].append("Subjectivity")
		# Adcionar o valor das novas colunas
		for row in range(1, rows):
			pol = labels[row, 0]; sub = labels[row, 1]
			if((pol < -0.33)):
				self.dataset[row].append(-1) #negativo
			elif((pol > 0.33)):
				self.dataset[row].append(1) #positivo 
			else:
				self.dataset[row].append(0) #neutro
			
			self.dataset[row].append(round(sub,2))
			#self.dataset[row].append(round(labels[row, 0],2))
			#self.dataset[row].append(round(labels[row, 1],2))

		return(self.dataset)

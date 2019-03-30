from googletrans import Translator
import numpy as np
import xlrd, copy
import csv

# installation of the package: pip install googletrans, xlrd, openpyxl

class Traducao():
	def __init__(self, file_name):
		self.file_name = file_name

	def read_csv_file(self):
		# Abrir ficheiro CSV
		f = open(self.file_name, encoding='utf8', mode="r")
		reader = csv.reader(f, delimiter=';')

		# agrupar todas as rows numa lista de listas
		self.M_data = []
		for row in reader:
			self.M_data.append(row)

		# Array com o nome das colunas -> linha 0 
		column_names =  self.M_data[0]

		# Obter número de linhas e colunas do ficheiro
		self.ncols = len(column_names)
		self.nrows = len(self.M_data)

		# return(self.traduzir())
		return(self.M_data)

	def traduzir(self):
		# id das colunas que têm frases a traduzir para inglês
		# coluna textPost - 2 + coluna Hashtags - 3
		cols_to_translate = [2, 3]

		# O ciclo de tradução demora bastante, devido aos pedidos de tradução
		translator = Translator()
		
		falhou = 0
		for row in range(0, self.nrows):
			for col in cols_to_translate:
				''' campo text -> tradução 
			    campo dest -> linguagem destino da tradução. Por omissão traduz para ingles 'en'
			    campo src  -> linguagem da frase inicial a traduzir. Por omissão deteta a linguagem'''
				try:
					trad = translator.translate(self.M_data[row][col])
					self.M_data[row][col] = trad.text
				except Exception as e:
					falhou += 1

		print("Tradução deu erro em " + str(falhou) + " linhas.")
		return(self.M_data)

#!/usr/bin/env python3

import tkinter as tk # for GUI
import cv2
import Tratamento

class Interface:
	def __init__(self, windows):
		self.windows_names = windows

	def exit(self):
		self.root.destroy()
		for w in self.windows_names:
			cv2.destroyWindow(w)

	def restart(self):
		self.root.destroy()
		for w in self.windows_names:
			cv2.destroyWindow(w)
			
		Tratamento.Tratamento()

	def help_window(self):
		self.root = tk.Tk()
		S = tk.Scrollbar(self.root)
		T = tk.Text(self.root, height=15, width=65)
		S.pack(side=tk.RIGHT, fill=tk.Y)
		T.pack(side=tk.LEFT, fill=tk.Y)
		S.config(command=T.yview)
		T.config(yscrollcommand=S.set)
		
		quote = "- Instructions for segmentation:\n"
		quote += "    Right click mouse -> Draw box for initial segmentation\n"
		quote += "    Key n -> Update segmentation\n"
		quote += "\n"
		quote += "- Options to adjust detailed segmentation, by drawing lines\n"
		quote += "whit the left click mouse: \n"
		quote += "    Left click mouse  -> Draw lines for detailed segmentation\n"
		quote += "    Key 0 -> Mark lines for obvious background\n"
		quote += "    Key 1 -> Mark lines for obvious foreground\n"
		quote += "    Key 2 -> Mark lines for probable background\n"
		quote += "    Key 3 -> Mark lines for probable foreground\n"
		quote += "\n"
		quote += "- To exit during segmentation process:\n"
		quote += "    Key q -> Quits program \n"
		
		T.insert(tk.END, quote)
		tk.mainloop()

	def legenda(self, n, f, g, o, cx ):
		self.root = tk.Tk()

		tk.Label(self.root,
			text="   Necrosis: " + str(n) + "%",
			fg = "black",
			bg = "red",
			font = "Times").pack()

		tk.Label(self.root, 
			text="Fibrin: " + str(f) + "%",
			fg = "black",
			bg = "orange",
			font = "Times").pack()

		tk.Label(self.root, 
			text="Granulation: " + str(g) + "%",
			fg = "black",	
			bg = "yellow",
			font = "Times").pack()

		tk.Label(self.root, 
			text="Unknown: " + str(o) + "%",
			fg = "white",	
			bg = "blue",
			font = "Times").pack()

		# Botão para sair
		btn1 = tk.Button(self.root, text="Exit", command=self.exit)
		btn1.pack(side="bottom", fill="both", expand="yes", padx="10", pady="10")

		btn2 = tk.Button(self.root, text="Restart", command=self.restart)
		btn2.pack(side="bottom", fill="both", expand="yes", padx="10", pady="10")
		
		x = 2*cx + 60
		y = 30 
		# set the dimensions of the screen 
		# and where it is placed
		self.root.geometry('+%d+%d'%(x,y)) 
		self.root.mainloop()
	
	
#!/usr/bin/env python3

import numpy as np
from skimage import filters, feature
import cv2
from PIL import Image, ImageColor
from colorsys import rgb_to_hls, hls_to_rgb
import pylab
import Interface
from GrabCut import *
import os
import sys
import mahotas
from tkinter import messagebox
from tkinter import *

class Tratamento():
    def __init__(self):
        # Interface inicial para seleção de uma imagem 
        self.root = tk.Tk()

        # create a button, then when pressed, will trigger a file chooser
        # dialog and allow the user to select an input image; then add the
        # button the GUI
        btn1 = tk.Button(self.root, text="Exit", command=self.exit)
        btn1.pack(side="bottom", fill="both", expand="yes", padx="10", pady="10")

        btn2 = tk.Button(self.root, text="Select an image", command=self.select_image)
        btn2.pack(side="bottom", fill="both", expand="yes", padx="10", pady="10")      

        btn3 = tk.Button(self.root, text="Help", command=self.help)
        btn3.pack(side="bottom", fill="both", expand="yes", padx="10", pady="10")

        self.root.geometry('+%d+%d'%(40,30))  
        # kick off the GUI
        self.root.mainloop()

    def help(self):
        interf = Interface.Interface([])
        interf.help_window()
    
    def exit(self):
        self.root.destroy()


    def validate_image_path(self, filename):
        if not os.path.exists(filename):
            print("Image path does not exist.")
            return False
        
        return True

    def select_image(self):
        # Caixa de dialogo para procurar um ficheiro, devolvendo a diretoria do ficheiro selecionado
        path = filedialog.askopenfilename()

        # Verificação da integridade da diretoria encontrada e se o ficheiro existe
        if (self.validate_image_path(path)):
            # Remover a janela inicial de seleção de imagem
            self.root.destroy()

            # Arrancar a fase de segmentação iterativa 
            gc = GrabCut(path)
            output = gc.run()

            # Realizar o processamento da área segmentada
            self.processamento(path, output)

    def processamento(self, filename, segmentation):
        # Load da imagem inicial, realizando a correção de BGR para RGB, trocando o canal azul 
        # o vermelho  
        aux = pylab.imread(filename)
        img = aux.copy()
        img[:, :, 0] = aux[:, :, 2]
        img[:, :, 2] = aux[:, :, 0]
        res = img.copy()

        l, c, _ = img.shape
        windows_names = []

        # Converter a regiao segmentada para uma escala de cinzentos, para aplicar o algortimos de canny
        gray = cv2.cvtColor(segmentation, cv2.COLOR_BGR2GRAY)
        # Aplicação do algoritmos e canny para ficar com as edges da região segmentada.
        # Serve como mascara para aplicar uma linha de  contorno visual na imagem original, para identificar a regiao selecionada
        edges = cv2.Canny(gray, 50, 100)

        # desenhar uma linha verde a contornar a região segmentada, na imagem original
        # Onde a mascara for superior a zero, colocar valores RGB a verde. 
        # img[ edges > 0 ] = [0,255,0]

        # Criação da janela para mostrar a imagem inicial com a linha de contorno na região segmentada
        cv2.namedWindow('input')        # Create a named window
        cv2.moveWindow('input', 40,30)  # Move it to (40,30)
        cv2.imshow("input",  img)
        windows_names.append("input")

        necrosis = 0 + 1e-5
        fibrin = 0 + 1e-5
        granulation = 0 + 1e-5
        unknown = 0 + 1e-5
        area_total = 0 + 1e-5

        id_l, id_c = np.nonzero(segmentation[:,:,1] > 0)
        #for i,j in zip(id_l, id_c):
        for i in range(0,l):
            for j in range(0,c):
                if not (np.array_equal(segmentation[i,j], [0,0,0]) ):
                    area_total += 1
                    B = segmentation[i-1:i+1, j-1:j+1, 0].mean()
                    G = segmentation[i-1:i+1, j-1:j+1, 1].mean()
                    R = segmentation[i-1:i+1, j-1:j+1, 2].mean()
                    (H,S,L) = self.rgb2hsl(R,G,B)

                    # Zonas negras da feridas identificadas como necrose
                    if(L < 50):
                        res[i,j] = [0,0,255]
                        necrosis += 1

                    # Zonas amareladas associadas a fibroses ou tecidos expostos
                    elif( L > 200 or (H>20 and H<45) ):
                        res[i,j] = [0, 155, 255]
                        fibrin += 1

                    # Zona vermelha identificada como granulação 
                    elif((H<20 or H>230)):
                        res[i,j] = [0,255,255]
                        granulation += 1

                    # zonas desconhecidas
                    else:
                        res[i,j] = [255,0,0]
                        unknown += 1
        
        cv2.namedWindow('Resultado')        # Create a named window
        cv2.moveWindow('Resultado', c + 50,30)  # Move it to (40,30)
        cv2.imshow("Resultado", res)
        windows_names.append("Resultado")

        necrosis = round((necrosis/area_total)*100)
        fibrin = round((fibrin/area_total)*100)
        granulation = round((granulation/area_total)*100)
        unknown = round((unknown/area_total)*100)


        interf = Interface.Interface(windows_names)
        interf.legenda(necrosis, fibrin, granulation, unknown, c )
  
        k = 0xFF & cv2.waitKey(0)
    

    def rgb2hsl(self, R,G,B):
        var_R = ( R / 255 )
        var_G = ( G / 255 )
        var_B = ( B / 255 )

        var_Min = min( var_R, var_G, var_B )    # Min. value of RGB
        var_Max = max( var_R, var_G, var_B )    # Max. value of RGB
        del_Max = var_Max - var_Min             # Delta RGB value

        L = ( var_Max + var_Min )/ 2            # Lightness calculation

        if( del_Max == 0 ):                    # This is a gray, no chroma...
            H = 0
            S = 0   

        else:                                   # Chromatic data...
            if ( L < 0.5 ):
                S = del_Max / ( var_Max + var_Min )
            else:
                S = del_Max / ( 2 - var_Max - var_Min )

            del_R = ( ( ( var_Max - var_R ) / 6 ) + ( del_Max / 2 ) ) / del_Max
            del_G = ( ( ( var_Max - var_G ) / 6 ) + ( del_Max / 2 ) ) / del_Max
            del_B = ( ( ( var_Max - var_B ) / 6 ) + ( del_Max / 2 ) ) / del_Max
            
            if( var_R == var_Max ):
                H = del_B - del_G
            elif ( var_G == var_Max ):
                H = ( 1 / 3 ) + del_R - del_B
            elif ( var_B == var_Max ):
                H = ( 2 / 3 ) + del_G - del_R

        if ( H < 0 ): H += 1
        if ( H > 1 ): H -= 1

        H *= 240
        S *= 240
        L *= 240

        return (H,S,L)

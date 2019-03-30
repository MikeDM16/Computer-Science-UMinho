import numpy as np, mahotas 
import skimage
import matplotlib
from skimage import filters, feature
from io import BytesIO
from IPython.display import Image
from ipywidgets import interact, interactive, fixed
from scipy import fftpack, ndimage, signal
import math, copy
#gray()


#-----------------------------------------------------------------------------------------------
#   Função de Labeling. 
#   - Recebe como argumento a imagem sobre a qual extrair os componentes ligados 
#   - Devolve o conjunto de elementos ligados de uma imagem, juntamente o número de 
# objetos encontrados  
def labeling(img):
    # Determinar o sub conjunto de objetos da imagem
    labeling, nr_objetos = mahotas.label(img)
    return labeling, nr_objetos
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#   Função para remoção de regiões ligadas. 
#   - Recebe como argumento uma lista com o conjunto de componentes ligadas de uma imagens pré processada
#   - Neste contexto remove apenas o fundo da imagem. ''' 
def remove_regions(labeling):
    # Determinar o tamanho de cada objeto da imagem 
    tamanhos = mahotas.labeled.labeled_size(labeling)
    # Ordenar os objetos retornados por tamanho 
    tamanhos.sort()
    
    # Remover os componentes ligados com tamanho superior ao do fundo
    t_borda = tamanhos[1]
    fundo = np.where(tamanhos > t_borda)
    removidos = mahotas.labeled.remove_regions(labeling, fundo)
    return removidos 
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#   Função de binarização. 
#   - Recebe como argumento a imagem a binarizar. 
#   - Retorna a matriz binária da imagem binarizada e o valor de corte 
# utilizado como threshold 
def binarizacao(img):
    # Converter argumento de entrada para imagem em nivel de cinzentos, caso não o seja
    matplotlib.pyplot.gray()

    # Inicialização da matriz para a imagem binarizada a zeros
    bin = np.zeros_like(img)
    
    # Valor a aplicar como treshold à imagem
    threshold_otsu = skimage.filters.threshold_otsu(img.astype('uint8'))
    
    #   Nas posições da imagem inicial onde a intensidade do pixel seja 
    # superior à intensidade de corte definida pelo threshold, o pixel 
    # toma o valor de 1 (branco)
    # for i in range(img.shape[0]):
    #   for j in range(img.shape[1]):
    #       if(img[i,j] > threshold_otsu):
    #           bin[i,j] = 1  
    bin = img > threshold_otsu
    
    return (bin, threshold_otsu)
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#   Função de criação da mascara de uma imagem da retina. 
#   Argumentos entrada: 
#       - imagem sobre a qual calcula a mascara da retina 
#   - Retona a mascara da retina e a mascara da borda (contorno) da retina ''' 
def mascaras(img):
    # Gradiente: Aplicação do filtro de canny à imagem iriginal, para realçar 
    # as transições fortes 
    f_canny = skimage.feature.canny(img, sigma=2)

    # Binarização da imagem resultante da aplicação do filtro de Canny à imagem origina. 
    # Binariza os contornos acentuados através do filtro de Canny
    # Usa a função binarizacao definida no inicio deste módulo 
    bin_f_canny, _ = binarizacao(f_canny)
    
    #   Aplicação da morfologia matemática de dilatação ao resultado da binarização do filtro de Canny, 
    # como forma de completar e evidenciar o contorno da borda da retirna. 
    dil_bin_f_canny = ndimage.morphology.binary_dilation(bin_f_canny)

    # Utilizar a função labeling para remover o fundo interior e ficar só com a borda
    # Função labeling e remove_regions definidas acima neste módulo 
    labels, n = labeling(dil_bin_f_canny)
    borda_reg = remove_regions(labels)
    
    # A Mascara da retina é conseguida pelo holefilling da contorno da borda da retina
    # O python resolve a questão da seed necessária ao algoritmo 
    borda_retina_mask = borda_reg
    retina_mask = ndimage.morphology.binary_fill_holes(borda_retina_mask)
        
    return (retina_mask, borda_retina_mask)
#-----------------------------------------------------------------------------------------------
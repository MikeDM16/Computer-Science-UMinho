# Third-party libraries
import numpy as np
import mahotas, time, skimage, scipy, math, copy, sys, pathlib, pickle
from PIL import Image
from matplotlib import pylab
from pylab import * 
from skimage import filters, feature
from io import BytesIO
from IPython.display import Image
from ipywidgets import interact, interactive, fixed
import scipy.sparse as sp
from multiprocessing import Process
from random import randint

import TPC5

#-----------------------------------------------------------------------------------------------
#   Função para calcular os valores (x,y) de cada ponto de uma das 12 retas.
#   - Omissão que as retas têm o centro do referêncial no indice [7,7] da janela 15x15
#   - Cada uma das 12 retas ocupa 15 pixeis dentro da janela (arredondamento int)
#   - Devolve duas matrizes 12x15, com as coordenas Xs e Ys respetivamente
#   - Não recebe argumentos. A geração dos pontos das 12 retas é independe das imagens
#     Este fator apenas depende do tamanho da janela e do ângulo entre as retas, que são 
# conhecidos nesta abordagem
def gera_Pontos_Retas():
    start_time = time.time()
    angle = 15 # Angulo entre as retas. 

    # Inicialização das matrizes a retornar
    coord_X = np.zeros((12,15), dtype=np.int8)
    coord_Y = np.zeros((12,15), dtype=np.int8)
    
    for inc in range(0,12): # Iteração para cada um das retas
        alfa = angle * inc # Incrementar o angulo para cada reta L_i
        m = math.tan( math.radians(alfa) ) #Declive reta = tang do angulo em radianos
        
        pontos_X = []
        pontos_Y = []
        # Se angulo entre 45 e 135 operar sobre o Y para evitar valores de X fora da janela
        if( (alfa>=45) and (alfa<=135) ):
            for y in range (-7, 8):
                x = round( y / m )  # y = mx + b => x = (y-b)/m
                pontos_X.append(x)
                pontos_Y.append(y)    
        # Se angulo entre [0, 45[ ou ]135, 180] operar sobre o X para evitar valores de Y fora da janela      
        else:
            for x in range (-7, 8):
                y = round(m * x)
                pontos_X.append(x)
                pontos_Y.append(y)
                
        coord_X[inc] = pontos_X
        coord_Y[inc] = pontos_Y

    #print("--- %s seconds Gera_Pontos_Retas---" % (time.time() - start_time))
    return (coord_X, coord_Y)
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#    Função de cálculo das matrizes I_inv, S e S0. 
#    Calcula previamente os valores de x e y das retas para duas matrizes. 
#    Otimização com listas de copreensão
#    Argumentos entrada: 
#       - n: indica o número da imagem a processar, segundo a numeração da DRIVE
#       - input_dir e output_dir: para indicar de onde ler e onde guardar as imagens criadas
#       - opt: opção para indicar se a imagem é de teste ou treino. Porque o nome dos ficheiros
# varia conforme o tipo de imagem (se treino ou teste)
def gere_caracteristicas(n, input_dir, output_dir, opt):
    start_time = time.time()
    
    if(opt == "teste"): # numeradas de 01, 02, 03 ... até 20
        if(n < 10): # para as imagem teste numeradas por 01, 02, ... 09 
            img_teste = pylab.imread(str(input_dir) + "images/0" + str(n) + "_test.tif")[:,:,1]
            mask_img = pylab.imread(str(input_dir) + "mask/0" + str(n) + "_test_mask.gif")
            vasos    = pylab.imread(str(input_dir) + "1st_manual/0" + str(n) + "_manual1.gif")
        else:  # Para as imagens teste numeradas por 11, 12, ..., 20
            img_teste = pylab.imread(str(input_dir) + "images/" + str(n) + "_test.tif")[:,:,1]
            mask_img = pylab.imread(str(input_dir) + "mask/" + str(n) + "_test_mask.gif")
            vasos = pylab.imread(str(input_dir) + "1st_manual/" + str(n) + "_manual1.gif")
    else:# gerar indices para as imagens de treino, numeradas de 21 a 41 
            img_teste = pylab.imread(str(input_dir) + "images/" + str(n) + "_training.tif")[:,:,1]
            mask_img = pylab.imread(str(input_dir) + "mask/" + str(n) + "_training_mask.gif")
            vasos = pylab.imread(str(input_dir) + "1st_manual/" + str(n) + "_manual1.gif")
    
    # Imagem invertida da imagem da retina inicial 
    # img_invertida = (255- img_teste) --> alternativa 
    img_invertida = np.invert(img_teste)

    # Mascara da imagem invertida, utilizando os processos desenvolvidos no TPC5 
    # A mascara é relevante para operar apenas dentro da área da retina, evitando processa o fundo
    # preto à volta da retina 
    mask_img_inv, _ = TPC5.mascaras(img_invertida)

    # Dimensões das imagens que representarão as caracteristicas a calcular
    L,C = img_teste.shape
    # Inicialização das caracteristicas como matrizes de zeros
    S_img = np.zeros((L, C))
    S0_img = np.zeros((L, C))

    # Uso da função que gera os pontos de cada umas das 12 retas inicialmente
    coord_X, coord_Y = gera_Pontos_Retas()

    #  - Poderia ser feito uma condição em cada posição (i,j), como forma de determinar se
    #  aquele índice, na máscara da imagem, tem um valor superior a zero. 
    #  - Com a utilização do método nonzero é possível obter em duas listas todos os 
    # índices (i,j) que correspondem APENAS às posições da mascara dentro da área da retina.
    id_l, id_c = np.nonzero(mask_img_inv > 0)
    for i,j in zip(id_l, id_c):
        # Obter a janela 15x15 da iteração (i,j) 
        janela = img_invertida[i-7:i+8, j-7:j+8].copy()

        #  - Obter a equivalente janela 15x15, mas na mascara binária da imagem invertida
        #  - Necessária para saber se a janela 15x15 com o pixel (i,j) no centro contêm 
        # elementos fora da borda da retina (background)
        pts_mask = mask_img_inv[i-7:i+8, j-7:j+8]
        
        # Verificar se o pixel se encontra próximo da borda da imagem, ou seja, 
        # se a janela 15x15 com esse pixel no centro apanha pontos fora da área da retina;
        if(pts_mask.sum() < 255):
            # indices dos pontos de background -> branco na mascara
            l_BG, c_BG = np.nonzero(pts_mask == 0)
            
            # indices dos pontos de foreground -> preto na mascara
            l_FG, c_FG = np.nonzero(pts_mask == 1)
           
            # Média só dos pontos da janela, que estão dentro da mascara
            N = janela[l_FG, c_FG].mean()
            
            # Alterar as intensidades dos pontos da janela 15x15 que estão no BG (fora mascara) para o valor de N 
            janela[l_BG, c_BG] = N

        N = janela.mean() # média da janela quadrada   
      
        # matriz dimensões 12x15 
        # - As colunas são as intensidades (15 pontos) de uma linha L, 
        # - As linhas representam cada uma das 12 linhas L
        intensidades = [ janela[7 + coord_Y[reta], 7 + coord_X[reta]] for reta in range(0,12)]
        
        # array dimensões 12x1 com a média das intensidades das 12 linhas
        medias = [(mean(intensidades[reta])) for reta in range(0,12)]

        # O valor de S escolhido usa a linha com a maior média de intensidade
        S_img[i,j] = max(medias) - N 
        
        # - Para a reta que apresenta a maior média de intensidades, calcula-se a sua reta 
        # perpendicular. Para este reta, designada por L0, calcula-se também a sua média de 
        # intensidades, mas utilizando apenas os seus 3 pontos centrais. 
        # - O valor da matriz S0 na posição (i,j) toma o valor L0 - N. 
        
        # - Se a reta estiver no indice 1, a reta perpendicular está no indice 1 + 6
        # retas com diferença de 15º --> 90/15 = 6 indices 
        r = (np.argmax(medias) + 6)%12

        # L0 só usa os três pontos centrais --> [5:8]
        intensidades = janela[7 + coord_Y[r][5:8], 7 + coord_X[r][5:8]]
        S0_img[i,j] = mean(intensidades) - N 

    # guardar as caracteristicas para utilização futura 
    pylab.imsave(str(output_dir) + "/S_img/" + str(n)+'_S.gif', S_img)
    pylab.imsave(str(output_dir) + "/Vasos/" + str(n)+'_Vasos.gif', vasos)
    pylab.imsave(str(output_dir) + "/S0_img/" + str(n)+'_S0.gif', S0_img)
    pylab.imsave(str(output_dir) + "/I_Inv/" + str(n)+'_I_Inv.gif', img_invertida)
    gere_indices(n, input_dir, output_dir, opt)

    if(opt=="teste"):
        print(" --- " + str( round((time.time() - start_time),0)) + " seconds to process S, S0, I_Inv and 1k random indices for image " + str(n) + "_test ---")
    else:  
        print(" --- " + str( round((time.time() - start_time),0)) + " seconds to process S, S0, I_Inv and 1k random indices for image " + str(n) + "_traning ---")
#-----------------------------------------------------------------------------------------------


#-----------------------------------------------------------------------------------------------
#   - gere_indices: Função que gera um conjunto de 1000 indices i,j aleatórios, que serão 
# utilizados para retirar pixels de uma imagem, para uso na SVM. 
#   - Só seleciona posições i,j de indices que pertencam ao interior da retina do olho --> usa mascara retina
#   Argumentos entrada: 
#       - n: indica o número da imagem a processar, segundo a numeração da DRIVE
#       - input_dir e output_dir: para indicar de onde ler a imagem e onde guardar a matriz com os indices
#       - opt: opção para indicar se a imagem é de teste ou treino. Porque o nome dos ficheiros
# varia conforme o tipo de imagem (se treino ou teste)
def gere_indices(n, input_dir, output_dir, opt):
    # Carregar a mascara da imagem, como forma de selecionar indices apenas dentro da área da retina
    if(opt == "teste"): # numeradas de 01, 02, 03 ... até 20
        if(n < 10): # para as imagem teste numeradas por 01, 02, ... 09 
            mask = pylab.imread(str(input_dir) + "mask/0" + str(n) + "_test_mask.gif")
        else:  # Para as imagens teste numeradas por 11, 12, ..., 20
            mask = pylab.imread(str(input_dir) + "mask/" + str(n) + "_test_mask.gif")
    else:# gerar indices para as imagens de treino, numeradas de 21 a 41 
            mask = pylab.imread(str(input_dir) + "mask/" + str(n) + "_training_mask.gif")
    
    # Inicialização da matriz 1000x2 que será guardada
    coords = np.zeros((1000,2))

    # Selecionar apenas os indices que pertencem dentro da área da retina 
    id_l, id_c = np.nonzero(mask > 0)
    for ponto in range(0,1000): # Iterar 1000 vezes, até obter mil pixeis distintos
        # selecionar um indice aleatório dentro dos indices devolvidos pela funcao nonzero 
        indice = randint(0, len(id_l)-1)

        # Recolher os valores  (i,j) do indice escolhido 
        coords[ponto] = [ id_l[indice], id_c[indice]]
        
        # Remover o pixel já utilizado das possiveis futuras hipoteses
        id_l = np.delete(id_l, indice, axis=0)
        id_c = np.delete(id_c, indice, axis=0)

    # Guardar a matriz num ficheiro binário 
    output = open(str(output_dir) + "/Indices/" + str(n)+'_indices.pkl', 'wb')
    pickle.dump(coords, output)
    output.close()
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#    Gera_Features_SVM Função responsável por processar cada uma das imagens de treino e teste,
# #guardando o resultado das imagens S, S0 e I_Inv numa imagem para posterior utilização
#    Argumentos entrada: 
#       - input_dir e output_dir: para indicar de onde ler e onde guardar as imagens criadas
#       - opt: opção para indicar se a imagem é de teste ou treino. Porque o nome dos ficheiros
# varia conforme o tipo de imagem (se treino ou teste)
def gera_Features_SVM(input_dir, output_dir, opt):
    start_time = time.time()
    print(time.ctime())

    # Por omissão processa as imagens de treino (pasta training da DRIVE). 
    # A numeração dessas imagens vai da imagem 21 à 40 
    nr_imgs = 21 
    if(opt == "teste"):
        # Se a opção for para processar as imagens de teste (parta test da DRIVE), a numeração
        # vai de 01,02, ... até 20
        nr_imgs = 1 

    jobs = [] # Lista para guardar os processos a criar em paralelo 
    for i in range(0, 20):
        # Para cada imagem, cria um processo (thread). 
        #   -  thread para calcular I_Inv, S, S0 e 1k indices aleatoreos 
        thread = Process(target=gere_caracteristicas, args=(i+nr_imgs, input_dir, output_dir, opt))
        jobs.append(thread) 

        # Para a versão que executa sequencialmente 
        # gere_caracteristicas(i+nr_imgs, input_dir, output_dir, opt)
 
    # Start the threads   
    for j in jobs:
        j.start()
    
    # Ensure all of the threads have finished
    for j in jobs:
        j.join()
               
    if(opt=="teste"):
        print(" --- " + str(round(((time.time() - start_time)/60),2)) + " min on gera_Features_SVM for all TEST images whit 20 threads ---")
    else:
        print(" --- " + str(round(((time.time() - start_time)/60),2)) + " min on gera_Features_SVM for all TRAINING images whit 20 threads ---")
    print(time.ctime())
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#  Segmentação baseada nas caracteristicas S '''
def segmentacao_pelo_S(input_dir, output_dir):
    start_time = time.time()
    print(time.ctime())

    # assumindo que a diretoria de input coincide com a pasta onde se guardaram as imagens de S,
    # com o nome 21_S.tif até 40_S.tif 
    for i in range(0, 20):
        #Ler imagem S correspondente. 
        S_img  = pylab.imread(str(input_dir) + str("S_img/")  + str(21+i) + str("_S.gif"))[:,:,1]

        # Segmentação baseada em threshold calculado pelo algoritmo de Otsu 
        S_segmentada, _ = TPC5.binarizacao(S_img)

        # Guardar imagem 
        pylab.imsave(str(output_dir) + str(21+i)+ str("_Seg.gif"), S_segmentada)

    print("--- " + str(round(((time.time() - start_time)/60),2)) + " min on  segmentacao_por_S for all TRAINING images ---")
    print(time.ctime())
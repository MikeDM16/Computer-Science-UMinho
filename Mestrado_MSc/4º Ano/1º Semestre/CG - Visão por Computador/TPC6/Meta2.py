# Third-party libraries
import numpy as np
import mahotas, time, skimage, scipy, math, copy, sys, pathlib, pickle
from PIL import Image
from matplotlib import pylab
from skimage import filters, feature
from io import BytesIO
from IPython.display import Image
from ipywidgets import interact, interactive, fixed
import scipy.sparse as sp
from sklearn.utils import shuffle
from random import randint

import SVM_Code, TPC5

#-----------------------------------------------------------------------------------------------
#   Função para leitura das caracteristicas S, S0, I_inv e 1k indices aleatorios de cada imagem 
#    Argumentos entrada: 
#       - número identificador da imagem
#       - dirétoria onde ler as caracteristicas, assumindo a estrutura criada pela função 
# gere_caracteristicas do Módulo Meta1.py
#     Devolve as caracteristicas lidas dos ficheiros 
def ler(n, input_dir):
    
    S_img  = pylab.imread(str(input_dir) + str("S_img/")  + str(n) + str("_S.gif"))[:,:,1]
    S0_img = pylab.imread(str(input_dir) + str("S0_img/") + str(n) + str("_S0.gif"))[:,:,1]
    I_inv  = pylab.imread(str(input_dir) + str("I_Inv/")  + str(n) + str("_I_Inv.gif"))[:,:,1]

    pkl_file = open(str(input_dir) + "Indices/" + str(n)+'_indices.pkl', 'rb')
    pts = np.asarray( pickle.load(pkl_file) )
    pkl_file.close()

    return(I_inv, S_img, S0_img, pts)
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#   Prepara_Dados_SVM : Função responsável por ler os dados das imagens I_inv, S e S0, 
#   - Normaliza os dados por coluna e por imagem (conjunto de 1000 pontos) 
#   - Prepara os atributos para a SVM, agrupando-os numa estrutura matricial. 
#   - A matriz features será 20000x3 (1000 pontos de 20 imagens)x(valores de S, S0 e I_inv)
#     A matriz target será 20000x1
#   
#   - Recebe como argumento de entrada a dirétoria onde ler as caracteristicas e uma opção, que 
# indica se os dados a preparar são de teste ou treino.  
def prepara_Dados_SVM(input_dir, opt):
    start_time = time.time()
    
    nr_imgs = 20
    vasos = []
    # Reuniar as imagnes dos vasos de cada imagem da retina. Necessário para construir a matriz target
    for i in range(0, nr_imgs):
        if(opt=="teste"):
            vasos.append( pylab.imread(str(input_dir) + str("Vasos/") + str(1+i) + str("_Vasos.gif"))[:,:,1] )
        else: 
            vasos.append( pylab.imread(str(input_dir) + str("Vasos/") + str(21+i) + str("_Vasos.gif"))[:,:,1] )
    
    # Inicialização das matrizes a retornar. 
    features = np.zeros((len(vasos) * 1000,3)) # Features terá 1000 pontos por imagem e 3 colunas (S, So e I_inv)
    target = np.zeros( (len(vasos) * 1000,1)) # target terá 1000 pontos por imagem, indicando se é ou nao vaso. 
    
    for i in range(0, len(vasos)):
        # ler as caracteristicas S, S0, I_inv e os 1000 pontos aleatórios onde retirar informaçoes da imagem
        if(opt=="teste"):
            I_inv, S, S0, pts = ler(1+i, input_dir) 
        else:
            I_inv, S, S0, pts = ler(21+i, input_dir)

        # Matrix auxiliar para reunir dados de apenas 1 imagem, com os valores S, S0 e I_inv por coluna
        attr_aux = np.zeros((1000,3))   
        
        # Através dos pontos aleatórios, reunir os 1000 pixeis necessários para a matriz 
        for p in range(0, pts.shape[0]):
            x = int(pts[p][0])
            y = int(pts[p][1])

            attr_aux[p] = [ S[x,y], S0[x,y], I_inv[x,y]]
    
            # Contrução da matriz feature. cada linha toma valor 1 se vasos, 0 se nao vaso
            if(vasos[i][x, y] > 0): # Se na imagem dos vasos o pixel for > 0 (branco) é vaso
                target[i*1000 + p] = 1
        
        # Determinar a média e o desvio padrão por coluna, para a normalização por imagem 
        medias = attr_aux.mean(0)
        stdev  = attr_aux.std(0)
        l, c = attr_aux.shape
        ''' for l in range(attr_aux.shape[0]): # Para cada linha da matrix auxiliar: 
                # Para cada uma das 3 colunas de uma linha, normalizar: 
                attr_aux[l,0] = (attr_aux[l,0] - medias[0] ) / stdev[0]
                attr_aux[l,1] = (attr_aux[l,1] - medias[1] ) / stdev[1]
                attr_aux[l,2] = (attr_aux[l,2] - medias[2] ) / stdev[2]
        '''   
        # Normalizar os valores de cada coluna, segundo a abordagem de Ricci. 
        # Redução do ciclo anterior por listas de compreenção. 
        attr_aux = [[ ((attr_aux[l,c] - medias[c] ) / stdev[c]) for c in range(0,3) ] for l in range(0,1000)]
        
        # Agregar a matriz auxiliar à matriz features total 
        features[i*1000:(i+1)*1000] = attr_aux
    

    # Realizar o shuffle aleatórios das linhas da matriz de treino
    # A função shuflle baralha duas matrizes de forma igual. aspeto importante para mater coerencia!
    random_state = np.random.RandomState(0)
    features, target = shuffle(features, target, random_state=random_state)
    target = target.ravel()

    if(opt=="teste"):
        print("--- %s seconds prepara_Dados_SVM Treino ---" % round((time.time() - start_time),2) )
    else: 
        print("--- %s seconds prepara_Dados_SVM Teste ---" % round((time.time() - start_time),2) )
    
    return features, target
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#   Função para realizar a aprendizagem e avaliação das SVMs
#   Argumentos de entrada
#       - dirétorio onde ler as caracteristicas previemente geradas e necessárias para construir 
# os dados de treino e teste da SVM 
def aprender_SVM(input_dir):
    
    # Preparar dados recolhidos das 20 imagens de treino 
    F_treino, T_treino =  prepara_Dados_SVM(input_dir + str("Treino/"), "treino" )
        # Preparar dados recolhidos das 20 imagens de teste 
    F_teste, T_teste =  prepara_Dados_SVM(input_dir + str("Teste/"), "teste")
    

    SVM_Code.svm_linear_3(F_treino, T_treino, F_teste, T_teste)
    print("\n")
    SVM_Code.svm_svc_linear_3(F_treino, T_treino, F_teste, T_teste)
    print("\n")

    # Obter os melhores parametros de C e Gamma, apenas com o conjunto das 20 imgs de treino, 
    # posteriormente subdividido em conjunto de treino e teste 
    #c, gamma = SVM_Code.svm_rbf_c_g(F_treino, T_treino)
    #print("\n")
    # Uso das 40 imgs, apenas para os valores C e gamma que optimizam o processo de 
    # classificação em teste
    c = 8.6
    gamma = 0.1 
    SVM_Code.svm_rbf_4(F_treino, T_treino, F_teste, T_teste, c, gamma)
    print("\n")
    # Testa da função com os valores existentes por omissão 
    SVM_Code.svm_rbf_4(F_treino, T_treino, F_teste, T_teste, 1000, 0.00001)
    
#SVM imports
import numpy as np
import pylab as pl
from sklearn.datasets import fetch_mldata
from sklearn.svm import LinearSVC
from time import time as tick
from sklearn import svm
from sklearn.svm import SVC
from sklearn.svm import LinearSVC
from sklearn.utils import shuffle
from sklearn import preprocessing
from sklearn.preprocessing import StandardScaler


#-----------------------------------------------------------------------------------------------
#    SVM Linear
#    Os dados já chegam à função misturados aleatoreamente (shuffle) e normalizados segundo os
# os critério do aritgo de Ricci, normalizando por coluna e por imagem. 
#    Argumentos entrada: 
#       - Par de matrizes features e target para treino da SVM
#       - Par de matrizes features e target para avaliação do modelo gerado
def svm_linear_3(X_train,y_train, X_test, y_test):
    clf = LinearSVC()

    tin = tick()
    clf = clf.fit(X_train, y_train)
    tout = tick()
    
    print("SVM Linear 3")
    print("Taxa de sucesso (Treino): ",
          np.mean(clf.predict(X_train) == y_train) * 100)
    print("Taxa de sucesso (Teste): ",
          np.mean(clf.predict(X_test) == y_test) * 100)
    print("Número de vectors de dados (treino/teste): {} / {}".
          format(X_train.shape[0], X_test.shape[0]))
    print('Training time: {:.3f} s'.format(tout - tin))
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#    SVM SVC Linear
#    Os dados já chegam à função misturados aleatoreamente (shuffle) e normalizados segundo os
# os critério do aritgo de Ricci, normalizando por coluna e por imagem. 
#    Argumentos entrada: 
#       - Par de matrizes features e target para treino da SVM
#       - Par de matrizes features e target para avaliação do modelo gerado
def svm_svc_linear_3(X_train,y_train, X_test, y_test):
    clf = SVC(kernel='linear')

    tin = tick()
    clf = clf.fit(X_train, y_train)
    tout = tick()
    print("SVM SVC Linear 3 ")
    print("Taxa de sucesso (Treino): ",
          np.mean(clf.predict(X_train) == y_train) * 100)
    print("Taxa de sucesso (Teste): ",
          np.mean(clf.predict(X_test) == y_test) * 100)
    print("Número de vectors de dados (treino/teste): {} / {}".
          format(X_train.shape[0], X_test.shape[0]))
    print("Número de vectores de suport: ", clf.support_vectors_.shape[0])
    print('Training time: {:.3f} s'.format(tout - tin))
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#    SVM não linear RBF
#    Os dados já chegam à função misturados aleatoreamente (shuffle) e normalizados segundo os
# os critério do aritgo de Ricci, normalizando por coluna e por imagem. 
#    Argumentos entrada: 
#       - Par de matrizes features e target para treino da SVM
#       - Par de matrizes features e target para avaliação do modelo gerado
def svm_rbf_4(X_train,y_train, X_test, y_test, C_aux, gamma_aux):
    clf = SVC(kernel='rbf', C=C_aux, gamma=gamma_aux)
    tin = tick()
    clf = clf.fit(X_train, y_train)
    tout = tick()

    print("SVM RBF c=" + str(C_aux) + ", gamma=" + str(gamma_aux))
    print("Taxa de sucesso (Treino): ",
          np.mean(clf.predict(X_train) == y_train) * 100)
    print("Taxa de sucesso (Teste): ",
          np.mean(clf.predict(X_test) == y_test) * 100)
    print("Número de vectors de dados (treino/teste): {} / {}".
          format(X_train.shape[0], X_test.shape[0]))
    print("Número de vectores de suport: ", clf.support_vectors_.shape[0])
    print('Training time: {:.3f} s'.format(tout - tin))
#-----------------------------------------------------------------------------------------------

#-----------------------------------------------------------------------------------------------
#    SVM Linear
#    Os dados já chegam à função misturados aleatoreamente (shuffle) e normalizados segundo os
# os critério do aritgo de Ricci, normalizando por coluna e por imagem. 
#    Argumentos entrada: 
#       - Par de matrizes features e target
#       - Os argumentos são dividios para serem criados subconjuntos de treino e teste
#       - Executa um conjunto de testes para determinar o melhor C e gamma. 
#     
#     Retorna o valor de C e gamma que optimiza o desempenho de classificação na fase de treino
#     Esses parametros são posteriormente usados para executar esta SVM para com as 20img de treino 
#  e as 20 imgs de teste. 
def svm_rbf_c_g(X, y):
      tin = tick()
      half = int(X.shape[0] / 2)
      X_train, X_test = X[:half], X[half:]
      y_train, y_test = y[:half], y[half:]
      
      c_aux = 0
      g_aux = 0
      pred_aux = 0
      train_aux = 0

      C_range = np.arange(0.1,10,0.5)
      gamma_range = np.arange(0.1,10,0.5)
      for C in C_range:
            for gamma in gamma_range:
                  # fit the model
                  clf = svm.SVC(gamma=gamma, C=C)
                  
                  clf = clf.fit(X_train, y_train)
                                    
                  train = np.mean(clf.predict(X_train) == y_train) 
                  predict = np.mean(clf.predict(X_test) == y_test) 
                  if(pred_aux < predict): 
                        pred_aux = predict
                        vector = clf.support_vectors_.shape[0]
                        train_aux = train
                        c_aux = C
                        g_aux = gamma

      tout = tick()
      print("SVM RBF (tuning hiper parametros)")
      print("Melhores parametros obtidos: c=" + str(c_aux) + ", gamma=" + str(g_aux))
      print("Taxa de sucesso (Treino): ", train_aux* 100)
      print("Taxa de sucesso (Teste): ",pred_aux * 100)
      print("Número de vectors de dados (treino/teste): {} / {}".
            format(X_train.shape[0], X_test.shape[0]))
      print("Número de vectores de suport: ", vector)
      print('Tempo execução : {:.3f} min'.format((tout - tin)/60))
      return c_aux, g_aux
#-----------------------------------------------------------------------------------------------

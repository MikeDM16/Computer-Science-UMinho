# -*- coding: utf-8 -*-
'''
Created on Nov 1, 2011

@author: csilva
'''

from sklearn.datasets import fetch_mldata
from sklearn.svm import SVC
from sklearn.utils import shuffle
from sklearn import preprocessing
from time import time as tick

import numpy as np

bc_dset = fetch_mldata('Breast Cancer')
X, y = bc_dset.data, bc_dset.target

clf = SVC(kernel='rbf', C=1000, gamma=0.00001)

random_state = np.random.RandomState(0)
X, y = shuffle(X, y, random_state=random_state)
half = int(X.shape[0] / 2)
X_train, X_test = X[:half], X[half:]
y_train, y_test = y[:half], y[half:]

scaler = preprocessing.StandardScaler().fit(X_train)
X_train = scaler.transform(X_train)
X_test = scaler.transform(X_test)

tin = tick()
clf = clf.fit(X_train, y_train)
tout = tick()

print("Taxa de sucesso (Treino): ",
      np.mean(clf.predict(X_train) == y_train) * 100)
print("Taxa de sucesso (Teste): ",
      np.mean(clf.predict(X_test) == y_test) * 100)
print("Número de vectors de dados (treino/teste): {} / {}".
      format(X_train.shape[0], X_test.shape[0]))
print("Número de vectores de suport: ", clf.support_vectors_.shape[0])
print('Training time: {:.3f} s'.format(tout - tin))


# C = 10, gamma = 0.0001
# Taxa de sucesso (Treino):  94.7214076246
# Taxa de sucesso (Teste):  97.3684210526
# Número de vectors de dados (treino/teste): 341 / 342
# Número de vectores de suport:  143

# C = 100, gamma = 0.0001
# Taxa de sucesso (Treino):  96.7741935484
# Taxa de sucesso (Teste):  97.3684210526
# Número de vectors de dados (treino/teste): 341 / 342
# Número de vectores de suport:  57

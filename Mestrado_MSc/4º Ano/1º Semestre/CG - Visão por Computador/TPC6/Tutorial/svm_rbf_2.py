# -*- coding: utf-8 -*-
'''
Created on Nov 1, 2011

@author: csilva
'''

from sklearn.datasets import fetch_mldata
from sklearn.svm import SVC
from sklearn.utils import shuffle
from time import time as tick

import numpy as np

bc_dset = fetch_mldata('Breast Cancer')
X, y = bc_dset.data, bc_dset.target

clf = SVC(kernel='rbf')

random_state = np.random.RandomState(0)
X, y = shuffle(X, y, random_state=random_state)
half = int(X.shape[0] / 2)
X_train, X_test = X[:half], X[half:]
y_train, y_test = y[:half], y[half:]

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

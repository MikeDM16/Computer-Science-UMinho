import json
import keras
import keras.preprocessing.text as kpt
from keras.layers.advanced_activations import LeakyReLU, PReLU
from keras.preprocessing.text import Tokenizer
import numpy as np
import xlrd, copy, random, time
import csv
from builtins import input
import sklearn.metrics
import matplotlib.pyplot as plt
from keras.utils import plot_model

from keras.models import Sequential
import keras.preprocessing.text as kpt
from keras.models import model_from_json
from keras.layers import Dense, Dropout, Activation

class ANN_Keras():
    def treino(self):   
        # Abrir o ficheiro csv
        f = open('labels.csv', encoding='utf-8', mode="r")
        reader = csv.reader(f, delimiter=';')

        # Agrupar todas as rows numa lista de listas
        dataset = []; i = 0;
        for row in reader:
            c = [row[i] for i in [2,9,10]] # dataset completo
            #c = [row[i] for i in [2,5,6]] # dataset só ingles
            if(i == 0): col_names=c; i+=1
            else:       dataset.append(c)

        neg = 0; neutro = 0; pos = 0
        for row in dataset:
            if(row[1] == "0" ): neutro += 1
            if(row[1] == "-1" ): neg += 1
            if(row[1] == "1" ): pos += 1
        
        t = pos + neg + neutro
        print("-- Resultados API Sentiment Analysis -- ")
        res = round((neg/t)*100, 2)
        print("Classificação Negativa: " + str(neg)  + " ("+ str(res)+"%)")
        res = round((neutro/t)*100, 2)
        print("Classificação Neutra: " + str(neutro )+ " ("+ str(res)+"%)")
        res = round((pos/t)*100, 2)
        print("Classificação Positiva: " + str(pos)  + " ("+ str(res)+"%)")
        print("Total : " + str(t))
 
        # Shuffle the data random (keras already allows this)
        random.shuffle(dataset)

        # Dividir em dados treino + teste
        div = int(len(dataset)*(2/3))
        dataset_treino = dataset[0:div]
        dataset_teste  = dataset[div:len(dataset)]
        
        start_time = time.time()
        
        self.max_words = 3500
        
        # Definicao da topologia da rede 
        self.model = Sequential()
        # 1º camada intermédia
        self.model.add(Dense(512, input_shape=(self.max_words,), 
                        activation='linear',
                        kernel_initializer="glorot_normal"))
        self.model.add(LeakyReLU(alpha=.001))   # add an advanced activation 
        
        self.model.add(Dropout(0.5))
        
        self.model.add(Dense(256, 
                        activation='linear',
                        kernel_initializer="glorot_normal"))
        self.model.add(LeakyReLU(alpha=.001))   # add an advanced activation

        self.model.add(Dropout(0.5))
        
        # camada saída
        self.model.add(Dense(3, activation='tanh'))

        # definição método aprendizagem
        self.model.compile(loss='categorical_crossentropy',
            optimizer='adam',
            metrics=['accuracy', 'mse'])
    
        # aprendizagem iterativa, com subpartes do dataset
        self.treino_ANN(self.model, dataset_treino, i)

        print(str( round((time.time() - start_time)/60,0)) + " minutes to train da ANN")

        # Guardar a rede criada (topologia + peses ligações)
        model_json = self.model.to_json()
        with open('model.json', 'w') as json_file:
            json_file.write(model_json)
        self.model.save_weights('model.h5')
        
        self.teste_ANN(dataset_teste)

    def treino_ANN(self, model, dataset, it):
        # Shuffle the data random (keras already allows this)
        #random.shuffle(dataset)

        # tweets -> coluna 0: textPost
        tweets = [x[0] for x in dataset]
        # index all the sentiment labels -> coluna 1: polaridade
        labels = np.asarray([x[1] for x in dataset])
        
        # create a new Tokenizer
        tokenizer = Tokenizer(num_words=self.max_words)
        # feed our tweets to the Tokenizer
        tokenizer.fit_on_texts(tweets)

        # Tokenizers come with a convenient list of words and IDs
        dictionary = tokenizer.word_index
        # Let's save this out so we can use it later
        with open('dictionary.json', 'w') as dictionary_file:
            json.dump(dictionary, dictionary_file)

        def convert_text_to_index_array(self, text):
            # one really important thing that `text_to_word_sequence` does
            # is make all texts the same length -- in this case, the length
            # of the longest text in the set.
            return [dictionary[word] for word in kpt.text_to_word_sequence(text)]

        allWordIndices = []
        # for each tweet, change each token to its ID in the Tokenizer's word_index
        for text in tweets:
            wordIndices = convert_text_to_index_array(self, text)
            allWordIndices.append(wordIndices)

        # now we have a list of all tweets converted to index arrays.
        # cast as an array for future usage.
        allWordIndices = np.asarray(allWordIndices)

        # create one-hot matrices out of the indexed tweets
        tweets = tokenizer.sequences_to_matrix(allWordIndices, mode='binary')
        # treat the labels as categories
        labels = keras.utils.to_categorical(labels, 3)
 
        train_x = tweets
        train_y = labels

        # regular cenários de treino 
        history = model.fit(train_x, train_y,
                    batch_size=350,
                    epochs=25,
                    verbose=1,
                    validation_split=0.33,
                    shuffle=True)

        print("Fim da iteração de treino.")
        self.print_history_accuracy(history)
        self.print_history_loss(history)

    def read_ANN_model(self):
        # read in your saved model structure
        json_file = open('model.json', 'r')
        loaded_model_json = json_file.read()
        json_file.close()
        # and create a model from that
        model = model_from_json(loaded_model_json)
        # and weight your nodes with your saved values
        model.load_weights('model.h5')
        return model

    def teste_ANN(self, dataset_teste):
        # Shuffle the data random (keras already allows this)
        # random.shuffle(dataset_teste)

        # tweets -> coluna 0: textPost
        tweets = [x[0] for x in dataset_teste]
        # index all the sentiment labels -> coluna 1: polaridade
        labels = np.asarray([x[1] for x in dataset_teste])
        
        # create a new Tokenizer
        tokenizer = Tokenizer(num_words=self.max_words)
        # feed our tweets to the Tokenizer
        tokenizer.fit_on_texts(tweets)

        # Tokenizers come with a convenient list of words and IDs
        dictionary = tokenizer.word_index
        # Let's save this out so we can use it later
        with open('dictionary.json', 'w') as dictionary_file:
            json.dump(dictionary, dictionary_file)

        def convert_text_to_index_array(self, text):
            # one really important thing that `text_to_word_sequence` does
            # is make all texts the same length -- in this case, the length
            # of the longest text in the set.
            return [dictionary[word] for word in kpt.text_to_word_sequence(text)]

        allWordIndices = []
        # for each tweet, change each token to its ID in the Tokenizer's word_index
        for text in tweets:
            wordIndices = convert_text_to_index_array(self, text)
            allWordIndices.append(wordIndices)

        # now we have a list of all tweets converted to index arrays.
        # cast as an array for future usage.
        allWordIndices = np.asarray(allWordIndices)

        # create one-hot matrices out of the indexed tweets
        tweets = tokenizer.sequences_to_matrix(allWordIndices, mode='binary')
        
        test_x = tweets
        # treat the labels as categories for the evaluate 
        test_y = keras.utils.to_categorical(labels, 3)
        
        model = self.read_ANN_model()

        # definição método aprendizagem -> necessário para o método evaluate
        model.compile(loss='categorical_crossentropy',
            optimizer='adam',
            metrics=['accuracy', 'mse'])
        
        # predicted é uma matriz com 3 colunas. A coluna com maior probabilidade indica 
        # previsao da rede 
        scores = model.evaluate(test_x, test_y)
        for i in range(0, len(scores)):
            print("Evaluate - " + model.metrics_names[i] + ": " + str(scores[i]))

        # --- Avaliação desempenho segundo função Predict 
        predicted = model.predict(test_x)
        predicted = [np.argmax(row) for row in predicted]

        # Adaptção dos dados para np array
        predicted = np.asarray(predicted, dtype=float)
        esperados = np.asarray(labels, dtype=float)
        
        correct = (predicted == esperados)
        accuracy = correct.sum() / correct.size
        #accuracy = sklearn.metrics.accuracy_score(esperados, predicted)

        # Calculo RMSE 
        mse = ( ((predicted - esperados)**2).mean() )
        print("Predicted - MSE: " + str(mse))
        print("Predicted - acc: " + str(accuracy))
        
    #utils para visulaização do historial de aprendizagem
    def print_history_accuracy(self, history):
        print(history.history.keys())
        plt.plot(history.history['acc'])
        plt.plot(history.history['val_acc'])
        plt.title('model accuracy')
        plt.ylabel('accuracy')
        plt.xlabel('epoch')
        plt.legend(['train', 'test'], loc='upper left')
        plt.show()

    def print_history_loss(self, history):
        print(history.history.keys())
        plt.plot(history.history['loss'])
        plt.plot(history.history['val_loss'])
        plt.title('model loss')
        plt.ylabel('loss')
        plt.xlabel('epoch')
        plt.legend(['train', 'test'], loc='upper left')
        plt.show()
using MXNet
using Distributions
using PyPlot

# https://stackoverflow.com/questions/42785437/neural-networks-example-using-mxnet-in-julia

xor_data = zeros(4,2)
xor_labels = zeros(4)

xor_data = [ [1,0,1,0];
	     [1,1,0,0] ]
xor_labels = [0,1,1,0]


batchsize = 4
trainprovider = mx.ArrayDataProvider(:data => xor_data, batch_size=batchsize, shuffle=true, :label => xor_labels)
evalprovider = mx.ArrayDataProvider(:data => xor_data, batch_size=batchsize, shuffle=true, :label => xor_labels)

data = mx.Variable(:data)
label = mx.Variable(:label)

# duas camadas de entrada. dois nodos, porque entram dois valores binarios. Adquado ao problema
# Uso da tang hiperbolica como função ativação/excitação 
# - duas camadas intermedias, todas conectadas entre si 
# - topologia

net = @mx.chain     mx.Variable(:data) =>
  
                    mx.FullyConnected(num_hidden=2) =>
                    mx.Activation(act_type=:tanh) =>

                    mx.FullyConnected(num_hidden=2) =>
                    mx.Activation(act_type=:tanh) =>

                    mx.FullyConnected(num_hidden=1) =>
		    mx.LinearRegressionOutPut(label)

# Definir que o contexto net, com aquela configuração de camadas anterior, fica na variavel model
# Define o contexto de excecução CPU ou GPU
model = mx.FeedForward(net, context=mx.cpu())


# Algoritmos de optimização rede / erro. Mais relevante que a tipologia é a parametrização 
# lr - learning rate / taxa aprendizagem
optimizer = mx.SGD(lr=0.01, momentum=0.9, weight_decay=0.00001)
# optimizer = mx.ADAM()
eval_metric = mx.MSE()

mx.fit(model, optimizer, trainprovider, n_epoch=50, eval_data = evalprovider, 
       initializer = mx.XavierInitializer(distribution = mx.xv_avg, magnitude = 1) )


plotprovider = mx.ArrayDataProvider(:Data => xor_data, :label => xor_label )
fit = mx.predict(model, plotprovider)


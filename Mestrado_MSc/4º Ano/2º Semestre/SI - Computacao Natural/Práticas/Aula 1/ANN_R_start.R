library(neuralnet)

# ANN com duas camadas intermédias + dataset infert
# Tantos neuronios de entrada (input) quantos parametros À direita da formula
# Tantos neuronios de saida (output) quantas variaveis à esquerda da formula
nn <- neuralnet(case~age+parity+induced+spontaneous, data=infert, hidden=2, err.fct="ce", linear.output=FALSE)
plot(nn)


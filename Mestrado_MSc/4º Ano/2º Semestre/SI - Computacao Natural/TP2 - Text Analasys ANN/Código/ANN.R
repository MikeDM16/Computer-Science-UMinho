library(neuralnet)
library(NeuralNetTools)
library(caTools)

# Set directory
setwd("../Desktop/TP2 CN/")

# read the file, header TRUE (or just T), seperator is the space and the decimal case mark
# whit
dataset <- read.delim("labels.csv", header=TRUE, sep=";")

posts = dataset[[3]]

'''# Min and max array to normalize data columns
max = apply(data, 2, max)
min = apply(data, 2, min)
data <- as.data.frame(scale(data, center=min, scale = max-min))
'''

# set seed for pseudo random
set.seed(80)

# split data, 70% for train and 30% for teste
split <- sample.split(dataset$TextPost, SplitRatio = 0.6)
split # uses the seed 

# define the train and teste dataset, by the split value.  
train <- subset(dataset, split==TRUE) # if split is TRUE
teste <- subset(dataset, split==FALSE)
train

feats = names(dataset) # nomes dos atributos
f <- paste(feats[10:11], collapse = '+') # string com nomes atributos separados por + 
f <- paste(f, '~', feats[3]) # formula na forma de string:  Classe_output ~ atributos_input
f <- as.formula(f) # converter de string para formula
f

# Creater a neural net, whit 3 hidden layers
nn <- neuralnet(f, train, hidden = 3, linear.output = T)
plotnet(nn)

# testar a previsão da rede, com todas as as linhas e as 4 primeuras colunas
predicted.nn.values <- compute(nn, teste[1:5] )
predicted.nn.values <- as.data.frame(sapply(predicted.nn.values$net.result, round, digits=2))

# minimum square error
MSE.NN <- sum(teste$Avaliacao - predicted.nn.values$net.result)^2 / nrow(teste)
r <- rmse(c(teste$Avaliacao), c(predicted.nn.values$net.result))

table(teste, predicted.nn.values)
?table

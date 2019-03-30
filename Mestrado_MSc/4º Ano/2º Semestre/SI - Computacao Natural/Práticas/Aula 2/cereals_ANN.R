library(neuralnet)
library(NeuralNetTools)
library(caTools)

# Set directory
setwd("../OneDrive - Universidade do Minho/2º Semestre/SI - Computacao Natural/Aula 2/")

# read the file, header TRUE (or just T), seperator is the space and the decimal case mark
# whit
data <- read.delim("cereals.csv", header=TRUE, sep=",")

# Min and max array to normalize data columns
max = apply(data, 2, max)
min = apply(data, 2, min)
data <- as.data.frame(scale(data, center=min, scale = max-min))

# set seed for pseudo random
set.seed(80)

# split data, 70% for train and 30% for teste
split <- sample.split(data$calories, SplitRatio = 0.6)
split # uses the seed 

# define the train and teste dataset, by the split value.  
train <- subset(data, split==TRUE) # if split is TRUE
teste <- subset(data, split==FALSE)
train

feats = names(data) # nomes dos atributos
f <- paste(feats[1:5], collapse = '+') # string com nomes atributos separados por + 
f <- paste(feats[6], '~', f) # formula na forma de string:  Classe_output ~ atributos_input
f <- as.formula(f) # converter de string para formula

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

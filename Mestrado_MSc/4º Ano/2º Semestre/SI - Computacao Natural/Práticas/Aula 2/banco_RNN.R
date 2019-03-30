library(neuralnet)
library(caTools)

# Set directory
setwd("../OneDrive - Universidade do Minho/2º Semestre/SI - Computacao Natural/Aula 2/")

# read the file, header TRUE (or just T), seperator is the space and the decimal case mark
# whit .
data <- read.delim("rna.ficha.banco.txt", header=TRUE, sep=" ", dec=".")

# set seed for random
set.seed(101)

# split data, 70% for train and 30% for teste
split <- sample.split(data$Vencimento, SplitRatio = 0.7)
split # uses the seed 

# define the train and teste dataset, by the split value.  
train <- subset(data, split==TRUE) # if split is TRUE
teste <- subset(data, split==FALSE)
train

feats = names(data) # nomes dos atributos
f <- paste(feats[1:4], collapse = '+') # string com nomes atributos separados por + 
f <- paste(feats[5], '~', f) # formula na forma de string
f <- as.formula(f) # converter de string para formula

# Creater a neural net
nn <- neuralnet(f, train, hidden = 3, linear.output = T)
plot(nn)

# testar a previsão da rede, com todas as as linhas e as 4 primeuras colunas
predicted.nn.values <- compute(nn, teste[1:4] )
predicted.nn.values <- as.data.frame(sapply(predicted.nn.values$net.result, round, digits=2))

# minimum square error
MSE.NN <- sum(teste$Avaliacao - predicted.nn.values$net.result)^2 / nrow(teste)


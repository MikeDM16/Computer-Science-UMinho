library(hydroGOF)
library("neuralnet")
library(leaps)

dados <- read.csv(("C:\\Users\\migue\\Desktop\\3º Ano\\2º Semestre\\Sistemas Representação Conhecimento Raciocínio\\Redes Neuronais\\Ficha12\\Livro1.csv"), header=TRUE, sep=",", dec=".")

Ver quais os três parametros principais
res <- regsubsets(default10yr ~ LTI + age + income + loan, dados, nvmax=3)
summary(res)

Usando metodo backward
res <- regsubsets(default10yr ~ LTI + age + income + loan, dados, method="backward")

treino <- dados[1:800, ]
teste <- dados[801:2000, ]

rncredito <- neuralnet(default10yr ~ LTI + age, treino, hidden = c(2,4), lifesign = "full", linear.output =  FALSE, threshold = 0.01, stepmax = 1e+06)
plot(rncredito)
test.01 <- subset(teste, select = c("LTI", "age"))
rncredito.resultados <- compute(rncredito, test.01)
resultados <- data.frame(atual = teste$default10yr, previsao = rncredito.resultados$net.result)

rmse(c(teste$default10yr),c(resultados$previsao))
library(neuralnet)
library(hydroGOF)

------------------------------------------ FatigueLevel 7 niveis -------------------------------------------
  dados <- read.csv("c:/Users/migue/Desktop/Exercício 3/exaustao_FatigueLevel_7.csv", header= TRUE, sep = ';', dec = ',' )
  
  treino <- dados[1:559,]
  teste <- dados[559:844,]
  
  formula01 <- Performance.Task ~ FatigueLevel 
  rna <- neuralnet(formula01, treino, hidden = c(5), threshold = 0.01, stepmax = 1e+07, lifesign = "full")
  teste.01 <- subset(teste,select = c("FatigueLevel", "Performance.KDTMean", "Performance.DDCMean", "Performance.DMSMean", "Performance.AEDMean"))
  rna.resultados <- compute(rna, teste.01)
  resultados <- data.frame(atual = teste$Performance.Task, previsao = rna.resultados$net.result)
  rmse(c(teste$Performance.Task),c(resultados$previsao))
  

library(neuralnet)
library(hydroGOF)
  

dados <- read.csv("c:/Users/migue/Desktop/Exercício 3/exaustao_FatigueLevel_2.csv", header= TRUE, sep = ';', dec = ',' )
treino <- dados[1:559,]
teste <- dados[559:844,]

formula01 <- Performance.Task ~ FatigueLevel + Performance.KDTMean + Performance.DMSMean 
rna <- neuralnet(formula01, treino, hidden = c(6,4,3), threshold = 0.01, stepmax = 1e+08, lifesign = "full")
teste.01 <- subset(teste,select = c("FatigueLevel", "Performance.KDTMean", "Performance.DMSMean"))
rna.resultados <- compute(rna, teste.01)
resultados <- data.frame(atual = teste$Performance.Task, previsao = rna.resultados$net.result)
resultados$previsao <- round(resultados$previsao, digits = 0)
out <- print(rmse(c(teste$Performance.Task),c(resultados$previsao)))

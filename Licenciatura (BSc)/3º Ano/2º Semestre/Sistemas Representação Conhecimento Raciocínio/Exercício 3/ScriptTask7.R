
------------------------------------------ FatigueLevel 7 niveis -------------------------------------------
  
  

  
library(neuralnet)
library(hydroGOF)

dados <- read.csv("c:/Users/migue/Desktop/Exercício 3/exaustao_FatigueLevel_2.csv", header= TRUE, sep = ';', dec = ',' )

treino <- dados[1:559,]
teste <- dados[559:844,]


formula01 <- Performance.Task ~ Performance.KDTMean + Performance.DMSMean + Performance.ADMSLMean + Performance.MAMean + Performance.MVMean + Performance.TBCMean + Performance.DDCMean + Performance.AEDMean
rna <- neuralnet(formula01, treino, hidden = c(6,5,4), threshold = 0.01, stepmax = 1e+07, lifesign = "full")
teste.01 <- subset(teste,select = c("Performance.KDTMean", "Performance.DMSMean", "Performance.ADMSLMean", "Performance.MAMean", "Performance.MVMean", "Performance.TBCMean", "Performance.DDCMean", "Performance.AEDMean"))
rna.resultados <- compute(rna, teste.01)
resultados <- data.frame(atual = teste$Performance.Task, previsao = rna.resultados$net.result)
rmse(c(teste$Performance.Task),c(resultados$previsao))
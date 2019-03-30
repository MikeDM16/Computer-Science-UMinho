library(neuralnet)
library(hydroGOF)
dados <- read.csv("C:/Users/migue/Desktop/Exercício 3/exaustao_FatigueLevel_7.csv", header= TRUE, sep = ';', dec = ',' )
treino <- dados[1:559,]
teste <- dados[559:844,]

formula01 <- Performance.Task ~ Performance.KDTMean + Performance.AEDMean + Performance.ADMSLMean + FatigueLevel
rna <- neuralnet(formula01, treino, hidden = c(6,3), lifesign = "full", threshold = 0.01, stepmax = 1e+06)
teste.01 <- subset(teste,select = c("Performance.KDTMean","Performance.MAMean","Performance.TBCMean","Performance.DMSMean","Performance.AEDMean","Performance.ADMSLMean","FatigueLevel"))
rna.resultados <- compute(rna, teste.01)
resultados <- data.frame(atual = teste$FatigueLevel, previsao = rna.resultados$net.result)
resultados$previsao <- round(resultados$previsao, digits = 0)
print(rmse(c(teste$FatigueLevel),c(resultados$previsao)))
library(neuralnet)
library(hydroGOF)
treino <- dados[1:559,]
teste <- dados[559:844,]

------------------------------------------ FatigueLevel 7 niveis -------------------------------------------
dados <- read.csv("~/OneDrive/Documentos/MiEI/3.º ano/2.º semestre/Sistemas de Representação de Conhecimento e Raciocínio/Trabalhos práticos/SRCR-1617/Exercício 3/exaustao_FatigueLevel_7.csv", header= TRUE, sep = ';', dec = ',' )

formula01 <- FatigueLevel ~ Performance.Task + Performance.MAMean + Performance.MVMean + Performance.DDCMean + Performance.DMSMean
rna <- neuralnet(formula01, treino, hidden = c(15,8,4), threshold = 0.01, stepmax = 1e+08, lifesign = "full")
teste.01 <- subset(teste,select = c("Performance.Task","Performance.MAMean","Performance.MVMean","Performance.DDCMean","Performance.DMSMean"))
rna.resultados <- compute(rna, teste.01)
resultados <- data.frame(atual = teste$FatigueLevel, previsao = rna.resultados$net.result)
rmse(c(teste$FatigueLevel),c(resultados$previsao))









dados <- read.csv("~/OneDrive/Documentos/MiEI/3.º ano/2.º semestre/Sistemas de Representação de Conhecimento e Raciocínio/Trabalhos práticos/SRCR-1617/Exercício 3/exaustao_FatigueLevel_2.csv", header= TRUE, sep = ';', dec = ',' )
treino <- dados[1:559,]
teste <- dados[559:844,]
formula01 <- FatigueLevel ~ Performance.Task + Performance.KDTMean + Performance.ADMSLMean + Performance.DMSMean
rna <- neuralnet(formula01, treino, hidden = c(5,5,4), threshold = 0.01, stepmax = 1e+08, lifesign = "full")
teste.01 <- subset(teste,select = c("Performance.Task","Performance.KDTMean","Performance.ADMSLMean", "Performance.DMSMean"))
rna.resultados <- compute(rna, teste.01)
resultados <- data.frame(atual = teste$FatigueLevel, previsao = rna.resultados$net.result)
resultados$previsao <- round(resultados$previsao, digits = 0)
out <- print(rmse(c(teste$FatigueLevel),c(resultados$previsao)))

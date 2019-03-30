library(leaps)

------------------------------------------ FatigueLevel 7 niveis -------------------------------------------
dados <- read.csv("~/OneDrive/Documentos/MiEI/3.º ano/2.º semestre/Sistemas de Representação de Conhecimento e Raciocínio/Trabalhos práticos/SRCR-1617/Exercício 3/exaustao_FatigueLevel_7.csv", header= TRUE, sep = ';', dec = ',' )


reggi <- regsubsets(FatigueLevel ~ Performance.KDTMean + Performance.MAMean + Performance.MVMean+ Performance.TBCMean + Performance.DDCMean +Performance.DMSMean + Performance.AEDMean + Performance.ADMSLMean + Performance.Task,dados, method = "backward")
summary(reggi)
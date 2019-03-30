library(leaps)

------------------------------------------ FatigueLevel 7 niveis -------------------------------------------
dados <- read.csv("c:/Users/migue/Desktop/Exercício 3/exaustao_FatigueLevel_7.csv", header= TRUE, sep = ';', dec = ',' )


reggi <- regsubsets(Performance.Task ~ Performance.KDTMean + Performance.MAMean + Performance.MVMean+ Performance.TBCMean + Performance.DDCMean +Performance.DMSMean + Performance.AEDMean + Performance.ADMSLMean + FatigueLevel,dados, method = "backward")
summary(reggi)
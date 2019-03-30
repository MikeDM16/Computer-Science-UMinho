library(leaps)
dados <- read.csv("C:/Users/Diogo/Desktop/SRCR/exaustao_FatigueLevel_2.csv", header= TRUE, sep = ';', dec = ',' )
reggi <- regsubsets(Performance.Task ~ Performance.KDTMean + Performance.MAMean + Performance.MVMean+ Performance.TBCMean + Performance.DDCMean +Performance.DMSMean + Performance.AEDMean + Performance.ADMSLMean + FatigueLevel,dados, method = "backward")
summary(reggi)

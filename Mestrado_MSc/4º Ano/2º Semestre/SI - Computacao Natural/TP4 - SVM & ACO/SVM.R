setwd("/Users/Miguel/OneDrive - Universidade do Minho/2º Semestre/SI - Computacao Natural/TP4 - SVM & ACO/")

#exemplo sem especifica??o do kernel (assume um por defeito)
runSVM <- function(file_name){
  library(e1071)
  library(xlsx)
  trainingInput <- read.xlsx(file_name, 1, header=T)
  ncols.trainingInput <- ncol(trainingInput)
  trainingInput <- trainingInput[ , 2 : ncols.trainingInput]
  
  trainingOutput<- read.xlsx(file_name, 2, header=T)
  trainingOutput <- matrix(c(trainingOutput[ , 2]), ncol=1)
  
  testData <- read.xlsx(file_name, 3, header=T)
  ncols.testData <- ncol(testData)
  testData <- testData[, 2 : ncols.testData]
  
  #generate column names for trainingInput and trainingOutput. required for formula construction
  colnames(trainingInput) <- inputColNames <- paste0("x", 1:ncol(trainingInput))
  colnames(trainingOutput) <- outputColNames <- paste0("y", 1:ncol(trainingOutput))
  
  
  #Column bind the data into one variable
  trainingdata <- cbind(trainingInput,trainingOutput)
  
  # estimate model and predict input values
  m <- svm(trainingInput,trainingOutput,kernel = 'sigmoid', epsilon = 1)
  pred <- predict(m, testData)
  
  # visualize
#   plot(trainingData, realData)
#   points(trainingData, log(trainingData), col = 2)
#   points(trainingData, pred, col = 4)
#write.xlsx(pred, file=file_name, sheetName="Test Result", col.names=F, row.names=F, append=T)

  return (pred)
}


valoresReais<-c(538.006475880555,486.103636638889,498.314949536111,725.948419627777)
valoresPrevistos<-c(runSVM("HVAC24hS16-11-2016--6.xls"),
                    runSVM("HVAC24hS16-11-2016--7.xls"),
                    runSVM("HVAC24hS16-11-2016--8.xls"),
                    runSVM("HVAC24hS16-11-2016--9.xls"))
mape <-((valoresReais-valoresPrevistos)/valoresReais)
mediaMape <-mean(mape)

valoresReais
valoresPrevistos
mape
mediaMape 






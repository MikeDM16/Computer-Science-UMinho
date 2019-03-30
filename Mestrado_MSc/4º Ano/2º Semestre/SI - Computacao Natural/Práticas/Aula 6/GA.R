library(GA)

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
# funcao a minimizar
f <- function(x) abs(x) - cos(x)
# O package só faz maximização --> A função a fitness deve ser -f(x) ou 1/f(x)
# Minimizar f(x) = maximizar -f(x)
fitness <- function(x) -f(x)

min <- -20; max <- +20

monitor <- function(obj){
  curve(f, min, max, main=paste("iteration=", obj@iter), font.main=1)
  points(obj@population, -obj@fitness, col=2, pch=20)
  rug(obj@population, col=2)
  Sys.sleep(0.2)
}

GA <- ga(type = "real-valued", 
         fitness = fitness, 
         min = min, max = max, 
         monitor=monitor)


plot(GA)
summary(GA)

# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
# install.packages("spuRs")
library(spuRs)

# data parameters
data("trees", package="spuRs")
tree <- trees[trees$ID == "1.3.11", 2:3]

# function f(x) = a(1-e^(-b*x))^c
# theta is an array whit a, b and c 
richards <- function(x, theta){
  theta[1] * (1 - exp(-theta[2]*x))^theta[3]
}

fitnessL2 <- function(theta, x, y){
  -sum( (y - richards(x,theta))^2 ) 
}

GA2 <- ga(type="real-valued", fitness=fitnessL2, x = tree$Age, y = tree$Vol,
         min = c(3000, 0, 2), 
         max = c(4000, 1, 4),
         popSize=500, 
         crossover=gareal_laplaceCrossover, 
         maxiter = 5000, run = 200, names = c("a", "b", "c") ) 

summary(GA2)

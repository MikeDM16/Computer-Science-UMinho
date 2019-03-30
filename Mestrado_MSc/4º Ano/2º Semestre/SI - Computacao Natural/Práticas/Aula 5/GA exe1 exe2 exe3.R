library(GA)

# -------------------------------------------------
# Exe1: Encontrar maximo função f
# -------------------------------------------------
f<- function(x) (x^2+x)*cos(x)

# min e máximo definem os limites no dominio/intervalo da função de avaliação 
min <- -10; max <- 10
curve(f, min, max, n=1000)

GA <- ga(type = "real-valued", fitness = f, min = min, max = max, monitor=FALSE)
summary(GA)

plot(GA)

curve(f, min, max, n=1000)
points(GA@solution, GA@fitnessValue, col=2, pch=19)

# -------------------------------------------------
# Exe2: Encontrar máximo função
# -------------------------------------------------
rastrigin <- function(x1,x2) {
  20+x1^2 + x2*2 -10*(cos(2*pi*x1) + cos(2*pi*x2))
}

min <- c(-5.12, -5.12); max <- c(5.12, 5.12)
GA <- ga(type = "real-valued", 
         fitness = function(x)-rastrigin(x[1], x[2]), 
         min = min, max = max, 
         popSize = 50, maxiter = 1000, run = 100)
summary(GA)

# -------------------------------------------------
# Exe3: Encontrar máximo função com restrição 
# -------------------------------------------------
f <- function(x) {
  100 * (x[1]^2 - x[2])^2 + (1 - x[1])^2
}

c1 <- function(x) {
  x[1]*x[2] + x[1] - x[2] + 1.5
}
c2 <- function(x){
  10 - x[1]*x[2]
}

fitness <- function(x){
  f <- -f(x) # we need to maximise -f(x)
  pen <- sqrt(.Machine$double.xmax) # penalty term
  penalty1 <- max(c1(x), 0)*pen # penalisation for 1st inequality constraint
  penalty2 <- max(c2(x), 0)*pen # penalisation for 2nd inequality constraint
  f - penalty1 - penalty2 # fitness function value
    
}

min <- c(0, 0); max <- c(1, 13)
GA <- ga(type = "real-valued", 
         fitness = fitness, 
         min = min, max = max, 
         maxiter = 5000, run = 1000, seed=123)
summary(GA)


# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
# Problema Travelling salesman
# método euristico alg. genético, q parte apenas de um subset de percursos
# swap mutation + orderer crossover

data("eurodist", package="datasets") # distancias entre 21 cidades europeias
D <- as.matrix(eurodist)
D

# route = [1,2,3,4,5,1]
# 1|2
# 2|3   linha 2 da tourlength
# 3|4
# 4|5
# 5|1
tourLength <- function(tour, distMatrix) {
  tour <- c(tour, tour[1])
  route <- embed(tour, 2)[, 2:1]
  sum(distMatrix[route]) 
}

tpsfitness <- function(tour, distMatrix){
  1 / tourLength(tour, distMatrix)
}

# permutation already includes swap mutation and ordeer crossover
GA.fit <- ga(type="permutation", fitness=tpsfitness, distMatrix=D, min=1,
             max = attr(eurodist, "Size"), popSize=10, maxiter=500, run =100, 
             pmutation = 0.2, monitor=NULL)

summary(GA.fit)

mds <- cmdscale(eurodist)
x <- mds[, 1]
y <- mds[, 2]
n <- length(x)

plot(x,y, type="n", asp=1, xlab="", ylab="", main="Tour after GA converted")
points(x,y,pch=16, cex=1.5, col="grey")
abline(h = pretty(range(x), 10), v = pretty(range(y), 10), col="lightgrey")
tour <- GA.fit@solution[1, ]
tour <- c(tour, tour[1]) # adicionar o regresso ao ponto partida
n <- length(tour)
arrows(x[tour[-n]], y[tour[-n]],x[tour[-1]], y[tour[-1]], length = 0.15, angle=45, col="steelblue", lwd=2)

text(x, y-100, labels(eurodist), cex=0.8)


# -----------------------------------------------------------------------------
# -----------------------------------------------------------------------------
# Problema Travelling salesman
# distancia associada a pesos: ir de A->B != B->A

D_pesos <- matrix(runif(21, min = 100, max = 500), ncol=21, nrow = 21)
D_pesos


tourLength <- function(tour, distMatrix) {
  tour <- c(tour, tour[1])
  route <- embed(tour, 2)[, 2:1]
  sum(distMatrix[route]) 
}

tpsfitness <- function(tour, distMatrix){
  1 / tourLength(tour, distMatrix)
}

# permutation already includes swap mutation and ordeer crossover
GA.fit <- ga(type="permutation", fitness=tpsfitness, distMatrix=D_pesos, min=1,
             max = attr(eurodist, "Size"), popSize=10, maxiter=500, run =100, 
             pmutation = 0.2, monitor=NULL)

summary(GA.fit)

mds <- cmdscale(eurodist)
x <- mds[, 1]
y <- mds[, 2]
n <- length(x)

plot(x,y, type="n", asp=1, xlab="", ylab="", main="Tour after GA converted")
points(x,y,pch=16, cex=1.5, col="grey")
abline(h = pretty(range(x), 10), v = pretty(range(y), 10), col="lightgrey")
tour <- GA.fit@solution[1, ]
tour <- c(tour, tour[1]) # adicionar o regresso ao ponto partida
n <- length(tour)
arrows(x[tour[-n]], y[tour[-n]],x[tour[-1]], y[tour[-1]], length = 0.15, angle=45, col="steelblue", lwd=2)

text(x, y-100, labels(eurodist), cex=0.8)
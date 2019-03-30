library(GA)

nclients = 12
ncol = 2
pos = c(33,30,1,31,0,29,0,31,2,32,30,0,31,1,29,0,31,0,32,2,30,30,31,31)
positions = matrix(data=pos, nrow=nclients, ncol = ncol, byrow=TRUE)
positions 

distClients = matrix(ncol=nclients, nrow=nclients)
distDeposito = matrix(ncol=nclients, nrow=1)

for(i in c(1:nclients)){
  x1 = positions[i,1];
  y1 = positions[i,2];
  # distancia deposito ao cliente - Vetor 
  d = sqrt((x1 - 0)^2 + (y1- 0)^2);
  distDeposito[i] = d; 
  
  # distancia entre cliente - Matriz 
  for(j in c(1:nclients)){
    x2 = positions[j,1];
    y2 = positions[j,2];
    d = sqrt((x1 - x2)^2 + (y1-y2)^2);
    distClients[i,j] = d;
    distClients[j,i] = d;
  }
}

aux = c(1:20)
aux = setdiff(aux, c(13:20))
length(aux)

tourLength <- function(tour, distClients, distDeposito) {
  # limpar do cromossoma os valores adicionados por excesso, para evitar repetir números e dar estoiro no default permutation
  aux = setdiff(tour, c(13:25))
  
  # distancia da origem à primeira paragem 
  d = distDeposito[aux[1]]
  
  # Distancia de cada estação à estaçãocseguinte
  for(i in length(aux)-1){
    d = d + distClients[aux[i], aux[i+1]]
  }
  
  # Distância regresso à origem 
  d = d + distDeposito[aux[length(aux)]]
  
}

tpsfitness <- function(tour, distClients, distDeposito){
  1 / tourLength(tour, distMatrix)
}

# permutation already includes swap mutation and ordeer crossover
GA.fit <- ga(type="permutation", fitness=tpsfitness, distMatrix=D_pesos, min=1,
             max = nclients, popSize=10, maxiter=500, run =100, 
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
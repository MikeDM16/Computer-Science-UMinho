#install.packages("GA")
#install.packages("genalg")
#install.packages("ggplot2")
#install.packages("animation")


# Permite a definição de outros tipos de cromossomas que nãp apenas binários
library(GA)
# Usado principalmente para cromossomas binários
library(genalg)
library(ggplot2)

#cromossoma <- c(1,0,1,1,0,0)


item <- c("pocketknife", "beans", "potatoes", "union", "sleeping bag", "rope", "compass");
survivalpoints <- c(10,20,15,2,30,10,30);
weight <- c(1,5,10,1,7,5,1);
weightlimit <- 20;
dataset <- data.frame(item = item, survivalpoints = survivalpoints, weight = weight);

# criação cromossoma
chromossome <- c(1,0,0,1,1,0,0);
dataset[chromossome == 1,] # show active values for the chromossome 
cat(chromossome %*% dataset) # get the survival points

evalFunc <- function(x) {
  current_solution_survivalpoints <- x %*% dataset$survivalpoints
  current_solution_weight <- x %*% dataset$weight
  
  if (current_solution_weight > weightlimit) 
    return(0) else return(-current_solution_survivalpoints)
}

# -------------------------------------------
# Run the algorithm - Peso limite = 20
# -------------------------------------------
iter = 100
GAmodel <- rbga.bin(size = 7, popSize = 200, iters = iter, mutationChance = 0.01, elitism = T, evalFunc = evalFunc)
cat(summary(GAmodel))
plot(GAmodel)

bestsolution <- GAmodel$population[which.min(GAmodel$evaluations), ]
dataset[bestsolution==1, ]

# -------------------------------------------------
# Run the algorithm - Existe sempre porção comida
# -------------------------------------------------
food <- c(0,1,1,1,0,0,0) # array para indicar onde ficam os indices de comida

evalFuncRestricao <- function(x) {
  current_solution_survivalpoints <- x %*% dataset$survivalpoints
  current_solution_weight <- x %*% dataset$weight
  has_food <- x %*% food
  
  # Se nenhum dos genes do cromossoma a avaliar for relativo a um indicio de comida, 
  # então a soma dos elementos de has_food será 0 
  if(sum(has_food) == 0) return(0)
  
  if (current_solution_weight > weightlimit) 
    return(0) else return(-current_solution_survivalpoints)
}

weightlimit <- 10;
GAmodel <- rbga.bin(size = 7, popSize = 200, iters = iter, mutationChance = 0.01, elitism = T, evalFunc = evalFuncRestricao)
cat(summary(GAmodel))
plot(GAmodel)

bestsolution <- GAmodel$population[which.min(GAmodel$evaluations), ]
dataset[bestsolution==1, ]

# -------------------------------------------

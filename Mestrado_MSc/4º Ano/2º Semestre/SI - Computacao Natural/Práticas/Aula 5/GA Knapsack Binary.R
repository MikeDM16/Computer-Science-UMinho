
# Permite a definição de outros tipos de cromossomas que nãp apenas binários
library(GA)
library(ggplot2)

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

GA <- ga(type = "binary", 
         fitness = evalFunc, 
         nBits = length(chromossome),
         maxiter = 5000, run = 1000, seed=123)
summary(GA)

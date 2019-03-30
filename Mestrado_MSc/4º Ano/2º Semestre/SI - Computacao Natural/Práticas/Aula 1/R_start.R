a <- c(3,4,5,6,8)
class(a)

b <- c("cat", "dog")
class(b)

a+3
b+3

a*3
b*3
# compara elemento a elemento
a==4

# Multilplica o array binários resultado da exp boleana (a==4) por a
(a==4)*a

a>4
a[a>4]

# obter resultado returnado por função 
l <- length(a)
l

# Para obter painel ajuda, colocar "?" antes da funçã0
?c

length(a) # nr elementos
sum(a)  # soma elementos
mean(a) # média vetor
var(a)  # desvio padrão / variancia

# Geração vetores
?seq
seq(from=1, to=5)
seq(5)
1:5
seq(1,5,by=2) 
seq(1,5) # = anterior

# Array com 9 elementos entre from e to 
# Por default ele não assume o length.out. Tem que ser explicitamente referido
seq(1,5, length.out=9) # default is length.out = null

?rep
rep(1, times=2)
rep(1:4, times=2)
rep(1:4, each=2)
rep(1:4, times=2, each=2)
# rep 1º elem 1x, 2º 2x, 3º 3x ... etc
rep(1:4, 1:4)


# Matrizes
?matrix
# Use byrow=TRUE to use it like C/JAva
matrix(0,3,5)
matrix(1:3,3,5)

m <- matrix(1:5,5,5)
m 
m[,1] # colunas usa [_, c]
m[4,] # linhas usar [l,_]
m*2

# Data Frames
?data.frame
as.data.frame(m) # Converter uma matriz em data frame
data()

a <- c(3,4,5)
b <- c("cat", "dog")
A <- matrix(0,3,2)
B <- matrix(0,2,3)
?list
# Realizar x=x para mantero nome das variaveis. senão ficam [[1]], [[2]]....
m_list <- list(a=a, b=b, A=A, B=B)
m_list

# Identifica o tipo das classes e sumatiza de forma apropriada
summary(chickwts)
plot(chickwts)


# Formulas: y ~ 5*x + 2
# Uso do cifrão $ para aceder a uma classe especifica do dataset
neuralnet

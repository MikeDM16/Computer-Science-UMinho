options = optimset('TolX', 10^-1)
format long
x0 = 0.04
[x,f,exitflag,output] = fsolve('funcao2',x0,options)
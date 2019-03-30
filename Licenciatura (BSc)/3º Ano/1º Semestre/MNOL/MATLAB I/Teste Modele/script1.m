options = optimset('TolX', 10^-2, 'Tolfun', 10^-1)
format long
x0=[0;0.1]
[x,f,exitflag,output]=fsolve('funcao1',x0,options)
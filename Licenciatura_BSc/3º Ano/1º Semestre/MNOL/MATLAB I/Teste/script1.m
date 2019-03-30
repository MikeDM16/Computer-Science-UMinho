options = optimset('TolX', 10^-3, 'Tolfun', 10^-2)
format long
x0=[0.2; 0.02]
[x,f,exitflag,output] = fsolve('f1',x0,options)
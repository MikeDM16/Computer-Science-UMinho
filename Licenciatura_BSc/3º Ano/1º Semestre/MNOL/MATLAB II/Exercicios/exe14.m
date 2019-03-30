n=5; % n vai até 5

i=1:2:n;
x1(i)=2; % para os i impares x1 = 2

i=2:2:n;
x1(i)=1; % para os i pares x1 = 1

t=pi/6;
m=10;

options = optimset('LargeScale', 'off');
[x,f,exitflag,output]=fminunc('f14',x1,options,t,m)
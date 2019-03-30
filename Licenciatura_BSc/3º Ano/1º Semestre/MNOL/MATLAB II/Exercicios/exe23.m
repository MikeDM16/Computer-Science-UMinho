n = 5;
i = 1:n;
x1 = i - ((n/2)+0.5);

options = optimset('Display','iter');
opt2= optimset('tolX', 10^-20, 'MaxFunEvals', 10000, 'MaxIter', 100000);
%O número máximo de cálculos de função e de iterações que estão por defeito no MATLAB
%não são suficientes para este problema convergir (exitflag=0), sendo, por isso,
%necessário aumentá-los usando as opções MaxFunEvals e MaxIter, tal como os avisos do
%MATLAB sugerem.
[x,f,exitflag, output] = fminsearch('f23', x1,opt2,n)
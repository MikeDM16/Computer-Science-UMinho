n = 5;
i = 1:n;
x1 = i - ((n/2)+0.5);

options = optimset('Display','iter');
opt2= optimset('tolX', 10^-20, 'MaxFunEvals', 10000, 'MaxIter', 100000);
%O n�mero m�ximo de c�lculos de fun��o e de itera��es que est�o por defeito no MATLAB
%n�o s�o suficientes para este problema convergir (exitflag=0), sendo, por isso,
%necess�rio aument�-los usando as op��es MaxFunEvals e MaxIter, tal como os avisos do
%MATLAB sugerem.
[x,f,exitflag, output] = fminsearch('f23', x1,opt2,n)
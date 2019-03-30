n = 2;
i = 1:n;
x1 = i - ((n/2)+0.5);

options = optimset('Display','iter');
opt2= optimset('tolX', 10^-20, 'MaxFunEvals', 10000, 'MaxIter', 100000);
[x,f,exitflag, output] = fminsearch('f24', x1,options)

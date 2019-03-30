i = 1:4;
x1(i) = 1;
options = optimset('Display','iter','MaxFunEval', 10000, 'MaxIter', 10000);
[x,f,exitflag, output] = fminsearch('f8', x1, options)

% NAO CONVERGIU (a.i)
% f minimo = -1.7743E-39
% x* = (-0.8274, 0, 2.0550, 1.6135)
% iter 488
% funcCounts 




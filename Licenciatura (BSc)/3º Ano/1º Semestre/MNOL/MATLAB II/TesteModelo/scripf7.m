i = 1:4;
%x1(i) = 1;
x2(i) = 10;
options = optimset('Display','iter');
[x,f,exitflag, output] = fminsearch('f7', x2, options)

% (a.i)
% minimo = -0.2500
% (a.ii)
% iter 104 
% funcCounts 187

% (b.i)
% minimo = 534.5362
% (b.ii)
% iter 386
% funcCounts 685

i = 1:4;
x1(i) = 1;
options = optimset('Display','iter');
[x,f,exitflag, output] = fminsearch('f26', x1,options)

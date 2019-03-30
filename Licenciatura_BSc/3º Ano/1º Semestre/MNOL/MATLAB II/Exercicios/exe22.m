x1 = [1;-0.1];
options = optimset('Display','iter');
[x,f,exitflag, output] = fminsearch('f22', x1, options)
x1 = [1;1];
options = optimset('Display','iter');
[x,f,exitflag, output] = fminsearch('f21', x1, options)
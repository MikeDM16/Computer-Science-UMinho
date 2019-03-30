x1 = [1 1];
options = optimset('LargeScale', 'off');
[x,f,exitflag, output] = fminunc('f12a', x1, options)

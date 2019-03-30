x1 = [1 1];
options = optimset('LargeScale', 'off', 'gradObj', 'on');
[x,f,exitflag, output] = fminunc('f12a', x1, options)

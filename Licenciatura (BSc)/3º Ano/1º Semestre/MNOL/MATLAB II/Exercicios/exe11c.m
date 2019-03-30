x1 = [-1 0.5];
options = optimset('LargeScale', 'on', 'gradObj', 'on');
[x,f,exitflag, output] = fminunc('f11b', x1, options)
%forçar uso das derivadas fornecidas e NEWTON


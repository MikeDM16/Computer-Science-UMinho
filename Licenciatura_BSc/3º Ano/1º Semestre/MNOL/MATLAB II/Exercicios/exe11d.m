x1 = [-1 0.5];
options = optimset('LargeScale', 'on', 'gradObj', 'on', 'hessian', 'on');
[x,f,exitflag, output] = fminunc('f11b', x1, options)
%for�ar uso das derivadas fornecidas e NEWTON


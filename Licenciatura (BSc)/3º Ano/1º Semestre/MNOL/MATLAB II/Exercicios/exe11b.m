x1 = [-1 0.5];
options = optimset('LargeScale', 'off', 'gradObj', 'on');
[x,f,exitflag, output] = fminunc('f11b', x1, options)
%for�ar uso das derivadas fornecidas e quasi newton
%reduziu de 21 funccounts para 7!!!

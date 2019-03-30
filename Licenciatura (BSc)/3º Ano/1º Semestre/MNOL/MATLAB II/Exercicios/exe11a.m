x1 = [-1 0.5];
option = optimset('LargeScale', 'off');
[x,f,exitflag, output] = fminunc('f11a', x1, option)
% Sem derivadas e com quasi newton 

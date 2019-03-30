x1 = [1 1];
%NEWTON e 1�s derivadas
optN1 = optimset('LargeScale', 'on', 'gradObj', 'on');
%NEWTON com 1�s derivadas e 2� derivadas
optN2 = optimset('LargeScale', 'on', 'gradObj', 'on', 'hessian', 'on');
%QUASI NEWTON 
optQN1 = optimset('LargeScale', 'off');
%QUASI NEWTON com 1�s derivadas;
optQN2 = optimset('LargeScale', 'off', 'gradObj', 'on');
% modo DFP 
opt3 = optimset('hessupdate', 'dfp');

[x,f,exitflag, output] = fminunc('f12a', x1, optN1)

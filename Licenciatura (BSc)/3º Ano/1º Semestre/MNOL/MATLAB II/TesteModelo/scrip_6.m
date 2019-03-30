x1 = [1; 1];
optA = optimset('LargeScale', 'off','hessupdate', 'dfp')
optB = optimset('LargeScale', 'off', 'gradObj','on', 'hessupdate', 'dfp');

[x,f,exitflag, output] = fminunc('f6', x1, optB)

% (a)
% minimo = 0.4126
% iter 8 
% funcCounts 33
% (b)
% minimo = 0.4129
% iter 8 
% funcCounts 11

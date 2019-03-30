x1 = [6,7,5];
options = optimset('LargeScale', 'off', 'tolX', 10^-10, 'tolFun', 10^(-10), 'hessupdate', 'dfp', 'display', 'iter');
% tolX, tolFun, LargeScale - quasi newton, display iter, 
% modo hessupdate = DFP

[x,f,exitflag, output] = fminunc('f13', x1, options)
% x* = (7,7,7)
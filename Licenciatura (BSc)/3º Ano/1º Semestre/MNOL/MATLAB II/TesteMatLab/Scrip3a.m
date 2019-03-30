i1 = 1:3;
x1a(i1) = 1; 
opt3 = optimset('LargeScale', 'off', 'hessupdate', 'dfp');
[x,f,exitflag, output] = fminunc('f3a', x1a, opt3)

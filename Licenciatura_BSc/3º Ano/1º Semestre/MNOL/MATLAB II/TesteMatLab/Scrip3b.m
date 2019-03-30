i2 = 1:25;
x1b(i2) = i2; 
opt3 = optimset('LargeScale', 'off', 'hessupdate', 'dfp');
[x,f,exitflag, output] = fminunc('f3a', x1b, opt3)

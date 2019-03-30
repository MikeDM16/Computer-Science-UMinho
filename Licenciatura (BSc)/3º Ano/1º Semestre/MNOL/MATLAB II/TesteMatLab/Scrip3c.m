i3 = 1:10;
x1c(i3) = 2.^i3; 
opt3 = optimset('LargeScale', 'off', 'hessupdate', 'dfp');
[x,f,exitflag, output] = fminunc('f3a', x1c, opt3)

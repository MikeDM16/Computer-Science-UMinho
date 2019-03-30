x1 = [1 1 1 1 1 1 1 1 1 1]';
options = optimset('LargeScale', 'off', 'hessupdate', 'dfp');
[x,f,optimset, output] = fminunc('f15Sumatorio', x1, options);


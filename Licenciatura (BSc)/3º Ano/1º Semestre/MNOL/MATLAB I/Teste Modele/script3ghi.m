x = [1.5 2 2.2 3 3.8 4]
f = [4.9 3.3 3 2 1.75 1.5]

p = polyfit(x,f,2)
ps = polyval(p,3.3)

%0.68x^2 - 4.9882x + 10.7328
%resreta = 1.041
%resquad = 0.10944


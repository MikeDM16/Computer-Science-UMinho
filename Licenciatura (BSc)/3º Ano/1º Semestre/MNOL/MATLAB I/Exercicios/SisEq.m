function [f] = SisEq(x)
% options = optimset('Jacobian', 'on', 'Tolx', 10^-2, 'Tolfun', 10^-1)
% x = [0 0.1]             
% format long
% [x, f, exitflag, output] = fsolve('SisEq', x, options)
f = [-5*x(1) + 3*sin(x(1)) + cos(x(2)); 4*cos(x(1)) + 2*sin(x(2)) - 5*x(2)];
end

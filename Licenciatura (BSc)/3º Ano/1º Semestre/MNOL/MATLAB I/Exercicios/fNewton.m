function [ f ] = fNewton ( x )
%options = optimset('Jacobian', 'on', 'Tolx', E2 10^-10, 'Tolfun', E1 10^-9)
%format long
%[x, f, exitflag, output] = fsolve('fNewton', 2)

    f = (x-1)*exp(x) - x - 17
    
    if nargout > 1
        j = exp(x) + (x-1)*exp(x) - 1
    end
end


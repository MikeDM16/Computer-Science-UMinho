function [ f ] = f22( x )
    t = [(x(1)^2 + x(2)^4),((2-x(1))^2 + (2-x(2))^2),(2*exp(-x(1)+x(2)))];
    f = max(t);
end


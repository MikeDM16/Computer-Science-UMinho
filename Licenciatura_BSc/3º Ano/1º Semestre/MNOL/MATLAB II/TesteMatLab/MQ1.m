function [ f ] = MQ1( c, x )
    f = c(1).*x + c(2)*exp(1./(x+1)) + c(3)*(1./(x-1));
end


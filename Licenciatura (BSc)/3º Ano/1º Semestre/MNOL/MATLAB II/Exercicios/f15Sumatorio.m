function [ f ] = f15Sumatorio ( x )
    n = length(x);
    %x1 = 1:1:n;
    
    i = 1:1:n;
    f = 1 + (1/4000)*sum(x.^2) - prod(cos(x/(i^0.5)));
end


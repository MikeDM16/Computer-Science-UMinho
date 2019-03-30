function [ f ] = f7( x )
    i = 1:21;
    t(i) = 0.25 + 0.75*(i-1)/20;
    t(i) = 0.2*i;
    u = x(4) - (x(1).*t.^2 + x(2).*t + x(3)).^2 - t.^0.5;
    
    f = max(u.^2) - max(abs(u));
end


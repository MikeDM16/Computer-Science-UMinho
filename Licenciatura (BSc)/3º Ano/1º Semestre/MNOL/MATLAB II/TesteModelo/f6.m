function [ f,g ] = f6( x )
    f = x(1)^2 + 2*x(2)^2 - 0.3*cos(3*pi*x(1)) - 0.4*cos(4*pi*x(2)) + 0.7;
    if(nargout > 1)
        g = [2*x(1) + 0.3*3*pi*sin(3*pi*x(1));
             4*x(2) + 0.4*4*pi*sin(4*pi*x(2))];
    end
end


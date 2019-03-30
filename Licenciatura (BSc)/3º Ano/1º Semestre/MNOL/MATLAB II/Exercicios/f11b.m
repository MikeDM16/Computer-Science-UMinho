function [ f,g,h ] = f11a( x )
    f = 0.25*x(1)^4 - 0.5*x(1)^2 + 0.1*x(1) + 0.5*x(2)^2;
    if(nargout > 1)
        g = [(x(1)^3 - x(1) +0.1); x(2)];
        if(nargout > 2)
            h = [(3*x(1)^2 - 1) 0; 0 1];
        end
    end
end


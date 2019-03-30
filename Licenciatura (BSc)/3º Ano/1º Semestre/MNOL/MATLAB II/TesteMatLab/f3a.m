function [ f ] = f3a( x )
    f = -( 0.1*sum( sin(2*pi.*x) ) + sum( x.^(1/3) ));
end


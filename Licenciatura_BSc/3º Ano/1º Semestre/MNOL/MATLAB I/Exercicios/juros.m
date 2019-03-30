function [ f ] = juros( x )
%UNTITLED6 Summary of this function goes here
%   options = optimset('Tolx', 10^-1); format long; x0 = 0.04
%   [xsol,fsol,exitflag,output]=fsolve('juros',x0,options)
f = ( (1-(1+x)^-12) \ x) - (315-91)\24

end


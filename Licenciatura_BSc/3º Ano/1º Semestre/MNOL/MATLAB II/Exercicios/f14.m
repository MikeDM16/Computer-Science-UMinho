function f= f14 (x,t,m)
    n=length(x);
    % n impar -> primeiro ramo de y(i)
    i=1:2:n-1;
    y(i)=x(i)*cos(t)-x(i+1)*sin(t);
    % n par -> segundo ramo de y(i)
    i=2:2:n-1;
    y(i)=x(i)*sin(t)+x(i+1)*cos(t);
    % i = n -> ultimo ramo de y(i)
    i=n;
    y(i)=x(i);
    
    %funcao f = sumatório de 1 até n 
    i=1:n;
    f=-sum(sin(y).*(sin(i.*y.^2/pi)).^(2*m));
end


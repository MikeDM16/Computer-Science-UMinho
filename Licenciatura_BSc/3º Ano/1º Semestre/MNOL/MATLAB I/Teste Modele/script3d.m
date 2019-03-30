x = [1.5 2 2.2 3 3.8 4]
f = [4.9 3.3 3 2 1.75 1.5]

s3 = spline(x,[-2 f 3])
s3.coefs

%1.68134(x-2.2)^3 - 1.3302(x-2.2)^2 - 1.2619(x-2.2) + 3
%2.2 é o ponto que inicia o segmento n onde se encontra o ponto que se quer
%estimar
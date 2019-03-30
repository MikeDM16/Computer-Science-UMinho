x = [1 3 4 7 9 10 11];
f = [8 10 10 13 18 20 26];
[c,s] = lsqcurvefit('f1b',[1 1 1], x, f)

% Modelo 5.5610*ln(x) + 1.0967*sin(x) + 0.0895*x^2
% o modelo é mau porque o residuo nao é proximo de 0
% residuo elevado = 82.4629


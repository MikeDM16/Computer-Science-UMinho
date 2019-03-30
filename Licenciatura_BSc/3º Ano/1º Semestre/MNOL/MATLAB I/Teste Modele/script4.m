x = [0 10 15 25 30 48 60 70 90]
f = [0 10 30 25 10 28 40 42 30]
d = [0 0.01*10 0.015*30 0.025*25 0.03*10 0.048*28 0.06*40 0.07*42 0.09*30]
%nao esquecer de criar uma nova linha da tabela conforme o contexto do
%problema
z = trapz(x,d)
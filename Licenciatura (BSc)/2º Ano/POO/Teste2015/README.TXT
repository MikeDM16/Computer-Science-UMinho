------------------------------------------------------------------------
This is the project README file. Here, you should describe your project.
Tell the reader (someone who does not know anything about this project)
all he/she needs to know. The comments should usually include at least:
------------------------------------------------------------------------

Parte I 

1.a) Se o objeto passado como argumento, não estiver iniciado, ou seja, 
nao apontar para nenhuma posiçao de memória, estão este está a null e o
metodo equals dará falso. 
    Se o objeto nao estiver a null, e portanto apontar para algum endereço 
de memoria, então vamos ver se ele tem a mesma classe que o objeto com que
estamos a chamar a comparaçao. 
    Só depois de garantir que o objeto nao está a null e é da mesma classe q
o nosso (importante caso queirarmos usar metodos get), é que com uma nova 
variavel do tipo desejado, fazemos cast da classe Objet para a classe 
desejada (Aluno) e só depois determinamos se são iguais, comparando cada uma
das variaveis de instancia individualmente.  

1.b) As subclasses de Aluno, como o caso da referida AlunoTE, herdam as
caracteristicas e metodos das suas classes superiores. Assim, qualquer objeto
da classe alunoTE, é tambem ele da classe Aluno. 
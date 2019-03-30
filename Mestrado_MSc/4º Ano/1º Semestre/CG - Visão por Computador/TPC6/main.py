# Third-party libraries
import sys, pathlib

# Externel python files 
import Meta1, Meta2

def main():
    opt = (-1)
    if(len(sys.argv[1:]) == 3):
        input_dir = str(sys.argv[1])
        output_dir =  str(sys.argv[2])
        opt =  int(sys.argv[3])
    
    if(len(sys.argv[1:]) == 2):
        input_dir = str(sys.argv[1])
        opt =  int(sys.argv[2])

    # Opção 1 : gerar as caracteristicas S, S0, I_inv e 1k pixeis aleatórios
    if(opt == 1):
        print("-> Gerando imagens img_invertida, S e S0 para TREINO")
        pathlib.Path(output_dir + "Treino/Indices/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Treino/S_img/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Treino/Vasos/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Treino/S0_img/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Treino/I_Inv/").mkdir(parents=True, exist_ok=True) 
        Meta1.gera_Features_SVM(input_dir+str("training/"), output_dir+str("/Treino"), "treino")
        
        print("-> Gerando imagens img_invertida, S e S0 para TESTE")
        pathlib.Path(output_dir + "Teste/Indices/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Teste/S_img/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Teste/Vasos/").mkdir(parents=True, exist_ok=True)
        pathlib.Path(output_dir + "Teste/S0_img/").mkdir(parents=True, exist_ok=True) 
        pathlib.Path(output_dir + "Teste/I_Inv/").mkdir(parents=True, exist_ok=True) 
        Meta1.gera_Features_SVM(input_dir+str("test/"), output_dir+str("/Teste"), "teste")

    # Opção 2 : realizar segmentação baseada em threshold de S
    if(opt == 2):
        pathlib.Path(output_dir).mkdir(parents=True, exist_ok=True) 
        Meta1.segmentacao_pelo_S(input_dir, output_dir)
    
    # Opção 3 : Realizar treino e teste de SVMs, para identificação de vasos da retina
    if(opt == 3):
        Meta2.aprender_SVM(input_dir)

if __name__ == "__main__": main()


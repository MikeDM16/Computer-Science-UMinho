
/**
 * Write a description of class Teste here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Teste
{
    public Map<String, ArrayList<Veiculo>> veiculosCliente(){
        Set<String> totalCli = new TreeSet<String>();
        ArrayList<Veiculo> = null; Veiculo v = null;
        Map<String, ArrayList<Veiculo>> retorno = new TreeMap<String, ArrayList<Veiculo>>;
        
        for(Map.Entry<String, Veiculo> m: this.veiculos.entrySet()){
            v = m.getValue();
            for(Contratavel c: v.getContratavel()){
                if(totalCli.contaisn(c.getCliente()==false)){
                    totalCli.add(c.getCliente());
                }
            }
        }
        
        for(String cli: totalCli){
            aux = new ArrayList<Veiculo>();
            for(Map.Entry<String, Veiculo> m: this.veiculos.entrySet()){
                v = m.getValue();
                for(Contratavel c: v.getContratavel()){
                    if(c.getCliente().equals(cli)){
                        aux.add(v);
                        break;
                    }
                }
            }
            retorno.put(cli, aux);
        }
        
        return retorno; 
    }
}

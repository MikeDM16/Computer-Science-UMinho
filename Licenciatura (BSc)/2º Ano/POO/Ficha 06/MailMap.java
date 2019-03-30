
/**
 * Write a description of class MailMap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Map;
import java.util.List;
import java.util.HashMap;
public class MailMap
{
    //variaveis da instancia MailMap
    private int totalEnderecos;
    private Map<String, List<Mail>> mailmap;
    
    //construtores da instancia MailMap
    public MailMap(){
        this.totalEnderecos = 0;
        this.mailmap = new HashMap<>();
    }
    
    public MailMap(MailMap p){
        this.totalEnderecos = p.getTotalEnderecos();
        Map<String, List<Mail>> copia = new HashMap<>();
        for(Map.Entry<String, List<Mail>> e: mailmap.entrySet())
            copia.put(e.getRemetente(), e.getValue().clone());      
    }
    
    
    public int getTotalEnderecos(){return this.totalEnderecos; }
    public Map<String, List<Mail>> getMailMap(){return; }
    
    public void addEmail(Mail m ){
        List<Mail> mbox = null;
        if(mailmap.constaisKey(m.getRemetente())){
            mbox = mailmap.get(m.getRemetente());
        }
        else{
            mbox = new ArrayList<>();
            mailmap.put(m.getRemetente(), mbox);
        }
        mbox.add(m.clone);
    }
}


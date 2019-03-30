package Exceptions;

public class ClientExistsException extends Exception {
    
    public ClientExistsException() {
        super();
    }
    
    public ClientExistsException(String m){
        super(m);
    }
    
}

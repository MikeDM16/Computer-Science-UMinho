package exceptions;


public class InexistentUserException extends RuntimeException{

    public InexistentUserException(){super(); }

    public InexistentUserException(String msg){
        super(msg);
    }
}

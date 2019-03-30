package exceptions;


public class ExistentUserException extends RuntimeException{

    public ExistentUserException(){super(); }

    public ExistentUserException(String msg){
        super(msg);
    }
}
package user.config;

public class InvalidOrderException extends Exception{
    public InvalidOrderException(String errorMessage){
        super(errorMessage);
    }
}
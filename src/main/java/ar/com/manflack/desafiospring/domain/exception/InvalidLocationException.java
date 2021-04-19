package ar.com.manflack.desafiospring.domain.exception;

public class InvalidLocationException extends Exception
{
    public InvalidLocationException()
    {
        super("The location provided isn't valid. Please check them.");
    }
}

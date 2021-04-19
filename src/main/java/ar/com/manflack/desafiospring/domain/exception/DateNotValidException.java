package ar.com.manflack.desafiospring.domain.exception;

public class DateNotValidException extends Exception
{
    public DateNotValidException(String message)
    {
        super(message);
    }

    public DateNotValidException()
    {
        super("Date not valid");
    }
}

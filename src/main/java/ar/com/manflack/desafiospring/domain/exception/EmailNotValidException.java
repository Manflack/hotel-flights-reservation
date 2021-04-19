package ar.com.manflack.desafiospring.domain.exception;

public class EmailNotValidException extends Exception
{
    public EmailNotValidException()
    {
        super("Email not valid, please check the spell");
    }
}

package ar.com.manflack.desafiospring.domain.exception;

public class InvalidCardDuesException extends Exception
{
    public InvalidCardDuesException() {
        super("Invalid number of dues");
    }
}

package ar.com.manflack.desafiospring.domain.exception;

public class CardNotProvidedException extends Exception
{
    public CardNotProvidedException()
    {
        super("The payment method wasn't provided");
    }
}

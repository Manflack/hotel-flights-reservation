package ar.com.manflack.desafiospring.domain.exception;

public class NoRoomAvailableException extends Exception
{
    public NoRoomAvailableException()
    {
        super("We apologies, given the parameters, we don't have rooms available. Please consider change some "
                + "parameters.");
    }
}

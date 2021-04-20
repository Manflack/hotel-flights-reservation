package ar.com.manflack.desafiospring.domain.exception.flight;

public class FlightNotAvailableException extends Exception
{
    public FlightNotAvailableException()
    {
        super("We apologies, given the parameters, we don't have flights available. Please consider change some "
                + "parameters.");
    }
}

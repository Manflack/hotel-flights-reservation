package ar.com.manflack.desafiospring.domain.exception.flight;

public class FlightDataErrorException extends Exception
{
    public FlightDataErrorException()
    {
        super("Can't put a same composition of flight number and seat type");
    }
}

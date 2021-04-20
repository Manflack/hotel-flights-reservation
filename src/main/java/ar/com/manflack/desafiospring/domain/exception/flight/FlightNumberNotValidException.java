package ar.com.manflack.desafiospring.domain.exception.flight;

public class FlightNumberNotValidException extends Exception
{
    public FlightNumberNotValidException()
    {
        super("The composition of the flight number isn't valid. Please, follow the next composition: FFFF-NNNN");
    }
}

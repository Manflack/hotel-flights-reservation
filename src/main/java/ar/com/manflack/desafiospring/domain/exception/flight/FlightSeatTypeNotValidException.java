package ar.com.manflack.desafiospring.domain.exception.flight;

public class FlightSeatTypeNotValidException extends Exception
{
    public FlightSeatTypeNotValidException()
    {
        super("Flight seat type isn't valid");
    }
}

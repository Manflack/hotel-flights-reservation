package ar.com.manflack.desafiospring.domain.exception;

public class ReservationNotValidException extends Exception
{
    public ReservationNotValidException()
    {
        super("Reservation isn't valid, check the spell");
    }
}

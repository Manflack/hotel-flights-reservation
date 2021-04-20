package ar.com.manflack.desafiospring.domain.exception.hotel;

public class ReservationNotValidException extends Exception
{
    public ReservationNotValidException()
    {
        super("Reservation isn't valid. Please validate all the fields provided.");
    }
}

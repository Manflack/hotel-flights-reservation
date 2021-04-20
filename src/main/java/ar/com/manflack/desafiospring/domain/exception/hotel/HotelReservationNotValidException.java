package ar.com.manflack.desafiospring.domain.exception.hotel;

public class HotelReservationNotValidException extends Exception
{
    public HotelReservationNotValidException()
    {
        super("Reservation isn't valid. Please validate all the fields provided.");
    }
}

package ar.com.manflack.desafiospring.domain.exception.hotel;

public class HotelNameNotValidException extends Exception
{
    public HotelNameNotValidException()
    {
        super("The name of the hotel isn't valid");
    }
}

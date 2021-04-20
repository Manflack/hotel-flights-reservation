package ar.com.manflack.desafiospring.domain.exception.hotel;

public class HotelPriceNotValidException extends Exception
{
    public HotelPriceNotValidException()
    {
        super("The hotel price isn't valid, please check the spell of the field");
    }
}

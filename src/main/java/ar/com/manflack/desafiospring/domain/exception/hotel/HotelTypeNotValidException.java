package ar.com.manflack.desafiospring.domain.exception.hotel;

public class HotelTypeNotValidException extends Exception
{
    public HotelTypeNotValidException()
    {
        super("Hotel type isn't valid, please check the spell");
    }
}

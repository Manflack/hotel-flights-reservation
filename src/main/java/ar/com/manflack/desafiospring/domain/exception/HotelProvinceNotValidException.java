package ar.com.manflack.desafiospring.domain.exception;

public class HotelProvinceNotValidException extends Exception
{
    public HotelProvinceNotValidException()
    {
        super("The hotel province isn't valid, check if the value provided it's only UNICODE letters");
    }
}

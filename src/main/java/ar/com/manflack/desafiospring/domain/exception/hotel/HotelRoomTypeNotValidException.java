package ar.com.manflack.desafiospring.domain.exception.hotel;

public class HotelRoomTypeNotValidException extends Exception
{
    public HotelRoomTypeNotValidException()
    {
        super("Type room isn't valid, please check if the amount and type are the same.");
    }
}

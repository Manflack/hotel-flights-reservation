package ar.com.manflack.desafiospring.domain.exception;

public class RoomTypeNotValidException extends Exception
{
    public RoomTypeNotValidException()
    {
        super("Type room isn't valid, please check if the amount and type are the same.");
    }
}

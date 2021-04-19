package ar.com.manflack.desafiospring.domain.exception;

public class HotelDataErrorException extends Exception
{
    public HotelDataErrorException()
    {
        super("Can't put hotel code equals to another one, please consider change the value.");
    }
}

package ar.com.manflack.desafiospring.domain.exception;

public class HotelCodeNotValidException extends Exception
{
    public HotelCodeNotValidException()
    {
        super("The composition of the hotel code isn't valid. Please, follow the next composition: LL-NNNN");
    }
}

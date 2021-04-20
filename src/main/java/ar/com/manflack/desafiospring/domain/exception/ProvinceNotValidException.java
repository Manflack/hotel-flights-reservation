package ar.com.manflack.desafiospring.domain.exception;

public class ProvinceNotValidException extends Exception
{
    public ProvinceNotValidException()
    {
        super("The province isn't valid or not exists");
    }
}

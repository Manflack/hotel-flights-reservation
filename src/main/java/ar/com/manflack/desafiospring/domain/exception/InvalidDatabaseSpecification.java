package ar.com.manflack.desafiospring.domain.exception;

public class InvalidDatabaseSpecification extends Exception
{
    public InvalidDatabaseSpecification()
    {
        super("The context provided isn't valid, please read the documentation to provide a valid directory and "
                + "filename");
    }
}

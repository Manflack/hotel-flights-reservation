package ar.com.manflack.desafiospring.app.dto;

public class StatusDTOFixture
{
    public static final Integer status = 200;
    public static final String message = "El proceso termino satisfactoriamente";

    public static StatusDTO withDefaults()
    {
        return new StatusDTO(status, message);
    }
}

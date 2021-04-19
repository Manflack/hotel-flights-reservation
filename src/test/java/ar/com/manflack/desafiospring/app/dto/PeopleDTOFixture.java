package ar.com.manflack.desafiospring.app.dto;

public class PeopleDTOFixture
{
    public static final String dni = "11222333";
    public static final String name = "John";
    public static final String lastname = "Cena";
    public static final String birthDate = "16/03/1999";
    public static final String mail = "thechamp@johncena.com";

    public static PeopleDTO withDefaults1()
    {
        return new PeopleDTO(dni, name, lastname, birthDate, mail);
    }

    public static PeopleDTO withDefaults2()
    {
        return new PeopleDTO("22333444", "Malibu", "Coco", "18/05/2000", "malibudecoco@gmail.com");
    }
}

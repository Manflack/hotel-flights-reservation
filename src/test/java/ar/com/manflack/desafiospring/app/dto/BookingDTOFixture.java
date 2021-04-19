package ar.com.manflack.desafiospring.app.dto;

import java.util.Arrays;
import java.util.List;

public class BookingDTOFixture
{
    public static final String dateFrom = "10/05/2021";
    public static final String dateTo = "10/09/2021";
    public static final String destination = "Buenos Aires";
    public static final String hotelCode = "AR-0001";
    public static final Integer peopleAmount = 2;
    public static final String roomType = "DOBLE";
    public static final List<PeopleDTO> people =
            Arrays.asList(PeopleDTOFixture.withDefaults1(), PeopleDTOFixture.withDefaults2());
    public static final CardDTO paymentMethod = CardDTOFixture.withDefaults();

    public static BookingDTO withDefaults()
    {
        return new BookingDTO(dateFrom, dateTo, destination, hotelCode, peopleAmount, roomType, people, paymentMethod);
    }
}

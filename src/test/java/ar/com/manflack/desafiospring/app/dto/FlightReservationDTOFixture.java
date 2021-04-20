package ar.com.manflack.desafiospring.app.dto;

import java.util.Arrays;
import java.util.List;

public class FlightReservationDTOFixture
{
    public static final String dateFrom = "15/03/1998";
    public static final String dateTo = "23/06/1998";
    public static final String origin = FlightDTOFixture.origin;
    public static final String destination = FlightDTOFixture.destination;
    public static final String flightNumber = FlightDTOFixture.number;
    public static final Integer seats = 2;
    public static final String seatType = FlightDTOFixture.seatType;
    public static final List<PeopleDTO> people =
            Arrays.asList(PeopleDTOFixture.withDefaults1(), PeopleDTOFixture.withDefaults2());
    public static final CardDTO paymentMethod = CardDTOFixture.withDefaults();

    public static FlightReservationDTO withDefaults()
    {
        return new FlightReservationDTO(dateFrom,
                dateTo,
                origin,
                destination,
                flightNumber,
                seats,
                seatType,
                people,
                paymentMethod);
    }
}

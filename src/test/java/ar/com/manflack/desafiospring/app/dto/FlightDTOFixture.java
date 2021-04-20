package ar.com.manflack.desafiospring.app.dto;

import java.time.LocalDate;

public class FlightDTOFixture
{
    public static final String number = "JOJO-9999";
    public static final String origin = "Morioh";
    public static final String destination = "Grand Hotel";
    public static final String seatType = "BUSINESS";
    public static final Integer price = 15000;
    public static final LocalDate departureDate = LocalDate.of(1998, 3, 15);
    public static final LocalDate returnDate = LocalDate.of(1998, 6, 23);

    public static FlightDTO withDefaults1()
    {
        return new FlightDTO(number, origin, destination, seatType, price, departureDate, returnDate);
    }

    public static FlightDTO withDefaults2()
    {
        return new FlightDTO("JOJO-1111",
                "Grand Hotel",
                "Morioh",
                "BUSINESS",
                18000,
                LocalDate.of(1998, 6, 23),
                LocalDate.of(1998, 9, 19));
    }
}

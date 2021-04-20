package ar.com.manflack.desafiospring.app.dto;

import java.time.LocalDate;

public class HotelDTOFixture
{
    public static final String code = "AR-0001";
    public static final String name = "Hotel Guerrero";
    public static final String province = "Buenos Aires";
    public static final String type = "Single";
    public static final Integer price = 100;
    public static final LocalDate availableSince = LocalDate.of(2021, 1, 1);
    public static final LocalDate availableUntil = LocalDate.of(2021, 2, 1);
    public static final Boolean isReserved = false;

    public static HotelDTO withDefaults1()
    {
        return new HotelDTO(code, name, province, type, price, availableSince, availableUntil, isReserved);
    }

    public static HotelDTO withDefaults2()
    {
        return new HotelDTO("AR-0002",
                "Hotel Safari",
                "San Luis",
                "Doble",
                300,
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2021, 2, 1),
                false);
    }

    public static HotelDTO withReservedTrue()
    {
        return new HotelDTO(code, name, province, type, price, availableSince, availableUntil, true);
    }
}

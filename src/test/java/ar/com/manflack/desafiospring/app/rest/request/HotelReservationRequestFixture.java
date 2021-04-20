package ar.com.manflack.desafiospring.app.rest.request;

import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.app.dto.BookingDTOFixture;

public class HotelReservationRequestFixture
{
    public static final String userName = "johncena@thecamp.com";
    public static final BookingDTO booking = BookingDTOFixture.withDefaults();

    public static HotelReservationRequest withDefaults()
    {
        return new HotelReservationRequest(userName, booking);
    }
}

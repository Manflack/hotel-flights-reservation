package ar.com.manflack.desafiospring.app.rest.request;

import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.app.dto.BookingDTOFixture;

public class ReservationRequestFixture
{
    public static final String userName = "johncena@thecamp.com";
    public static final BookingDTO booking = BookingDTOFixture.withDefaults();

    public static ReservationRequest withDefaults()
    {
        return new ReservationRequest(userName, booking);
    }
}

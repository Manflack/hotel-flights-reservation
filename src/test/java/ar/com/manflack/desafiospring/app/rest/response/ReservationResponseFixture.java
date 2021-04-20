package ar.com.manflack.desafiospring.app.rest.response;

import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.app.dto.BookingDTOFixture;
import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import ar.com.manflack.desafiospring.app.dto.StatusDTOFixture;

public class ReservationResponseFixture
{
    public static final String userName = "myTest@mail.com";
    public static final Double amount = 12300.0;
    public static final Double interest = 0.0;
    public static final Double total = 12300.0;
    public static final BookingDTO booking = BookingDTOFixture.withDefaults();
    public static final StatusDTO statusCode = StatusDTOFixture.withDefaults();

    public static ReservationResponse withDefaults()
    {
        return new ReservationResponse(userName, amount, interest, total, booking, statusCode);
    }
}

package ar.com.manflack.desafiospring.app.rest.request;

import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRequest
{
    private String userName;
    private BookingDTO booking;
}

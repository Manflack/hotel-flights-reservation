package ar.com.manflack.desafiospring.app.rest.response;

import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelReservationResponse
{
    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private BookingDTO booking;
    private StatusDTO statusCode;
}

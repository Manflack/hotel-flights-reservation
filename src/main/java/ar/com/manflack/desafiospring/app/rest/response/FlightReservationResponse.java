package ar.com.manflack.desafiospring.app.rest.response;

import ar.com.manflack.desafiospring.app.dto.FlightReservationDTO;
import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationResponse
{
    private String userName;
    private Double amount;
    private Double interest;
    private Double total;
    private FlightReservationDTO flightReservation;
    private StatusDTO statusCode;
}

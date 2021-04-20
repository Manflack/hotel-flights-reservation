package ar.com.manflack.desafiospring.app.rest.request;

import ar.com.manflack.desafiospring.app.dto.FlightReservationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationRequest
{
    private String userName;
    private FlightReservationDTO flightReservation;
}

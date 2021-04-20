package ar.com.manflack.desafiospring.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightReservationDTO
{
    private String dateFrom;
    private String dateTo;
    private String origin;
    private String destination;
    private String flightNumber;
    private Integer seats;
    private String seatType;
    private List<PeopleDTO> people;
    private CardDTO paymentMethod;
}

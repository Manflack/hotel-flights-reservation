package ar.com.manflack.desafiospring.app.dto;

import java.time.LocalDate;

import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FlightDTO
{
    private String number;
    private String origin;
    private String destination;
    private String seatType;
    private Integer price;
    private LocalDate departureDate;
    private LocalDate returnDate;

    public FlightDTO(String number, String origin, String destination, String seatType, String price,
            String departureDate, String returnDate) throws DateNotValidException
    {
        this.number = number;
        this.origin = origin;
        this.destination = destination;
        this.seatType = seatType;
        this.price = Integer.valueOf(price.replace("$", "").replace(".", ""));
        this.departureDate = DateUtils.getDateFromString(departureDate);
        this.returnDate = DateUtils.getDateFromString(returnDate);
    }
}

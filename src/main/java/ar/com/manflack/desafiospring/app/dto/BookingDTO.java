package ar.com.manflack.desafiospring.app.dto;

import java.util.List;

import ar.com.manflack.desafiospring.app.dto.CardDTO;
import ar.com.manflack.desafiospring.app.dto.PeopleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO
{
    private String dateFrom;
    private String dateTo;
    private String destination;
    private String hotelCode;
    private Integer peopleAmount;
    private String roomType;
    private List<PeopleDTO> people;
    private CardDTO paymentMethod;
}

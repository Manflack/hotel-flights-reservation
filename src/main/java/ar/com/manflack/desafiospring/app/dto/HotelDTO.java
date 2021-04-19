package ar.com.manflack.desafiospring.app.dto;

import java.time.LocalDate;

import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO
{
    private String code;
    private String name;
    private String province;
    private String type;
    private Integer price;
    private LocalDate availableSince;
    private LocalDate availableUntil;
    private Boolean isReserved;

    public HotelDTO(String code, String name, String province, String type, String price, String availableSince,
            String availableUntil, String isReserved) throws DateNotValidException
    {
        this.code = code;
        this.name = name;
        this.province = province;
        this.type = type;
        this.price = Integer.valueOf(price.replace("$", "").replace(".", ""));
        this.availableSince = DateUtils.getDateFromString(availableSince);
        this.availableUntil = DateUtils.getDateFromString(availableUntil);
        this.isReserved = Boolean.valueOf(isReserved);
    }
}

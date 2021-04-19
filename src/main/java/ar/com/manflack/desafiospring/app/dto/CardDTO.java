package ar.com.manflack.desafiospring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO
{
    private String type;
    private String number;
    private Integer dues;
}

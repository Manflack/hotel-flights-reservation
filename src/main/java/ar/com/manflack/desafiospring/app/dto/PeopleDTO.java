package ar.com.manflack.desafiospring.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDTO
{
    private String dni;
    private String name;
    private String lastname;
    private String birthDate;
    private String mail;
}

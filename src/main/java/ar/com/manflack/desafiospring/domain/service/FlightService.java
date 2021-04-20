package ar.com.manflack.desafiospring.domain.service;

import java.util.List;

import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import ar.com.manflack.desafiospring.domain.exception.ProvinceNotValidException;

public interface FlightService
{
    List<FlightDTO> getAllFlights(String departureDate, String returnDate, String origin, String destination)
            throws ProvinceNotValidException, DateNotValidException;
}

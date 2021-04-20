package ar.com.manflack.desafiospring.domain.service;

import java.util.List;

import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.app.dto.FlightReservationDTO;
import ar.com.manflack.desafiospring.app.rest.response.FlightReservationResponse;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightNotAvailableException;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightSeatTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;

public interface FlightService
{
    List<FlightDTO> getAllFlights(String departureDate, String returnDate, String origin, String destination)
            throws ProvinceNotValidException, DateNotValidException;

    FlightReservationResponse makeFlightReservation(String username, FlightReservationDTO flightReservation)
            throws EmailNotValidException, ReservationNotValidException, CardNotProvidedException,
            FlightSeatTypeNotValidException, DateNotValidException, ProvinceNotValidException,
            FlightNotAvailableException, InvalidCardDuesException;
}

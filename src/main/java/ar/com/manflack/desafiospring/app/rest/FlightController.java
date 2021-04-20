package ar.com.manflack.desafiospring.app.rest;

import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import ar.com.manflack.desafiospring.app.rest.request.FlightReservationRequest;
import ar.com.manflack.desafiospring.app.rest.response.FlightReservationResponse;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightNotAvailableException;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightSeatTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
import ar.com.manflack.desafiospring.domain.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FlightController
{
    @Autowired
    private FlightService flightService;

    @GetMapping("/api/v1/flights")
    public ResponseEntity<?> getAllHotels(@RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo, @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) throws ProvinceNotValidException, DateNotValidException
    {
        return new ResponseEntity<>(flightService.getAllFlights(dateFrom, dateTo, origin, destination), HttpStatus.OK);
    }

    @PostMapping("/api/v1/flight-reservation")
    public ResponseEntity<?> makeFlightReservation(@RequestBody FlightReservationRequest request)
            throws EmailNotValidException, ReservationNotValidException, CardNotProvidedException,
            FlightSeatTypeNotValidException, DateNotValidException, ProvinceNotValidException,
            FlightNotAvailableException, InvalidCardDuesException
    {
        FlightReservationResponse response =
                flightService.makeFlightReservation(request.getUserName(), request.getFlightReservation());
        response.setStatusCode(new StatusDTO(HttpStatus.OK.value(), "El proceso termino satisfactoriamente"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

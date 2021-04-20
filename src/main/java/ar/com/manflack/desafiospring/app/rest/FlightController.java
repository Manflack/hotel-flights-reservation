package ar.com.manflack.desafiospring.app.rest;

import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import ar.com.manflack.desafiospring.domain.exception.ProvinceNotValidException;
import ar.com.manflack.desafiospring.domain.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<?> makeFlightReservation() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

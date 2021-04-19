package ar.com.manflack.desafiospring.app.rest;

import ar.com.manflack.desafiospring.app.rest.request.ReservationRequest;
import ar.com.manflack.desafiospring.app.rest.response.ReservationResponse;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HotelController
{
    @Autowired
    private HotelService hotelService;

    @GetMapping("/api/v1/hotels")
    public ResponseEntity<?> getAllHotels(@RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo, @RequestParam(required = false) String destination)
            throws DateNotValidException
    {
        return new ResponseEntity<>(hotelService.getAllHotels(dateFrom, dateTo, destination), HttpStatus.OK);
    }

    @PostMapping("/api/v1/booking")
    public ResponseEntity<?> makeReservation(@RequestBody ReservationRequest request)
            throws DateNotValidException, NoRoomAvailableException, EmailNotValidException, InvalidCardDuesException,
            CardNotProvidedException
    {
        ReservationResponse response = hotelService.makeReservation(request.getUserName(), request.getBooking());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
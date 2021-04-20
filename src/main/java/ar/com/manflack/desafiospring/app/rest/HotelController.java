package ar.com.manflack.desafiospring.app.rest;

import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import ar.com.manflack.desafiospring.app.rest.request.HotelReservationRequest;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponse;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelRoomTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
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
            throws DateNotValidException, ProvinceNotValidException
    {
        return new ResponseEntity<>(hotelService.getAllHotels(dateFrom, dateTo, destination), HttpStatus.OK);
    }

    @PostMapping("/api/v1/booking")
    public ResponseEntity<?> makeHotelReservation(@RequestBody HotelReservationRequest request)
            throws DateNotValidException, HotelNoRoomAvailableException, EmailNotValidException, InvalidCardDuesException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelRoomTypeNotValidException
    {
        HotelReservationResponse response = hotelService.makeReservation(request.getUserName(), request.getBooking());
        response.setStatusCode(new StatusDTO(HttpStatus.OK.value(), "El proceso termino satisfactoriamente"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

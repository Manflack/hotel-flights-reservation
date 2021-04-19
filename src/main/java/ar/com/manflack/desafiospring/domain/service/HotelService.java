package ar.com.manflack.desafiospring.domain.service;

import java.util.List;

import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.rest.response.ReservationResponse;
import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.domain.exception.*;

public interface HotelService
{
    List<HotelDTO> getAllHotels(String dateFrom, String dateTo, String destination) throws DateNotValidException;

    ReservationResponse makeReservation(String username, BookingDTO bookingDTO)
            throws DateNotValidException, NoRoomAvailableException, EmailNotValidException, InvalidCardDuesException,
            CardNotProvidedException;

}

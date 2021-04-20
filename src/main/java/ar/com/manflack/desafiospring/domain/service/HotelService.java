package ar.com.manflack.desafiospring.domain.service;

import java.util.List;

import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponse;
import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelCodeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelRoomTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;

public interface HotelService
{
    List<HotelDTO> getAllHotels(String dateFrom, String dateTo, String destination)
            throws DateNotValidException, ProvinceNotValidException;

    HotelReservationResponse makeReservation(String username, BookingDTO bookingDTO)
            throws DateNotValidException, HotelNoRoomAvailableException, EmailNotValidException,
            InvalidCardDuesException, CardNotProvidedException, ProvinceNotValidException,
            HotelRoomTypeNotValidException, ReservationNotValidException, HotelCodeNotValidException;

}

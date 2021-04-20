package ar.com.manflack.desafiospring.app.rest;

import java.util.Arrays;
import java.util.List;

import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.dto.HotelDTOFixture;
import ar.com.manflack.desafiospring.app.rest.request.HotelReservationRequestFixture;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponse;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponseFixture;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelCodeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelRoomTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
import ar.com.manflack.desafiospring.domain.service.impl.HotelServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(MockitoJUnitRunner.class)
@EnableWebMvc
public class HotelControllerTest
{
    @InjectMocks
    private HotelController controller;

    @Mock
    private HotelServiceImpl service;

    @Test
    public void getAllHotels_withoutParams_OK() throws DateNotValidException, ProvinceNotValidException
    {
        List<HotelDTO> hotelList = Arrays.asList(HotelDTOFixture.withDefaults1(), HotelDTOFixture.withDefaults2());

        when(service.getAllHotels(null, null, null)).thenReturn(hotelList);

        ResponseEntity<?> response = controller.getAllHotels(null, null, null);
        assertNotNull(response);
        assertNotNull(response.getBody());

        List<HotelDTO> responseAsList = (List<HotelDTO>) response.getBody();
        assertNotNull(responseAsList);
        assertEquals(hotelList.size(), responseAsList.size());
        assertEquals(hotelList, responseAsList);

        verify(service, times(1)).getAllHotels(null, null, null);
    }

    @Test
    public void makeReservation_defaultParams_OK()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException,
            DateNotValidException, CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException, HotelCodeNotValidException
    {
        when(service.makeReservation(any(), any())).thenReturn(HotelReservationResponseFixture.withDefaults());

        ResponseEntity<?> response = controller.makeHotelReservation(HotelReservationRequestFixture.withDefaults());
        assertNotNull(response);
        assertNotNull(response.getBody());

        HotelReservationResponse hotelReservationResponse = (HotelReservationResponse) response.getBody();
        assertNotNull(hotelReservationResponse);
        assertEquals(HotelReservationResponseFixture.withDefaults(), hotelReservationResponse);

        verify(service, times(1)).makeReservation(any(), any());
    }
}
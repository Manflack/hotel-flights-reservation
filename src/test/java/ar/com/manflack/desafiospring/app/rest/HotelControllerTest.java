package ar.com.manflack.desafiospring.app.rest;

import java.util.Arrays;
import java.util.List;

import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.dto.HotelDTOFixture;
import ar.com.manflack.desafiospring.app.rest.request.ReservationRequest;
import ar.com.manflack.desafiospring.app.rest.request.ReservationRequestFixture;
import ar.com.manflack.desafiospring.app.rest.response.ReservationResponse;
import ar.com.manflack.desafiospring.app.rest.response.ReservationResponseFixture;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.service.impl.HotelServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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
    public void getAllHotels_withoutParams_OK() throws DateNotValidException, InvalidLocationException
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
            throws RoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, InvalidLocationException, ReservationNotValidException, NoRoomAvailableException
    {
        when(service.makeReservation(any(), any())).thenReturn(ReservationResponseFixture.withDefaults());

        ResponseEntity<?> response = controller.makeReservation(ReservationRequestFixture.withDefaults());
        assertNotNull(response);
        assertNotNull(response.getBody());

        ReservationResponse reservationResponse = (ReservationResponse) response.getBody();
        assertNotNull(reservationResponse);
        assertEquals(ReservationResponseFixture.withDefaults(), reservationResponse);

        verify(service, times(1)).makeReservation(any(), any());
    }
}
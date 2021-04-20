package ar.com.manflack.desafiospring.domain.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.app.dto.FlightDTOFixture;
import ar.com.manflack.desafiospring.app.dto.FlightReservationDTOFixture;
import ar.com.manflack.desafiospring.app.rest.response.FlightReservationResponse;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightNotAvailableException;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightSeatTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
import ar.com.manflack.desafiospring.domain.repository.FlightRepository;
import ar.com.manflack.desafiospring.domain.service.impl.FlightServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@EnableWebMvc
public class FlightServiceImplTest
{
    @InjectMocks
    private FlightServiceImpl service;

    @Mock
    private FlightRepository flightRepository;

    @Test
    public void getAllFlights_withoutParams_OK() throws DateNotValidException, ProvinceNotValidException
    {
        List<FlightDTO> hotelList = Arrays.asList(FlightDTOFixture.withDefaults1(), FlightDTOFixture.withDefaults2());

        when(flightRepository.getAll()).thenReturn(hotelList);

        List<FlightDTO> response = service.getAllFlights(null, null, null, null);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(hotelList, response);

        verify(flightRepository, times(1)).getAll();
    }

    @Test
    public void makeFlightReservation_OK()
            throws ProvinceNotValidException, DateNotValidException, EmailNotValidException, InvalidCardDuesException,
            FlightSeatTypeNotValidException, CardNotProvidedException, FlightNotAvailableException,
            ReservationNotValidException
    {
        List<FlightDTO> hotelList = Arrays.asList(FlightDTOFixture.withDefaults1(), FlightDTOFixture.withDefaults2());

        when(flightRepository.getAll()).thenReturn(hotelList);
        when(flightRepository.findByNumberAndOriginAndDestinationAndSeatTypeAndBetweenDepartureDateAndReturnDate(any(),
                any(),
                any(),
                any(),
                any(),
                any())).thenReturn(Optional.of(FlightDTOFixture.withDefaults1()));

        FlightReservationResponse response =
                service.makeFlightReservation("joestar@jojo.com", FlightReservationDTOFixture.withDefaults());
        assertNotNull(response);
        assertNotNull(response.getFlightReservation());

        verify(flightRepository,
                times(1)).findByNumberAndOriginAndDestinationAndSeatTypeAndBetweenDepartureDateAndReturnDate(any(),
                any(),
                any(),
                any(),
                any(),
                any());
    }
}

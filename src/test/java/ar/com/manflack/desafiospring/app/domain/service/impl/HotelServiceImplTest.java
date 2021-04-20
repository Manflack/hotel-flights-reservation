package ar.com.manflack.desafiospring.app.domain.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ar.com.manflack.desafiospring.app.dto.*;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponse;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponseFixture;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelRoomTypeNotValidException;
import ar.com.manflack.desafiospring.domain.repository.HotelRepository;
import ar.com.manflack.desafiospring.domain.service.impl.HotelServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class HotelServiceImplTest
{
    @InjectMocks
    private HotelServiceImpl service;

    @Mock
    private HotelRepository hotelRepository;

    private ObjectMapper mapper;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setup()
    {
        mapper = new ObjectMapper().findAndRegisterModules()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void getAllHotels_withoutParams_OK() throws DateNotValidException, ProvinceNotValidException
    {
        List<HotelDTO> hotelList = Arrays.asList(HotelDTOFixture.withDefaults1(), HotelDTOFixture.withDefaults2());

        when(hotelRepository.getAll()).thenReturn(hotelList);

        List<HotelDTO> response = service.getAllHotels(null, null, null);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(hotelList, response);

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_withDestination_OK() throws DateNotValidException, ProvinceNotValidException
    {
        List<HotelDTO> hotelList = Arrays.asList(HotelDTOFixture.withDefaults1(), HotelDTOFixture.withDefaults2());

        when(hotelRepository.getAll()).thenReturn(hotelList);

        List<HotelDTO> response = service.getAllHotels(null, null, "Buenos Aires");
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(HotelDTOFixture.withDefaults1(), response.get(0));

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_withDates_OK() throws DateNotValidException, ProvinceNotValidException
    {
        List<HotelDTO> hotelList = Arrays.asList(HotelDTOFixture.withDefaults1(), HotelDTOFixture.withDefaults2());

        when(hotelRepository.getAll()).thenReturn(hotelList);

        List<HotelDTO> response = service.getAllHotels("01/01/2021", "01/02/2021", null);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(hotelList, response);

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_DateNotValidException_case1() throws DateNotValidException, ProvinceNotValidException
    {
        exceptionRule.expect(DateNotValidException.class);

        service.getAllHotels("01/01/2021", null, null);

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_DateNotValidException_case2() throws DateNotValidException, ProvinceNotValidException
    {
        exceptionRule.expect(DateNotValidException.class);

        service.getAllHotels(null, "01/01/2021", null);

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_DateNotValidException_case3() throws DateNotValidException, ProvinceNotValidException
    {
        exceptionRule.expect(DateNotValidException.class);

        service.getAllHotels("01/01/2022", "01/01/2021", null);

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_InvalidLocationException_case1() throws DateNotValidException, ProvinceNotValidException
    {
        exceptionRule.expect(ProvinceNotValidException.class);

        service.getAllHotels(null, null, "This is a JoJo reference");

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void getAllHotels_InvalidLocationException_case2() throws DateNotValidException, ProvinceNotValidException
    {
        exceptionRule.expect(ProvinceNotValidException.class);

        when(hotelRepository.getAll()).thenReturn(Collections.singletonList(HotelDTOFixture.withDefaults1()));

        service.getAllHotels(null, null, "This is a JoJo reference");

        verify(hotelRepository, times(1)).getAll();
    }

    @Test
    public void makeReservation_OK()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        when(hotelRepository.findByCodeAndDestinationAndTypeAndBetweenDateFromAndDateToAndIsReserved(any(),
                any(),
                any(),
                any(),
                any(),
                any())).thenReturn(Optional.of(HotelDTOFixture.withDefaults1()));
        when(hotelRepository.getAll()).thenReturn(Collections.singletonList(HotelDTOFixture.withDefaults1()));
        when(hotelRepository.saveAndFlush(any())).thenReturn(HotelDTOFixture.withReservedTrue());

        HotelReservationResponse hotelReservationResponse =
                service.makeReservation("kujo.jotaro@joestar.com", BookingDTOFixture.withDefaults());

        assertNotNull(hotelReservationResponse);
        assertNotNull(hotelReservationResponse.getBooking());
        assertEquals(HotelReservationResponseFixture.amount, hotelReservationResponse.getAmount());
        assertEquals(HotelReservationResponseFixture.interest, hotelReservationResponse.getInterest());
        assertEquals(HotelReservationResponseFixture.total, hotelReservationResponse.getTotal());

        verify(hotelRepository, times(1)).findByCodeAndDestinationAndTypeAndBetweenDateFromAndDateToAndIsReserved(any(),
                any(),
                any(),
                any(),
                any(),
                any());
        verify(hotelRepository, times(1)).getAll();
        verify(hotelRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void makeReservation_EmailNotValidException_case1()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        exceptionRule.expect(EmailNotValidException.class);

        service.makeReservation("yareyaredaze", BookingDTOFixture.withDefaults());
    }

    @Test
    public void makeReservation_EmailNotValidException_case2()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        exceptionRule.expect(EmailNotValidException.class);

        service.makeReservation("yareyaredaze@", BookingDTOFixture.withDefaults());
    }

    @Test
    public void makeReservation_EmailNotValidException_case3()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        exceptionRule.expect(EmailNotValidException.class);

        service.makeReservation("@com.com.com", BookingDTOFixture.withDefaults());
    }

    @Test
    public void makeReservation_ReservationNotValidException_case1()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        exceptionRule.expect(ReservationNotValidException.class);

        service.makeReservation("kujo.jotaro@joestar.com", null);
    }

    @Test
    public void makeReservation_ReservationNotValidException_case2()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        exceptionRule.expect(ReservationNotValidException.class);

        BookingDTO booking = BookingDTOFixture.withDefaults();
        booking.setPeople(null);

        service.makeReservation("kujo.jotaro@joestar.com", booking);
    }

    @Test
    public void makeReservation_CardNotProvidedException_case1()
            throws HotelRoomTypeNotValidException, EmailNotValidException, InvalidCardDuesException, DateNotValidException,
            CardNotProvidedException, ProvinceNotValidException, ReservationNotValidException,
            HotelNoRoomAvailableException
    {
        exceptionRule.expect(CardNotProvidedException.class);

        BookingDTO booking = BookingDTOFixture.withDefaults();
        booking.setPaymentMethod(null);

        service.makeReservation("kujo.jotaro@joestar.com", booking);
    }
}

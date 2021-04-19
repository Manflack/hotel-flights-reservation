package ar.com.manflack.desafiospring.app.domain.service.impl;

import java.util.Arrays;
import java.util.List;

import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.dto.HotelDTOFixture;
import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import ar.com.manflack.desafiospring.domain.exception.InvalidLocationException;
import ar.com.manflack.desafiospring.domain.repository.HotelRepository;
import ar.com.manflack.desafiospring.domain.service.impl.HotelServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
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
public class HotelServiceImplTest
{
    @InjectMocks
    private HotelServiceImpl service;

    @Mock
    HotelRepository hotelRepository;

    private ObjectMapper mapper;

    @Before
    public void setup()
    {
        mapper = new ObjectMapper().findAndRegisterModules()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void getAllHotels_withoutParams_OK() throws DateNotValidException, InvalidLocationException
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
    public void getAllHotels_withDestination_OK() throws DateNotValidException, InvalidLocationException
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
    public void getAllHotels_withDates_OK()
    {

    }
}

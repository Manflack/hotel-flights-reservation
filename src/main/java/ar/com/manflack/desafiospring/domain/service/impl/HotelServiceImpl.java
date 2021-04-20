package ar.com.manflack.desafiospring.domain.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.com.manflack.desafiospring.app.dto.CardDTO;
import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.enums.CardInterestEnum;
import ar.com.manflack.desafiospring.app.rest.response.ReservationResponse;
import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelReservationNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelRoomTypeNotValidException;
import ar.com.manflack.desafiospring.domain.repository.HotelRepository;
import ar.com.manflack.desafiospring.domain.service.HotelService;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import ar.com.manflack.desafiospring.domain.util.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class HotelServiceImpl implements HotelService
{
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<HotelDTO> getAllHotels(String dateFrom, String dateTo, String destination)
            throws DateNotValidException, ProvinceNotValidException
    {
        List<HotelDTO> hotelList = hotelRepository.getAll();

        if (StringUtils.isNotBlank(destination))
            validateDestinationInDatabaseAvoidCall(destination, hotelList);

        // we'll prepare a stream of hotelDTO to be filtered given the case of any params is provided
        Stream<HotelDTO> hotelListFiltered = hotelList.stream();

        // check if some date filter was enabled
        if (StringUtils.isNotBlank(dateFrom) || StringUtils.isNotBlank(dateTo))
        {
            // only when we have two date to filter, we can filter with that params, otherwise, throw exception
            if (StringUtils.isBlank(dateFrom) || StringUtils.isBlank(dateTo))
                throw new DateNotValidException(
                        "You must provide dateFrom and dateTo with the format " + DateUtils.DATE_FORMAT);
            else
            {
                LocalDate since = DateUtils.getDateFromString(dateFrom);
                LocalDate until = DateUtils.getDateFromString(dateTo);

                // validate if dateFrom is before to dateTo
                DateUtils.validateSinceAndUntil(dateFrom, dateTo);
                // then filter by availability of the dates
                hotelListFiltered =
                        hotelListFiltered.filter(hotel -> DateUtils.validateAvailabilityHotel(hotel, since, until));
            }
        }

        // if the destination param was provided, we filter by them
        if (StringUtils.isNotBlank(destination))
            hotelListFiltered =
                    hotelListFiltered.filter(hotel -> StringUtils.equalsIgnoreCase(hotel.getProvince(), destination));

        // if any filter was provided, the hotelList will be replaced by hotelListFiltered
        if (StringUtils.isNotBlank(dateFrom) || StringUtils.isNotBlank(dateTo) || StringUtils.isNotBlank(destination))
            hotelList = hotelListFiltered.collect(Collectors.toList());

        return hotelList;
    }

    @Override
    public ReservationResponse makeReservation(String username, BookingDTO bookingDTO)
            throws DateNotValidException, HotelNoRoomAvailableException, EmailNotValidException,
            InvalidCardDuesException, CardNotProvidedException, ProvinceNotValidException,
            HotelReservationNotValidException, HotelRoomTypeNotValidException
    {
        ValidatorUtils.validateEmail(username);

        // if the body has some content null, throw exception
        if (bookingDTO == null || bookingDTO.getPeople() == null)
            throw new HotelReservationNotValidException();

        if (bookingDTO.getPaymentMethod() == null)
            throw new CardNotProvidedException();

        // validate typeRoom given the name of the type and the amount of people
        ValidatorUtils.validateTypeRoom(bookingDTO.getRoomType(), bookingDTO.getPeopleAmount());
        // validate if dateFrom is before dateTo
        DateUtils.validateSinceAndUntil(bookingDTO.getDateFrom(), bookingDTO.getDateTo());
        // validate if the destination exists given the database
        validateDestinationInDatabase(bookingDTO.getDestination());

        CardDTO paymentMethod = bookingDTO.getPaymentMethod();

        // wrapper the dates
        LocalDate dateFrom = DateUtils.getDateFromString(bookingDTO.getDateFrom());
        LocalDate dateTo = DateUtils.getDateFromString(bookingDTO.getDateTo());

        // get by filter, simulation of a Repository with JPA
        Optional<HotelDTO> optionalHotel =
                hotelRepository.findByCodeAndDestinationAndTypeAndBetweenDateFromAndDateToAndIsReserved(bookingDTO.getHotelCode(),
                        bookingDTO.getDestination(),
                        bookingDTO.getRoomType(),
                        dateFrom,
                        dateTo,
                        false);

        // if isn't present, throw exception because no hotel was found
        if (!optionalHotel.isPresent())
            throw new HotelNoRoomAvailableException();

        HotelDTO hotel = optionalHotel.get();
        hotel.setIsReserved(true);
        // set isReserved in true and save in database local storage, not in file
        hotel = hotelRepository.saveAndFlush(hotel);

        // calculate days of stays, amount, interest and total(amount + interest)
        long daysOfStay = DAYS.between(dateFrom, dateTo);
        daysOfStay = daysOfStay == 0 ? 1 : daysOfStay;
        double amount = hotel.getPrice() * daysOfStay;
        double interest = CardInterestEnum.returnInterestByCreditDues(paymentMethod.getDues());
        double total = amount + (amount / 100 * interest);

        // the status code will being managed by controller
        return new ReservationResponse(username, amount, interest, total, bookingDTO, null);
    }

    // check if any the String destination exists in any hotel
    private void validateDestinationInDatabase(String destination) throws ProvinceNotValidException
    {
        List<HotelDTO> hotelList = hotelRepository.getAll();
        long counter = hotelList.stream().filter(hotel -> hotel.getProvince().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }

    // same that validateDestinationInDatabase, but this will be used by getAll to avoid double calls to the repository
    private void validateDestinationInDatabaseAvoidCall(String destination, List<HotelDTO> hotelList)
            throws ProvinceNotValidException
    {
        long counter = hotelList.stream().filter(hotel -> hotel.getProvince().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }
}

package ar.com.manflack.desafiospring.domain.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.com.manflack.desafiospring.app.dto.CardDTO;
import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.app.enums.CardInterestEnum;
import ar.com.manflack.desafiospring.app.rest.response.HotelReservationResponse;
import ar.com.manflack.desafiospring.app.dto.BookingDTO;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelCodeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
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
            validateDestinationInDatabase(destination, hotelList);

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
    public HotelReservationResponse makeReservation(String username, BookingDTO booking)
            throws DateNotValidException, HotelNoRoomAvailableException, EmailNotValidException,
            InvalidCardDuesException, CardNotProvidedException, ProvinceNotValidException,
            HotelRoomTypeNotValidException, ReservationNotValidException, HotelCodeNotValidException
    {
        ValidatorUtils.validateEmail(username);

        // if the body has some content null, throw exception
        if (booking == null || booking.getPeople() == null)
            throw new ReservationNotValidException();

        if (booking.getPaymentMethod() == null)
            throw new CardNotProvidedException();

        CardDTO paymentMethod = booking.getPaymentMethod();

        // validate typeRoom given the name of the type and the amount of people
        ValidatorUtils.validateRoomType(booking.getRoomType(), booking.getPeopleAmount());
        // validate if dateFrom is before dateTo
        DateUtils.validateSinceAndUntil(booking.getDateFrom(), booking.getDateTo());
        // validate hotelCode provided
        ValidatorUtils.validateHotelCode(booking.getHotelCode());
        // validate number of dues given the card
        ValidatorUtils.validateCard(paymentMethod);
        // validate if the destination exists against database
        validateDestinationInDatabase(booking.getDestination());

        // wrapper the dates
        LocalDate dateFrom = DateUtils.getDateFromString(booking.getDateFrom());
        LocalDate dateTo = DateUtils.getDateFromString(booking.getDateTo());

        // get by filter, simulation of a Repository with JPA
        Optional<HotelDTO> optionalHotel =
                hotelRepository.findByCodeAndDestinationAndTypeAndBetweenDateFromAndDateToAndIsReserved(booking.getHotelCode(),
                        booking.getDestination(),
                        booking.getRoomType(),
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
        return new HotelReservationResponse(username, amount, interest, total, booking, null);
    }

    // check if any the String destination exists in any hotel
    private void validateDestinationInDatabase(String destination) throws ProvinceNotValidException
    {
        validateDestinationInDatabase(destination, hotelRepository.getAll());
    }

    // same that validateDestinationInDatabase, but this will be used by getAll to avoid double calls to the repository
    private void validateDestinationInDatabase(String destination, List<HotelDTO> hotelList)
            throws ProvinceNotValidException
    {
        long counter = hotelList.stream().filter(hotel -> hotel.getProvince().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }
}

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
import ar.com.manflack.desafiospring.domain.repository.HotelRepository;
import ar.com.manflack.desafiospring.domain.service.HotelService;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import ar.com.manflack.desafiospring.domain.util.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@PropertySource("classpath:/application.properties")
public class HotelServiceImpl implements HotelService
{
    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public List<HotelDTO> getAllHotels(String dateFrom, String dateTo, String destination)
            throws DateNotValidException, InvalidLocationException
    {
        List<HotelDTO> hotelList = hotelRepository.getAll();
        if (StringUtils.isNotBlank(destination))
            validateDestinationInDatabaseAvoidCall(destination, hotelList);
        Stream<HotelDTO> hotelListStream = hotelList.stream();

        if (StringUtils.isNotBlank(dateFrom) || StringUtils.isNotBlank(dateTo))
        {
            if (StringUtils.isBlank(dateFrom) || StringUtils.isBlank(dateTo))
                throw new DateNotValidException(
                        "You must provide dateFrom and dateTo with the format " + DateUtils.DATE_FORMAT);
            else
            {
                LocalDate since = DateUtils.getDateFromString(dateFrom);
                LocalDate until = DateUtils.getDateFromString(dateTo);

                DateUtils.validateSinceAndUntil(dateFrom, dateTo);
                hotelListStream = hotelListStream.filter(hotel ->
                {
                    LocalDate localDateFrom = hotel.getAvailableSince();
                    LocalDate localDateTo = hotel.getAvailableUntil();

                    return localDateFrom.isAfter(since) && localDateTo.isBefore(until);
                });
            }
        }

        if (StringUtils.isNotBlank(destination))
            hotelListStream = hotelListStream.filter(hotel -> StringUtils.equals(hotel.getProvince(), destination));

        if (StringUtils.isNotBlank(dateFrom) || StringUtils.isNotBlank(dateTo) || StringUtils.isNotBlank(destination))
            hotelList = hotelListStream.collect(Collectors.toList());

        return hotelList;
    }

    @Override
    public ReservationResponse makeReservation(String username, BookingDTO bookingDTO)
            throws DateNotValidException, NoRoomAvailableException, EmailNotValidException, InvalidCardDuesException,
            CardNotProvidedException, InvalidLocationException, ReservationNotValidException, RoomTypeNotValidException
    {
        ValidatorUtils.validateEmail(username);

        if (bookingDTO == null || bookingDTO.getPeople() == null)
            throw new ReservationNotValidException();

        if (bookingDTO.getPaymentMethod() == null)
            throw new CardNotProvidedException();

        ValidatorUtils.validateTypeRoom(bookingDTO.getRoomType(), bookingDTO.getPeopleAmount());
        DateUtils.validateSinceAndUntil(bookingDTO.getDateFrom(), bookingDTO.getDateTo());
        validateDestinationInDatabase(bookingDTO.getDestination());

        CardDTO paymentMethod = bookingDTO.getPaymentMethod();

        LocalDate dateFrom = DateUtils.getDateFromString(bookingDTO.getDateFrom());
        LocalDate dateTo = DateUtils.getDateFromString(bookingDTO.getDateTo());

        Optional<HotelDTO> optionalHotel =
                hotelRepository.findByCodeAndDestinationAndTypeAndBetweenDateFromAndDateToAndIsReserved(bookingDTO.getHotelCode(),
                        bookingDTO.getDestination(),
                        bookingDTO.getRoomType(),
                        dateFrom,
                        dateTo,
                        false);

        if (!optionalHotel.isPresent())
            throw new NoRoomAvailableException();

        HotelDTO hotel = optionalHotel.get();
        hotel.setIsReserved(true);
        hotel = hotelRepository.saveAndFlush(hotel);

        long daysOfStay = DAYS.between(dateFrom, dateTo);
        daysOfStay = daysOfStay == 0 ? 1 : daysOfStay;
        double amount = hotel.getPrice() * daysOfStay;
        double interest = CardInterestEnum.returnInterestByCreditDues(paymentMethod.getDues());
        double total = amount + (amount / 100 * interest);

        return new ReservationResponse(username, amount, interest, total, bookingDTO, null);
    }

    private void validateDestinationInDatabase(String destination) throws InvalidLocationException
    {
        List<HotelDTO> hotelList = hotelRepository.getAll();
        long counter = hotelList.stream().filter(hotel -> hotel.getProvince().equals(destination)).count();
        if (counter == 0)
            throw new InvalidLocationException();
    }

    private void validateDestinationInDatabaseAvoidCall(String destination, List<HotelDTO> hotelList)
            throws InvalidLocationException
    {
        long counter = hotelList.stream().filter(hotel -> hotel.getProvince().equals(destination)).count();
        if (counter == 0)
            throw new InvalidLocationException();
    }
}

package ar.com.manflack.desafiospring.domain.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.com.manflack.desafiospring.app.dto.CardDTO;
import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.app.dto.FlightReservationDTO;
import ar.com.manflack.desafiospring.app.enums.CardInterestEnum;
import ar.com.manflack.desafiospring.app.rest.response.FlightReservationResponse;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightNotAvailableException;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightSeatTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.HotelNoRoomAvailableException;
import ar.com.manflack.desafiospring.domain.exception.hotel.ReservationNotValidException;
import ar.com.manflack.desafiospring.domain.repository.FlightRepository;
import ar.com.manflack.desafiospring.domain.service.FlightService;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import ar.com.manflack.desafiospring.domain.util.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class FlightServiceImpl implements FlightService
{
    @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<FlightDTO> getAllFlights(String departureDate, String returnDate, String origin, String destination)
            throws ProvinceNotValidException, DateNotValidException
    {
        List<FlightDTO> flightsList = flightRepository.getAll();

        // validate province against database
        if (StringUtils.isNotBlank(origin))
            validateProvinceOrigin(origin, flightsList);

        if (StringUtils.isNotBlank(destination))
            validateProvinceDestination(destination, flightsList);

        // we'll prepare a stream of flightDTO to be filtered given the case of any params is provided
        Stream<FlightDTO> flightListFiltered = flightsList.stream();

        // check if some date filter was enabled
        if (StringUtils.isNotBlank(departureDate) || StringUtils.isNotBlank(returnDate))
        {
            // only when we have two date to filter, we can filter with that params, otherwise, throw exception
            if (StringUtils.isBlank(departureDate) || StringUtils.isBlank(returnDate))
                throw new DateNotValidException(
                        "You must provide dateFrom and dateTo with the format " + DateUtils.DATE_FORMAT);
            else
            {
                LocalDate since = DateUtils.getDateFromString(departureDate);
                LocalDate until = DateUtils.getDateFromString(returnDate);

                // validate if dateFrom is before to dateTo
                DateUtils.validateSinceAndUntil(departureDate, returnDate);
                // then filter by availability of the dates
                flightListFiltered =
                        flightListFiltered.filter(flight -> DateUtils.validateAvailabilityFlight(flight, since, until));
            }
        }

        // if the origin/destination param was provided, we filter by them
        if (StringUtils.isNotBlank(origin))
            flightListFiltered =
                    flightListFiltered.filter(flight -> StringUtils.equalsIgnoreCase(flight.getOrigin(), origin));

        if (StringUtils.isNotBlank(destination))
            flightListFiltered =
                    flightListFiltered.filter(flight -> StringUtils.equalsIgnoreCase(flight.getDestination(),
                            destination));

        // if any filter was provided, the flightsList will be replaced by flightListFiltered
        if (StringUtils.isNotBlank(departureDate) || StringUtils.isNotBlank(returnDate)
                || StringUtils.isNotBlank(origin) || StringUtils.isNotBlank(destination))
            flightsList = flightListFiltered.collect(Collectors.toList());

        return flightsList;
    }

    @Override
    public FlightReservationResponse makeFlightReservation(String username, FlightReservationDTO flightReservation)
            throws EmailNotValidException, ReservationNotValidException, CardNotProvidedException,
            FlightSeatTypeNotValidException, DateNotValidException, ProvinceNotValidException,
            FlightNotAvailableException, InvalidCardDuesException
    {
        ValidatorUtils.validateEmail(username);

        // if the body has some content null, throw exception
        if (flightReservation == null || flightReservation.getPeople() == null)
            throw new ReservationNotValidException();

        if (flightReservation.getPaymentMethod() == null)
            throw new CardNotProvidedException();

        CardDTO paymentMethod = flightReservation.getPaymentMethod();

        // validate if the seat type is business or economy
        ValidatorUtils.validateSeatType(flightReservation.getSeatType());
        // validate if dateFrom is before dateTo
        DateUtils.validateSinceAndUntil(flightReservation.getDateFrom(), flightReservation.getDateTo());
        // validate origin/destination exists against database
        validateProvinceOrigin(flightReservation.getOrigin());
        validateProvinceDestination(flightReservation.getDestination());
        // validate number of dues given the card
        ValidatorUtils.validateCard(paymentMethod);

        // wrapper the dates
        LocalDate dateFrom = DateUtils.getDateFromString(flightReservation.getDateFrom());
        LocalDate dateTo = DateUtils.getDateFromString(flightReservation.getDateTo());

        // get by filter, simulation of a Repository with JPA
        Optional<FlightDTO> optionalFlight =
                flightRepository.findByNumberAndOriginAndDestinationAndSeatTypeAndBetweenDepartureDateAndReturnDate(
                        flightReservation.getFlightNumber(),
                        flightReservation.getOrigin(),
                        flightReservation.getDestination(),
                        flightReservation.getSeatType(),
                        dateFrom,
                        dateTo);

        // if isn't present, throw exception because no hotel was found
        if (!optionalFlight.isPresent())
            throw new FlightNotAvailableException();

        FlightDTO flight = optionalFlight.get();

        // calculate days of stays, amount, interest and total(amount + interest)
        long daysOfStay = DAYS.between(dateFrom, dateTo);
        daysOfStay = daysOfStay == 0 ? 1 : daysOfStay;
        double amount = flight.getPrice() * daysOfStay;
        double interest = CardInterestEnum.returnInterestByCreditDues(paymentMethod.getDues());
        double total = amount + (amount / 100 * interest);

        return new FlightReservationResponse(username, amount, interest, total, flightReservation, null);
    }

    private void validateProvinceOrigin(String origin) throws ProvinceNotValidException
    {
        validateProvinceOrigin(origin, flightRepository.getAll());
    }

    private void validateProvinceDestination(String destination) throws ProvinceNotValidException
    {
        validateProvinceDestination(destination, flightRepository.getAll());
    }

    // check if any the String destination exists in flight origin, avoid double call to repository
    private void validateProvinceOrigin(String destination, List<FlightDTO> flightList) throws ProvinceNotValidException
    {
        long counter = flightList.stream().filter(flight -> flight.getOrigin().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }

    // same that validateDestinationInDatabase but destination instead origin, avoid double call to repository
    private void validateProvinceDestination(String destination, List<FlightDTO> flightList)
            throws ProvinceNotValidException
    {
        long counter = flightList.stream().filter(flight -> flight.getDestination().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }
}

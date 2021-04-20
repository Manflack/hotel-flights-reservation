package ar.com.manflack.desafiospring.domain.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import ar.com.manflack.desafiospring.domain.exception.ProvinceNotValidException;
import ar.com.manflack.desafiospring.domain.repository.FlightRepository;
import ar.com.manflack.desafiospring.domain.service.FlightService;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceImpl implements FlightService
{
    @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<FlightDTO> getAllFlights(String departureDate, String returnDate, String origin, String destination)
            throws ProvinceNotValidException, DateNotValidException
    {
        List<FlightDTO> flightList = flightRepository.getAll();

        // validate province against database
        if (StringUtils.isNotBlank(origin))
            validateProvinceOriginAvoidCall(origin, flightList);

        if (StringUtils.isNotBlank(destination))
            validateProvinceDestinationAvoidCall(destination, flightList);

        // we'll prepare a stream of flightDTO to be filtered given the case of any params is provided
        Stream<FlightDTO> flightListFiltered = flightList.stream();

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

        // if any filter was provided, the flightList will be replaced by flightListFiltered
        if (StringUtils.isNotBlank(departureDate) || StringUtils.isNotBlank(returnDate)
                || StringUtils.isNotBlank(origin) || StringUtils.isNotBlank(destination))
            flightList = flightListFiltered.collect(Collectors.toList());

        return flightList;
    }

    // check if any the String destination exists in flight origin, avoid double call to repository
    private void validateProvinceOriginAvoidCall(String destination, List<FlightDTO> flightList)
            throws ProvinceNotValidException
    {
        long counter = flightList.stream().filter(flight -> flight.getOrigin().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }

    // same that validateDestinationInDatabase but destination instead origin, avoid double call to repository
    private void validateProvinceDestinationAvoidCall(String destination, List<FlightDTO> flightList)
            throws ProvinceNotValidException
    {
        long counter = flightList.stream().filter(flight -> flight.getDestination().equals(destination)).count();
        if (counter == 0)
            throw new ProvinceNotValidException();
    }
}

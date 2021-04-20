package ar.com.manflack.desafiospring.domain.repository;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;

import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.domain.exception.InvalidDatabaseSpecification;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightDataErrorException;
import ar.com.manflack.desafiospring.domain.util.DateUtils;
import ar.com.manflack.desafiospring.domain.util.ValidatorUtils;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

@Repository
@PropertySource("classpath:/application.properties")
public class FlightRepository
{
    private static final List<FlightDTO> storageData = new ArrayList<>();

    @Value("${database.directory}")
    private String PATH_DATABASE;

    @Value("${database.flights.filename:dbFlights.csv}")
    private String FILENAME;

    @PostConstruct
    private void setup() throws Exception
    {
        if (StringUtils.isBlank(PATH_DATABASE) || StringUtils.isBlank(FILENAME))
            throw new InvalidDatabaseSpecification();

        List<String[]> localData = readDatabase();

        for (int i = 1; i < localData.size(); i++)
        {
            String[] data = localData.get(i);

            String number = data[0];
            String origin = data[1];
            String destination = data[2];
            String seatType = data[3];
            String price = data[4];
            String departureDate = data[5];
            String returnDate = data[6];

            DateUtils.validateSinceAndUntil(departureDate, returnDate);

            ValidatorUtils.validateFlightWithStringData(number,
                    origin,
                    destination,
                    seatType,
                    price,
                    departureDate,
                    returnDate);

            if (getByFlightNumberAndSeatType(number, seatType) != null)
                throw new FlightDataErrorException();

            storageData.add(new FlightDTO(number, origin, destination, seatType, price, departureDate, returnDate));
        }
    }

    public Optional<FlightDTO> findByNumberAndOriginAndDestinationAndSeatTypeAndBetweenDepartureDateAndReturnDate(
            String number, String origin, String destination, String seatType, LocalDate departureDate,
            LocalDate returnDate)
    {
        return storageData.stream()
                .filter(flight -> number.equalsIgnoreCase(flight.getNumber())
                        && origin.equalsIgnoreCase(flight.getOrigin())
                        && destination.equalsIgnoreCase(flight.getDestination())
                        && seatType.equalsIgnoreCase(flight.getSeatType()) && DateUtils.validateAvailabilityFlight(
                        flight,
                        departureDate,
                        returnDate))
                .findAny();
    }

    public FlightDTO getByFlightNumberAndSeatType(String flightNumber, String seatType)
    {
        for (FlightDTO flight : storageData)
        {
            if (StringUtils.equalsIgnoreCase(flight.getNumber(), flightNumber)
                    && StringUtils.equalsIgnoreCase(flight.getSeatType(), seatType))
                return flight;
        }
        return null;
    }

    public List<FlightDTO> getAll()
    {
        return storageData;
    }

    private List<String[]> readDatabase() throws Exception
    {
        File fileReader = ResourceUtils.getFile(PATH_DATABASE + FILENAME);
        CSVReader csvReader = new CSVReader(new FileReader(fileReader));
        List<String[]> list = csvReader.readAll();
        csvReader.close();
        return list;
    }
}

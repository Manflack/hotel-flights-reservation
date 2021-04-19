package ar.com.manflack.desafiospring.domain.repository;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.Destination;

import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.domain.exception.HotelDataErrorException;
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
public class HotelRepository
{
    private static final List<HotelDTO> storageData = new ArrayList<>();

    @Value("${database.directory:dbHotels.csv}")
    private String PATH_DATABASE;

    @PostConstruct
    private void setup() throws Exception
    {
        List<String[]> localData = readDatabase();

        for (int i = 1; i < localData.size(); i++)
        {
            String[] data = localData.get(i);

            String code = data[0];
            String name = data[1];
            String province = data[2];
            String type = data[3];
            String price = data[4];
            String availableSince = data[5];
            String availableUntil = data[6];
            String isReserved = data[7];

            DateUtils.validateSinceAndUntil(availableSince, availableUntil);

            ValidatorUtils.validateHotelWithStringData(code,
                    name,
                    province,
                    type,
                    price,
                    availableSince,
                    availableUntil,
                    isReserved);

            if (getByCode(code) != null)
                throw new HotelDataErrorException();

            storageData.add(new HotelDTO(code,
                    name,
                    province,
                    type,
                    price,
                    availableSince,
                    availableUntil,
                    isReserved));
        }
    }

    public Optional<HotelDTO> findByCodeAndDestinationAndTypeAndBetweenDateFromAndDateToAndIsReserved(String code,
            String destination, String type, LocalDate dateFrom, LocalDate dateTo, Boolean isReserved)
    {
        return storageData.stream()
                .filter(hotel -> code.equals(hotel.getCode())
                        && destination.equals(hotel.getProvince())
                        && StringUtils.equalsIgnoreCase(type.toLowerCase(), hotel.getType().toLowerCase())
                        && dateFrom.isAfter(hotel.getAvailableSince().minusDays(1))
                        && dateTo.isBefore(hotel.getAvailableUntil().plusDays(1))
                        && isReserved.equals(hotel.getIsReserved())
                )
                .findAny();
    }

    public HotelDTO saveAndFlush(HotelDTO hotel)
    {
        storageData.removeIf(persistentHotel -> persistentHotel.getCode().equals(hotel.getCode()));
        storageData.add(hotel);
        return hotel;
    }

    public List<HotelDTO> getAll()
    {
        return storageData;
    }

    public HotelDTO getByCode(String code)
    {
        for (HotelDTO hotel : storageData)
        {
            if (hotel.getCode().equals(code))
                return hotel;
        }
        return null;
    }

    private List<String[]> readDatabase() throws Exception
    {
        File fileReader = ResourceUtils.getFile(PATH_DATABASE);
        CSVReader csvReader = new CSVReader(new FileReader(fileReader));
        List<String[]> list = csvReader.readAll();
        csvReader.close();
        return list;
    }
}

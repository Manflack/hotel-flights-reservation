package ar.com.manflack.desafiospring.domain.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.annotation.PostConstruct;

import ar.com.manflack.desafiospring.app.dto.FlightDTO;
import ar.com.manflack.desafiospring.app.dto.HotelDTO;
import ar.com.manflack.desafiospring.domain.exception.DateNotValidException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
public class DateUtils
{
    public static String DATE_FORMAT = "dd/MM/yyyy";

    public static void validateDate(String date) throws DateNotValidException
    {
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try
        {
            sdf.parse(date);
        } catch (ParseException e)
        {
            throw new DateNotValidException();
        }
    }

    public static LocalDate getDateFromString(String date) throws DateNotValidException
    {
        DateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try
        {
            return LocalDate.ofEpochDay(sdf.parse(date).toInstant().getEpochSecond() / 86400);
        } catch (ParseException e)
        {
            throw new DateNotValidException();
        }
    }

    public static void validateSinceAndUntil(String sinceStr, String untilStr) throws DateNotValidException
    {
        LocalDate since = getDateFromString(sinceStr);
        LocalDate until = getDateFromString(untilStr);
        if (since.isAfter(until))
            throw new DateNotValidException("The date of from must be minor that date to");
    }

    public static boolean validateAvailabilityHotel(HotelDTO hotel, LocalDate since, LocalDate until)
    {
        LocalDate localDateFrom = hotel.getAvailableSince();
        LocalDate localDateTo = hotel.getAvailableUntil();
        return localDateFrom.isBefore(since.plusDays(1)) && localDateTo.isAfter(until.minusDays(1));
    }

    public static boolean validateAvailabilityFlight(FlightDTO flight, LocalDate since, LocalDate until)
    {
        LocalDate localDateFrom = flight.getDepartureDate();
        LocalDate localDateTo = flight.getReturnDate();
        return localDateFrom.isEqual(since) && localDateTo.isEqual(until);
    }
}

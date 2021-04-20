package ar.com.manflack.desafiospring.domain.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ar.com.manflack.desafiospring.app.enums.RoomTypesEnum;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightNumberNotValidException;
import ar.com.manflack.desafiospring.domain.exception.flight.FlightSeatTypeNotValidException;
import ar.com.manflack.desafiospring.domain.exception.hotel.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ValidatorUtils
{
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static void validateHotelWithStringData(String code, String name, String province, String type, String price,
            String availableSince, String availableUntil, String isReserved)
            throws HotelCodeNotValidException, HotelNameNotValidException, ProvinceNotValidException,
            HotelTypeNotValidException, HotelPriceNotValidException, DateNotValidException,
            ReservationNotValidException
    {
        validateCode(code);
        validateName(name);
        validateProvince(province);
        validateType(type);
        validatePrice(price);
        DateUtils.validateDate(availableSince);
        DateUtils.validateDate(availableUntil);
        validateIsReserved(isReserved);
    }

    public static void validateFlightWithStringData(String number, String origin, String destination, String seatType,
            String price, String departureDate, String returnDate)
            throws FlightNumberNotValidException, ProvinceNotValidException, FlightSeatTypeNotValidException
    {
        validateNumber(number);
        validateProvince(origin);
        validateProvince(destination);
        validateSeatType(seatType);
    }

    private static void validateCode(String code) throws HotelCodeNotValidException
    {
        if (StringUtils.isBlank(code) || code.split("-").length == 1 || code.split("-")[0].length() != 2
                || code.split("-")[1].length() != 4)
            throw new HotelCodeNotValidException();

    }

    private static void validateName(String name) throws HotelNameNotValidException
    {
        if (StringUtils.isBlank(name))
            throw new HotelNameNotValidException();
    }

    private static void validateProvince(String province) throws ProvinceNotValidException
    {
        if (StringUtils.isBlank(province))
            throw new ProvinceNotValidException();
    }

    private static void validateType(String type) throws HotelTypeNotValidException
    {
        if (StringUtils.isBlank(type) || !StringUtils.isAlpha(type) || !StringUtils.equalsAny(type.toLowerCase(),
                "single",
                "doble",
                "triple",
                "múltiple"))
            throw new HotelTypeNotValidException();
    }

    private static void validatePrice(String price) throws HotelPriceNotValidException
    {
        if (StringUtils.isBlank(price) || !StringUtils.contains(price, "$") || price.split("\\$").length != 2)
            throw new HotelPriceNotValidException();

    }

    private static void validateIsReserved(String isReserved) throws ReservationNotValidException
    {
        if (StringUtils.isBlank(isReserved) || !StringUtils.equalsAny(isReserved.toLowerCase(), "no", "si"))
            throw new ReservationNotValidException();
    }

    public static void validateEmail(String emailStr) throws EmailNotValidException
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        if (!matcher.find())
            throw new EmailNotValidException();
    }

    public static void validateRoomType(String type, Integer amountOfPeople) throws HotelRoomTypeNotValidException
    {
        RoomTypesEnum roomEnum = RoomTypesEnum.returnRoomTypeGivenAmountPeople(amountOfPeople);

        if (roomEnum == null || !roomEnum.getType().equalsIgnoreCase(type))
            throw new HotelRoomTypeNotValidException();

    }

    private static void validateNumber(String number) throws FlightNumberNotValidException
    {
        if (StringUtils.isBlank(number) || number.split("-").length == 1 || number.split("-")[0].length() != 4
                || number.split("-")[1].length() != 4)
            throw new FlightNumberNotValidException();

    }

    public static void validateSeatType(String seatType) throws FlightSeatTypeNotValidException
    {
        if (StringUtils.isBlank(seatType) || !StringUtils.isAlpha(seatType)
                || !StringUtils.equalsAnyIgnoreCase(seatType, "economy", "business"))
            throw new FlightSeatTypeNotValidException();
    }
}

package ar.com.manflack.desafiospring.app.handler;

import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import ar.com.manflack.desafiospring.domain.exception.*;
import ar.com.manflack.desafiospring.domain.exception.hotel.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler
{
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handlerException(Exception ex)
    {
        StatusDTO response = new StatusDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());

        if (ex instanceof HotelNoRoomAvailableException || ex instanceof DateNotValidException
                || ex instanceof ReservationNotValidException || ex instanceof HotelTypeNotValidException
                || ex instanceof EmailNotValidException || ex instanceof CardNotProvidedException
                || ex instanceof InvalidCardDuesException || ex instanceof HotelCodeNotValidException
                || ex instanceof HotelDataErrorException || ex instanceof HotelNameNotValidException
                || ex instanceof HotelPriceNotValidException || ex instanceof ProvinceNotValidException
                || ex instanceof HotelRoomTypeNotValidException)
        {
            response.setCode(HttpStatus.BAD_REQUEST.value());
        }

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}

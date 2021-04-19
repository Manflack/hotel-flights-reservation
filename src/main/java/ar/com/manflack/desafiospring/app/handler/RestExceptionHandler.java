package ar.com.manflack.desafiospring.app.handler;

import ar.com.manflack.desafiospring.app.dto.StatusDTO;
import ar.com.manflack.desafiospring.domain.exception.NoRoomAvailableException;
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

        if (ex instanceof NoRoomAvailableException)
        {
            response.setCode(HttpStatus.BAD_REQUEST.value());
        }

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }
}

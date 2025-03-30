package school.faang.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.MessageDto;
import school.faang.user_service.exception.DataValidationException;

@RestController
public class ExceptionsController {
    @ExceptionHandler(DataValidationException.class)
    public ResponseEntity<?> handleDataValidationException(Exception e){
        return new ResponseEntity<>(new MessageDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(), false), HttpStatus.BAD_REQUEST);
    }
}

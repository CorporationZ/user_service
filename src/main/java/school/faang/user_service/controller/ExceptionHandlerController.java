package school.faang.user_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(500).body("An error occurred: " + ex.getMessage());
    }

}

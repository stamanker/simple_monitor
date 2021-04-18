package maxz.monitor.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> exception(Throwable ex) {
        log.error("Error: {}", ex.getMessage(), ex);
        ex.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

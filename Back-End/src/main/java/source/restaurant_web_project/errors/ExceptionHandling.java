package source.restaurant_web_project.errors;

import org.modelmapper.Conditions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling{

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> notFoundException(BadRequestException exc){
        return ResponseEntity.status(404).body(exc.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> conflictException(ConflictException exc){
        return ResponseEntity.status(409).body(exc.getMessage());
    }

    @ExceptionHandler(ExpectedException.class)
    public ResponseEntity<?> expectedException(ExpectedException exc){

        return ResponseEntity.status(417).body(exc.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundException(NotFoundException exc){
        return ResponseEntity.status(404).body(exc.getMessage());
    }
}

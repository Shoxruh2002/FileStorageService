package uz.sh;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid( MethodArgumentNotValidException ex,
                                                                                            HttpHeaders headers,
                                                                                            HttpStatus status,
                                                                                            WebRequest request ) {
        StringBuilder message = new StringBuilder();
        for ( FieldError error : ex.getBindingResult().getFieldErrors() ) {
            message.append("Cause : ").append(error.getDefaultMessage()).append("\n");
        }

        AppErrorDto appErrorDto = new AppErrorDto(HttpStatus.BAD_REQUEST, message.toString());
        return new org.springframework.http.ResponseEntity<>(new DataDTO<>(appErrorDto), headers, status);
    }


    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<DataDTO<AppErrorDto>> handle400( BadRequestException e, WebRequest webRequest ) {
        return new ResponseEntity<>
                (new DataDTO<>(new AppErrorDto(HttpStatus.BAD_REQUEST, e.getMessage(), webRequest)), HttpStatus.OK);
    }


    @ExceptionHandler(value = {FileIOException.class})
    public ResponseEntity<DataDTO<AppErrorDto>> handle500( FileIOException e, WebRequest webRequest ) {
        return new ResponseEntity<>
                (new DataDTO<>(new AppErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), webRequest)), HttpStatus.OK);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<DataDTO<AppErrorDto>> handle404( NotFoundException e, WebRequest webRequest ) {
        return new ResponseEntity<>
                (new DataDTO<>(new AppErrorDto(HttpStatus.NOT_FOUND, e.getMessage(), webRequest)), HttpStatus.OK);
    }

}

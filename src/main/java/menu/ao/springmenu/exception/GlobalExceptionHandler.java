package menu.ao.springmenu.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PersistenceException.class)
    private ResponseEntity<ResponseBodyException>  handlerPersistenceException(PersistenceException pe, HttpServletRequest request)
    {

      return  response(pe.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request, Instant.now());
    }

    @ExceptionHandler(NotFoundException.class)
    private ResponseEntity<ResponseBodyException>  handlerNotFoundException(NotFoundException nf, HttpServletRequest request)
    {

        return  response(nf.getMessage(), HttpStatus.NOT_FOUND, request, Instant.now());
    }

    @ExceptionHandler(UserIsPresentException.class)
    private ResponseEntity<ResponseBodyException>  handlerUserIsPresentException(UserIsPresentException up, HttpServletRequest request)
    {

        return  response(up.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request, Instant.now());
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<ResponseBodyException>  handlerLoginPersistenceException(BadCredentialsException lp, HttpServletRequest request)
    {

        return  response(lp.getMessage(), HttpStatus.BAD_REQUEST, request, Instant.now());
    }


   protected ResponseEntity<ResponseBodyException>  response(final String message, final HttpStatus status, final HttpServletRequest request, Instant data)
   {

       return ResponseEntity
               .status(status)
               .body(new ResponseBodyException(data, status.value(),message,request.getRequestURI()));
   }
}

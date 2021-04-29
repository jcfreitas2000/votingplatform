package br.tec.josecarlos.votingplatform.config.errorhandler.advicecontroller;

import br.tec.josecarlos.votingplatform.config.errorhandler.ExceptionHandlerD;
import br.tec.josecarlos.votingplatform.config.errorhandler.ExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HttpMessageConversionExceptionHandlerController {

    @Autowired
    private ExceptionHandlerD exceptionHandlerD;

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ExceptionResponse> handle(HttpMessageConversionException exception) {
        return exceptionHandlerD
                .handle(HttpStatus.BAD_REQUEST, exception.getMessage());
    }
}

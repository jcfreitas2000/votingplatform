package br.tec.josecarlos.votingplatform.config.errorhandler.advicecontroller;

import br.tec.josecarlos.votingplatform.config.errorhandler.ExceptionHandlerD;
import br.tec.josecarlos.votingplatform.config.errorhandler.ExceptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private ExceptionHandlerD exceptionHandlerD;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception exception) {
        exception.printStackTrace();
        return exceptionHandlerD
                .handle(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown server error. Please, try again later.");
    }
}

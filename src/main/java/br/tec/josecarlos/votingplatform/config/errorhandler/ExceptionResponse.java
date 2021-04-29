package br.tec.josecarlos.votingplatform.config.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponse {

    private final int statusCode;
    private final String message;
}

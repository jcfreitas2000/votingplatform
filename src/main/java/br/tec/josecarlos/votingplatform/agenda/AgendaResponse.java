package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Getter
@Setter
public class AgendaResponse {

    private UUID id;
    private String name;

    public AgendaResponse(Agenda agenda) {
        new ModelMapper().map(agenda, this);
    }
}

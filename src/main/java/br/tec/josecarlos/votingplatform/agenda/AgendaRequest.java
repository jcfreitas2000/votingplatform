package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class AgendaRequest {

    @NotEmpty
    private String name;

    public Agenda map() {
        Agenda agenda = new Agenda();
        new ModelMapper().map(this, agenda);
        return agenda;
    }
}

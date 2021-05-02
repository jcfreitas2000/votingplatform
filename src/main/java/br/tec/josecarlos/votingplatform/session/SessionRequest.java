package br.tec.josecarlos.votingplatform.session;

import br.tec.josecarlos.votingplatform.models.Session;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class SessionRequest {

    @Future
    private LocalDateTime deadline;
    @NotNull
    @Valid
    private AgendaRequest agenda;

    public Session map() {
        Session session = new Session();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        modelMapper.map(this, session);

        return session;
    }

    @Getter
    @Setter
    public static class AgendaRequest {

        @NotNull
        private UUID id;
    }
}

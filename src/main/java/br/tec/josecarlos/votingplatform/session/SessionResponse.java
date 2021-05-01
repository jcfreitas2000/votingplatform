package br.tec.josecarlos.votingplatform.session;

import br.tec.josecarlos.votingplatform.models.Session;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SessionResponse {

    private UUID id;
    private LocalDateTime deadline;

    public SessionResponse(Session session) {
        new ModelMapper().map(session, this);
    }
}

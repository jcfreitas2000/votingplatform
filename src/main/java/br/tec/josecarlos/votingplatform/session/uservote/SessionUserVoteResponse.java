package br.tec.josecarlos.votingplatform.session.uservote;

import br.tec.josecarlos.votingplatform.models.SessionUserVote;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class SessionUserVoteResponse {

    private String userCpf;
    private Boolean agreed;

    public SessionUserVoteResponse(SessionUserVote sessionUserVote) {
        new ModelMapper().map(sessionUserVote, this);
    }
}

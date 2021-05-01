package br.tec.josecarlos.votingplatform.session.uservote;

import br.tec.josecarlos.votingplatform.models.SessionUserVote;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class SessionUserVoteRequest {

    @NotNull
    private String userCpf;
    @NotNull
    private Boolean agreed;

    public SessionUserVote map() {
        SessionUserVote sessionUserVote = new SessionUserVote();
        new ModelMapper().map(this, sessionUserVote);

        return sessionUserVote;
    }
}

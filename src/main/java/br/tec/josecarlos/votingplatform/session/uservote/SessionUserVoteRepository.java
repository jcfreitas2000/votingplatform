package br.tec.josecarlos.votingplatform.session.uservote;

import br.tec.josecarlos.votingplatform.models.Session;
import br.tec.josecarlos.votingplatform.models.SessionUserVote;
import br.tec.josecarlos.votingplatform.models.SessionUserVotePK;
import org.springframework.data.repository.CrudRepository;

public interface SessionUserVoteRepository extends CrudRepository<SessionUserVote, SessionUserVotePK> {

    long countBySessionAndAgreed(Session session, Boolean agreed);
}

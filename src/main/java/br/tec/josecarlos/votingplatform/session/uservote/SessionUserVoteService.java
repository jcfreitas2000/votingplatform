package br.tec.josecarlos.votingplatform.session.uservote;

import br.tec.josecarlos.votingplatform.models.Session;
import br.tec.josecarlos.votingplatform.models.SessionUserVote;
import br.tec.josecarlos.votingplatform.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class SessionUserVoteService {

    @Autowired
    private SessionUserVoteRepository sessionUserVoteRepository;

    @Autowired
    private SessionService sessionService;

    public SessionUserVoteReport count(UUID sessionId) {
        Session session = sessionService.find(sessionId);
        return new SessionUserVoteReport(
                sessionUserVoteRepository.countBySessionAndAgreed(session, true),
                sessionUserVoteRepository.countBySessionAndAgreed(session, false)
        );
    }

    public SessionUserVote create(UUID sessionId, SessionUserVote sessionUserVote) {
        sessionUserVote.setSession(sessionService.find(sessionId));
        if (sessionUserVote.getSession().getDeadline().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This voting session has already expired.");
        }

        try {
            sessionUserVoteRepository.save(sessionUserVote);

            log.info("New vote for Session {} by {}", sessionUserVote.getSession().getId(), sessionUserVote.getUserCpf());

            return sessionUserVote;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Each CPF can only vote once in each session");
        }
    }
}

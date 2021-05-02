package br.tec.josecarlos.votingplatform.session;

import br.tec.josecarlos.votingplatform.agenda.AgendaService;
import br.tec.josecarlos.votingplatform.models.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AgendaService agendaService;

    public Session find(UUID id) {
        return sessionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Session> list() {
        return (List<Session>) sessionRepository.findAll();
    }

    public Session create(Session session) {
        session.setAgenda(agendaService.find(session.getAgenda().getId()));
        sessionRepository.save(session);

        log.info("Successfully created a new session: {}", session);

        return session;
    }
}

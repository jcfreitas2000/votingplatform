package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    public Agenda find(UUID id) {
        return agendaRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Agenda %s not found.", id.toString())
                ));
    }

    public List<Agenda> list() {
        return (List<Agenda>) agendaRepository.findAll();
    }

    public Agenda create(Agenda agenda) {
        agendaRepository.save(agenda);

        log.info("Successfully created a new agenda: {}", agenda);

        return agenda;
    }
}

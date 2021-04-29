package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AgendaRepository extends CrudRepository<Agenda, UUID> {
}

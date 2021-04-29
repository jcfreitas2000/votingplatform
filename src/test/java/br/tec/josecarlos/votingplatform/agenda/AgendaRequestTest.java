package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgendaRequestTest {

    @Test
    public void testMapToAgenda() {
        AgendaRequest agendaRequest = new AgendaRequest();
        agendaRequest.setName("Test Agenda");

        Agenda agenda = agendaRequest.map();

        assertEquals(agendaRequest.getName(), agenda.getName());
    }
}

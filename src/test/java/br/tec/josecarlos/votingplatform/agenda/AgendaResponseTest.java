package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgendaResponseTest {

    @Test
    public void testMapFromAgenda() {
        Agenda agenda = new Agenda();
        agenda.setName("Test Agenda");

        AgendaResponse agendaResponse = new AgendaResponse(agenda);

        assertEquals(agenda.getName(), agendaResponse.getName());
    }
}

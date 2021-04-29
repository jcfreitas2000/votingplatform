package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AgendaService.class)
class AgendaServiceTest {

    @MockBean
    private AgendaRepository agendaRepository;

    @Autowired
    private AgendaService agendaService;

    private Agenda genericAgenda;

    @BeforeEach
    public void setUp() {
        genericAgenda = new Agenda();
        genericAgenda.setName("Generic Agenda");
    }

    @Test
    public void testFind_whenExistingId_thanReturnAgenda() {
        when(agendaRepository.findById(any())).thenReturn(Optional.of(genericAgenda));

        Agenda agenda = agendaService.find(genericAgenda.getId());

        assertEquals(genericAgenda, agenda);
    }

    @Test
    public void testFind_whenNotFoundId_thanThrowsResponseStatusException() {
        when(agendaRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> agendaService.find(genericAgenda.getId()));
    }

    @Test
    public void testList_whenHasNoData_thanReturnEmptyList() {
        when(agendaRepository.findAll()).thenReturn(Collections.emptyList());

        List<Agenda> agendaList = agendaService.list();

        assertTrue(agendaList.isEmpty());
    }


    @Test
    public void testList_whenHasData_thanReturnListOfAll() {
        List<Agenda> listOfThreeGenericAgendas = List.of(genericAgenda, genericAgenda, genericAgenda);
        when(agendaRepository.findAll()).thenReturn(listOfThreeGenericAgendas);

        List<Agenda> agendaList = agendaService.list();

        assertEquals(listOfThreeGenericAgendas.size(), agendaList.size());
    }

    @Test
    public void testCreate_whenSuccessfullyCreates_thanReturnNewAgenda() {
        when(agendaRepository.save(any())).thenReturn(genericAgenda);

        Agenda agenda = agendaService.create(genericAgenda);

        assertEquals(genericAgenda, agenda);
    }
}

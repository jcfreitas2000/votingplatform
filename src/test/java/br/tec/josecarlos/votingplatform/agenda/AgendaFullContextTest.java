package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AgendaFullContextTest {

    private static final String URI = "/agendas";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgendaRepository agendaRepository;

    private Agenda genericAgenda;

    @BeforeEach
    public void setUp() {
        genericAgenda = new Agenda();
        genericAgenda.setName("Generic Agenda");
    }

    @AfterEach
    public void clean() {
        agendaRepository.deleteAll();
    }

    @Test
    public void testFind_whenExistingId_thanReturnAgenda() throws Exception {
        agendaRepository.save(genericAgenda);

        new AgendaMockMvcD(mockMvc, URI, genericAgenda)
                .testFind_whenExistingId_thanReturnAgenda_mockMvc();
    }

    @Test
    public void testFind_whenDoesNotExistsId_thanReturn404() throws Exception {
        new AgendaMockMvcD(mockMvc, URI, genericAgenda)
                .testFind_whenDoesNotExistsId_thanReturn404_mockMvc();
    }

    @Test
    public void testList_whenThereAreAgendas_thanReturnListOfAll() throws Exception {
        final Agenda anotherGenericAgenda = new Agenda();
        anotherGenericAgenda.setName("Another Generic Agenda");
        agendaRepository.save(genericAgenda);
        agendaRepository.save(anotherGenericAgenda);


        new AgendaMockMvcD(mockMvc, URI, genericAgenda)
                .testList_whenThereAreAgendas_thanReturnListOfAll_mockMvc();
    }

    @Test
    public void testList_whenThereAreNotAgendas_thanReturnEmpty() throws Exception {
        new AgendaMockMvcD(mockMvc, URI, genericAgenda)
                .testList_whenThereAreNotAgendas_thanReturnEmpty_mockMvc();
    }

    @Test
    public void testCreate_whenValidAgenda_thanCreates() throws Exception {
        new AgendaMockMvcD(mockMvc, URI, genericAgenda)
                .testCreate_whenValidAgenda_thanCreates_mockMvc();
    }

    @Test
    public void testCreate_whenInvalidAgendaName_thanBadRequest() throws Exception {
        genericAgenda.setName("");

        new AgendaMockMvcD(mockMvc, URI, genericAgenda)
                .testCreate_whenInvalidAgendaName_thanBadRequest_mockMvc();
    }
}

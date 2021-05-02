package br.tec.josecarlos.votingplatform.session;

import br.tec.josecarlos.votingplatform.agenda.AgendaRepository;
import br.tec.josecarlos.votingplatform.models.Agenda;
import br.tec.josecarlos.votingplatform.models.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SessionFullContextTest {

    private static final String URI = "/sessions";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private AgendaRepository agendaRepository;

    private Session genericSession;
    private Agenda genericAgenda;

    @BeforeEach
    public void setUp() {
        genericAgenda = new Agenda();
        genericAgenda.setName("Generic Agenda");

        genericSession = new Session();
        genericSession.setAgenda(genericAgenda);
    }

    @AfterEach
    public void clean() {
        sessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    public void testFind_whenExistingId_thanReturnSession() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);

        mockMvc
                .perform(
                        get(String.format("%s/%s", URI, genericSession.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(genericSession.getId().toString())))
                .andExpect(jsonPath("deadline", startsWith(genericSession.getDeadline().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    public void testFind_whenDoesNotExistsId_thanReturn404() throws Exception {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        mockMvc
                .perform(
                        get(String.format("%s/%s", URI, genericSession.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(notFound.value()))
                .andExpect(jsonPath("statusCode", is(notFound.value())));
    }

    @Test
    public void testList_whenThereAreSessions_thanReturnListOfAll() throws Exception {
        agendaRepository.save(genericAgenda);

        final Session anotherGenericSession = new Session();
        anotherGenericSession.setAgenda(genericAgenda);
        sessionRepository.save(genericSession);
        sessionRepository.save(anotherGenericSession);

        mockMvc
                .perform(get(URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(genericSession.getId().toString())))
                .andExpect(jsonPath("$[0].deadline", startsWith(genericSession.getDeadline().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    public void testList_whenThereAreNotSessions_thanReturnEmpty() throws Exception {
        mockMvc
                .perform(get(URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testCreate_whenEmptyDeadline_thanCreatesDefaultDeadline() throws Exception {
        agendaRepository.save(genericAgenda);
        genericSession.setDeadline(null);

        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(genericSession))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        Matchers.containsString(String.format("%s/", URI))
                ))
                // TODO improve this test to compare if both are close dates, and not exactly the same
                .andExpect(jsonPath("deadline", startsWith(new Session().getDeadline().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    public void testCreate_whenValidDeadline_thanCreates() throws Exception {
        agendaRepository.save(genericAgenda);
        genericSession.setDeadline(LocalDateTime.now().plus(10, ChronoUnit.MINUTES));

        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(genericSession))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        Matchers.containsString(String.format("%s/", URI))
                ))
                .andExpect(jsonPath("deadline", startsWith(genericSession.getDeadline().truncatedTo(ChronoUnit.SECONDS).toString())));
    }

    @Test
    public void testCreate_whenSessionDeadlineInPast_thanBadRequest() throws Exception {
        genericSession.setDeadline(LocalDateTime.now().minus(10, ChronoUnit.MINUTES));

        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(genericSession))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode", is(HttpStatus.BAD_REQUEST.value())));
    }
}

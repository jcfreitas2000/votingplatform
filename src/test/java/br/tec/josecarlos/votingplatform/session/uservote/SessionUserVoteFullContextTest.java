package br.tec.josecarlos.votingplatform.session.uservote;

import br.tec.josecarlos.votingplatform.agenda.AgendaRepository;
import br.tec.josecarlos.votingplatform.models.Agenda;
import br.tec.josecarlos.votingplatform.models.Session;
import br.tec.josecarlos.votingplatform.models.SessionUserVote;
import br.tec.josecarlos.votingplatform.session.SessionRepository;
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
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SessionUserVoteFullContextTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private SessionUserVoteRepository sessionUserVoteRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private AgendaRepository agendaRepository;

    private Session genericSession;
    private Agenda genericAgenda;
    private SessionUserVote genericSessionUserVote;

    @BeforeEach
    public void setUp() {
        genericAgenda = new Agenda();
        genericAgenda.setName("Generic Agenda");

        genericSession = new Session();
        genericSession.setAgenda(genericAgenda);

        genericSessionUserVote = new SessionUserVote();
        genericSessionUserVote.setSession(genericSession);
        genericSessionUserVote.setAgreed(true);
        genericSessionUserVote.setUserCpf("69651093005");
    }

    @AfterEach
    public void cleanUp() {
        sessionUserVoteRepository.deleteAll();
        sessionRepository.deleteAll();
        agendaRepository.deleteAll();
    }

    @Test
    public void testCountBySession_whenEmptyVotes_thanReturn0() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);

        mockMvc
                .perform(
                        get(String.format("%s", getURI(genericSession.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("countAgreed", is(0)))
                .andExpect(jsonPath("countDisagreed", is(0)));
    }

    @Test
    public void testCountBySession_whenThereAreVotes_thanReturnCorrectAgreedAndDisagreedCount() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);
        sessionUserVoteRepository.save(genericSessionUserVote);
        sessionUserVoteRepository.save(createSessionUserVote("30467886067", false));
        sessionUserVoteRepository.save(createSessionUserVote("04574227001", false));

        mockMvc
                .perform(
                        get(String.format("%s", getURI(genericSession.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("countAgreed", is(1)))
                .andExpect(jsonPath("countDisagreed", is(2)));
    }

    @Test
    public void testCountBySession_whenThereAreVotesInDifferentSessions_thanReportOnlyReturnCountOfTheSearchedSession() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);

        Session anotherGenericSession = new Session();
        anotherGenericSession.setAgenda(genericAgenda);
        anotherGenericSession.setDeadline(genericSession.getDeadline());
        sessionRepository.save(anotherGenericSession);

        sessionUserVoteRepository.save(genericSessionUserVote);
        sessionUserVoteRepository.save(createSessionUserVote("30467886067", false));
        sessionUserVoteRepository.save(createSessionUserVote("04574227001", false));

        genericSessionUserVote.setSession(anotherGenericSession);
        sessionUserVoteRepository.save(genericSessionUserVote);

        mockMvc
                .perform(
                        get(String.format("%s", getURI(anotherGenericSession.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("countAgreed", is(1)))
                .andExpect(jsonPath("countDisagreed", is(0)));
    }

    @Test
    public void testCountBySession_whenDoesNotExistsSessionId_thanReturn404() throws Exception {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        mockMvc
                .perform(
                        get(String.format("%s", getURI(genericSession.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(notFound.value()))
                .andExpect(jsonPath("statusCode", is(notFound.value())));
    }


    @Test
    public void testCreate_whenValidRequestBody_thanCreates() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);

        mockMvc
                .perform(post(getURI(genericSession.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(genericSessionUserVote))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        Matchers.containsString(String.format("%s", getURI(genericSession.getId())))
                ))
                .andExpect(jsonPath("userCpf", is(genericSessionUserVote.getUserCpf())));
    }

    @Test
    public void testCreate_whenInvalidRequestBody_thanBadRequest() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);

        mockMvc
                .perform(post(getURI(genericSession.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode", is(HttpStatus.BAD_REQUEST.value())));
    }

    @Test
    public void testCreate_whenDuplicatedVoteByUser_thanReturnConflict() throws Exception {
        agendaRepository.save(genericAgenda);
        sessionRepository.save(genericSession);
        sessionUserVoteRepository.save(genericSessionUserVote);

        mockMvc
                .perform(post(getURI(genericSession.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(genericSessionUserVote))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("statusCode", is(HttpStatus.CONFLICT.value())));
    }

    @Test
    public void testCreate_whenSessionDeadlineExpired_thanReturnBadRequest() throws Exception {
        agendaRepository.save(genericAgenda);
        genericSession.setDeadline(LocalDateTime.now().minus(1, ChronoUnit.SECONDS));
        sessionRepository.save(genericSession);

        mockMvc
                .perform(post(getURI(genericSession.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jacksonObjectMapper.writeValueAsString(genericSessionUserVote))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode", is(HttpStatus.BAD_REQUEST.value())));
    }

    private String getURI(UUID sessionId) {
        return String.format("/sessions/%s/votes", sessionId);
    }

    private SessionUserVote createSessionUserVote(String userCpf, Boolean agreed) {
        SessionUserVote sessionUserVote = new SessionUserVote();
        sessionUserVote.setSession(genericSession);
        sessionUserVote.setUserCpf(userCpf);
        sessionUserVote.setAgreed(agreed);
        return sessionUserVote;
    }
}

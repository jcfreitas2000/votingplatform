package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.config.errorhandler.ExceptionHandlerD;
import br.tec.josecarlos.votingplatform.models.Agenda;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({AgendaController.class, ExceptionHandlerD.class})
class AgendaControllerTest {

    private static final String URI = "/agendas";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgendaService agendaService;

    private Agenda genericAgenda;

    @BeforeEach
    public void setUp() {
        genericAgenda = new Agenda();
        genericAgenda.setName("Generic Agenda");
    }

    @Test
    public void testFind_whenExistingId_thanReturnAgenda() throws Exception {
        when(agendaService.find(any())).thenReturn(genericAgenda);

        mockMvc
                .perform(
                        get(String.format("%s/%s", URI, genericAgenda.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(genericAgenda.getId().toString())))
                .andExpect(jsonPath("name", is(genericAgenda.getName())));
    }

    @Test
    public void testFind_whenDoesNotExistsId_thanReturn404() throws Exception {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        when(agendaService.find(any())).thenThrow(new ResponseStatusException(notFound));

        mockMvc
                .perform(
                        get(String.format("%s/%s", URI, genericAgenda.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(notFound.value()))
                .andExpect(jsonPath("statusCode", is(notFound.value())));
    }

    @Test
    public void testList_whenThereAreAgendas_thanReturnListOfAll() throws Exception {
        when(agendaService.list()).thenReturn(List.of(genericAgenda, genericAgenda));

        mockMvc
                .perform(get(URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(genericAgenda.getId().toString())))
                .andExpect(jsonPath("$[0].name", is(genericAgenda.getName())));
    }

    @Test
    public void testList_whenThereAreNotAgendas_thanReturnEmpty() throws Exception {
        when(agendaService.list()).thenReturn(List.of());

        mockMvc
                .perform(get(URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testCreate_whenValidAgenda_thanCreates() throws Exception {
        when(agendaService.create(any())).thenReturn(genericAgenda);

        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(genericAgenda))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        Matchers.endsWith(String.format("%s/%s", URI, genericAgenda.getId().toString()))
                ));
    }

    @Test
    public void testCreate_whenInvalidAgendaName_thanBadRequest() throws Exception {
        genericAgenda.setName("");
        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(genericAgenda))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode", is(HttpStatus.BAD_REQUEST.value())));
    }
}

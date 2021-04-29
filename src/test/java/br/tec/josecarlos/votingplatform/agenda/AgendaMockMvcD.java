package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AllArgsConstructor
public class AgendaMockMvcD {

    private final MockMvc mockMvc;
    private final String URI;
    private final Agenda genericAgenda;

    public void testFind_whenExistingId_thanReturnAgenda_mockMvc() throws Exception {
        mockMvc
                .perform(
                        get(String.format("%s/%s", URI, genericAgenda.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(genericAgenda.getId().toString())))
                .andExpect(jsonPath("name", is(genericAgenda.getName())));
    }

    public void testFind_whenDoesNotExistsId_thanReturn404_mockMvc() throws Exception {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        mockMvc
                .perform(
                        get(String.format("%s/%s", URI, genericAgenda.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(notFound.value()))
                .andExpect(jsonPath("statusCode", is(notFound.value())));
    }

    public void testList_whenThereAreAgendas_thanReturnListOfAll_mockMvc() throws Exception {
        mockMvc
                .perform(get(URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(genericAgenda.getId().toString())))
                .andExpect(jsonPath("$[0].name", is(genericAgenda.getName())));
    }

    public void testList_whenThereAreNotAgendas_thanReturnEmpty_mockMvc() throws Exception {
        mockMvc
                .perform(get(URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    public void testCreate_whenValidAgenda_thanCreates_mockMvc() throws Exception {
        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(genericAgenda))
                )
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        "Location",
                        Matchers.containsString(String.format("%s/", URI))
                ));
    }

    public void testCreate_whenInvalidAgendaName_thanBadRequest_mockMvc() throws Exception {
        mockMvc
                .perform(post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(genericAgenda))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("statusCode", is(HttpStatus.BAD_REQUEST.value())));
    }
}

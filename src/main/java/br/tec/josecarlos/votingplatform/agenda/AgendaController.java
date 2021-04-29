package br.tec.josecarlos.votingplatform.agenda;

import br.tec.josecarlos.votingplatform.models.Agenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("agendas")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping
    public List<AgendaResponse> list() {
        return agendaService.list().stream()
                .map(AgendaResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public AgendaResponse find(@PathVariable UUID id) {
        return new AgendaResponse(agendaService.find(id));
    }

    @PostMapping
    public ResponseEntity<AgendaResponse> create(@RequestBody @Valid AgendaRequest agendaRequest, UriComponentsBuilder uriComponentsBuilder) {
        Agenda agenda = agendaService.create(agendaRequest.map());

        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/agendas/{id}")
                                .buildAndExpand(agenda.getId()).toUri()
                )
                .body(new AgendaResponse(agenda));
    }
}

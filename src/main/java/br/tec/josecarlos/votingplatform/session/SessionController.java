package br.tec.josecarlos.votingplatform.session;

import br.tec.josecarlos.votingplatform.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<SessionResponse> list() {
        return sessionService.list().stream()
                .map(SessionResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public SessionResponse find(@PathVariable UUID id) {
        return new SessionResponse(sessionService.find(id));
    }

    @PostMapping
    public ResponseEntity<SessionResponse> create(@RequestBody @Valid SessionRequest sessionRequest, UriComponentsBuilder uriComponentsBuilder) {
        Session session = sessionService.create(sessionRequest.map());

        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/sessions/{id}")
                                .buildAndExpand(session.getId()).toUri()
                )
                .body(new SessionResponse(session));
    }
}

package br.tec.josecarlos.votingplatform.session.uservote;

import br.tec.josecarlos.votingplatform.models.SessionUserVote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("sessions/{sessionId}/votes")
public class SessionUserVoteController {

    @Autowired
    private SessionUserVoteService sessionUserVoteService;

    @GetMapping
    public SessionUserVoteReport count(@PathVariable UUID sessionId) {
        return sessionUserVoteService.count(sessionId);
    }

    @PostMapping
    public ResponseEntity<SessionUserVoteResponse> create(@PathVariable UUID sessionId, @RequestBody @Valid SessionUserVoteRequest sessionUserVoteRequest, UriComponentsBuilder uriComponentsBuilder) {
        SessionUserVote sessionUserVote = sessionUserVoteService.create(sessionId, sessionUserVoteRequest.map());

        return ResponseEntity
                .created(
                        uriComponentsBuilder
                                .path("/sessions/{id}/votes")
                                .buildAndExpand(sessionUserVote.getSession().getId()).toUri()
                )
                .body(new SessionUserVoteResponse(sessionUserVote));
    }
}

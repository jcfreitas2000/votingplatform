package br.tec.josecarlos.votingplatform.session.uservote;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionUserVoteReport {

    private final Long countAgreed;
    private final Long countDisagreed;
}

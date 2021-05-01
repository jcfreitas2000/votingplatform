package br.tec.josecarlos.votingplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = {"userCpf", "session"})
@Entity
@IdClass(SessionUserVotePK.class)
public class SessionUserVote extends AbstractEntity<SessionUserVotePK> {

    @Id
    private String userCpf;
    @Id
    @ManyToOne
    private Session session;

    @Column(nullable = false)
    private Boolean agreed;

    @Override
    public SessionUserVotePK getId() {
        return new SessionUserVotePK(userCpf, session);
    }
}

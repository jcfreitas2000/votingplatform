package br.tec.josecarlos.votingplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(of = {"user", "session"})
@Entity
@IdClass(SessionUserVotePK.class)
public class SessionUserVote {

    @Id
    @ManyToOne
    private User user;
    @Id
    @ManyToOne
    private Session session;

    private boolean agreed;
}

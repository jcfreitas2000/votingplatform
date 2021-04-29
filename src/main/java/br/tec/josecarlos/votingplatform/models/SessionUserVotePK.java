package br.tec.josecarlos.votingplatform.models;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionUserVotePK implements Serializable {

    private User user;
    private Session session;
}

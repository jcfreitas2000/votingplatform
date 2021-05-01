package br.tec.josecarlos.votingplatform.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUserVotePK implements Serializable {

    private String userCpf;
    @ManyToOne
    private Session session;
}

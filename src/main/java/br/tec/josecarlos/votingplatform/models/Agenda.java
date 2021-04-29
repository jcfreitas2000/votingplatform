package br.tec.josecarlos.votingplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Agenda {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;
}

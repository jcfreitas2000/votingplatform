package br.tec.josecarlos.votingplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id = UUID.randomUUID();

    private String name;

    @Column(unique = true)
    private String cpf;
}

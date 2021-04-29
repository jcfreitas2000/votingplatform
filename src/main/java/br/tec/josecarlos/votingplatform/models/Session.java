package br.tec.josecarlos.votingplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Session {

    @Id
    private UUID id;

    private LocalDateTime deadline;

    public Session() {
        id = UUID.randomUUID();
        deadline = LocalDateTime.now().plus(1, ChronoUnit.MINUTES);
    }
}

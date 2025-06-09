package dev.raul.biometria_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "colaboradores")
public class Colaborador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "unique_id", nullable = false)
    private byte[] uniqueId;

    @Lob
    @Column(name = "template", nullable = false)
    private byte[] template;
}

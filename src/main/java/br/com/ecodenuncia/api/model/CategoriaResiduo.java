package br.com.ecodenuncia.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_CATEGORIA_RESIDUO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResiduo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CATEGORIA")
    private Long id;

    @Column(name = "NM_CATEGORIA", nullable = false, unique = true, length = 80)
    private String nome;

    @Column(name = "DS_CATEGORIA", nullable = false, length = 255)
    private String descricao;
}

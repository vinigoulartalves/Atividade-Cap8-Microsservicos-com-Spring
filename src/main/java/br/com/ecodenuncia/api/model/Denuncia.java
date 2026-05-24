package br.com.ecodenuncia.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_DENUNCIA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DENUNCIA")
    private Long id;

    @Column(name = "NM_TITULO", nullable = false, length = 150)
    private String titulo;

    @Column(name = "DS_DESCRICAO", nullable = false, length = 2000)
    private String descricao;

    @Column(name = "DS_ENDERECO", nullable = false, length = 200)
    private String endereco;

    @Column(name = "NM_BAIRRO", nullable = false, length = 100)
    private String bairro;

    @Column(name = "NM_CIDADE", nullable = false, length = 100)
    private String cidade;

    @Column(name = "SG_ESTADO", nullable = false, length = 2)
    private String estado;

    @Column(name = "VL_LATITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "VL_LONGITUDE", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_CATEGORIA", nullable = false, length = 30)
    private CategoriaResiduo categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_STATUS", nullable = false, length = 20)
    private StatusDenuncia status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "DT_CRIACAO", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "DT_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.dataCriacao = now;
        this.dataAtualizacao = now;
        if (this.status == null) {
            this.status = StatusDenuncia.PENDENTE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}

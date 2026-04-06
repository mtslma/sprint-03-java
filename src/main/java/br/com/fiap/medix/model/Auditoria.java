package br.com.fiap.medix.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_AUDITORIA")
@Data
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_tabela")
    private String nomeTabela;

    @Column(name = "operacao")
    private String operacao;

    @Column(name = "usuario_bd")
    private String usuarioBd;

    @Column(name = "data_operacao")
    private LocalDateTime dataOperacao;

    @Column(name = "valor_antigo")
    private String valorAntigo;

    @Column(name = "valor_novo")
    private String valorNovo;
}
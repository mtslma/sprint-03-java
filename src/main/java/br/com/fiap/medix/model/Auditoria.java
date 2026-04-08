package br.com.fiap.medix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_AUDITORIA")
@Getter @Setter
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tabela_nome") // Antes estava nome_tabela
    private String tabelaNome;

    @Column(name = "operacao")
    private String operacao;

    @Column(name = "usuario_db") // Antes estava usuario_bd
    private String usuarioDb;

    @Column(name = "data_evento") // Antes estava data_operacao
    private LocalDateTime dataEvento;

    @Column(name = "dados_antigos") // Antes estava valor_antigo
    private String dadosAntigos;

    @Column(name = "dados_novos") // Antes estava valor_novo
    private String dadosNovos;
}
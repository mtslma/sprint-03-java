package br.com.fiap.medix.repository;

import br.com.fiap.medix.model.Agendamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataHoraInicio >= :agora " +
            "AND a.status != 'CANCELADO' " +
            "ORDER BY a.dataHoraInicio ASC")
    List<Agendamento> findNextAgendamento(@Param("pacienteId") Long pacienteId,
                                          @Param("agora") LocalDateTime agora,
                                          Pageable pageable);

    List<Agendamento> findAllByPacienteIdOrderByDataHoraInicioDesc(Long pacienteId);

    // FUNCTION 2: Cálculo Matemático
    @Query(value = "SELECT FN_CALCULA_DURACAO_TOTAL(:unidadeId) FROM DUAL", nativeQuery = true)
    Long calcularOcupacaoMinutos(@Param("unidadeId") Long unidadeId);

    // PROCEDURE 1
    @Procedure(procedureName = "SP_GET_HISTORICO_JSON", outputParameterName = "p_saida")
    String chamarHistoricoProcedure(@Param("p_paciente_id") Long pacienteId);

    // PROCEDURE 2
    @Procedure(procedureName = "SP_RELATORIO_NAVEGACAO", outputParameterName = "p_saida")
    String chamarRelatorioNavegacaoProcedure();
}
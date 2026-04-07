package br.com.fiap.medix.repository;

import br.com.fiap.medix.dto.AgendamentoRequest;
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

    // Lista todos os agendamentos de um paciente específico
    List<Agendamento> findAllByPacienteIdOrderByDataHoraInicioDesc(Long pacienteId);

    // Busca o próximo agendamento (o mais próximo do horário atual)
    @Query("SELECT a FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.dataHoraInicio >= :agora " +
            "AND a.status != 'CANCELADO' " +
            "ORDER BY a.dataHoraInicio ASC")
    List<Agendamento> findNextAgendamento(Long pacienteId, LocalDateTime agora, Pageable pageable);

    @Query("SELECT COUNT(a) > 0 FROM Agendamento a WHERE a.paciente.id = :pacienteId " +
            "AND a.status != 'CANCELADO' " +
            "AND ((a.dataHoraInicio < :fim AND a.dataHoraFim > :inicio))")
    boolean existsOverlappingAgendamento(Long pacienteId, LocalDateTime inicio, LocalDateTime fim);

    // Chama a PROCEDURE 1: Finalização Automática
    @Procedure(procedureName = "SP_FINALIZA_CONSULTAS_ANTIGAS")
    void finalizarConsultasAntigas();

    // Chama a PROCEDURE 2: Atualização de Status com Parâmetros
    @Procedure(procedureName = "SP_ATUALIZA_STATUS_AGENDAMENTO")
    void atualizarStatusViaProcedure(@Param("p_id") Long id, @Param("p_status") String status);

    @Query(value = "SELECT FN_VERIFICA_SALA_DISPONIVEL(:salaId, :inicio) FROM DUAL", nativeQuery = true)
    String verificarDisponibilidadeSala(@Param("salaId") Long salaId, @Param("inicio") LocalDateTime inicio);
}

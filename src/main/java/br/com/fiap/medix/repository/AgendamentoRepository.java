package br.com.fiap.medix.repository;

import br.com.fiap.medix.dto.AgendamentoRequest;
import br.com.fiap.medix.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}

package br.com.fiap.medix.service;

import br.com.fiap.medix.dto.AgendamentoRequest;
import br.com.fiap.medix.dto.DashboardPacienteDTO;
import br.com.fiap.medix.enums.StatusAgendamento;
import br.com.fiap.medix.model.*;
import br.com.fiap.medix.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    @Autowired private AgendamentoRepository repository;
    @Autowired private ColaboradorRepository colaboradorRepository;
    @Autowired private UnidadeRepository unidadeRepository;
    @Autowired private SalaRepository salaRepository;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequest dto, Usuario pacienteLogado) {
        if (!(pacienteLogado instanceof Paciente)) {
            throw new RuntimeException("Apenas pacientes podem realizar agendamentos.");
        }

        LocalDateTime inicio = dto.dataHoraInicio();

        Colaborador medico = colaboradorRepository.findAvailableDoctor(dto.medicoId(), inicio)
                .orElseThrow(() -> new RuntimeException("Medico nao disponivel."));
        UnidadeSaude unidade = unidadeRepository.findById(dto.unidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade nao encontrada."));
        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new RuntimeException("Sala nao encontrada."));

        Agendamento agendamento = new Agendamento();
        agendamento.setPaciente((Paciente) pacienteLogado);
        agendamento.setMedico(medico);
        agendamento.setUnidade(unidade);
        agendamento.setSala(sala);
        agendamento.setDataHoraInicio(inicio);
        agendamento.setDataHoraFim(dto.dataHoraFim() != null ? dto.dataHoraFim() : inicio.plusMinutes(30));
        agendamento.setTipo(dto.tipo());
        agendamento.setEspecialidade(dto.especialidade());
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        return repository.save(agendamento);
    }

    @Transactional
    public void atualizarStatus(Long id, StatusAgendamento novoStatus) {
        Agendamento agendamento = repository.findById(id).orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
        agendamento.setStatus(novoStatus);
        repository.save(agendamento);
    }

    // =====================================================================
    // MÉTODOS DA SPRINT 3 (CHAMAM AS FUNCTIONS E PROCEDURES NO BANCO)
    // =====================================================================

    public Long consultarOcupacaoUnidade(Long unidadeId) {
        return repository.calcularOcupacaoMinutos(unidadeId);
    }

    public String testarHistorico(Long id) {
        return repository.chamarHistoricoProcedure(id);
    }

    public String testarNavegacao() {
        return repository.chamarRelatorioNavegacaoProcedure();
    }

    // =====================================================================

    public List<Agendamento> listarMeusAgendamentos(Usuario usuario) {
        return repository.findAllByPacienteIdOrderByDataHoraInicioDesc(usuario.getId());
    }

    public Agendamento buscarProximoAgendamento(Usuario usuario) {
        List<Agendamento> lista = repository.findNextAgendamento(usuario.getId(), LocalDateTime.now(), PageRequest.of(0, 1));
        return lista.isEmpty() ? null : lista.get(0);
    }

    public DashboardPacienteDTO carregarDashboard(Usuario usuario) {
        List<Agendamento> todos = repository.findAllByPacienteIdOrderByDataHoraInicioDesc(usuario.getId());
        long realizados = todos.stream().filter(a -> StatusAgendamento.FINALIZADO.equals(a.getStatus())).count();
        long cancelados = todos.stream().filter(a -> StatusAgendamento.CANCELADO.equals(a.getStatus())).count();
        String nome = (usuario instanceof Paciente) ? ((Paciente) usuario).getNome() : "Usuario";

        return new DashboardPacienteDTO(nome, todos.isEmpty() ? null : todos.get(0), todos.stream().limit(3).toList(), realizados, cancelados);
    }
}
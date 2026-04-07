package br.com.fiap.medix.service;

import br.com.fiap.medix.dto.AgendamentoRequest;
import br.com.fiap.medix.enums.StatusAgendamento;
import br.com.fiap.medix.model.*;
import br.com.fiap.medix.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    @Autowired private AgendamentoRepository repository;
    @Autowired private ColaboradorRepository colaboradorRepository;
    @Autowired private UnidadeRepository unidadeRepository;
    @Autowired private SalaRepository salaRepository;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequest dto, Usuario pacienteLogado) {
        // 1. Validar se quem está logado é de fato um Paciente
        if (!(pacienteLogado instanceof Paciente)) {
            throw new RuntimeException("Apenas pacientes podem realizar agendamentos.");
        }

        // 2. Buscar médico operacional disponível
        Colaborador medico = colaboradorRepository.findAvailableDoctor(dto.especialidade(), dto.dataHora())
                .orElseThrow(() -> new RuntimeException("Não há médicos disponíveis para esta especialidade neste horário."));

        // 3. Buscar Unidade e Sala
        UnidadeSaude unidade = unidadeRepository.findById(dto.unidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada."));

        // No mundo real validaríamos se a sala pertence à unidade
        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new RuntimeException("Sala não encontrada."));

        // 4. Calcular fim baseado no Enum de duração
        LocalDateTime inicio = dto.dataHora();
        LocalDateTime fim = inicio.plusMinutes(dto.tipo().getDuracaoMinutos());

        // 5. Montar e Salvar
        Agendamento agendamento = new Agendamento();
        agendamento.setPaciente((Paciente) pacienteLogado);
        agendamento.setMedico(medico);
        agendamento.setUnidade(unidade);
        agendamento.setSala(sala);
        agendamento.setDataHoraInicio(inicio);
        agendamento.setDataHoraFim(fim);
        agendamento.setTipo(dto.tipo());
        agendamento.setEspecialidade(dto.especialidade());

        return repository.save(agendamento);
    }

    @Transactional
    public void cancelarAgendamento(Long id) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        // Regra: Não pode cancelar com menos de 24h de antecedência
        LocalDateTime agora = LocalDateTime.now();
        if (agora.isAfter(agendamento.getDataHoraInicio().minusDays(1))) {
            throw new RuntimeException("Cancelamento permitido apenas com 24h de antecedência.");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        repository.save(agendamento);
    }

    @Transactional
    public void confirmarAgendamento(Long id) {
        Agendamento agendamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));

        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        repository.save(agendamento);
    }
}
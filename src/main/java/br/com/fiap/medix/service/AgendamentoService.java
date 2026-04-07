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
        // 1. Validar se quem está logado é de fato um Paciente
        if (!(pacienteLogado instanceof Paciente)) {
            throw new RuntimeException("Apenas pacientes podem realizar agendamentos.");
        }

        // --- MELHORIA 1: Validar Horário Retroativo ---
        // Impede agendamentos no passado ou com menos de 30min de antecedência
        if (dto.dataHora().isBefore(LocalDateTime.now().plusMinutes(30))) {
            throw new RuntimeException("Agendamentos devem ser feitos com no mínimo 30 minutos de antecedência.");
        }

        // 2. Buscar médico operacional disponível (Lógica já implementada no Repository)
        Colaborador medico = colaboradorRepository.findAvailableDoctor(dto.especialidade(), dto.dataHora())
                .orElseThrow(() -> new RuntimeException("Não há médicos disponíveis para esta especialidade neste horário."));

        // 3. Buscar Unidade e Sala
        UnidadeSaude unidade = unidadeRepository.findById(dto.unidadeId())
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada."));

        Sala sala = salaRepository.findById(dto.salaId())
                .orElseThrow(() -> new RuntimeException("Sala não encontrada."));

        // 4. Calcular fim baseado no Enum de duração
        LocalDateTime inicio = dto.dataHora();
        LocalDateTime fim = inicio.plusMinutes(dto.tipo().getDuracaoMinutos());

        String disponibilidade = repository.verificarDisponibilidadeSala(dto.salaId(), inicio);
        if ("OCUPADA".equals(disponibilidade)) {
            throw new RuntimeException("A sala selecionada já está ocupada neste horário.");
        }

        // --- MELHORIA 2: Verificar Conflito de Agenda do Paciente ---
        // Verifica se o paciente já tem algum agendamento ativo que sobreponha este horário
        boolean pacienteOcupado = repository.existsOverlappingAgendamento(pacienteLogado.getId(), inicio, fim);
        if (pacienteOcupado) {
            throw new RuntimeException("Você já possui um agendamento confirmado ou pendente neste intervalo de horário.");
        }

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
        agendamento.setStatus(StatusAgendamento.AGENDADO); // Garante o status inicial

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

    public List<Agendamento> listarMeusAgendamentos(Usuario usuario) {
        return repository.findAllByPacienteIdOrderByDataHoraInicioDesc(usuario.getId());
    }

    public Agendamento buscarProximoAgendamento(Usuario usuario) {
        var lista = repository.findNextAgendamento(usuario.getId(), LocalDateTime.now(), PageRequest.of(0, 1));
        return lista.isEmpty() ? null : lista.get(0);
    }

    // Adicione este método dentro da classe AgendamentoService
    public DashboardPacienteDTO carregarDashboard(Usuario usuario) {
        // 1. Pega o próximo (usando o método que você já criou)
        Agendamento proximo = this.buscarProximoAgendamento(usuario);

        // 2. Pega o histórico recente (ex: os últimos 3)
        List<Agendamento> todos = repository.findAllByPacienteIdOrderByDataHoraInicioDesc(usuario.getId());
        List<Agendamento> recentes = todos.stream().limit(3).toList();

        // 3. Estatísticas simples para o "frufru" do front-end
        long realizados = todos.stream()
                .filter(a -> a.getStatus().name().equals("FINALIZADO"))
                .count();

        long cancelados = todos.stream()
                .filter(a -> a.getStatus().name().equals("CANCELADO"))
                .count();

        // Se o seu modelo for Paciente, podemos pegar o nome
        String nome = (usuario instanceof Paciente) ? ((Paciente) usuario).getNome() : "Usuário";

        return new DashboardPacienteDTO(
                nome,
                proximo,
                recentes,
                realizados,
                cancelados
        );
    }

    @Transactional
    public void rodarLimpezaAutomatica() {
        repository.finalizarConsultasAntigas();
    }

    @Transactional
    public void atualizarStatusSeguro(Long id, String novoStatus) {
        // Em vez de usar save(), usamos a lógica do banco
        repository.atualizarStatusViaProcedure(id, novoStatus);
    }
}
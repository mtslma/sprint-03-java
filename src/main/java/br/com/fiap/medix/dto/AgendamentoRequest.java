package br.com.fiap.medix.dto;

import br.com.fiap.medix.enums.TipoAgendamento;
import java.time.LocalDateTime;

public record AgendamentoRequest(
        Long unidadeId,
        Long salaId,
        String especialidade,
        LocalDateTime dataHora,
        TipoAgendamento tipo
) {}
package br.com.fiap.medix.service;

import br.com.fiap.medix.enums.TipoColaborador;
import br.com.fiap.medix.model.Colaborador;
import br.com.fiap.medix.model.UnidadeSaude;
import br.com.fiap.medix.model.Usuario;
import br.com.fiap.medix.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository repository;

    public List<UnidadeSaude> listarTodas() {
        return repository.findAll();
    }

    public UnidadeSaude buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidade de Saúde não encontrada com o ID: " + id));
    }

    @Transactional
    public UnidadeSaude salvar(UnidadeSaude unidade, Usuario logado) {
        validarAcessoAdministrativo(logado);

        // Garante que cada sala saiba a qual unidade pertence (para o banco gravar o ID certo)
        if (unidade.getSalas() != null) {
            unidade.getSalas().forEach(sala -> sala.setUnidade(unidade));
        }

        return repository.save(unidade);
    }

    @Transactional
    public UnidadeSaude atualizar(Long id, UnidadeSaude dadosNovos, Usuario logado) {
        validarAcessoAdministrativo(logado);

        UnidadeSaude unidadeExistente = buscarPorId(id);

        unidadeExistente.setNome(dadosNovos.getNome());
        unidadeExistente.setEndereco(dadosNovos.getEndereco());

        // Lógica para atualizar salas (limpa as antigas e adiciona as novas do JSON)
        if (dadosNovos.getSalas() != null) {
            unidadeExistente.getSalas().clear();
            dadosNovos.getSalas().forEach(sala -> {
                sala.setUnidade(unidadeExistente);
                unidadeExistente.getSalas().add(sala);
            });
        }

        return repository.save(unidadeExistente);
    }

    @Transactional
    public void excluir(Long id, Usuario logado) {
        validarAcessoAdministrativo(logado);
        UnidadeSaude unidade = buscarPorId(id);
        repository.delete(unidade);
    }

    private void validarAcessoAdministrativo(Usuario usuario) {
        if (usuario instanceof Colaborador colab) {
            if (colab.getTipoColaborador() != TipoColaborador.ADMINISTRATIVO) {
                throw new AccessDeniedException("Acesso permitido apenas para colaboradores ADMINISTRATIVOS.");
            }
        } else {
            throw new AccessDeniedException("Acesso negado. Apenas colaboradores podem gerenciar unidades.");
        }
    }
}
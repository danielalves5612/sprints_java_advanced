package br.com.fiap.previsaoSafra.service;

import br.com.fiap.previsaoSafra.controller.dto.ColheitaDTO;
import br.com.fiap.previsaoSafra.model.Colheita;
import br.com.fiap.previsaoSafra.repository.ColheitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColheitaService {

    @Autowired
    private ColheitaRepository colheitaRepository;

    public Colheita cadastrarColheita(ColheitaDTO colheitaDTO) {
        Colheita colheita = new Colheita();
        colheita.setNome(colheitaDTO.getNome());
        colheita.setTipo(colheitaDTO.getTipo());
        colheita.setEstacaoDoAno(colheitaDTO.getEstacaoDoAno());
        return colheitaRepository.save(colheita); 
    }

    public Colheita atualizarColheita(Long id, ColheitaDTO novaColheita) {
        Optional<Colheita> colheitaExistente = colheitaRepository.findById(id);

        if (colheitaExistente.isPresent()) {
            Colheita colheitaAtualizada = colheitaExistente.get();
            colheitaAtualizada.setNome(novaColheita.getNome());
            colheitaAtualizada.setTipo(novaColheita.getTipo());
            colheitaAtualizada.setEstacaoDoAno(novaColheita.getEstacaoDoAno());
            colheitaAtualizada.setDadosProducao(novaColheita.getDadosProducao());
            return colheitaRepository.save(colheitaAtualizada);
        } else {
            throw new RuntimeException("Colheita não encontrada com o ID: " + id);
        }
    }

    public List<Colheita> listarColheitas() {
        return colheitaRepository.findAll();
    }

    public List<Colheita> listarColheitaPorAtributo(String nome, String tipo, String estacaoDoAno) {
        return colheitaRepository.findByNomeContainingOrTipoContainingOrEstacaoDoAnoContaining(nome, tipo, estacaoDoAno);
    }

    public void removerColheita(Long id) {
        if (colheitaRepository.existsById(id)) {
            colheitaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Colheita não encontrada com o ID: " + id);
        }
    }

    public Colheita buscarColheitaPorId(Long id) {
        Optional<Colheita> colheitaOptional = colheitaRepository.findById(id);
        return colheitaOptional.orElseThrow(() -> new RuntimeException("Colheita não encontrada com o ID: " + id));
    }
}

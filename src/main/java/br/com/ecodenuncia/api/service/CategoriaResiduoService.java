package br.com.ecodenuncia.api.service;

import br.com.ecodenuncia.api.dto.CategoriaResiduoRequest;
import br.com.ecodenuncia.api.dto.CategoriaResiduoResponse;
import br.com.ecodenuncia.api.exception.RecursoNaoEncontradoException;
import br.com.ecodenuncia.api.exception.RegraNegocioException;
import br.com.ecodenuncia.api.model.CategoriaResiduo;
import br.com.ecodenuncia.api.repository.CategoriaResiduoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaResiduoService {

    private final CategoriaResiduoRepository categoriaResiduoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResiduoResponse> listar() {
        return categoriaResiduoRepository.findAll().stream()
                .map(CategoriaResiduoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResiduoResponse buscarPorId(Long id) {
        return CategoriaResiduoResponse.from(buscarEntidade(id));
    }

    @Transactional
    public CategoriaResiduoResponse criar(CategoriaResiduoRequest request) {
        if (categoriaResiduoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraNegocioException(
                    "Ja existe uma categoria com o nome: " + request.nome());
        }

        CategoriaResiduo categoria = CategoriaResiduo.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .build();

        return CategoriaResiduoResponse.from(categoriaResiduoRepository.save(categoria));
    }

    @Transactional
    public CategoriaResiduoResponse atualizar(Long id, CategoriaResiduoRequest request) {
        CategoriaResiduo categoria = buscarEntidade(id);

        boolean nomeMudou = !categoria.getNome().equalsIgnoreCase(request.nome());
        if (nomeMudou && categoriaResiduoRepository.existsByNomeIgnoreCase(request.nome())) {
            throw new RegraNegocioException(
                    "Ja existe uma categoria com o nome: " + request.nome());
        }

        categoria.setNome(request.nome());
        categoria.setDescricao(request.descricao());
        return CategoriaResiduoResponse.from(categoriaResiduoRepository.save(categoria));
    }

    @Transactional
    public void deletar(Long id) {
        if (!categoriaResiduoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException(
                    "Categoria de residuo nao encontrada com id: " + id);
        }
        categoriaResiduoRepository.deleteById(id);
    }

    private CategoriaResiduo buscarEntidade(Long id) {
        return categoriaResiduoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria de residuo nao encontrada com id: " + id));
    }
}

package br.com.ecodenuncia.api.repository;

import br.com.ecodenuncia.api.model.CategoriaResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaResiduoRepository extends JpaRepository<CategoriaResiduo, Long> {

    Optional<CategoriaResiduo> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}

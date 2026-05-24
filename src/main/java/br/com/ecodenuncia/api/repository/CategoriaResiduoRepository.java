package br.com.ecodenuncia.api.repository;

import br.com.ecodenuncia.api.model.CategoriaResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaResiduoRepository extends JpaRepository<CategoriaResiduo, Long> {
}

package br.com.ecodenuncia.api.repository;

import br.com.ecodenuncia.api.model.Denuncia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {

    Page<Denuncia> findByUsuarioId(Long usuarioId, Pageable pageable);
}

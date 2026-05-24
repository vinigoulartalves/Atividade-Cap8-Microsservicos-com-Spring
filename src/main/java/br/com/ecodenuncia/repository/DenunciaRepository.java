package br.com.ecodenuncia.repository;

import br.com.ecodenuncia.model.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
}

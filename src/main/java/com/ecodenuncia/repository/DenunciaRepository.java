package com.ecodenuncia.repository;

import com.ecodenuncia.model.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
}

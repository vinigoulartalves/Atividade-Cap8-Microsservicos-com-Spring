package com.ecodenuncia.api.repository;

import com.ecodenuncia.api.model.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
}

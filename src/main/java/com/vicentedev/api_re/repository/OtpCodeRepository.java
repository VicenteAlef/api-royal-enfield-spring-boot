package com.vicentedev.api_re.repository;

import com.vicentedev.api_re.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpCodeRepository extends JpaRepository<OtpCode, UUID> {

    Optional<OtpCode> findTopByUserIdAndUsedFalseOrderByCreatedAtDesc(UUID userId);

    Optional<OtpCode> findByUserIdAndCodeAndUsedFalse(UUID userId, String code);

    void deleteByUserId(UUID userId);
}

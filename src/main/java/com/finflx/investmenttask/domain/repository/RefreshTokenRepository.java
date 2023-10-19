package com.finflx.investmenttask.domain.repository;

import com.finflx.investmenttask.domain.entity.RefreshToken;
import com.finflx.investmenttask.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUser(User user);

    void deleteByUserAndExpiryDateBefore(User user, LocalDateTime expiryDate);
}

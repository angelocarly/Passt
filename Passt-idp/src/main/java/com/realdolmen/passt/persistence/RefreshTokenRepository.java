package com.realdolmen.passt.persistence;

import com.realdolmen.passt.domain.JwtRefreshToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Angelo Carly
 */
public interface RefreshTokenRepository extends JpaRepository<JwtRefreshToken, Integer>
{

    Optional<JwtRefreshToken> findById(UUID tokenId);

    @Query("SELECT t FROM JwtRefreshToken t WHERE t.jti = :jti AND t.clientId = :clientId")
    Optional<JwtRefreshToken> findByJtiAndClientId(@Param("jti") UUID jti, @Param("clientId") String clientId);
    
    @Query("SELECT t FROM JwtRefreshToken t WHERE t.userId = :userId AND t.clientId = :clientId")
    List<JwtRefreshToken> findByUserIdAndClientId(@Param("userId") UUID userId, @Param("clientId") String clientId);
    
    @Query("SELECT t FROM JwtRefreshToken t WHERE t.userId = :userId AND t.clientId = :clientId AND t.jti = :jti")
    Optional<JwtRefreshToken> findByUserIdClientIdJti(@Param("userId") UUID userId, @Param("clientId") String clientId, @Param("jti") UUID jti);
    
    
    List<JwtRefreshToken> findByUserId(UUID userId);
    
    /**
     * Transactional because of this
     * https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
     */
    @Transactional
    void deleteById(UUID id);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM JwtRefreshToken WHERE jti = :jti AND clientId = :clientId")
    void deleteByJtiAndClientId(@Param("jti") UUID jti, @Param("clientId") String clientId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM JwtRefreshToken WHERE userId = :userId AND clientId = :clientId")
    void deleteByUserIdAndClientId(@Param("userId") UUID userId, @Param("clientId") String clientId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM JwtRefreshToken WHERE userId = :userId AND jti = :jti")
    void deleteByJtiAndUserId( @Param("jti") UUID jti, @Param("userId") UUID userId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM JwtRefreshToken WHERE clientId = :clientId AND ati = :ati")
    void deleteByAtiAndClientId(@Param("ati") UUID ati, @Param("clientId") String clientId);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM JwtRefreshToken WHERE userId = :userId")
    void deleteByUserId(@Param("userId") UUID userId);
}

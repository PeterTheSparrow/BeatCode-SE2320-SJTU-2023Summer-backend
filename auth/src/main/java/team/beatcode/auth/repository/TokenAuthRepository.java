package team.beatcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.beatcode.auth.entity.TokenAuth;

public interface TokenAuthRepository extends JpaRepository<TokenAuth, byte[]> {
    void deleteByToken(byte[] token);
}

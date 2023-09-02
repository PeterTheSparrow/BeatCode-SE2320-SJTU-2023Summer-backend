package team.beatcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.beatcode.auth.entity.Code;

import java.util.Optional;


public interface CodeRepository extends JpaRepository<Code, Integer> {
    Optional<Code> findByEmail(String email);

}

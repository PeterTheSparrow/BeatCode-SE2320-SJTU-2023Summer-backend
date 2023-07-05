package team.beatcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.beatcode.auth.entity.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Integer> {
    UserAuth getUserAuthByName(String name);
}

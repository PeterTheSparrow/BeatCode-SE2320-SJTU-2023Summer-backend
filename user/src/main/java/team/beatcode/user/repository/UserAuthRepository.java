package team.beatcode.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.beatcode.auth.entity.UserAuth;

import java.util.Optional;
public interface UserAuthRepository extends JpaRepository<UserAuth, Integer>{
    Optional<UserAuth> findUser_authById(Integer userId);
}

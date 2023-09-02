package team.beatcode.user.repository;

import team.beatcode.user.entity.User_auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface User_authRepository extends JpaRepository<User_auth, Integer>{
    Optional<User_auth> findUser_authByUserId(Integer userId);
}

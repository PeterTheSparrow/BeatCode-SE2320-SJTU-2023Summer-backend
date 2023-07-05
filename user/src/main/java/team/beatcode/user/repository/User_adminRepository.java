package team.beatcode.user.repository;

import org.springframework.stereotype.Repository;
import team.beatcode.user.entity.User_admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface User_adminRepository extends JpaRepository<User_admin, Integer>{

    Optional<User_admin> findUser_adminByuserId(Integer id);

}

package team.beatcode.user.repository;

import team.beatcode.user.entity.User_info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface User_infoRepository extends JpaRepository<User_info, Integer>{

    Optional<User_info> findUser_infoByUserId(Integer userId);

}

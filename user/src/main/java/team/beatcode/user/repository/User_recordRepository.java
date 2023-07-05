package team.beatcode.user.repository;

import team.beatcode.user.entity.User_record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface User_recordRepository extends JpaRepository<User_record, Integer>{
    Optional<User_record> findUser_recordByUserId(Integer userId);

    List<User_record> findAll();

}

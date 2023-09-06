package team.beatcode.auth.dao;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.repository.UserAuthRepository;

@Repository
public class UserAuthDao {
    @Autowired
    UserAuthRepository userAuthRepository;

    public UserAuth getUserAuthById(int id) {
        return userAuthRepository.findById(id).orElse(null);
    }

    public UserAuth getUserAuthByName(String name) {
        return userAuthRepository.getUserAuthByName(name);
    }

    public UserAuth save(@NonNull UserAuth userAuth) {
        return userAuthRepository.save(userAuth);
    }

}

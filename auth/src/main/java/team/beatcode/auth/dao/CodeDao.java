package team.beatcode.auth.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.auth.entity.Code;
import team.beatcode.auth.repository.CodeRepository;

import java.util.Optional;

@Repository
public class CodeDao {
    @Autowired
    private CodeRepository codeRepository;

    public Optional<Code> findByEmail(String email) {
        return codeRepository.findByEmail(email);
    }

    public void save(Code code) {
        codeRepository.save(code);
    }
}

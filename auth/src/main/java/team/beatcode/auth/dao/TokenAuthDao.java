package team.beatcode.auth.dao;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.auth.entity.TokenAuth;
import team.beatcode.auth.repository.TokenAuthRepository;
import team.beatcode.auth.utils.TokenUtils;

@Repository
public class TokenAuthDao {
    @Autowired
    TokenAuthRepository tokenAuthRepository;

    public TokenAuth getByTokenBytes(byte[] token) {
        return tokenAuthRepository.findById(token).orElse(null);
    }

    public TokenAuth getByToken(String token) {
        byte[] bytes = TokenUtils.StringToBytes(token);
        if (bytes == null) return null;
        else return tokenAuthRepository.findById(bytes).orElse(null);
    }

    public TokenAuth save(@NonNull TokenAuth tokenAuth) {
        // 本数据库经常被修改，比起性能更怕出错
        return tokenAuthRepository.saveAndFlush(tokenAuth);
    }

    public void remove(String token) {
        byte[] bytes = TokenUtils.StringToBytes(token);
        if (bytes == null) return;
        tokenAuthRepository.deleteByToken(bytes);
    }
}

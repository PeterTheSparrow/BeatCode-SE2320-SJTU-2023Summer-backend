package team.beatcode.auth.dao;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.beatcode.auth.entity.IpAuth;
import team.beatcode.auth.repository.IpAuthRepository;
import team.beatcode.auth.utils.IpInStr;

@Repository
public class IpAuthDao {
    @Autowired
    IpAuthRepository ipAuthRepository;

    public IpAuth getByIp(@NonNull String ipStr) {
        return ipAuthRepository.getIpAuthByIpAddr(IpInStr.ipAddrStrToHex(ipStr));
    }

    public IpAuth save(@NonNull IpAuth ipAuth) {
        // 本数据库经常被修改，比起性能更怕出错
        return ipAuthRepository.saveAndFlush(ipAuth);
    }
}

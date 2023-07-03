package team.beatcode.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.beatcode.auth.entity.IpAuth;

public interface IpAuthRepository extends JpaRepository<IpAuth, byte[]> {
    IpAuth getIpAuthByIpAddr(byte[] ipAddr);
}

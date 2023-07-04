package team.beatcode.auth.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;

@FeignClient(value="user",configuration =
        {FeignClientsConfiguration.class})
public interface UserFeign {
}

package team.beatcode.consumer.feign;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/*
   鸣谢：https://zhuanlan.zhihu.com/p/197707903?utm_id=0
 */

// @Configuration注解将使它全局生效
// 启用自动的log对象
@Slf4j
public class AuthFeignConfig {
    @Bean
    public Retryer feignRetryer() {
        //最大请求次数为5，初始间隔时间为100ms，下次间隔时间1.5倍递增，重试间最大间隔时间为1s，
        return new Retryer.Default();
    }

    @Bean
    public ErrorDecoder feignError() {
        return (key, response) -> {
            if (response.status() == 400) {
                log.error("请求xxx服务400参数错误,返回:{}", response.body());
            }

            if (response.status() == 409) {
                log.error("请求xxx服务409异常,返回:{}", response.body());
            }

            if (response.status() == 404) {
                log.error("请求xxx服务404异常,返回:{}", response.body());
            }

            // 其他异常交给Default去解码处理
            // 这里使用单例即可，Default不用每次都去new
            return new ErrorDecoder.Default().decode(key, response);
        };
    }

    @Bean
    public RequestInterceptor ipPasser() {
        return new IpPassInterceptor();
    }
}

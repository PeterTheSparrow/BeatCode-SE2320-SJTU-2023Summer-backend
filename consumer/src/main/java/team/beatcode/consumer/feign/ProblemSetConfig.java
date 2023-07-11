package team.beatcode.consumer.feign;

import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

// 下面这个注解是为了让这个类被扫描到
@Slf4j
public class ProblemSetConfig {
    // 设置方法为POST
//    @Bean
//    public feign.Contract feignContract() {
//        // 设置方法为POST
//        return new feign.Contract.Default();
//    }

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
    public RequestInterceptor tokenPasser() {
        return new TokenPassInterceptor();
    }
}

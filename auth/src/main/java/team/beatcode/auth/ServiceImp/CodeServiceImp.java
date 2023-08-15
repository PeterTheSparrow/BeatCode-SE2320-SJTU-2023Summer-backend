package team.beatcode.auth.ServiceImp;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.beatcode.auth.Service.CodeService;
import team.beatcode.auth.dao.CodeDao;
import team.beatcode.auth.entity.Code;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CodeServiceImp implements CodeService {
    @Autowired
    private CodeDao codeDao;

    @Override
    public String createCode(String email) {
        // 生成随机的验证码
        String randomCode = RandomUtil.randomNumbers(6);
        // 创建时间
        LocalDateTime createTime = LocalDateTime.now();
        // 过期时间：十分钟
        LocalDateTime expireTime = createTime.plusMinutes(10);

        // 判断是否已经存在该邮箱的验证码
        // 如果存在，更新验证码和过期时间
        // 如果不存在，创建新的验证码
        Optional<Code> codeOptional = codeDao.findByEmail(email);

        Code code;
        if (codeOptional.isPresent()) {
            code = codeOptional.get();
        } else {
            code = new Code();
            code.setEmail(email);
        }
        code.setCode(randomCode);
        code.setCreateTime(createTime);
        code.setExpireTime(expireTime);
        codeDao.save(code);

        return randomCode;
    }

    @Override
    public Integer checkCode(String email, String code) {
        // 检查参数
        if (email == null || code == null) {
            return 1;
        }

        // 查数据库
        Optional<Code> codeOptional = codeDao.findByEmail(email);

        if(codeOptional.isPresent()) {
            Code codeEntity = codeOptional.get();
            // 检查验证码是否正确
            if (codeEntity.getCode().equals(code)) {
                // 检查验证码是否过期
                if (codeEntity.getExpireTime().isAfter(LocalDateTime.now())) {
                    return 0;
                } else {
                    return 2;
                }
            } else {
                return 1;
            }
        }
        return 1;
    }
}

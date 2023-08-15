package team.beatcode.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "beatcode_mysql", name = "verification_code")
public class Code {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 邮箱
    @Column(name = "email")
    private String email;

    // 验证码
    @Column(name = "code")
    private String code;

    // 创建时间
    @Column(name = "create_time")
    private LocalDateTime createTime;

    // 过期时间
    @Column(name = "expire_time")
    private LocalDateTime expireTime;
}

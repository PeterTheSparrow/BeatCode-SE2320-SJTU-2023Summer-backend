package team.beatcode.user.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "user_auth")
public class User_auth {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_role")
    private Integer userRole;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;
}

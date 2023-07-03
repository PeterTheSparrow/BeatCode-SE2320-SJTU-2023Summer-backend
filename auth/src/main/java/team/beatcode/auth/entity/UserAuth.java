package team.beatcode.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(schema = "beatcode_mysql", name = "user_auth")
public class UserAuth {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_role")
    private int role;

    @Column(name = "user_name")
    private String name;

    @Column(name = "password")
    private String pass;
}

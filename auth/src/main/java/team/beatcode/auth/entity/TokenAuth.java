package team.beatcode.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(schema = "beatcode_mysql", name = "token_auth")
public class TokenAuth {
    @Id
    @Column(name = "token")
    private byte[] token;

    @Column(name = "last_login")
    private long lastLogin;

    @Column(name = "last_fresh")
    private long lastFresh;

    @Column(name = "user_id")
    private int userId;
}

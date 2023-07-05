package team.beatcode.user.entity;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "user_record")
public class User_record {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "accept_num", nullable = false)
    private Integer accept_num;

    @Column(name = "accept_submit", nullable = false)
    private Integer accept_submit;

    @Column(name = "submit_num", nullable = false)
    private Integer submit_num;
}

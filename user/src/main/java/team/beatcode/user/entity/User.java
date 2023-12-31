package team.beatcode.user.entity;

import lombok.Data;
@Data
public class User {
    private Integer userId;
    private String userName;
    private String email;
    private String phone;
    private Integer accept_num;
    private Integer accept_submit;
    private Integer submit_num;
}

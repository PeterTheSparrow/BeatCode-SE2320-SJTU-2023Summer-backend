package team.beatcode.user.entity;

import lombok.Data;
@Data
public class User {
    private String userName;
//    private String password;
    private String email;
    private String phone;
    private Integer accept_num;
    private Integer accept_submit;
    private Integer submit_num;
}

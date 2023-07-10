package team.beatcode.user.entity;

import lombok.Data;
@Data
public class Rank {
    private Integer userId;
    private String userName;
    private Integer acceptNum;
    private Integer acceptSubmit;
    private Integer submitNum;
}

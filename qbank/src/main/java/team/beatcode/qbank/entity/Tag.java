package team.beatcode.qbank.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Tag {
    // 这里的名称一定和数据库中的名称一致
    private String tag_name;
    private String tag_description;
    private String tag_color;
}

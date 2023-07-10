package team.beatcode.qbank.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 返回题库首页的题目信息
 * */
@Data
@Getter
@Setter
public class ProblemReturn {
    String id;
    String title;
    List<Tag> tags;
}

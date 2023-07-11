package team.beatcode.qbank.entity;

import lombok.*;

import java.util.List;

/**
 * 返回题库首页的题目信息
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemReturn {
    Integer id;
    String title;
    List<Tag> tags;
    String difficulty;

    public ProblemReturn(Problem problem) {
        this.id = problem.getProblem_id();
        this.title = problem.getTitle();
        this.tags = problem.getTags();
        this.difficulty = problem.getDifficulty();
    }

    public static class DetailReturn extends ProblemReturn {
        String detail;

        public DetailReturn(Problem problem) {
            super(problem);
            this.detail = problem.getDescription();
        }
    }
}

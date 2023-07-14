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
    List<Problem.Tag> tags;
    String difficulty;

    public ProblemReturn(Problem problem) {
        this.id = problem.getTitle().getId();
        this.title = problem.getTitle().getName();
        this.tags = problem.getTags();
        this.difficulty = problem.getDifficulty();
    }

    // 不加Getter注解会害了你
    @Getter
    @Setter
    public static class Detail extends ProblemReturn {
        String detail;
        Integer time_limit;
        Integer memory_limit;
        Integer version;

        public Detail(Problem problem) {
            super(problem);
            this.detail = problem.getDetail();
            this.time_limit = problem.getConfig().getTLimit();
            this.memory_limit = problem.getConfig().getMLimit();
            this.version = problem.getVersion();
        }
    }

    @Data
    @AllArgsConstructor
    public static class Paged {
        List<ProblemReturn> page;
        Integer total;
    }
}

package team.beatcode.qbank.entity;

import lombok.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
    String condition;

    public ProblemReturn(Problem problem,String problem_condition) {
        this.id = problem.getTitle().getId();
        this.title = problem.getTitle().getName();
        this.tags = problem.getTags();
        this.difficulty = problem.getDifficulty();
        //get the regex of single problem
        String regex="<"+this.id.toString()+">([\\s\\S]*)</"+this.id.toString()+">";
        Pattern pattern= Pattern.compile(regex);
        Matcher matcher= pattern.matcher(problem_condition);
        if(matcher.find())
        {
            condition=matcher.group(1);
        }
        else condition="";
    }
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

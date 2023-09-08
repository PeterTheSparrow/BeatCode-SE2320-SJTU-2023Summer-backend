package team.beatcode.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import team.beatcode.user.entity.Problem;

import java.util.List;

@Data
public class User_problem {
    String problemId;
    String problemTitle;
    String problemDifficulty;

    public User_problem(Problem problem) {
        this.problemId = problem.getTitle().getId().toString();
        this.problemTitle = problem.getTitle().getName();
        this.problemDifficulty = problem.getDifficulty();
    }

    @Data
    @AllArgsConstructor
    public static class Paged{
        List<User_problem> problems;
        Integer total;
    }

}

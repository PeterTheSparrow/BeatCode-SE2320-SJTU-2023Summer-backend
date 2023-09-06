package team.beatcode.qbank.service;

import org.springframework.data.domain.Page;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.List;

public interface ProblemService {

    ProblemReturn.Paged getProblemListEx(String titleContains,
                                         String difficulty,
                                         Integer pageIndex,
                                         Integer pageSize,
                                         String problem_condition) throws MessageException;

    ProblemReturn.Detail getProblemDetail(Integer problemId);
    int getProblemVersion(Integer problemId);

    Page<Problem> getUserProblem(List<Integer> ProblemIds, Integer pageIndex, Integer pageSize);
}

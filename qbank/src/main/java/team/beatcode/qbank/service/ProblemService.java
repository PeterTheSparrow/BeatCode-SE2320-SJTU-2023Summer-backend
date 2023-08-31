package team.beatcode.qbank.service;

import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.utils.msg.MessageException;

public interface ProblemService {

    ProblemReturn.Paged getProblemListEx(String titleContains,
                                         String difficulty,
                                         Integer pageIndex,
                                         Integer pageSize) throws MessageException;

    ProblemReturn.Detail getProblemDetail(Integer problemId);
    int getProblemVersion(Integer problemId);
}

package team.beatcode.qbank.service;

import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.List;

public interface ProblemService {

    List<ProblemReturn> getProblemListEx(String titleContains,
                                         String difficulty,
                                         Integer pageIndex,
                                         Integer pageSize) throws MessageException;

    ProblemReturn.Detail getProblemDetail(Integer problemId);
}

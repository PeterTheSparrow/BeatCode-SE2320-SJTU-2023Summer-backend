package team.beatcode.qbank.serviceImp;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import team.beatcode.qbank.dao.ProblemDao;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.service.ProblemService;
import team.beatcode.qbank.utils.Macros;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;

@Service
public class ProblemServiceImp implements ProblemService {
    ProblemDao problemDao;

    public ProblemServiceImp(ProblemDao problemDao) {
        this.problemDao = problemDao;
    }

    @Override
    public ProblemReturn.Paged getProblemListEx(String titleContains,
                                                String difficulty,
                                                Integer pageIndex,
                                                Integer pageSize,
                                                String problem_condition)
            throws MessageException {
        if (Macros.correctHardLevel(difficulty)) {
            Page<Problem> problems = problemDao.findByAll(titleContains,
                    difficulty, pageIndex, pageSize);
            if (problems == null)
                throw new MessageException(MessageEnum.SEARCH_PAGE_FAULT);

            return new ProblemReturn.Paged(
                    problems.stream().map(p -> new ProblemReturn(p, problem_condition)).toList(),
                    problems.getTotalPages());
        }
        else
            throw new MessageException(MessageEnum.SEARCH_DIFFICULTY_UNKNOWN);
    }

    @Override
    public ProblemReturn.Detail getProblemDetail(Integer problemId) {
        Problem p = problemDao.findProblemById(problemId);
        if (p == null)
            return null;
        else {
            return new ProblemReturn.Detail(p);
        }
    }
}

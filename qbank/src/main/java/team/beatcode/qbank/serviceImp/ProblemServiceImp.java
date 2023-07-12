package team.beatcode.qbank.serviceImp;

import org.springframework.stereotype.Service;
import team.beatcode.qbank.dao.ProblemDao;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.service.ProblemService;
import team.beatcode.qbank.utils.Macros;
import team.beatcode.qbank.utils.msg.MessageEnum;
import team.beatcode.qbank.utils.msg.MessageException;

import java.util.List;

@Service
public class ProblemServiceImp implements ProblemService {
    ProblemDao problemDao;

    public ProblemServiceImp(ProblemDao problemDao) {
        this.problemDao = problemDao;
    }

    @Override
    public List<ProblemReturn> getProblemListEx(String titleContains,
                                                String difficulty,
                                                Integer pageIndex,
                                                Integer pageSize)
            throws MessageException {
        if (Macros.correctHardLevel(difficulty)) {
            List<Problem> problems = problemDao.findByAll(titleContains,
                    difficulty, pageIndex, pageSize);
            if (problems == null)
                throw new MessageException(MessageEnum.SEARCH_PAGE_FAULT);

            return problems.stream().map(ProblemReturn::new).toList();
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

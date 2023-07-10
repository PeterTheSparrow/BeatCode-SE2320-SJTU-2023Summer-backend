package team.beatcode.qbank.serviceImp;

import org.springframework.stereotype.Service;
import team.beatcode.qbank.dao.ProblemDao;
import team.beatcode.qbank.entity.ProblemReturn;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.service.ProblemService;

import java.util.List;

@Service
public class ProblemServiceImp implements ProblemService {
    ProblemDao problemDao;

    public ProblemServiceImp(ProblemDao problemDao) {
        this.problemDao = problemDao;
    }

    @Override
    public List<ProblemReturn> getProblemList(Integer pageIndex, Integer pageSize, String searchIndex, String searchKeyWord) {
        // 分类讨论，根据关键词的类型，分类，去dao里面找
        // 题目名称（title）、标签（tag）
        // 然后还需要根据分页做一个筛选

        List<Problem> problemList = null;
        List<Problem> problemList2;

        // 1. 如果关键词类型是title
        if (searchIndex.equals("title")) {
            problemList = problemDao.findProblemsByTitleContaining(searchKeyWord);
        }
        // 2. 如果关键词类型是tag
        else if (searchIndex.equals("tag")) {
            problemList = problemDao.findByTagsTag_nameContainingIgnoreCase(searchKeyWord);
        }
        // 3. 根据分页做筛选
        // 筛选出list中序号为：(pageIndex-1)*pageSize ~ pageIndex*pageSize-1 的元素
        // 如果为空，就直接返回
        if (problemList == null) {
            return null;
        }
        if (problemList.size() < pageIndex*pageSize-1) {
            // 如果不够一页，就直接返回
            problemList2 = problemList;
        }
        else {
            problemList2 = problemList.subList((pageIndex-1)*pageSize, pageIndex*pageSize-1);
        }

        List<ProblemReturn> problemReturnList = new java.util.ArrayList<>();

        // 4. 将problemList2转换成problemReturnList
        for (Problem problem : problemList2) {
            ProblemReturn problemReturn = new ProblemReturn();
            problemReturn.setId(problem.getId());
            problemReturn.setTitle(problem.getTitle());
            // 这样出来的tags是空的
            problemReturn.setTags(problem.getTags());


            problemReturnList.add(problemReturn);
        }

        return problemReturnList;
    }
}

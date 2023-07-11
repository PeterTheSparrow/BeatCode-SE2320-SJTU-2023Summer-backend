package team.beatcode.qbank.daoImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import team.beatcode.qbank.entity.Problem;
import team.beatcode.qbank.repository.ProblemRepository;

import java.util.List;

@Repository
public class ProblemDaoImp implements team.beatcode.qbank.dao.ProblemDao {
    ProblemRepository problemRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    public ProblemDaoImp(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }


    /*
    * 根据tag_name模糊搜索适配的题目
    * */
    public List<Problem> findByTagsTag_nameContainingIgnoreCase(String tagName) {
        // Criteria: 用于封装条件
        Criteria criteria = new Criteria();
        // regex: 正则表达式，i: 忽略大小写
        criteria.and("tags.tag_name").regex(tagName, "i");
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Problem.class);
    }

    @Override
    public List<Problem> findProblemsByDifficulty(String difficulty) {
        return problemRepository.findProblemsByDifficultyContaining(difficulty);
    }


    @Override
    public List<Problem> findProblemsByTitleContaining(String title) {
        return problemRepository.findProblemsByTitleContaining(title);
    }
}

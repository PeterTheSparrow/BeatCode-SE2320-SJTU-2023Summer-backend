package team.beatcode.qbank.dao;

import team.beatcode.qbank.entity.Problem;

import java.util.List;

public interface ProblemDao {
    List<Problem> findProblemsByTitleContaining(String title);


    List<Problem> findByTagsTag_nameContainingIgnoreCase(String tagName);
}

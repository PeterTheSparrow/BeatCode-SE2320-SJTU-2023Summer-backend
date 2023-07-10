package team.beatcode.qbank.service;

import team.beatcode.qbank.entity.ProblemReturn;

import java.util.List;

public interface ProblemService {
    List<ProblemReturn> getProblemList(Integer pageIndex, Integer pageSize, String searchIndex, String searchKeyWord);
}

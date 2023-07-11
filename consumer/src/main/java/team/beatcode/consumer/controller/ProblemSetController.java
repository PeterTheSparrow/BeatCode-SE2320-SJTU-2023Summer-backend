package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.beatcode.consumer.feign.ProblemSetFeign;
import team.beatcode.qbank.entity.ProblemReturn;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ProblemSetController {
    @Autowired
    ProblemSetFeign problemSetFeign;

    @RequestMapping("/GetProblemList")
    public List<ProblemReturn> getProblemList(@RequestBody Map<String, Object> map) {
        return problemSetFeign.getProblemList(map);
    }
}

package team.beatcode.judge.service;

import org.springframework.stereotype.Service;

@Service
public interface VersionService {
    /**
     * 向QBank发送请求，检查版本号。如果不是最新，自动更新题目测试用例文件。
     * @param pid 题号
     */
    boolean checkVersion(int pid);
}

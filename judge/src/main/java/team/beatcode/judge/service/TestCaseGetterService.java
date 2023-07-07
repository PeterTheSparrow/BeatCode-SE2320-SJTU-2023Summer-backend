package team.beatcode.judge.service;

import org.springframework.web.client.RestTemplate;

public interface TestCaseGetterService {
    /**
     * 下载新的压缩包
     * @param restTemplate feign干不了这活
     * @param pid 题目的id
     * @param dir 本地保存路径
     * @return 操作是否成功
     */
    boolean downloadTestCasesZip(RestTemplate restTemplate, int pid, String dir);

    /**
     * 下载完的工作
     * @param pid 题目的id
     * @param zd 下载的压缩包路径
     * @param fd 题库的路径
     * @return 操作是否成功
     */
    boolean updateLocalTestCase(int pid, String zd, String fd);
}

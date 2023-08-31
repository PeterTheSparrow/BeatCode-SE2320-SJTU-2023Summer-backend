package team.beatcode.judge.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import team.beatcode.judge.feign.QBankFeign;
import team.beatcode.judge.service.VersionService;
import team.beatcode.judge.utils.Testcase7zTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VersionServiceImpl implements VersionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QBankFeign qBankFeign;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static class FileVersionMap {
        private final Map<String, Integer> versionMap;

        public FileVersionMap() {
            versionMap = new HashMap<>();
        }

        public int getVersion(String filePath) {
            return versionMap.getOrDefault(filePath, -1);
        }

        public void setVersion(String filePath, int version) {
            versionMap.put(filePath, version);
        }
    }
    private FileVersionMap fileVersionMap;

    private final ScheduledExecutorService executorService;
    private static final int executorServiceDelaySecond = 30;

    private static final String jsonFilePath =
            "D:/Test/version.json"
                    .replace("/", File.separator);

    public VersionServiceImpl() {
        loadFileVersionsFromJson();

        executorService = Executors.newSingleThreadScheduledExecutor();
        // 每30秒将json文件更新一次
        executorService.scheduleWithFixedDelay(
                this::saveFileVersionsToJson,
                executorServiceDelaySecond,
                executorServiceDelaySecond,
                TimeUnit.SECONDS);
    }

    private synchronized void loadFileVersionsFromJson() {
        try {
            File jsonFile = new File(jsonFilePath);
            fileVersionMap = jsonFile.exists() ?
                    mapper.readValue(jsonFile, FileVersionMap.class) :
                    new FileVersionMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void saveFileVersionsToJson() {
        try {
            mapper.writeValue(new File(jsonFilePath), fileVersionMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    // 程序关闭前被调用一次，保存json内容
    private synchronized void cleanup() {
        executorService.shutdown();
        saveFileVersionsToJson();
    }
    //**********************************************业务

    /**
     * 本地文件检查更新逻辑
     *
     * @param pid 题号
     * @return 出错时返回false。如果返回true，文件必然是最新的。
     */
    @Override
    public boolean checkVersion(int pid) {
        // check
        Integer localVersion = getLocalVersion(pid);
        Integer remoteVersion = qBankFeign.getVersion(pid);

        if (localVersion >= remoteVersion)
            return true;

        // update version
        setLocalVersion(pid, remoteVersion);

        // update file
        return update(pid);
    }

    //**********************************************内部工具函数

    private Integer getLocalVersion(int pid) {
        return fileVersionMap.getVersion(String.valueOf(pid));
    }

    // todo 改用自定义锁逻辑，对不同的pid加锁
    private synchronized void setLocalVersion(int pid, int version) {
        fileVersionMap.setVersion(String.valueOf(pid), version);
    }

    /**
     * 确实需要更新，获取、解压、覆盖。使用了并发锁。
     *
     * @param pid 题号
     */
    // todo 改用自定义锁逻辑，对不同的pid加锁
    private synchronized boolean update(int pid) {
        // pre-download
        if (!Testcase7zTools.cleanse(pid))
            return false;

        // download - attr
        String request = String.format(
                "http://question-bank/GetTestCase?pid=%d", pid);
        File dest = new File(Testcase7zTools.testcaseDownloadPath(pid));

        // download - get response
        ResponseEntity<Resource> response =
                restTemplate.getForEntity(request, Resource.class);
        Resource resource = response.getBody();

        // download - check
        if (resource == null) {
            System.out.println("Can't get resource. Path: " + request);
            System.out.println("                    Response: " + response);
            return false;
        }

        // download - get & save
        try (InputStream inputStream = resource.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(dest)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // extract & overwrite
        return Testcase7zTools.extract(pid) &&
                Testcase7zTools.cleanse(pid);
    }
}

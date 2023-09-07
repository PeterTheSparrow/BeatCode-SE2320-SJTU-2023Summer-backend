package team.beatcode.judge.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 该类用于与本地版本文件同步信息
 * 设计成了单例的
 */
public class FileVersionMap {

    //**********************************************yml配置

    @Value("${utils.fileVersion.jsonPath}")
    private static String jsonFilePathYml;

    //**********************************************配置

    private static final Map<String, Integer> versionMap;

    private static final ScheduledExecutorService executorService;
    private static final int executorServiceDelaySecond = 120;

    private static final ObjectMapper mapper;

    private static final String jsonFilePath =
            jsonFilePathYml.replace("/", File.separator);

    static {
        mapper = new ObjectMapper();
        versionMap = loadFileVersionsFromJson();

        executorService = Executors.newSingleThreadScheduledExecutor();
        // 每executorServiceDelaySecond秒将json文件更新一次
        // 首次触发时间也是executorServiceDelaySecond
        executorService.scheduleWithFixedDelay(
                FileVersionMap::saveFileVersionsToJson,
                executorServiceDelaySecond,
                executorServiceDelaySecond,
                TimeUnit.SECONDS);
    }

    //**********************************************业务

    public static int getVersion(String filePath) {
        return versionMap.getOrDefault(filePath, -1);
    }

    public static void setVersion(String filePath, int version) {
        versionMap.put(filePath, version);
    }

    //**********************************************内部工具

    private static Map<String, Integer> loadFileVersionsFromJson() {
        try {
            File jsonFile = new File(jsonFilePath);
            if (jsonFile.exists() && jsonFile.length() > 0)
                return mapper.readValue(jsonFile, new TypeReference<>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private static synchronized void saveFileVersionsToJson() {
        try {
            mapper.writeValue(new File(jsonFilePath), versionMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void cleanup() {
        saveFileVersionsToJson();
        executorService.shutdown();
    }
}

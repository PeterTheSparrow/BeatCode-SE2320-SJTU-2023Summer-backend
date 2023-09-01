package team.beatcode.judge.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team.beatcode.judge.feign.QBankFeign;
import team.beatcode.judge.service.VersionService;
import team.beatcode.judge.utils.FileVersionMap;
import team.beatcode.judge.utils.Testcase7zTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QBankFeign qBankFeign;


    //**********************************************业务

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
        return FileVersionMap.getVersion(String.valueOf(pid));
    }

    // todo 改用自定义锁逻辑，对不同的pid加锁
    private synchronized void setLocalVersion(int pid, int version) {
        FileVersionMap.setVersion(String.valueOf(pid), version);
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
        File dest = Testcase7zTools.compressed(pid);
        if (dest == null) {
            System.out.println("Can't create download target " + pid);
            return false;
        }

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

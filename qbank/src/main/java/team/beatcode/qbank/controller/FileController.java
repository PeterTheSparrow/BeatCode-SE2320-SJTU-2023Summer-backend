package team.beatcode.qbank.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import team.beatcode.qbank.utils.Macros;

import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class FileController {
    public ResponseEntity<StreamingResponseBody> getFileFromPath(String path) {
        if (path == null) {
            return ResponseEntity.noContent().build();
        }

        // 使用lambda规定文件的流传输方式
        StreamingResponseBody responseBody = outputStream -> {
            try (FileInputStream fileInputStream = new FileInputStream(path)) {
                fileInputStream.transferTo(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(responseBody);
    }

    /**
     * 通过流传输得到最新的test-case压缩包
     * @param pid 题目的id
     * @return 流
     */
    @RequestMapping("/GetTestCase")
    public ResponseEntity<StreamingResponseBody> getTestCase(@RequestParam int pid) {
        return getFileFromPath(Macros.getTestCaseFilePath(pid));
    }

}

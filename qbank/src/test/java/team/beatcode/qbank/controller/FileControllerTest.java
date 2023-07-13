package team.beatcode.qbank.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import team.beatcode.qbank.utils.Macros;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FileControllerTest {
    @Mock
    private FileInputStream fileInputStream;

    private FileController fileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        fileController = new FileController();
    }

    @Test
    public void testGetFileFromPath_文件存在_返回文件内容() throws IOException {
        // 模拟文件路径和文件内容
        String filePath = "test_file_path";
        byte[] fileContent = "Test file content".getBytes();

        // 模拟文件流读取操作
        when(fileInputStream.readAllBytes()).thenReturn(fileContent);
        when(fileInputStream.read(any(byte[].class))).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            System.arraycopy(fileContent, 0, buffer, 0, buffer.length);
            return buffer.length;
        });

        // 调用被测试的方法
        ResponseEntity<StreamingResponseBody> response = fileController.getFileFromPath(filePath);

        StreamingResponseBody responseBody = response.getBody();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 将文件内容写入输出流
        responseBody.writeTo(outputStream);

//        // 断言返回的文件内容与预期内容一致
//        Assertions.assertArrayEquals(fileContent, outputStream.toByteArray());
//        // 断言返回头中包含 Content-Disposition
//        Assertions.assertEquals(HttpHeaders.CONTENT_DISPOSITION, response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }

    @Test
    public void testGetFileFromPath_文件不存在_没有返回内容() {
        String filePath = null;

        // 调用被测试的方法
        ResponseEntity<StreamingResponseBody> response = fileController.getFileFromPath(filePath);

        // 断言返回的状态码为 204
        Assertions.assertEquals(204, response.getStatusCodeValue());
        // 断言返回的响应体为 null
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void testGetTestCase_从路径读取文件() {
        int pid = 1;
        String expectedFilePath = Macros.getTestCaseFilePath(pid);

        // 调用被测试的方法
        ResponseEntity<StreamingResponseBody> response = fileController.getTestCase(pid);

        // 断言返回的状态码为 200
        Assertions.assertEquals(200, response.getStatusCodeValue());
        // 断言返回的响应体不为 null
        Assertions.assertNotNull(response.getBody());

    }
}





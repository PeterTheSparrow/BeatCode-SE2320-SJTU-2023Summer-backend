package team.beatcode.submission.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import team.beatcode.submission.entity.Submission;
import team.beatcode.submission.service.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.function.Function;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SubmitControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubmissionService submissionService;
    @Test
    public void testSubmit() throws Exception {
        // 模拟SubmissionService的行为
        when(submissionService.saveSubmission(any())).thenReturn("123"); // 假设返回一个Submission的ID

        // 构建一个模拟的POST请求
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("language", "Java");
        requestBody.put("code", "Your Java code here");
        requestBody.put("problem_id", "1");
        requestBody.put("user_id", "123");
        requestBody.put("user_name", "testuser");

        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/Submit")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON);

        // 发送请求并验证响应状态
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        // 验证SubmissionService的方法是否被调用
        verify(submissionService, times(1)).saveSubmission(any());
    }
    @Test
    public void testGetFullSubmission() throws Exception {
        // 模拟SubmissionService的行为
        Submission result=new Submission();
        when(submissionService.getSubmission(any())).thenReturn(result); // 假设返回一个Submission的ID

        // 构建一个模拟的POST请求
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "123");

        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/GetFullSubmission")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON);

        // 发送请求并验证响应状态
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        // 验证SubmissionService的方法是否被调用
        verify(submissionService, times(1)).getSubmission(any());
    }
    @Test
    public void testGetSubmissions() throws Exception {
        // 模拟SubmissionService的行为

        // 模拟从数据库中获取数据
        List<Submission> submissionList = new ArrayList<>() ;

        // 构造 Pageable 对象，包括页码、每页大小等信息
        PageRequest pageRequest = PageRequest.of(0, 10); // 例如，获取第一页，每页10个

        // 构造 Page 对象
        Page<Submission> result = new PageImpl<>(submissionList, pageRequest, 0);

        when(submissionService.getPaginatedSubmissions(any())).thenReturn(result); // 假设返回一个Submission的ID

        // 构建一个模拟的POST请求
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sortDirection", "asc");
        requestBody.put("sortBy", "submission_time");
        requestBody.put("user_name", "");
        requestBody.put("problem_id", "");
        requestBody.put("problem_name", "");
        requestBody.put("page", "1");
        requestBody.put("pageSize", "20");

        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/GetSubmissions")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON);

        // 发送请求并验证响应状态
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        // 验证SubmissionService的方法是否被调用
        verify(submissionService, times(1)).getPaginatedSubmissions(any());
    }
    @Test
    public void testGetProblemSubmissions() throws Exception {
        // 模拟SubmissionService的行为
        // 模拟从数据库中获取数据
        List<Submission> submissionList = new ArrayList<>() ;

        // 构造 Pageable 对象，包括页码、每页大小等信息
        PageRequest pageRequest = PageRequest.of(0, 10); // 例如，获取第一页，每页10个

        // 构造 Page 对象
        Page<Submission> result = new PageImpl<>(submissionList, pageRequest, 0);
        when(submissionService.getPaginatedProblemSubmissions(any())).thenReturn(result); // 假设返回一个Submission的ID

        // 构建一个模拟的POST请求
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("sortDirection", "asc");
        requestBody.put("sortBy", "submission_time");
        requestBody.put("problem_id", "1");
        requestBody.put("state", "Accepted");
        requestBody.put("page", "1");
        requestBody.put("pageSize", "20");

        ObjectMapper objectMapper = new ObjectMapper();
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/GetProblemSubmissions")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON);

        // 发送请求并验证响应状态
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        // 验证SubmissionService的方法是否被调用
        verify(submissionService, times(1)).getPaginatedProblemSubmissions(any());
    }

}

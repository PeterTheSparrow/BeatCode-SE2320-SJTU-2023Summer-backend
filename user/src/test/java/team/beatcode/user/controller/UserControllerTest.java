package team.beatcode.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUser() throws Exception {
        // 模拟数据
        Integer userId = 1;
        User user = new User();
        user.setUserId(userId);
        user.setUserName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");

        // 模拟服务方法调用
        when(userService.getUser(userId)).thenReturn(user);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .content("{\"userId\": 1}")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("1234567890"))
                .andDo(print());
    }

    @Test
    public void testRegister() throws Exception {
        // 模拟数据
        Integer userId = 2;
        String userName = "Jane Smith";
        String email = "jane.smith@example.com";
        String phone = "9876543210";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .content("{\"user_id\": 2, \"name\": \"Jane Smith\", \"email\": \"jane.smith@example.com\", \"phone\": \"9876543210\"}")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());

        // 验证服务方法是否被调用
        verify(userService).register(userId, userName, email, phone);
    }

    @Test
    public void testGetRanks() throws Exception {
        // 模拟数据
        User_record record1 = new User_record();
        record1.setUserId(1);
        record1.setAcceptNum(4);
        record1.setAcceptSubmit(6);
        record1.setSubmitNum(13);
        User_record record2 = new User_record();
        record2.setUserId(2);
        record2.setAcceptNum(2);
        record2.setAcceptSubmit(5);
        record2.setSubmitNum(15);
        List<User_record> userRecords = Arrays.asList(record1, record2);

        // 模拟服务方法调用
        when(userService.getRanks()).thenReturn(userRecords);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/ranks"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].acceptNum").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].acceptSubmit").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].submitNum").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].acceptNum").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].acceptSubmit").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].submitNum").value(15))
                .andDo(print());
    }
}



package team.beatcode.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import team.beatcode.user.entity.User;
import team.beatcode.user.service.UserService;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class  UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // 初始化mockMvc，如果需要的话
    }

    @Test
    public void testGetUser() {
        // 设置测试数据和模拟行为
        Integer userId = 1;
        User user = new User();
        user.setUserId(userId);
        // 根据需要设置其他属性

        when(userService.getUser(userId)).thenReturn(user);

        // 执行对UserController.getUser()的API请求，并使用mockMvc或直接方法调用来验证结果
        // 断言预期结果
    }

    @Test
    public void testRegister() {
        // 设置测试数据和模拟行为
        Integer userId = 1;
        String userName = "John";
        String email = "john@example.com";
        String phone = "1234567890";

        // 执行对UserController.register()的API请求，并使用mockMvc或直接方法调用来验证结果
        // 断言预期行为，例如验证userService.register()是否使用正确的参数进行调用
    }

}

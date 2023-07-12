package team.beatcode.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import team.beatcode.user.dao.UserDao;
import team.beatcode.user.entity.User;
import team.beatcode.user.entity.User_info;
import team.beatcode.user.entity.User_record;
import team.beatcode.user.service.impl.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUser() {
        // 设置测试数据和模拟行为
        Integer userId = 1;
        User_info userInfo = new User_info();
        userInfo.setUserId(userId);
        userInfo.setUserName("John");
        userInfo.setEmail("john@example.com");
        userInfo.setPhone("1234567890");
        // 根据需要设置其他属性

        // 模拟userDao.getUser_info(userId)方法的行为
        when(userDao.getUser_info(userId)).thenReturn(userInfo);

        User_record userRecord = new User_record();
        userRecord.setUserId(userId);
        userRecord.setAcceptNum(32);
        userRecord.setAcceptSubmit(45);
        userRecord.setSubmitNum(76);
        // 根据需要设置其他属性

        when(userDao.getUser_record(userId)).thenReturn(userRecord);

        // 执行对userService.getUser()的方法调用，并验证返回结果是否符合预期
        User user = userService.getUser(userId);

        // 断言预期结果
        assertEquals(userId, user.getUserId());
        // 验证其他属性是否符合预期

        // 验证mock的方法是否被调用
        verify(userDao, times(1)).getUser_info(userId);
        verify(userDao, times(1)).getUser_record(userId);
    }

    @Test
    public void testRegister() {
        // 设置测试数据和模拟行为
        Integer userId = 1;
        String userName = "John";
        String email = "john@example.com";
        String phone = "1234567890";

        // 执行对userService.register()的方法调用

        userService.register(userId, userName, email, phone);

        // 断言验证register方法是否被被调用
        verify(userDao).register(anyInt(), anyString(), anyString(), anyString());
    }

    @Test
    public void testGetRanks() {
        // 设置测试数据和模拟行为
        List<User_record> userRecordList = List.of(new User_record(), new User_record());
        when(userDao.getRecords()).thenReturn(userRecordList);

        // 执行对userService.getRanks()的方法调用
        List<User_record> ranks = userService.getRanks();

        // 断言预期结果
        assertEquals(userRecordList, ranks);

        // 断言验证getRecords方法是否被调用
        verify(userDao).getRecords();
    }

    // 根据需要添加更多的测试方法

}

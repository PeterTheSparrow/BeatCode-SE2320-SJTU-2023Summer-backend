package team.beatcode.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.feign.UserFeign;
import team.beatcode.auth.repository.UserAuthRepository;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.msg.MessageEnum;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthApplicationTests {

    // Log部分主要是集成测试
    @Autowired
    private MockMvc mockMvc;

    // 那边关我Auth毛事，不发真请求
    @MockBean
    UserFeign userFeign;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static UserAuth defaultUser;
    private static final String defaultName = "IDEA";
    private static final String wrongName = "WebStorm";
    private static final String defaultPass = "PyCharm";
    private static final String wrongPass = "CLion";
    private static final int defaultRole = Macros.AUTH_CODE_USER;
    private static final int wrongRole = Macros.AUTH_CODE_ADMIN;

    @Autowired
    UserAuthRepository userAuthRepository;

    @BeforeClass
    public static void initDefault() {
        defaultUser = new UserAuth();
        defaultUser.setName(defaultName);
        defaultUser.setRole(defaultRole);
        defaultUser.setPass(defaultPass);
    }

    @Before
    @Transactional
    @Rollback
    public void init() {
        MockitoAnnotations.openMocks(this);
        // 每次测试前将数据库清空并塑造成需要的样子
        // [粗暴的] preparation
        userAuthRepository.deleteAll();
        userAuthRepository.saveAndFlush(defaultUser);
    }

    public void checkAuth(String token, int role, boolean res) throws Exception {
        Message check = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.get(
                                        role == Macros.AUTH_CODE_USER ? "/CheckUser" : "/CheckAdmin")
                                .header(Macros.TOKEN_NAME, token))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);
        if (res)
            Assert.assertEquals(check.getStatus(), MessageEnum.AUTH_SUCCESS.getStatus());
        else
            Assert.assertNotEquals(check.getStatus(), MessageEnum.AUTH_SUCCESS.getStatus());
    }

    // 强联系的模块不适合再Mock，使用事务回滚复原数据库
    @Test
    @Transactional
    @Rollback
    public void simpleLogin_Check_Logout() throws Exception {
        // Login part
        Message login = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\", \"pass\": \"%s\"}",
                                                defaultName, defaultPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(login.getStatus(), MessageEnum.SUCCESS.getStatus());

        Map<String, Object> loginData = mapper.convertValue(login.getData(),
                TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));

        Assert.assertEquals(((Integer) loginData.get(Macros.IS_ADMIN)).intValue(), defaultRole);

        String token = (String) loginData.get(Macros.TOKEN_NAME);
        Assert.assertNotNull(token);

        // Check part
        checkAuth(token, defaultRole, true);
        checkAuth(token, wrongRole, false);

        // Logout part
        Message check = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.get("/Logout")
                                .header(Macros.TOKEN_NAME, token))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);
        Assert.assertEquals(check.getStatus(), MessageEnum.SUCCESS.getStatus());

        // Check part
        checkAuth(token, defaultRole, false);
        checkAuth(token, wrongRole, false);
    }

    @Test
    @Transactional
    @Rollback
    public void simpleRegister_Check_Logout() throws Exception {
        // Register part
        Message register = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Register")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\", \"pass\": \"%s\"}",
                                                // avoid dup username
                                                wrongName, wrongPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(register.getStatus(), MessageEnum.SUCCESS.getStatus());

        Map<String, Object> registerData = mapper.convertValue(register.getData(),
                TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));
        // can't register an admin
        Assert.assertEquals(((Integer) registerData.get(Macros.IS_ADMIN)).intValue(), Macros.AUTH_CODE_USER);

        String token = (String) registerData.get(Macros.TOKEN_NAME);
        Assert.assertNotNull(token);

        // I've sent it
        Mockito.verify(userFeign).registerUser(Mockito.anyMap());

        // Check part
        checkAuth(token, Macros.AUTH_CODE_USER, true);
        checkAuth(token, Macros.AUTH_CODE_ADMIN, false);

        // Logout part
        Message check = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.get("/Logout")
                                .header(Macros.TOKEN_NAME, token))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);
        Assert.assertEquals(check.getStatus(), MessageEnum.SUCCESS.getStatus());

        // Check part
        checkAuth(token, Macros.AUTH_CODE_USER, false);
        checkAuth(token, Macros.AUTH_CODE_ADMIN, false);
    }

    @Test
    @Transactional
    @Rollback
    public void login_passwordFailure() throws Exception {
        Message login = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\", \"pass\": \"%s\"}",
                                                // give a wrong password
                                                defaultName, wrongPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(login.getStatus(), MessageEnum.USER_BAD_PASS_FAIL.getStatus());
        Assert.assertEquals(login.getData(), "");
    }

    @Test
    @Transactional
    @Rollback
    public void login_noUserFailure() throws Exception {
        Message login = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\", \"pass\": \"%s\"}",
                                                // give a wrong name
                                                wrongName, defaultPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(login.getStatus(), MessageEnum.USER_NOT_FOUND_FAULT.getStatus());
        Assert.assertEquals(login.getData(), "");
    }

    @Test
    @Transactional
    @Rollback
    public void login_paramLackFailure() throws Exception {
        Message login = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        // don't give a password
                                        .content(String.format("{\"name\": \"%s\", \"pussy\": \"%s\"}",
                                                defaultName, defaultPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(login.getStatus(), MessageEnum.PARAM_FAIL.getStatus());
        Assert.assertEquals(login.getData(), "");
    }

    @Test
    @Transactional
    @Rollback
    public void logout_noTokenFailure() throws Exception {
        // no need to Login first
        Message check = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.get("/Logout")
                                // Hide token
                                /* .header(Macros.TOKEN_NAME, token) */)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);
        Assert.assertEquals(check.getStatus(), MessageEnum.TOKEN_FAULT.getStatus());
    }


    @Test
    @Transactional
    @Rollback
    public void logout_badTokenFailure() throws Exception {
        // need login first
        Message login = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\", \"pass\": \"%s\"}",
                                                defaultName, defaultPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);
        Map<String, Object> loginData = mapper.convertValue(login.getData(),
                TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));
        String token = (String) loginData.get(Macros.TOKEN_NAME);

        Message check = mapper.readValue(
                mockMvc.perform(MockMvcRequestBuilders.get("/Logout")
                                .header(Macros.TOKEN_NAME,
                                        "HaHaHa我是傻逼！" + token))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);
        Assert.assertEquals(check.getStatus(), MessageEnum.TOKEN_FAULT.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    public void register_userExistFailure() throws Exception {
        Message register = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Register")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\", \"pass\": \"%s\"}",
                                                defaultName, defaultPass)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(register.getStatus(), MessageEnum.USER_EXIST_FAULT.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    public void register_paramLackFailure() throws Exception {
        Message register = mapper.readValue(
                mockMvc.perform(
                                MockMvcRequestBuilders.post("/Register")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(String.format("{\"name\": \"%s\"}",
                                                defaultName)))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                Message.class);

        Assert.assertEquals(register.getStatus(), MessageEnum.PARAM_FAIL.getStatus());
    }
}

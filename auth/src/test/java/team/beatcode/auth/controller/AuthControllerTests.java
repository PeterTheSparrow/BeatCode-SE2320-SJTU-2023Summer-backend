package team.beatcode.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sjtu.reins.web.utils.Message;
import team.beatcode.auth.dao.TokenAuthDao;
import team.beatcode.auth.dao.UserAuthDao;
import team.beatcode.auth.entity.TokenAuth;
import team.beatcode.auth.entity.UserAuth;
import team.beatcode.auth.utils.Macros;
import team.beatcode.auth.utils.UUIDUtils;
import team.beatcode.auth.utils.msg.MessageEnum;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {
    /**
     * 使用@Mock创建假对象<br/>
     * 使用@InjectMocks将Mock对象装配进去，就不需要使用@MockBean<br/>
     * 因为controller被隐式装配了就用了@MockBean
     */
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TokenAuthDao tokenAuthDao;
    @MockBean
    private UserAuthDao userAuthDao;

    private static final ObjectMapper mapper = new ObjectMapper();

    private final long now = System.currentTimeMillis();

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    private Message run(String tokenStr, int user, long ll, long lf,
                        String name, int role, String pass,
                        String url) throws Exception {
        byte[] tokenBys = UUIDUtils.StringToBytes(tokenStr);

        if (tokenBys != null) {
            TokenAuth ipAuth = new TokenAuth();
            ipAuth.setUserId(user);
            ipAuth.setToken(tokenBys);
            ipAuth.setLastLogin(ll);
            ipAuth.setLastFresh(lf);
            Mockito.when(tokenAuthDao.getByToken(tokenStr)).thenReturn(ipAuth);

            if (name != null) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(user);
                userAuth.setName(name);
                userAuth.setRole(role);
                userAuth.setPass(pass);
                Mockito.when(userAuthDao.getUserAuthById(user)).thenReturn(userAuth);
            }
            else
                Mockito.when(userAuthDao.getUserAuthById(user)).thenReturn(null);
        } else {
            Mockito.when(tokenAuthDao.getByToken(tokenStr)).thenReturn(null);
            Mockito.when(userAuthDao.getUserAuthById(user))
                    .thenThrow(new RuntimeException("You shouldn't get this..."));
        }
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url);
        String result = mockMvc.perform(tokenStr == null ?
                        builder : builder.header(Macros.TOKEN_NAME, tokenStr))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        return mapper.readValue(result, Message.class);
    }

    @Test
    public void testCheckAuth_genericSuccess() throws Exception {
        String tokenStr = "Ylk8M0AMSkC+U3d9D5FaIg==";
        int user = 18;
        long ll = now - 10000;
        long lf = now - 1000;
        String name = "TestUser";
        int role = Macros.AUTH_CODE_USER;
        String pass = "PasswordA";

        Message message = run(tokenStr, user, ll, lf, name, role, pass,
                "/CheckUser");

        Mockito.verify(tokenAuthDao).getByToken(Mockito.anyString());
        Mockito.verify(userAuthDao).getUserAuthById(Mockito.anyInt());

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_SUCCESS.getStatus());
        Object data = message.getData();
        Map<String, Object> map = mapper.convertValue(data,
                TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));
        System.out.println(map);
        Assert.assertEquals(name, map.get(Macros.USER_CONTEXT_NAME));
        Assert.assertEquals(role, map.get(Macros.USER_CONTEXT_ROLE));
        Assert.assertEquals(user, map.get(Macros.USER_CONTEXT_ID));
    }

    @Test
    public void testCheckAuth_badTokenFailure() throws Exception {
        String tokenStr = "Ylk8M0AMSkC+U3d9D5FaIg=";
        int user = 1;
        long ll = now - 20000;
        long lf = now - 1000;
        String name = "TestUser乙";
        int role = Macros.AUTH_CODE_USER;
        String pass = "PasswordB";

        Message message = run(tokenStr, user, ll, lf, name, role, pass,
                "/CheckUser");

        Mockito.verify(tokenAuthDao).getByToken(Mockito.anyString());
        Mockito.verifyNoInteractions(userAuthDao);

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_FAIL.getStatus());
        Assert.assertEquals(message.getData(), "");
    }

    @Test
    public void testCheckAuth_wrongRoleFailure() throws Exception {
        String tokenStr = "6z9hupEWREqa43b4ng4hcQ==";
        int user = 123456;
        long ll = now - 4000;
        long lf = now - 1000;
        String name = "TestUser丙";
        int role = Macros.AUTH_CODE_USER;
        String pass = "PasswordC";

        Message message = run(tokenStr, user, ll, lf, name, role, pass,
                "/CheckAdmin");

        Mockito.verify(tokenAuthDao).getByToken(Mockito.anyString());
        Mockito.verify(userAuthDao).getUserAuthById(Mockito.anyInt());

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_FAIL.getStatus());
        Assert.assertEquals(message.getData(), "");
    }

    @Test
    public void testCheckAuth_noTokenError() throws Exception {
        int user = 11;
        long ll = now - 1000;
        long lf = now - 100;
        String name = "TestUser丁";
        int role = Macros.AUTH_CODE_USER;
        String pass = "PasswordD";

        Message message = run(null, user, ll, lf, name, role, pass,
                "/CheckUser");

        Mockito.verifyNoInteractions(tokenAuthDao);
        Mockito.verifyNoInteractions(userAuthDao);

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_ERROR.getStatus());
        Assert.assertEquals(message.getData(), "");
    }

    @Test
    public void testCheckAuth_userAbsentError() throws Exception {
        String tokenStr = "6z9hupEWREqa43b4ng4hcQ==";
        int user = 100;
        long ll = now - 1000;
        long lf = now - 10;
        int role = Macros.AUTH_CODE_USER;

        Message message = run(tokenStr, user, ll, lf, null, role, null,
                "/CheckUser");

        Mockito.verify(tokenAuthDao).getByToken(Mockito.anyString());
        Mockito.verify(userAuthDao).getUserAuthById(Mockito.anyInt());

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_ERROR.getStatus());
        Assert.assertEquals(message.getData(), "");
    }

    @Test
    public void testCheckAuth_plusOneYearFailure() throws Exception {
        String tokenStr = "6z9hupEWREqa43b4ng4hcQ==";
        int user = 100;
        long ll = now - Macros.AUTH_MAX_REFRESH - 100;
        long lf = now - 10;
        String name = "TestUser戊";
        int role = Macros.AUTH_CODE_ADMIN;
        String pass = "23456789";

        Message message = run(tokenStr, user, ll, lf, name, role, pass,
                "/CheckAdmin");

        Mockito.verify(tokenAuthDao).getByToken(Mockito.anyString());
        Mockito.verifyNoInteractions(userAuthDao);

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_FAIL.getStatus());
        Assert.assertEquals(message.getData(), "");
    }

    @Test
    public void testCheckAuth_shelveFailure() throws Exception {
        String tokenStr = "6z9hupEWREqa43b4ng4hcQ==";
        int user = 100;
        long ll = now - Macros.AUTH_MAX_REFRESH - 1;
        long lf = now - Macros.AUTH_MAX_REFRESH - 1;
        String name = "TestUser己";
        int role = Macros.AUTH_CODE_ADMIN;
        String pass = "0987654";

        Message message = run(tokenStr, user, ll, lf, name, role, pass,
                "/CheckAdmin");

        Mockito.verify(tokenAuthDao).getByToken(Mockito.anyString());
        Mockito.verifyNoInteractions(userAuthDao);

        Assert.assertEquals(message.getStatus(), MessageEnum.AUTH_FAIL.getStatus());
        Assert.assertEquals(message.getData(), "");
    }
}

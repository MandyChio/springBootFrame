package cn.zmd.frame;

import cn.zmd.frame.controller.ExampleController;
import cn.zmd.frame.entity.User;
import cn.zmd.frame.repository.UserRepository;
import cn.zmd.frame.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;

/**
 * mock的好处
 * 1.可以在测试远程调用未完成的接口
 * 2.mockbean注解哪个类，MockMvcBuilders创建哪个类就在容器内初始化，不需要启动整个容器
 * 3.可以使用注解WebMvcTest(ExampleController.class) 直接进行初始化
 * Created by zmd on 2018/5/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MockTest {
    MockMvc mockMvc ;
    //Before 在Test之前执行，用于实例化mock
    @Before
    public void init(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ExampleController()).build();
    }

    @Test
    public void test1() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/home/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Autowired
    UserService userService ;

    @MockBean
    UserRepository userRepository ;

    @Test
    public void test2(){
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(new User()));
        User u = userService.getUserById(1);
        System.out.println(u.basicInfo().toString());
    }

}

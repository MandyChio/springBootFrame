package cn.cljd.example;

import cn.zmd.frame.entity.User;
import cn.zmd.frame.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Created by zmd on 2018/5/7.
 */
//RunWith必须有，不然无法启动
@RunWith(SpringRunner.class)
//SpringBootTest会按照默认的配置文件启动容器，如果有多个环境，可以指定配置文件启动
@SpringBootTest
public class ExampleTest {

    @Autowired
    UserService userService ;

    @Test
    public void test1(){
        User u = userService.getUserById(1);
        System.out.println(u.basicInfo().toString());
    }

}

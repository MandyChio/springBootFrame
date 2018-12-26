package cn.zmd.frame.controller;

import cn.zmd.frame.entity.User;
import cn.zmd.frame.repository.UserRepository;
import cn.zmd.frame.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class ExampleController {

    private final Logger logger = LoggerFactory.getLogger(ExampleController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/user")
    public User testGetUser(int uid){
        return userService.getUserById(uid);
    }

    @RequestMapping("/identity")
    public User testByIdentity(int identity){
        return userRepository.findByUserIdentity(identity);
    }

    @RequestMapping("/hello")
    public String testHello(){
        return "hello springboot";
    }
}

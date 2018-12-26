package cn.zmd.frame.service.user;


import cn.zmd.frame.entity.User;
import cn.zmd.frame.repository.UserRepository;
import cn.zmd.frame.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserService extends BaseService {

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private RedisTemplate<String, Object>  redisTemplate;


    @Autowired
    UserRepository userRepository;

    /**
     * 手动使用redis缓存
     * @param userId
     * @return
     */
    public User getUserById(int userId){
        testRedis();

        String key = "hy_test_user_" + userId;
        User user = (User)redisTemplate.opsForValue().get(key);
        if (user == null) {
            user = userRepository.findById(userId).orElse(null);
            if (user != null){
                redisTemplate.opsForValue().set(key, user, 600, TimeUnit.SECONDS);
            }
        }

        return user;
    }


    public void testRedis(){
        redisTemplate.opsForValue().set("hy_test_redis", "hello wolrd", 600, TimeUnit.SECONDS);

        Object value = redisTemplate.opsForValue().get("hy_test_redis");

        logger.info("value in redis is:{}", value);
    }
}

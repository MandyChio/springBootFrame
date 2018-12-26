package cn.zmd.frame.repository;

import cn.zmd.frame.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * JPA参考： https://docs.spring.io/spring-data/jpa/docs/2.0.6.RELEASE/reference/html/#jpa.query-methods<br/>
 * Cache参考：https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cache
 */
@Repository
@Transactional
@CacheConfig(cacheNames = "user")
public interface UserRepository extends JpaRepository<User, Integer> {

    @Cacheable(key = "'username' + #a0")
    List<User> findByName(String name);

    @Cacheable(key = "'userIdentity'+#a0")
    User findByUserIdentity(int identity);


    @CacheEvict(key = "'userIdentity'+#a0")
    @Modifying
    @Query("update User u set u.name = ?2 where u.id = ?1")
    int updateUserName(int id, String name);

}

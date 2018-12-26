package cn.zmd.frame.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import javax.persistence.*;

/**
 * entity参考：@Entity @Table @Id @GeneratedValue @Column @Transient
 * lombok参考：@Data @NoArgsConstructor https://projectlombok.org/features/all
 */
@Entity
@Table(name="user")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String name;

    private String avatar;

    @Column(name = "user_identity")
    private int userIdentity;

    //标记为@Transient 的字段不会被存储
    @Transient
    private String desc;

    public JSONObject basicInfo(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("avatar", avatar);
        jsonObject.put("identity", userIdentity);

        return jsonObject;
    }

    public boolean isNormalUser(){
        return false;
    }
}

package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.User;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 获取所有用户
    @Select("SELECT * FROM users")
    List<User> getAllUsers();

    // 使用id获取用户详情
    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(int id);

    //使用邮箱获得用户ID
    @Select("SELECT id FROM users where email = #{email};")
    Integer getIDByEmail(String email);

    //使用电话号码获得用户ID
    @Select("SELECT id FROM users where phone_number = #{phoneNumber};")
    Integer getIDByPhoneNumber(String phoneNumber);

    //使用电话号码获得用户ID
    @Select("SELECT id FROM users where name = #{name};")
    Integer getIDByName(String name);

   //获得指定用户(限定比较指标，比较参数，排序指标，排序方向，数目和偏移量)
    //#{}: 参数可被自动转义，可防止SQL注入
    //用于替换SQL中的值（如 WHERE column = ?）
    //${}: 参数值会直接拼接到SQL中，有SQL注入风险
    //用于直接替换SQL片段（如列名、表名、排序方向等）
    @Select("SELECT * FROM users WHERE ${compareIndex} = #{compareParam}\n" +
            "ORDER BY ${order} ${direction}\n" +
            "LIMIT #{offset}, #{limit};")
    List<User> getUsersByAllParam(String compareIndex, String compareParam, String order,String direction, int limit, int offset);


    // 添加用户
    @Insert("INSERT INTO users (username, email, phoneNumber, password, name, role, createdAt, lastActivityAt, status) VALUES (#{username}, #{email}, #{phoneNumber}, #{password}, #{name}, #{role}, #{createdAt}, #{lastActivityAt}, #{status})")
    int addUser(User user);

    // 更新用户信息
    @Update("UPDATE users SET username = #{username}, email = #{email}, phoneNumber = #{phoneNumber}, password = #{password}, name = #{name}, role = #{role}, createdAt = #{createdAt}, lastActivityAt = #{lastActivityAt}, status = #{status} WHERE id = #{id}")
    int updateUser(User user);

    //更新用户状态
    @Update("UPDATE users SET status = #{status} where userID = #{userID}")
    void updateUserStatus(int userID, String status);

    //更新用户角色
    @Update("UPDATE users SET role = #{role} where userID = #{userID}")
    void updateUserRole(int userID, String role);

    // 删除用户
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteUser(int id);

    @Select("SELECT id FROM users")
    List<Long> findAllUserIds();

    @Select("SELECT course_id FROM course_user WHERE user_id = #{userId}")
    List<Integer> getCourseIdsByUserId(int userId);
}

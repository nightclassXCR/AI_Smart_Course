package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 获取所有用户
    @Select("SELECT * FROM user")
    List<User> getAllUsers();

    // 获取用户详情
    @Select("SELECT * FROM user WHERE id = #{id}")
    User getUserById(int id);

    // 添加用户
    @Insert("INSERT INTO user (username, email, phoneNumber, password, name, role, createdAt, lastActivityAt, status) VALUES (#{username}, #{email}, #{phoneNumber}, #{password}, #{name}, #{role}, #{createdAt}, #{lastActivityAt}, #{status})")
    int addUser(User user);

    // 更新用户信息
    @Update("UPDATE user SET username = #{username}, email = #{email}, phoneNumber = #{phoneNumber}, password = #{password}, name = #{name}, role = #{role}, createdAt = #{createdAt}, lastActivityAt = #{lastActivityAt}, status = #{status} WHERE id = #{id}")
    int updateUser(User user);

    // 删除用户
    @Delete("DELETE FROM user WHERE id = #{id}")
    int deleteUser(int id);
}

package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.base.UserService;
import com.dd.ai_smart_course.service.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 每个测试方法运行在独立的事务中，测试后自动回滚，不污染数据库
@DisplayName("UserService 集成测试")
public class UserImplIntegrationTest {

    @Autowired
    private UserService userService;

    /**
     * 创建一个唯一的测试用户对象，避免因唯一性约束导致测试失败。
     * 使用 UUID 来确保每次测试运行时的用户名、邮箱等都是唯一的。
     * @param baseName 基础名称，用于区分测试场景
     * @return User
     */
    private User createUniqueTestUser(String baseName) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        User user = new User();
        user.setUsername(baseName + "_" + uniqueSuffix);
        user.setName(baseName + "_" + uniqueSuffix); // 假设 User 实体有 name 字段
        user.setEmail(baseName + "_" + uniqueSuffix + "@example.com");
        user.setPhoneNumber("138" + System.currentTimeMillis() % 100000000); // 简单生成唯一手机号
        user.setPassword("defaultPassword123");
        user.setStatus("NORMAL");
        user.setRole("ROLE_STUDENT"); // 使用一个合法的角色
        return user;
    }


    @Test
    @DisplayName("添加用户时，因角色不合法而失败")
    public void testAddUser_FailWithInvalidRole() {
        // Arrange
        User user = createUniqueTestUser("invalidRoleUser");
        user.setRole("INVALID_ROLE"); // 设置一个非法的角色

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            userService.addUser(user);
        }, "角色不合法时，应抛出业务异常");
    }



    @Test
    @DisplayName("更新一个不存在的用户时失败")
    public void testUpdateUser_FailWhenUserNotExists() {
        // Arrange
        User nonExistentUser = createUniqueTestUser("nonExistentUser");
        nonExistentUser.setId(9999999); // 一个几乎不可能存在的ID

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            userService.updateUser(nonExistentUser);
        }, "更新不存在的用户时，应抛出业务异常");
    }

}

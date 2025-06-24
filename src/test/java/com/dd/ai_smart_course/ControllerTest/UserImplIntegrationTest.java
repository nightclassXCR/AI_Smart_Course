package com.dd.ai_smart_course.ControllerTest;

import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.service.impl.UserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserImplIntegrationTest {

    @Autowired
    private UserImpl userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        // 插入一个测试用户，避免重复添加
        List<User> existing = userService.getUsersByUsername("test_user_001", true, "created_at", 10, 0);
        if (existing == null || existing.isEmpty()) {
            User user = new User();
            user.setUsername("test_user_001");
            user.setEmail("test001@example.com");
            user.setPhoneNumber("12345678901");
            user.setStatus("NORMAL");
            user.setRole("ROLE_USER");

            int result = userService.addUser(user);
            assertEquals(1, result, "应成功插入测试用户");
        }
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() > 0, "用户列表不应为空");
    }

    @Test
    public void testGetUsersByStatus_Valid() {
        List<User> users = userService.getUsersByStatus("NORMAL", true, "created_at", 10, 0);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testGetUsersByStatus_InvalidStatus() {
        List<User> users = userService.getUsersByStatus("INVALID_STATUS", true, "created_at", 10, 0);
        assertNull(users);
    }

    @Test
    public void testGetUsersByRole_Valid() {
        List<User> users = userService.getUsersByRole("ROLE_USER", false, "created_at", 10, 0);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testGetUsersByRole_InvalidRole() {
        List<User> users = userService.getUsersByRole("ROLE_INVALID", true, "created_at", 10, 0);
        assertNull(users);
    }

    @Test
    public void testUpdateUser() {
        List<User> users = userService.getUsersByUsername("test_user_001", true, "created_at", 10, 0);
        assertFalse(users.isEmpty());

        User user = users.get(0);
        user.setEmail("updated001@example.com");

        int result = userService.updateUser(user);
        assertEquals(1, result);

        User updated = userService.getUserById(user.getId());
        assertEquals("updated001@example.com", updated.getEmail());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("delete_user_001");
        user.setEmail("delete@example.com");
        user.setPhoneNumber("10000000000");
        user.setStatus("NORMAL");
        user.setRole("ROLE_USER");

        int result = userService.addUser(user);
        assertEquals(1, result);

        List<User> inserted = userService.getUsersByUsername("delete_user_001", true, "created_at", 10, 0);
        assertFalse(inserted.isEmpty());
        int id = inserted.get(0).getId();

        int deleted = userService.deleteUser(id);
        assertEquals(1, deleted);

        User deletedUser = userService.getUserById(id);
        assertNull(deletedUser);
    }
}

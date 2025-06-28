package com.dd.ai_smart_course.service.impl;

import com.dd.ai_smart_course.entity.User;
import com.dd.ai_smart_course.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserImpl userService;

    private User testUser;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setPassword("password123");
        testUser.setName("Test User");
        testUser.setRole("ROLE_USER");
        testUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        testUser.setLastActivityAt(new Timestamp(System.currentTimeMillis()));
        testUser.setStatus("NORMAL");

        User testUser2 = new User();
        testUser2.setId(2);
        testUser2.setUsername("admin");
        testUser2.setEmail("admin@example.com");
        testUser2.setRole("ROLE_ADMIN");
        testUser2.setStatus("NORMAL");

        testUsers = Arrays.asList(testUser, testUser2);
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        when(userMapper.getAllUsers()).thenReturn(testUsers);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUsers, result);
        verify(userMapper, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        int userId = 1;
        when(userMapper.getUserById(userId)).thenReturn(testUser);

        // When
        User result = userService.getUserById(userId);

        // Then
        assertNotNull(result);
        assertEquals(testUser, result);
        assertEquals(userId, result.getId());
        verify(userMapper, times(1)).getUserById(userId);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserNotExists() {
        // Given
        int userId = 999;
        when(userMapper.getUserById(userId)).thenReturn(null);

        // When
        User result = userService.getUserById(userId);

        // Then
        assertNull(result);
        verify(userMapper, times(1)).getUserById(userId);
    }

    @Test
    void getUsersByStatus_ShouldReturnUsers_WhenValidStatusAndOrder() {
        // Given
        String status = "NORMAL";
        boolean isDESC = true;
        String order = "username";
        int limit = 10;
        int offset = 0;

        when(userMapper.getUsersByAllParam("status", status, order, "DESC", limit, offset))
                .thenReturn(testUsers);

        // When
        List<User> result = userService.getUsersByStatus(status, isDESC, order, limit, offset);

        // Then
        assertNotNull(result);
        assertEquals(testUsers, result);
        verify(userMapper, times(1)).getUsersByAllParam("status", status, order, "DESC", limit, offset);
    }

    @Test
    void getUsersByStatus_ShouldReturnUsers_WhenAscendingOrder() {
        // Given
        String status = "NORMAL";
        boolean isDESC = false;
        String order = "created_at";
        int limit = 5;
        int offset = 0;

        when(userMapper.getUsersByAllParam("status", status, order, "ASC", limit, offset))
                .thenReturn(testUsers);

        // When
        List<User> result = userService.getUsersByStatus(status, isDESC, order, limit, offset);

        // Then
        assertNotNull(result);
        assertEquals(testUsers, result);
        verify(userMapper, times(1)).getUsersByAllParam("status", status, order, "ASC", limit, offset);
    }

    @Test
    void getUsersByStatus_ShouldReturnNull_WhenInvalidStatus() {
        // Given
        String invalidStatus = "INVALID_STATUS";
        String order = "username";

        // When
        List<User> result = userService.getUsersByStatus(invalidStatus, true, order, 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getUsersByStatus_ShouldReturnNull_WhenInvalidOrder() {
        // Given
        String status = "NORMAL";
        String invalidOrder = "invalid_column";

        // When
        List<User> result = userService.getUsersByStatus(status, true, invalidOrder, 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getUsersByUsername_ShouldReturnUsers_WhenValidUsernameAndOrder() {
        // Given
        String username = "testuser";
        boolean isDESC = true;
        String order = "created_at";
        int limit = 10;
        int offset = 0;

        when(userMapper.getUsersByAllParam("username", username, order, "DESC", limit, offset))
                .thenReturn(Arrays.asList(testUser));

        // When
        List<User> result = userService.getUsersByUsername(username, isDESC, order, limit, offset);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userMapper, times(1)).getUsersByAllParam("username", username, order, "DESC", limit, offset);
    }

    @Test
    void getUsersByUsername_ShouldReturnUsers_WhenAscendingOrder() {
        // Given
        String username = "testuser";
        boolean isDESC = false;
        String order = "email";
        int limit = 10;
        int offset = 0;

        when(userMapper.getUsersByAllParam("username", username, order, "ASC", limit, offset))
                .thenReturn(Arrays.asList(testUser));

        // When
        List<User> result = userService.getUsersByUsername(username, isDESC, order, limit, offset);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userMapper, times(1)).getUsersByAllParam("username", username, order, "ASC", limit, offset);
    }

    @Test
    void getUsersByUsername_ShouldReturnNull_WhenInvalidOrder() {
        // Given
        String username = "testuser";
        String invalidOrder = "invalid_column";

        // When
        List<User> result = userService.getUsersByUsername(username, true, invalidOrder, 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getUsersByRole_ShouldReturnUsers_WhenValidRoleAndOrder() {
        // Given
        String role = "ROLE_USER";
        boolean isDESC = true;
        String order = "last_activity_at";
        int limit = 10;
        int offset = 0;

        when(userMapper.getUsersByAllParam("role", role, order, "DESC", limit, offset))
                .thenReturn(Arrays.asList(testUser));

        // When
        List<User> result = userService.getUsersByRole(role, isDESC, order, limit, offset);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userMapper, times(1)).getUsersByAllParam("role", role, order, "DESC", limit, offset);
    }

    @Test
    void getUsersByRole_ShouldReturnUsers_WhenAscendingOrder() {
        // Given
        String role = "ROLE_ADMIN";
        boolean isDESC = false;
        String order = "phone_number";
        int limit = 5;
        int offset = 0;

        when(userMapper.getUsersByAllParam("role", role, order, "ASC", limit, offset))
                .thenReturn(testUsers);

        // When
        List<User> result = userService.getUsersByRole(role, isDESC, order, limit, offset);

        // Then
        assertNotNull(result);
        assertEquals(testUsers, result);
        verify(userMapper, times(1)).getUsersByAllParam("role", role, order, "ASC", limit, offset);
    }

    @Test
    void getUsersByRole_ShouldReturnNull_WhenInvalidRole() {
        // Given
        String invalidRole = "INVALID_ROLE";
        String order = "username";

        // When
        List<User> result = userService.getUsersByRole(invalidRole, true, order, 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getUsersByRole_ShouldReturnNull_WhenInvalidOrder() {
        // Given
        String role = "ROLE_USER";
        String invalidOrder = "invalid_column";

        // When
        List<User> result = userService.getUsersByRole(role, true, invalidOrder, 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void addUser_ShouldReturnPositiveNumber_WhenUserAddedSuccessfully() {
        // Given
        when(userMapper.addUser(testUser)).thenReturn(1);

        // When
        int result = userService.addUser(testUser);

        // Then
        assertEquals(1, result);
        verify(userMapper, times(1)).addUser(testUser);
    }

    @Test
    void addUser_ShouldReturnZero_WhenUserAddFailed() {
        // Given
        when(userMapper.addUser(testUser)).thenReturn(0);

        // When
        int result = userService.addUser(testUser);

        // Then
        assertEquals(0, result);
        verify(userMapper, times(1)).addUser(testUser);
    }

    @Test
    void updateUser_ShouldReturnPositiveNumber_WhenUserUpdatedSuccessfully() {
        // Given
        when(userMapper.updateUser(testUser)).thenReturn(1);

        // When
        int result = userService.updateUser(testUser);

        // Then
        assertEquals(1, result);
        verify(userMapper, times(1)).updateUser(testUser);
    }

    @Test
    void updateUser_ShouldReturnZero_WhenUserUpdateFailed() {
        // Given
        when(userMapper.updateUser(testUser)).thenReturn(0);

        // When
        int result = userService.updateUser(testUser);

        // Then
        assertEquals(0, result);
        verify(userMapper, times(1)).updateUser(testUser);
    }

    @Test
    void deleteUser_ShouldReturnPositiveNumber_WhenUserDeletedSuccessfully() {
        // Given
        int userId = 1;
        when(userMapper.deleteUser(userId)).thenReturn(1);

        // When
        int result = userService.deleteUser(userId);

        // Then
        assertEquals(1, result);
        verify(userMapper, times(1)).deleteUser(userId);
    }

    @Test
    void deleteUser_ShouldReturnZero_WhenUserDeleteFailed() {
        // Given
        int userId = 999;
        when(userMapper.deleteUser(userId)).thenReturn(0);

        // When
        int result = userService.deleteUser(userId);

        // Then
        assertEquals(0, result);
        verify(userMapper, times(1)).deleteUser(userId);
    }

    // 测试边界情况和异常情况
    @Test
    void getUsersByStatus_ShouldHandleNullStatus() {
        // When
        List<User> result = userService.getUsersByStatus(null, true, "username", 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getUsersByUsername_ShouldHandleNullUsername() {
        // When
        List<User> result = userService.getUsersByUsername(null, true, "username", 10, 0);

        // Then
        assertNotNull(result); // 这个方法没有对username进行null检查，所以会调用mapper
        verify(userMapper, times(1)).getUsersByAllParam("username", null, "username", "DESC", 10, 0);
    }

    @Test
    void getUsersByRole_ShouldHandleNullRole() {
        // When
        List<User> result = userService.getUsersByRole(null, true, "username", 10, 0);

        // Then
        assertNull(result);
        verify(userMapper, never()).getUsersByAllParam(anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt());
    }

    // 测试常量验证
    @Test
    void testAllowedUserRoleConstants() {
        assertTrue(UserImpl.ALLOWED_USER_ROLE.contains("ROLE_ADMIN"));
        assertTrue(UserImpl.ALLOWED_USER_ROLE.contains("ROLE_USER"));
        assertEquals(2, UserImpl.ALLOWED_USER_ROLE.size());
    }

    @Test
    void testAllowedUserStatusConstants() {
        assertTrue(UserImpl.ALLOWED_USER_STATUS.contains("NORMAL"));
        assertTrue(UserImpl.ALLOWED_USER_STATUS.contains("BANNED"));
        assertEquals(2, UserImpl.ALLOWED_USER_STATUS.size());
    }

    @Test
    void testAllowedUserColumnNameConstants() {
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("username"));
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("role"));
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("created_at"));
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("last_activity_at"));
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("email"));
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("phone_number"));
        assertTrue(UserImpl.ALLOWED_USER_COLUMN_NAME.contains("status"));
        assertEquals(7, UserImpl.ALLOWED_USER_COLUMN_NAME.size());
    }
}
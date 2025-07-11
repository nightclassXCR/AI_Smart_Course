package com.dd.ai_smart_course.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleCodeManagerTest {

    @Mock
    private MailSender mockMailSender; // 模拟邮箱依赖

    @InjectMocks
    private SimpleCodeManager simpleCodeManager; // Inject mocks into SimpleCodeManager

    private final String SENDER_EMAIL = "your-email@example.com";
    private final String EMAIL_SUBJECT = "验证码";

    @BeforeEach
    void setUp() {
        // Initialize simpleCodeManager. Since it has a constructor with @Autowired,
        // @InjectMocks will attempt to inject mockMailSender into it automatically.
        // We can further customize the templateMessage if needed, but its default
        // initialization in SimpleCodeManager's constructor is fine for most tests.
        // If the 'templateMessage' object needs to be explicitly mocked or inspected
        // before each test, one might consider using a spy or manually setting it.
        // For this case, verifying the 'sent' message's properties is sufficient.
    }

    @Test
    void testSendIdentifyingCode_Success() {
        // Given
        String userEmail = "test@example.com";
        String identifyingCode = "123456";
        String expectedMessageText = "Dear your identifying code is" + identifyingCode;

        // Mock mailSender.send to do nothing as it's a void method
        doNothing().when(mockMailSender).send(any(SimpleMailMessage.class));

        // When
        String result = simpleCodeManager.sendIdentifyingCode(userEmail, identifyingCode);

        // Then
        assertEquals("SENDING_SUCCESS", result);

        // Capture the SimpleMailMessage sent to verify its contents
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertArrayEquals(new String[]{userEmail}, sentMessage.getTo());
        assertEquals(EMAIL_SUBJECT, sentMessage.getSubject());
        assertEquals(expectedMessageText, sentMessage.getText());
        assertEquals(SENDER_EMAIL, sentMessage.getFrom());
    }

    @Test
    void testSendIdentifyingCode_MailSendFail() {
        // Given
        String userEmail = "fail@example.com";
        String identifyingCode = "654321";

        // Mock mailSender.send to throw an exception
        doThrow(new MailException("Simulated mail send failure") {}).when(mockMailSender).send(any(SimpleMailMessage.class));

        // When
        String result = simpleCodeManager.sendIdentifyingCode(userEmail, identifyingCode);

        // Then
        assertEquals("SENDING_FAIL", result);
        verify(mockMailSender, times(1)).send(any(SimpleMailMessage.class)); // Still attempted to send
    }

    @Test
    void testSendIdentifyingCode_InvalidEmailAddress() {
        // Given
        String userEmail = "invalid-email"; // Invalid email format
        String identifyingCode = "789012";

        // When
        String result = simpleCodeManager.sendIdentifyingCode(userEmail, identifyingCode);

        // Then
        assertEquals("EMAIL_ADDRESS_ERROR", result);
        verify(mockMailSender, never()).send(any(SimpleMailMessage.class)); // Mail sender should not be called
    }

    // --- Tests for isValidEmail (static method) ---
    @Test
    void testIsValidEmail_ValidEmails() {
        assertTrue(SimpleCodeManager.isValidEmail("test@example.com"));
        assertTrue(SimpleCodeManager.isValidEmail("user.name@domain.co.uk"));
        assertTrue(SimpleCodeManager.isValidEmail("user123@sub.domain.net"));
        assertTrue(SimpleCodeManager.isValidEmail("firstname.lastname@example.net"));
        assertTrue(SimpleCodeManager.isValidEmail("email@example-domain.com"));
        assertTrue(SimpleCodeManager.isValidEmail("email@domain.travel"));
        assertTrue(SimpleCodeManager.isValidEmail("email@123.123.123.123"));
        assertTrue(SimpleCodeManager.isValidEmail("email@localhost")); // 有效通常在公网中使用
    }

    @Test
    void testIsValidEmail_InvalidEmails() {
        assertFalse(SimpleCodeManager.isValidEmail("invalid-email")); // No @
        assertFalse(SimpleCodeManager.isValidEmail("user@.com")); // Domain starts with dot
        assertFalse(SimpleCodeManager.isValidEmail("@domain.com")); // No local part
        assertFalse(SimpleCodeManager.isValidEmail("user@domain.")); // Domain ends with dot
        assertFalse(SimpleCodeManager.isValidEmail("user@domain..com")); // Double dot
        assertFalse(SimpleCodeManager.isValidEmail("user@domain_name.com")); // Underscore in domain
        assertFalse(SimpleCodeManager.isValidEmail("user@domain com")); // Space in domain
        assertFalse(SimpleCodeManager.isValidEmail("user.domain.com")); // Missing @
        assertFalse(SimpleCodeManager.isValidEmail("user@domain,com")); // Comma
        assertFalse(SimpleCodeManager.isValidEmail("user@.domain.com")); // Domain starts with .main ends with dash
    }

    // --- Tests for setters (optional, but good for full coverage if they can be used externally) ---
    @Test
    void testSetMailSender() {
        // Given
        MailSender newMockMailSender = mock(MailSender.class);
        String userEmail = "test2@example.com";
        String identifyingCode = "987654";

        // When
        simpleCodeManager.setMailSender(newMockMailSender);
        simpleCodeManager.sendIdentifyingCode(userEmail, identifyingCode);

        // Then
        // Verify the original mockMailSender was NOT used
        verify(mockMailSender, never()).send(any(SimpleMailMessage.class));
        // Verify the newMockMailSender WAS used
        verify(newMockMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSetTemplateMessage() {
        // Given
        SimpleMailMessage newTemplate = new SimpleMailMessage();
        newTemplate.setFrom("new-sender@example.com");
        newTemplate.setSubject("New Subject Line");
        newTemplate.setText("New template text: "); // This text will be prefixed by the service logic

        String userEmail = "test3@example.com";
        String identifyingCode = "112233";

        // When
        simpleCodeManager.setTemplateMessage(newTemplate);
        simpleCodeManager.sendIdentifyingCode(userEmail, identifyingCode);

        // Then
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals("New Subject Line", sentMessage.getSubject());
        assertEquals("new-sender@example.com", sentMessage.getFrom());
        // The service prefixes "Dear your identifying code is"
        assertEquals("Dear your identifying code is" + identifyingCode, sentMessage.getText());
        // Note: The `setText` in `sendIdentifyingCode` overrides the template's text.
        // So, this test primarily verifies `setFrom` and `setSubject` from the new template.
    }
}
package unit;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class LoginPageUnitTest {

    @Mock
    private WebDriver mockDriver;

    @Mock
    private WebElement mockNameInput;

    @Mock
    private WebElement mockEmailInput;

    @Mock
    private WebElement mockSignupButton;

    private LoginPage loginPage;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        loginPage = new LoginPage(mockDriver);
        
        Field nameField = LoginPage.class.getDeclaredField("nameInput");
        nameField.setAccessible(true);
        nameField.set(loginPage, mockNameInput);
        
        Field emailField = LoginPage.class.getDeclaredField("emailInput");
        emailField.setAccessible(true);
        emailField.set(loginPage, mockEmailInput);
        
        Field buttonField = LoginPage.class.getDeclaredField("signupButton");
        buttonField.setAccessible(true);
        buttonField.set(loginPage, mockSignupButton);
    }

    @Test
    public void testEnterName() {
        String testName = "John Doe";
        
        loginPage.enterName(testName);
        
        verify(mockNameInput, times(1)).sendKeys(testName);
    }

    @Test
    public void testEnterEmail() {
        String testEmail = "john.doe@example.com";
        
        loginPage.enterEmail(testEmail);
        
        verify(mockEmailInput, times(1)).sendKeys(testEmail);
    }

    @Test
    public void testSignupMethod() {
        String testName = "Jane Smith";
        String testEmail = "jane.smith@example.com";
        
        loginPage.signup(testName, testEmail);
        
        verify(mockNameInput, times(1)).sendKeys(testName);
        verify(mockEmailInput, times(1)).sendKeys(testEmail);
        verify(mockSignupButton, times(1)).click();
    }
}

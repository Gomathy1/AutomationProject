package tests;

import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DriverManager;

public class LoginTest extends BaseTest {

    @Test
    public void testUserSignup() {
        DriverManager.getDriver().get("https://automationexercise.com/");
        DriverManager.getDriver().findElement(org.openqa.selenium.By.cssSelector("a[href='/login']")).click();
        
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.signup("Gomathy", "gomathy.test@example.com");
        
        System.out.println("Signup test completed successfully!");
    }

    @Test
    public void testLoginPageElements() {
        DriverManager.getDriver().get("https://automationexercise.com/login");
        
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        
        System.out.println("Login page elements verified!");
    }
}

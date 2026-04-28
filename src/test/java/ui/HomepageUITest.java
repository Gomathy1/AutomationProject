package ui;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.DriverManager;

public class HomepageUITest extends BaseUITest {

    @Test
    public void testHomepageTitle() {
        DriverManager.getDriver().get("https://automationexercise.com/");
        
        String actualTitle = DriverManager.getDriver().getTitle();
        Assert.assertTrue(actualTitle.contains("Automation Exercise"), 
            "Homepage title should contain 'Automation Exercise'");
        
        System.out.println("Homepage title verified: " + actualTitle);
    }

    @Test
    public void testHomepageLogoDisplayed() {
        DriverManager.getDriver().get("https://automationexercise.com/");
        
        boolean isLogoDisplayed = DriverManager.getDriver()
            .findElement(org.openqa.selenium.By.cssSelector("img[alt='Website for automation practice']"))
            .isDisplayed();
        
        Assert.assertTrue(isLogoDisplayed, "Logo should be displayed on homepage");
        
        System.out.println("Homepage logo is displayed!");
    }
}

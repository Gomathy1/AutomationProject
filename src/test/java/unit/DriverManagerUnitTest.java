package unit;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.DriverManager;

public class DriverManagerUnitTest {

    @AfterMethod
    public void cleanup() {
        DriverManager.quitDriver();
    }

    @Test
    public void testSetDriverChrome() {
        DriverManager.setDriver("chrome");
        WebDriver driver = DriverManager.getDriver();
        
        Assert.assertNotNull(driver, "Chrome driver should be initialized");
    }

    @Test
    public void testQuitDriverCleansUp() {
        DriverManager.setDriver("chrome");
        Assert.assertNotNull(DriverManager.getDriver(), "Driver should exist before quit");
        
        DriverManager.quitDriver();
        
        Assert.assertNull(DriverManager.getDriver(), "Driver should be null after quit");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUnsupportedBrowser() {
        DriverManager.setDriver("safari");
    }
}

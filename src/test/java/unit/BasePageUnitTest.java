package unit;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.BasePage;

import java.lang.reflect.Field;

public class BasePageUnitTest {

    @Mock
    private WebDriver mockDriver;

    private BasePage basePage;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        basePage = new BasePage(mockDriver);
    }

    @Test
    public void testBasePageInitialization() throws Exception {
        Assert.assertNotNull(basePage, "BasePage should be initialized");
        
        Field driverField = BasePage.class.getDeclaredField("driver");
        driverField.setAccessible(true);
        WebDriver driver = (WebDriver) driverField.get(basePage);
        
        Assert.assertNotNull(driver, "Driver should be initialized");
    }

    @Test
    public void testWebDriverWaitInitialization() throws Exception {
        Field waitField = BasePage.class.getDeclaredField("wait");
        waitField.setAccessible(true);
        WebDriverWait wait = (WebDriverWait) waitField.get(basePage);
        
        Assert.assertNotNull(wait, "WebDriverWait should be initialized");
    }
}

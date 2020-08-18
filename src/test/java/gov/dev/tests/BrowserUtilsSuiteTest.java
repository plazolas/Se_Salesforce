package gov.dev.tests;

import gov.va.salesforceTests.framework.BrowserUtils;
import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.globals.Globals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import gov.va.salesforceTests.logger.Logger;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VACOPlazoO
 */
public class BrowserUtilsSuiteTest {

    public static Framework framework = null;
    public static BrowserUtils browserUtils;
    public static Globals globals =  null;
    public static WebDriver driver = null;
    public static Logger logger;

    @BeforeAll
    public static void setup() {
        logger = new Logger();
        logger.log("Begin " + BrowserUtilsSuiteTest.class.getSimpleName());
        framework = Framework.getInstance();
        globals = framework.globals;

        try {
            driver = framework.getInstance().getDriver();
        } catch (Exception setDriverError) {
            logger.log(setDriverError.getMessage());
            logger.log(setDriverError.getStackTrace().toString());
            System.exit(1);
        }

        try {
            browserUtils = new BrowserUtils(globals.implicitWait, globals.polling);
        } catch (Exception setDriverError) {
            logger.log(setDriverError.getMessage());
            logger.log(setDriverError.getStackTrace().toString());
            System.exit(1);
        }
        logger.log("End " + BrowserUtilsSuiteTest.class.getSimpleName());
    }

    @Test
    public void waitForTitleTest() {
        driver.get("https://www.google.com/");
        try {
            BrowserUtils.waitForTitle("Google");
        } catch (Exception eTitle1) {
            logger.log(eTitle1.getMessage());
            logger.log(eTitle1.getStackTrace().toString());
        }
        String title = driver.getTitle();
        assertTrue(title.contains("Google"));
    }

    @Test
    public void waitForUrlTest() {
        String url = "https://www.google.com/";
        driver.get(url);
        BrowserUtils.waitForURL(url);
        String url2 = driver.getCurrentUrl();
        assertEquals(url, url2);
    }

    @Test
    public void findByTagAttributeTextExitingElementTest() {
        driver.get("https://www.google.com");
        WebElement e = browserUtils.findByTagAttributeText("title", "innerHTML", "Google");
        assertTrue(e instanceof WebElement);
        assertEquals("title", e.getTagName());
        assertEquals("Google", e.getAttribute("innerHTML"));
    }

    @Test
    public void findByTagAttributeTextNonExitingElementTest() {
        WebElement e = browserUtils.findByTagAttributeText("body", "class", "bogus");
        assertNull(e);
    }
}
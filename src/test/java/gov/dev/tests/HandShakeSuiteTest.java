package gov.dev.tests;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import gov.va.salesforceTests.logger.Logger;
import gov.va.salesforceTests.classes.User;

import gov.va.salesforceTests.globals.Globals;
import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.framework.HandShake;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author VACOPlazoO
 */
public class HandShakeSuiteTest {

    public static Framework framework = null;
    public static WebDriver driver = null;
    public static Globals globals =  null;
    public static HandShake handshake = null;
    public static Logger logger;

    @BeforeAll
    public static void setup() {
        logger = new Logger();
        logger.log("Begin " + HandShakeSuiteTest.class.getSimpleName());
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
                handshake = new HandShake();
            } catch (Exception setDriverError) {
                logger.log(setDriverError.getMessage());
                logger.log(setDriverError.getStackTrace().toString());
                System.exit(1);
            }
            globals.setStatus("setup");
            globals.api = "false";

        logger.log("End " + HandShakeSuiteTest.class.getSimpleName());
    }

    @Test
    public void testGlobals() {
        logger.log("globals.url = " + globals.url);
        assertEquals("https://va--fasaoa2dev.my.salesforce.com", globals.url);
    }

    @Test
    public void loginInvalidUserTest() {
        long loginTime = 0;
        User user = new User("John", "john@va.gov", "bogus123", true);
        try {
            Thread.sleep(1000);
            loginTime = handshake.doLogin(user, globals.url);
            assertTrue(loginTime == 0 || loginTime == 99999);
            handshake.doLogout(globals.logoutUrl);
        } catch (Exception loginError) {
            logger.log(loginError.getMessage());
            logger.log(loginError.getStackTrace().toString());
        }
    }

    @Test
    public void noVerifyCodeNeededForValidUserWithWhiteListedIPTest() {
        long loginTime = 0;
        User user = new User(globals.userFirstName, globals.username, globals.password, true);
        try {
            Thread.sleep(1000);
            loginTime = handshake.doLogin(user, globals.url);
            Thread.sleep(2000);
            logger.log(Float.toString(loginTime));
            assertTrue(loginTime > 0 && loginTime < 99999);
            assertEquals("false",globals.verify_code);
            handshake.doLogout(globals.logoutUrl);
        } catch (Exception loginError) {
            logger.log(loginError.getMessage());
            logger.log(loginError.getStackTrace().toString());
        }

    }
}
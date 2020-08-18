package gov.dev.tests;

import gov.va.salesforceTests.globals.Globals;
import gov.va.salesforceTests.logger.Logger;
import gov.va.salesforceTests.framework.HandShake;
import gov.va.salesforceTests.framework.Framework;

import gov.va.salesforceTests.seleniumTests.Accounts.TestSalesforceAPIAccountConnectedApp;

import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * Unit Test for TestSalesforceAPIAccountConnectedApp.
 * to fetch data vi connected app from Salesforce Objects/Tables
 *
 * @author VACOPlazoO
 */
public class AccountAPITest {

    private static TestSalesforceAPIAccountConnectedApp accountAPITest;

    private static HandShake handshake;
    private static Framework framework;
    private static Logger logger;
    private static Globals globals;
    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        accountAPITest = new TestSalesforceAPIAccountConnectedApp();
        logger = new Logger();
        accountAPITest.log("Begin " + AccountAPITest.class.getSimpleName());
        framework = new Framework();
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

        globals.api = "true";

        logger.log("Begin " + AccountAPITest.class.getSimpleName());
    }

    @Test
    public void performanceAccountAPITest() {

        Boolean flag;

        try {
            accountAPITest.performanceTest();
            flag = true;
        } catch (Exception e) {
            logger.log(e.getMessage());
            logger.log(e.getStackTrace().toString());
            flag = false;
        }
        assertTrue(flag);
        assertTrue(globals.status == "SUCCESS");
    }

}
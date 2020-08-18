package gov.dev.tests;

import gov.va.salesforceTests.globals.Globals;
import gov.va.salesforceTests.logger.Logger;
import gov.va.salesforceTests.framework.HandShake;
import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.framework.BrowserUtils;

import gov.va.salesforceTests.seleniumTests.Participants.Test1022ParticipantSearch;

import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

public class ParticipantSearchTest {

    private static Test1022ParticipantSearch partSearch;

    private static HandShake handshake;
    private static Framework framework;
    private static BrowserUtils browser;
    private static Logger logger;
    private static Globals globals;
    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        partSearch = new Test1022ParticipantSearch();
        logger = new Logger();
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

    }

    @Test
    public void performanceParticipantSearchTest() {

        try {
            partSearch.performanceTest();
        } catch (Exception e) {
            logger.log(e.getMessage());
            logger.log(e.getStackTrace().toString());
            assertTrue(false);
        }
        assertTrue(globals.status == "SUCCESS");
    }

    @Test
    public void regressionParticipantSearchTest() {

        String paticipantDetails = "**3073";        // Participant Corp Id
        String notesDetails = "test note -hb";   // latest note

        try {
            partSearch.regressionTest();
        } catch (Exception e) {
            logger.log(e.getMessage());
            logger.log(e.getStackTrace().toString());
            assertTrue(false);
        }
        System.out.println(globals.status);
        System.out.println(globals.participantDetails);
        System.out.println(globals.notesDetails);
        assertTrue(globals.status.contains("SUCCESS"));
        assertTrue(globals.participantDetails.contains(paticipantDetails));
        assertTrue(globals.notesDetails.contains(notesDetails));
    }

    @AfterAll
    public static void cleanup() {

        try {
            handshake.doLogout(globals.logoutUrl);
        } catch (Exception end) {
            logger.log("ERROR==> UNABLE TO LOGOUT!");
            logger.log(end.getMessage());
            browser.check(globals.output_log);
        }
        //framework.closeDriver();
        //framework.endCurrentDrivers();

    }

}
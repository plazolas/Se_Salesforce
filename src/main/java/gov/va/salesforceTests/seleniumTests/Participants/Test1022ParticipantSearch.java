package gov.va.salesforceTests.seleniumTests.Participants;

import gov.va.salesforceTests.utils.GenTestId;
import gov.va.salesforceTests.utils.Reports;
import gov.va.salesforceTests.classes.User;
import gov.va.salesforceTests.framework.HandShake;
import gov.va.salesforceTests.pageObjects.LoadEmpwrAppPage;
import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.framework.BrowserUtils;

import gov.va.salesforceTests.pageObjects.Participants.Participants;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.time.*;

/**
 * Class Test1022ParticipantSearch implements performance and regression tests
 * for eMPWR-VA Home Page Participant Search
 * Automation and Performance test for Jira story EMPWR-1022
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class Test1022ParticipantSearch extends Framework {

    /**
     * Change Jira Issue:
     */
    private String issue = "EMPWR-1022";

    private String test_id = "";

    public BrowserUtils browser;
    private Reports reports;
    private Participants participants;

    public Test1022ParticipantSearch() {
        reports = new Reports();
        participants = new Participants();
    }

    /**
     * Method performanceTest: performance automation test for the story.
     * @throws InterruptedException exception.
     */
    public void performanceTest() throws InterruptedException {

        String str;
        long startTime;
        long loginTime = 0;
        long loadHomePageTime = 0;
        long loadParticipantTime = 0;
        long loadComponentTime = 0;
        long loadDetailsTime = 0;
        long loadNotesTime = 0;
        long current_test_time;
        Boolean waffleFound = false;
        int c_user, c_test;

        List<User> users = new ArrayList<>();

        User current_user;

        StringBuilder strBuilder2 = new StringBuilder();
        HandShake handshake;
        LoadEmpwrAppPage loadEmpwrAppPage;

        User user = new User(globals.userFirstName, globals.username, globals.password, true);
        for (int u = 0; u < globals.number_of_users; u++) {
            users.add(user);
        }

        try {
            driver = Framework.getInstance().getDriver();
        } catch (Exception getDriverError) {
            getDriverError.getMessage();
            getDriverError.printStackTrace();
            System.exit(1);
        }

        handshake = new HandShake();
        loadEmpwrAppPage = new LoadEmpwrAppPage();
        browser = new BrowserUtils(globals.implicitWait, globals.polling);

        /**
         * USERS ITERATIONS
         */
        for (c_user = 1; c_user <= users.size(); c_user++) {
            log("******************************************");
            log("*** Processing user: " + c_user + "             *******");
            log("******************************************");

            try {
                driver = Framework.getInstance().getDriver();
            } catch (Exception getDriverError) {
                getDriverError.getMessage();
                getDriverError.printStackTrace();
                System.exit(1);
            }

            try {
                test_id = new GenTestId().getTestid();
                if (test_id.length() == 0) {
                    throw new NoSuchAlgorithmException("Cant generate Unique Test Id");
                }
            } catch (NoSuchAlgorithmException ex) {
                log("Error Message: " + ex.getMessage());
                System.exit(1);
            }
            log("test_id: " + test_id);

            current_user = users.get(c_user - 1);
            if (users.size() > 1 && c_user > 2) {
                Thread.sleep(3000);
            }

            try {
                long verifyWait;
                startTime = System.currentTimeMillis();
                loginTime = handshake.doLogin(current_user, globals.testURL);
                if (globals.verify_code.contains("true") && loginTime > 0 && globals.driverMode.contains("headless")) {
                    log("User needs to input verify code. Please enter verify code:");
                    verifyWait = 5000;
                    handshake.enterVerifyCode();
                    Thread.sleep(verifyWait);
                    loginTime = System.currentTimeMillis() - startTime - verifyWait;
                } else {
                    log("NO VERIFY CODE NEEDED!!!");
                }
            } catch (Exception eLogin) {
                log(eLogin.getMessage());
                eLogin.printStackTrace();
                System.exit(1);
            }
            if (loginTime == 0) {
                log("0X ==> Could not login!");
                continue;
            }
            log("login time: " + loginTime);

            int i = 0;
            while (!driver.getTitle().contains("Home") && i < 5) {
                Thread.sleep(1000);
                closeActiveTabs();
                i++;
            }

            /**
             * TESTS ITERATIONS
             */
            for (c_test = 1; c_test <= globals.number_of_tests; c_test++) {
                strBuilder2.setLength(0);

                if (c_test == 1 && waffleFound == false) {
                    try {
                        loadHomePageTime = loadEmpwrAppPage.load(globals.appName);
                        waffleFound = true;
                    } catch (Exception loadPageError) {
                        log(loadPageError.getMessage());
                        log("0x ==> could not re-load app page: EXITING @  iteration: " + c_test + " , user: " + c_user);
                        browser.check(globals.output_log);
                        c_test = globals.number_of_tests + 1;
                        continue;
                    }
                    if (loadHomePageTime == 0) {
                        log("Could not find waffle");
                        browser.check(globals.output_log);
                        c_test = globals.number_of_tests + 1;
                    }
                }

                Thread.sleep(2000);

                try {
                    loadComponentTime = loadEmpwrAppPage.navigateToComponent("Home");
                } catch (Exception error) {
                    log(error.getMessage());
                    error.printStackTrace();
                    browser.check(globals.output_log);
                }
                loadComponentTime += loadHomePageTime;

                Thread.sleep(2000);

                String participant_fallback_url = globals.homepage + "/lightning/r/Contact/" + globals.participant_sf_id + "view";
                loadParticipantTime = participants.searchParticipant(globals.participant_ssn, globals.participant_name, participant_fallback_url);
                log("load Participant Time: " + loadParticipantTime);
                if (loadParticipantTime == 0) {
                    log("Could not load Participant!");
                    continue;
                }

                Thread.sleep(2000);

                try {
                    loadDetailsTime = participants.viewDetails("details");
                    log("load Details: " + loadDetailsTime);
                } catch (Exception err2) {
                    log(err2.getMessage());
                }

                try {
                    loadNotesTime = participants.viewDetails("notes");
                    log("load Notes: " + loadNotesTime);
                } catch (Exception err2) {
                    log(err2.getMessage());
                }

                current_test_time = loginTime + loadComponentTime + loadParticipantTime + loadDetailsTime;

                log("............... EXEC TIME PROFILE REPORT .................");
                log("Test Date: " + LocalDate.now() + " " + LocalTime.now());
                log("Iteration: " + c_test);
                log("Context: " + issue);
                log("....................... PROFILE ..........................");
                log("LOGIN: " + loginTime);
                log("LOAD COMPONENT: " + loadComponentTime);
                log("LOAD PARTICIPANT: " + loadParticipantTime);
                log("LOAD DETAILS: " + loadDetailsTime);
                log("LOAD NOTES: " + loadNotesTime);
                log("TOTAL TEST TIME: " + current_test_time);
                log("....................... END PROFILE .......................");

                strBuilder2
                        .append("," + loginTime + ",")
                        .append(loadComponentTime + ",")
                        .append(loadParticipantTime + ",")
                        .append(loadDetailsTime + ",")
                        .append(loadNotesTime + ",")
                        .append(current_test_time + "\n");

                try {
                    str = strBuilder2.toString();
                    reports.create(str, issue, globals.reportsPath, globals.appName, test_id);
                    if (str.length() == 0) {
                        throw new IOException("ignore");
                    }
                } catch (IOException ex) {
                    log(ex.getMessage());
                }
                closeActiveTabs();
            }
            log("TEST END TIME for user " + current_user.getName() + ": " + LocalDate.now() + " " + LocalTime.now());
            Thread.sleep(2000);
            try {
                handshake.doLogout(globals.logoutUrl);
            } catch (Exception end) {
                log("ERROR ==> UNABLE TO LOGOUT!");
                log(end.getMessage());
                browser.check(globals.output_log);
            }
            log("LOGGED OUT");
        }
        closeActiveTabs();
        if (!globals.driverMode.contains("headless")) driver.close();
        globals.setStatus("SUCCESS");
        log("===========> TEST END <===========");
    }

    /**
     * Method regressionTest: regression automation test for the story.
     * @throws InterruptedException exception.
     */
    public void regressionTest() throws InterruptedException {

        long loginTime = 0;
        long loadParticipantTime = 0;
        long loadDetailsTime = 0;
        long loadNotesTime = 0;

        User current_user;

        HandShake handshake;
        LoadEmpwrAppPage loadEmpwrAppPage;

        current_user = new User(globals.userFirstName, globals.username, globals.password, true);

        try {
            driver = Framework.getInstance().getDriver();
        } catch (Exception getDriverError) {
            getDriverError.getMessage();
            getDriverError.printStackTrace();
            System.exit(1);
        }

        handshake = new HandShake();
        loadEmpwrAppPage = new LoadEmpwrAppPage();
        browser = new BrowserUtils(globals.implicitWait, globals.polling);

        try {
            driver = Framework.getInstance().getDriver();
        } catch (Exception getDriverError) {
            getDriverError.getMessage();
            getDriverError.printStackTrace();
            System.exit(1);
        }

        try {
            long verifyWait;
            loginTime = handshake.doLogin(current_user, globals.testURL);
            if (globals.verify_code.contains("true") && loginTime > 0 && globals.driverMode.contains("headless")) {
                log("User needs to input verify code. Please enter verify code:");
                verifyWait = 5000;
                handshake.enterVerifyCode();
                Thread.sleep(verifyWait);
            } else {
                log("NO VERIFY CODE NEEDED!!!");
            }
        } catch (Exception eLogin) {
            log(eLogin.getMessage());
            eLogin.printStackTrace();
            System.exit(1);
        }
        if (loginTime == 0) {
            log("0X ==> Could not login!");
            System.exit(1);
        }
        log("login time: " + loginTime);

        int i = 0;
        while (!driver.getTitle().contains("Home") && i < 5) {
            Thread.sleep(1000);
            closeActiveTabs();
            i++;
        }

        try {
            loadEmpwrAppPage.load(globals.appName);
        } catch (Exception loadPageError) {
            log(loadPageError.getMessage());
            log("0x ==> could not re-load app page");
            closeActiveTabs();
            System.exit(1);
        }

        Thread.sleep(2000);

        try {
            loadEmpwrAppPage.navigateToComponent("Home");
        } catch (Exception error) {
            log(error.getMessage());
            error.printStackTrace();
            browser.check(globals.output_log);
        }

        Thread.sleep(2000);

        String participant_fallback_url = globals.homepage + "/lightning/r/Contact/" + globals.participant_sf_id + "view";
        loadParticipantTime = participants.searchParticipant(globals.participant_ssn, globals.participant_name, participant_fallback_url);
        log("load Participant Time: " + loadParticipantTime);
        if (loadParticipantTime == 0) {
            log("Could not load Participant!");
            closeActiveTabs();
            System.exit(1);
        }

        Thread.sleep(2000);

        try {
            participants.viewDetails("details");
            log("load Details: " + loadDetailsTime);
        } catch (Exception err2) {
            log(err2.getMessage());
        }

        try {
            participants.viewDetails("notes");
            log("load Notes: " + loadNotesTime);
        } catch (Exception err2) {
            log(err2.getMessage());
        }

        log("TEST END TIME for user " + current_user.getName() + ": " + LocalDate.now() + " " + LocalTime.now());
        Thread.sleep(2000);
        try {
            handshake.doLogout(globals.logoutUrl);
        } catch (Exception end) {
            log("ERROR ==> UNABLE TO LOGOUT!");
            log(end.getMessage());
            browser.check(globals.output_log);
        }
        log("LOGGED OUT");

        closeActiveTabs();
        //if (!globals.driverMode.contains("headless")) driver.close();
        globals.setStatus("SUCCESS");
        log("===========> TEST END <===========");
    }
}


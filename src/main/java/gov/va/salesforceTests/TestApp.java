package gov.va.salesforceTests;

import gov.va.salesforceTests.seleniumTests.Accounts.TestMuleSoftAPI;
import gov.va.salesforceTests.seleniumTests.Participants.Test1022ParticipantSearch;
import gov.va.salesforceTests.utils.WindowsProcess;
import gov.va.salesforceTests.utils.TestProperties;
import gov.va.salesforceTests.logger.Logger;
import java.io.IOException;

import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.seleniumTests.Accounts.TestMuleSoftAPI;
import gov.va.salesforceTests.seleniumTests.Accounts.TestSalesforceAPIAccountConnectedApp;
import gov.va.salesforceTests.seleniumTests.Participants.TestSearchParticipantApiSensitive;

/**
 * TestApp class for launching automated Selenium performance and regression tests on
 * Salesforce Applications.
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class TestApp {

    private static WindowsProcess windowsProcess;
    private static TestProperties config;
    private static  String os;
    private static  String webdriver;

    /**
     * Method main launches automated Selenium tests.
     * @param args is the past argument values.
     * @throws InterruptedException type exception
     */
    public static void main(String[] args) throws InterruptedException {

        String test;

        if (args.length > 0) {
            System.out.println("Number of tests: " + args.length);
        } else {
            System.out.println("No tests provided.");
        }

        try {
            config = new TestProperties();
        } catch (IOException e ) {
            Logger.log(e.getStackTrace().toString());
            Logger.log(e.getMessage());
            System.exit(1);
        }
        os = config.getPropValue("test.platform");
        webdriver = config.getPropValue("webdriver.type");
        String browserExe = WindowsProcess.getDriverExecFromBrowser(webdriver);
        windowsProcess = new WindowsProcess(browserExe);

        for (int i = 0; i < args.length; i++) {
            test = args[i];
            System.out.println("testing..." + test);
            switch (test) {
                case "1022":
                    Test1022ParticipantSearch test1022ParticipantSearch = new Test1022ParticipantSearch();
                    //test1022ParticipantSearch.regressionTest();
                    test1022ParticipantSearch.performanceTest();
                    break;
                case "SalesforceAPI":
                    TestSalesforceAPIAccountConnectedApp testSalesforceAPIAccountConnectedApp = new TestSalesforceAPIAccountConnectedApp();
                    testSalesforceAPIAccountConnectedApp.performanceTest();
                    break;
                case "mulesoft":
                    TestMuleSoftAPI testMuleSoftAPI = new TestMuleSoftAPI();
                    testMuleSoftAPI.doTest();
                    break;
                case "SearchParticipant":
                    TestSearchParticipantApiSensitive testSearchParticipantApiSensitive = new TestSearchParticipantApiSensitive();
                    testSearchParticipantApiSensitive.doTest();
                    break;
                default:
                    Logger.log(test + " is not a valid test");
            }
        }
        //Framework.closeDriver();
        killDrivers();
    }

    /**
     * Method killDrivers class sends sig int signal to all running web drivers.
     */
    private static void killDrivers() {
        while (windowsProcess.isRunning()) {
            try {
                windowsProcess.kill();
            } catch (Exception e) {
                Logger.log(e.getStackTrace().toString());
                Logger.log(e.getMessage());
                break;
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.salesforceTests.globals;

import gov.va.salesforceTests.utils.TestProperties;
import gov.va.salesforceTests.logger.Logger;
import org.openqa.selenium.Cookie;

import java.io.IOException;

/**
 * Class Globals
 *
 * Holds Session Configuration Variables
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class Globals {

    /**
     * Method Globals is the constructor.
     */
    public Globals() {
        try {
            prop = new TestProperties();
        } catch (IOException e) {
            Logger.log(e.getStackTrace().toString());
            Logger.log(e.getMessage());
            System.exit(1);
        }
        setGlobals();
        configGlobals();
        outputGlobals();
    }

    private static TestProperties prop;

    public static String appName;
    public static String environment;
    public static String url;
    public static String homepage;
    public static String testURL;
    public static String platform;
    public static String run;
    public static String truststore;
    public static String log_level;

    public static int number_of_tests;
    public static int number_of_users;
    public static int implicitWait;
    public static int polling;
    public static String browser;

    public static String driverPathFirefox;
    public static String driverPathChrome;
    public static String driverPathIE;
    public static String driverFirefox;
    public static String driverChrome;
    public static String driverIE;
    public static String driverMode;

    public static String logoutUrl;
    public static String reportsPath;
    public static String output_log;
    public static String workdir;
    public static String auth_code;
    public static String access_token;
    public static Cookie cookie;

    public static String userFirstName;
    public static String username;
    public static String password;
    public static String verify_code;

    public static String AccountId;
    public static String AccountName;
    public static String AccountZipcode;
    public static String AccountsUrl;
    public static String AccountNameUrl;
    public static String ACCOUNT_DETAILS_XPATH;
    public static String ACCOUNT_ZIPCODE_XPATH;

    public static String participant_ssn;
    public static String participant_sf_id;
    public static String participant_corp_id;
    public static String participant_name;
    public static String transaction_search;

    public static String getAuthTokenUrl;
    public static String getAccessTokenUrl;
    public static String getAccountDataUrl;
    public static String response_type;
    public static String client_id;
    public static String redirect_uri;
    public static String client_secret;
    public static String grant_type;
    public static String status;
    public static String api = "false";

    public static String participantDetails;
    public static String notesDetails;

    private static void setGlobals() {
        appName = prop.getPropValue("test.appName");
        homepage = prop.getPropValue("test.homepage");
        environment = prop.getPropValue("test.env");
        url = prop.getPropValue("test.url");
        testURL = prop.getPropValue("test.url");
        platform = prop.getPropValue("test.platform");
        run = prop.getPropValue("test.run");
        truststore = prop.getPropValue("test.truststore");
        log_level = prop.getPropValue("logging.level.root");

        number_of_tests = Integer.parseInt(prop.getPropValue("test.number_of_tests"));
        number_of_users = Integer.parseInt(prop.getPropValue("test.number_of_users"));
        implicitWait = Integer.parseInt(prop.getPropValue("test.implicitWait"));
        polling = Integer.parseInt(prop.getPropValue("test.polling"));
        browser = prop.getPropValue("webdriver.type");

        driverPathFirefox = prop.getPropValue("gecko.driver.windows.path");
        driverPathChrome = prop.getPropValue("chrome.driver.windows.path");
        driverPathIE = prop.getPropValue("ie.driver.windows.path");
        driverFirefox = prop.getPropValue("webdriver.path.firefox");
        driverChrome = prop.getPropValue("webdriver.path.chrome");
        driverIE = prop.getPropValue("webdriver.path.ie");
        driverMode = prop.getPropValue("webdriver.mode");

        logoutUrl = prop.getPropValue("test.logoutURL");
        reportsPath = prop.getPropValue("test.reportsPath");
        output_log = prop.getPropValue("test.output_log");
        workdir = (prop.getPropValue("test.run").contains("local")) ? prop.getPropValue("test.workdir") + "/local.properties" : "/salesforce/local.properties";

        userFirstName = prop.getPropValue("user.name");
        username = prop.getPropValue("user.username");
        password = prop.getPropValue("user.password");
        verify_code = "false";

        getAuthTokenUrl = prop.getPropValue("test.getAuthTokenUrl");
        getAccessTokenUrl = prop.getPropValue("test.getAccessTokenUrl");
        getAccountDataUrl = prop.getPropValue("test.getAccountDataUrl");
        response_type = prop.getPropValue("test.response_type");
        client_id = prop.getPropValue("test.client_id");
        redirect_uri = prop.getPropValue("test.redirect_uri");
        client_secret = prop.getPropValue("test.client_secret");
        grant_type = prop.getPropValue("test.grant_type");
        status = "set";
    }

    /**
     * Method configGlobals sets globals variables on a per env basis.
     */
    public static void configGlobals() {

        switch (environment) {
            case "dev":
                // ACCOUNTS
                AccountId = "0013500000D4O5xAAF";
                AccountName = "Walmart";
                AccountZipcode = "49301";
                AccountsUrl = url + "/lightning/o/Account/list?filterName=Recent";
                AccountNameUrl = url + "/lightning/r/Account/" + AccountId + "/view";
                ACCOUNT_DETAILS_XPATH = "//*[@id=\"detailTab__item\"]";
                ACCOUNT_ZIPCODE_XPATH = "//*[@id=\"window\"]";

                participant_ssn = "137124444";
                participant_sf_id = "0033500000Hwao5AAB";
                participant_corp_id = "333073";
                participant_name = "MARTHA";
                transaction_search = "A21";
                break;

            case "uat":
                participant_ssn = "137124444";
                participant_sf_id = "TBD";
                participant_corp_id = "333073";
                participant_name = "MARTHA";
                transaction_search = "A21";
                break;

            case "prod":
                participant_ssn = "137124444";
                participant_sf_id = "TBD";
                participant_corp_id = "333073";
                participant_name = "MARTHA";
                transaction_search = "A21";
                break;
        }
    }

    /**
     * Method outputGlobals outputs/logs some of the globals variables for debugging.
     */
    public void outputGlobals() {
        Logger.log("********* TRACE ***************");
        Logger.log(Logger.echoTrace(7));
        Logger.log("********* CONFIG Begin ********");
        Logger.log("App Name: " + appName);
        Logger.log("Home Page URL: " + homepage);
        Logger.log("Number of tests: " + number_of_tests);
        Logger.log("Number of users: " + number_of_users);
        Logger.log("Reports Path: " + reportsPath);
        Logger.log("Output Logger: " + output_log);
        Logger.log("Implicit Wait: " + implicitWait);
        Logger.log("Platform: " + platform);
        Logger.log("run: " + run);
        Logger.log("browser: " + browser);
        Logger.log("trustStore: " + truststore);
        Logger.log("getAccountDataUrl: " + getAccountDataUrl);
        Logger.log("getAuthTokenUrl: " + getAuthTokenUrl);
        Logger.log("getAccessTokenUrl: " + getAccessTokenUrl);
        Logger.log("response_type: " + response_type);
        Logger.log("client_id: " + client_id);
        Logger.log("redirect_uri: " + redirect_uri);
        Logger.log("client_secret: " + client_secret);
        Logger.log("grant_type: " + grant_type);
        Logger.log("log_level: " + log_level);
        Logger.log("api: " + api);
        Logger.log("status: " + status);
        Logger.log("********* CONFIG End ********\n");
    }

    /**
     * Method setDriverMode webdrive mode.
     *      if value is not 'headless' the webdriver mode will user an interactive browser.
     * @param mode string is webdriver mode.
     */
    public static void setDriverMode(String mode) {
        driverMode = (mode.contains("headless")) ? "headless" : mode;
    }

    /**
     * Method setStatus sets global variable status.
     * @param s is the status string to be set to.
     */
    public static void setStatus(String s) { status = s; }

    /**
     * Method setApi sets global variable api type.
     * @param s is the api type string to be set to.
     *          'true' runs test as api.
     *          'false' runs test as selenium tests.
     */
    public static void setApi(String s) { api = s; }
}

package gov.va.salesforceTests.framework;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.logging.*;
import com.google.common.base.Splitter;
import java.util.concurrent.TimeUnit;

import gov.va.salesforceTests.globals.Globals;
import gov.va.salesforceTests.logger.Logger;
import gov.va.salesforceTests.utils.TestProperties;
import gov.va.salesforceTests.utils.WindowsProcess;

/**
 * Class Framework class
 *    Sets up and instantiates Selenium webdriver for multi-thread testing.
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class Framework extends Logger {

    public static WebDriver driver = null;
    public static Globals globals = null;
    private static Framework instance = null;
    private static int IMPLICIT_TIMEOUT = 0;
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
    private static ThreadLocal<String> sessionId = new ThreadLocal<String>();
    private static ThreadLocal<String> sessionBrowser = new ThreadLocal<String>();
    private static ThreadLocal<String> sessionPlatform = new ThreadLocal<String>();
    private static ThreadLocal<String> sessionVersion = new ThreadLocal<String>();
    private static String getEnv = null;

    /**
     * Method Framework class constructor.
     */
    public Framework() {
        globals = getGlobals();
    }

    /**
     * Method getGlobals retrieves global variables instance.
     * @return global variables instance
     */
    private Globals getGlobals() {
        if(globals == null) globals = new Globals();
        return globals;
    }

    /**
     * Method getInstance retrieves active driver instance.
     *
     * Creates new or retrieves active webdriver instance.
     *
     * @return active webdriver instance.
     */
    public static Framework getInstance() {
        if ( instance == null ) {
            instance = new Framework();
            globals = instance.getGlobals();
            try {
                Framework.setDriver(globals.browser, globals.platform, globals.environment, globals.run);
            } catch (Exception e) {
                log(e.getMessage());
                log(e.getStackTrace().toString());
                log("0X CANNOT INSTANCIATE SELENIUM DRIVER X0");
                endCurrentDrivers();
                System.exit(1);
            }
            driver = getDriver();
        }
        return instance;
    }

    /**
     * Method setDriver method to create driver instance
     *
     * @param browser current browser
     * @param platform current platform: windows or linux
     * @param environment current env: dev, uat, prod
     * @param run current run: local or minicube
     */
    public static void setDriver(String browser, String platform, String environment, String run) {

        if (driver != null ) return;

        DesiredCapabilities caps = null;
        String getPlatform = null;
        
        IMPLICIT_TIMEOUT = globals.implicitWait;

        switch (browser) {
            case "firefox":
                caps = DesiredCapabilities.firefox();
                FirefoxBinary firefoxBinary = new FirefoxBinary();
                FirefoxOptions ffOpts = new FirefoxOptions();
                ffOpts.setBinary(firefoxBinary);
                if(globals.driverMode.contains("headless")){
                    firefoxBinary.addCommandLineOptions("--headless");
                }
                if ( run.equalsIgnoreCase("local") ) {
                    System.setProperty("webdriver.gecko.driver", globals.driverPathFirefox);
                } else {
                    System.setProperty("webdriver.gecko.driver", globals.driverFirefox);
                }
                try {
                    webDriver.set(new FirefoxDriver(ffOpts.merge(caps)));
                } catch (Exception exp) {
                    log(exp.getMessage());
                    log(exp.getStackTrace().toString());
                    System.exit(1);
                }
                break;

            case "chrome":
                caps = DesiredCapabilities.chrome();
                ChromeOptions chOptions = new ChromeOptions();
                Map<String, Object> chromePrefs = new HashMap<String, Object>();
                chromePrefs.put("credentials_enable_service", false);
                chOptions.setExperimentalOption("prefs", chromePrefs);
                chOptions.addArguments("--disable-plugins", "--disable-extensions", "--disable-popup-blocking");
                if(globals.driverMode.contains("headless")){
                    chOptions.addArguments("--headless");
                } else {
                    log("--user-data-dir="+System.getenv("LOCALAPPDATA")+"\\Google\\Chrome\\User Data");
                    chOptions.setExperimentalOption("useAutomationExtension", false);
                    chOptions.addArguments("--remote-debugging-port=9222");
                    chOptions.addArguments("disable-extensions");
                    chOptions.addArguments("--user-data-dir="+System.getenv("LOCALAPPDATA")+"\\Google\\Chrome\\User Data");
                    chOptions.addArguments("disable-infobars");
                    chOptions.addArguments("--no-sandbox");
                    chOptions.addArguments("--ignore-certificate-errors");
                    chOptions.addArguments("start-maximized");
                    //chOptions.addArguments("--disable-dev-shm-usage");

                }
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
                caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
                caps.setCapability(ChromeOptions.CAPABILITY, chOptions);
                caps.setCapability("applicationCacheEnabled", false);
                if ( run.equalsIgnoreCase("local") ) {
                    System.setProperty("webdriver.chrome.driver", globals.driverPathChrome);
                } else {
                    System.setProperty("webdriver.chrome.driver", globals.driverChrome);
                }
                try {
                    webDriver.set(new ChromeDriver(chOptions.merge(caps)));
                } catch (Exception exp) {
                    log(exp.getMessage());
                    log(exp.getStackTrace().toString());
                    System.exit(1);
                }
                break;

            case "ie":
                caps = DesiredCapabilities.internetExplorer();
                InternetExplorerOptions ieOpts = new InternetExplorerOptions();
                ieOpts.requireWindowFocus();
                ieOpts.merge(caps);
                caps.setCapability("requireWindowFocus", true);
                if ( globals.run.equalsIgnoreCase("local") ) {
                    log("globals.driverPathIE " + globals.driverPathIE);
                    System.setProperty("webdriver.ie.driver", globals.driverPathIE);
                } else {
                    System.setProperty("webdriver.ie.driver", globals.driverIE);
                }
                try {
                    webDriver.set(new InternetExplorerDriver(ieOpts.merge(caps)));
                } catch (Exception exp) {
                    log(exp.getMessage());
                    log(exp.getStackTrace().toString());
                    System.exit(1);
                }
                break;
        }

        getEnv = environment;
        getPlatform = platform;
        try {
            sessionId.set(((RemoteWebDriver) webDriver.get()).getSessionId().toString());
        } catch (Exception exp) {
            log(exp.getMessage());
            log(exp.getStackTrace().toString());
        }
        sessionBrowser.set(caps.getBrowserName());
        sessionVersion.set(caps.getVersion());
        sessionPlatform.set(getPlatform);

        log("\n" +
                "==> WEBDRIVER INFO: "
                + getSessionBrowser().toUpperCase()
                + "/" + getSessionPlatform().toUpperCase()
                + "/" + getEnv.toUpperCase()
                + "/Session ID=" + getSessionId() + "<==\n");

        getDriver().manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
        getDriver().manage().window().maximize();

        log("****************************************************");
        log(Logger.echoTrace(5));
        log("****************************************************\n");
    }

    /**
     * Method getDriver method to retrieve active driver
     *
     * @return current active webdriver
     */
    public static WebDriver getDriver() {
        return webDriver.get();
    }

    /**
     * Method closeDriver method to close active driver
     */
    public static void closeDriver() {
        try {
            getDriver().close();
        } catch ( Exception e ) {
            log(e.getMessage());
        }
    }

    /**
     * quitDriver method to quit active driver
     */
    public static void quitDriver() {
        try {
            getDriver().quit();
        } catch ( Exception e ) {
            log(e.getMessage());
        }
    }

    /**
     * Method getSessionId method to retrieve active id
     *
     * @return String
     */
    public static String getSessionId() {
        return sessionId.get();
    }

    /**
     * Method getSessionBrowser method to retrieve active browser
     * @return String
     */
    public static String getSessionBrowser() {
        return sessionBrowser.get();
    }

    /**
     * Method getSessionVersion method to retrieve active version
     *
     * @return String
     */
    public static String getSessionVersion() {
        return sessionVersion.get();
    }

    /**
     * Method getSessionPlatform method to retrieve active platform
     * @return String
     */
    public static String getSessionPlatform() {
        return sessionPlatform.get();
    }

    /**
     * parseURIQueryParameters method to retrieve params from uri
     * @return Map of Strings.
     */
    public static Map<String, String> parseURIQueryParameters(String uri) {
        String query = uri.split("\\?")[3];
        final Map<String, String> uriMap =  Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);
        return uriMap;
    }

    /**
     * closeActiveTabs method closes all active tabs for the user
     */
    public static void closeActiveTabs() {

        WebElement e;
        List<WebElement> els;
        String str;
        int i, j = 0;
        int max_buttons = 4;

        try {
            Thread.sleep(2000);
            log("********** CLOSING TABS *************");
            els = getDriver().findElements(By.tagName("button"));
            log("Total buttons: " + els.size());
            if (els.size() > 0) {
                for (i = 0; i < els.size() && j < max_buttons; i++) {
                    e = els.get(i);
                    if (e.isDisplayed() && e.isEnabled()) {
                        str = e.getAttribute("innerHTML");
                        if (str.contains("Close")) {
                            j++;
                            log(i + ": Closing Tab" + str);
                            try {
                                Thread.sleep(500);
                                e.click();
                            } catch (Exception er) {
                                er.getMessage();
                            }
                        }
                    }
                }
            }
            log("********** CLOSED  TABS ****************");
        } catch (Exception ex) {
            log("NO more Closing buttons found");
        }
    }

    /**
     * endCurrentDrivers method ends all webdrivers.
     */
    public static void endCurrentDrivers() {
        WindowsProcess windowsProcess;
        TestProperties config;
        String os;
        String webdriver;
        try {
            config = new TestProperties();
            os = config.getPropValue("test.platform");
            log(os);
            webdriver = config.getPropValue("webdriver.type");
            String browserExe = WindowsProcess.getDriverExecFromBrowser(webdriver);
            windowsProcess = new WindowsProcess(browserExe);
            while (windowsProcess.isRunning()) {
                try {
                    windowsProcess.kill();
                } catch (Exception e) {
                    Logger.log(e.getStackTrace().toString());
                    Logger.log(e.getMessage());
                    break;
                }
            }
        } catch (IOException e ) {
            Logger.log(e.getStackTrace().toString());
            Logger.log(e.getMessage());
            System.exit(1);
        }
    }
}
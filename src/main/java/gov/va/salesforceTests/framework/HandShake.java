package gov.va.salesforceTests.framework;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.util.*;
import java.net.URLDecoder;

import gov.va.salesforceTests.classes.User;

/**
 * Class HandShake
 *
 * Automates login and logout of Salesforce
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class HandShake extends Framework {

    private static WebDriver driver = null;
    private static String code = "";
    private static WebElement e = null;
    private final BrowserUtils browser;
    private static JavascriptExecutor js;

    /**
     * Constructor
     */
    public HandShake() {
        driver = super.getInstance().getDriver();
        browser = new BrowserUtils(0, 0);
        js = (JavascriptExecutor) driver;
    }

    /**
     * Method doLogin automates login to Salesforce
     * @return time it took to login in milliseconds
     * @param current_user  is the current user object.
     * @param url  to navigate to
     * @throws Exception type exception
     */
    public long doLogin(User current_user, String url) throws Exception {

        String username = current_user.getUsername();
        String password = current_user.getPassword();
        String currentURL = "";
        long startTime = 0;
        long totalTime = 0;

        String verify_identity = "Verifying Your Identity";
        int i = 0;

        List<WebElement> els;

        driver.get(url);

        Thread.sleep(1000);

        if(globals.api.contains("false") || !globals.status.contains("setup")) {
            try {
                els = driver.findElements(By.tagName("a"));
                if (els.size() > 0) {
                    for (i = 0; i < els.size(); i++) {
                        e = els.get(i);
                        if (e.getAttribute("id").contains("clear_link")) {
                            //log(e.getAttribute("outerHTML"));
                            if(e.isEnabled() && e.isDisplayed()) {
                                try {
                                    e.click();
                                } catch (Exception unclickable) {
                                    log("unclickable element clear_link");
                                }
                            } else {
                                String script = "arguments[0].click()";
                                try {
                                    log("clear clicked via javascript");
                                    js.executeScript(script,e);
                                } catch (Exception exec) {
                                    log("javascript error");
                                    exec.printStackTrace();
                                    exec.getMessage();
                                }
                            }
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                log(ex.getMessage());
                ex.getStackTrace();
                log("Exception: error trying to clear user");
            }
        }

        startTime = System.currentTimeMillis();
        try {
            log("Attempting to login user: " + username);
            driver.findElement(By.id("username")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("Login")).click();
        } catch (Exception ex) {
            log("0XXXXX ==> Unable to login user: " + username);
            return 0;
        }
        browser.waitForPageLoaded();
        totalTime = System.currentTimeMillis() - startTime;


        if (globals.api.contains("false")) {
            if (driver.getTitle().contains("Login")) {
                log("0x ==> unable to login");
                return 0;
            }
        }

        try {
            els = driver.findElements(By.tagName("input"));
            if (els.size() > 0) {
                for (i = 0; i < els.size(); i++) {
                    e = els.get(i);
                    if (e.getAttribute("name").contains("smc")) {
                        log(verify_identity);
                        i = els.size();
                        globals.verify_code = "true";
                        totalTime = 99999;
                        log("Verify code must be entered!");
                    }
                }
            }
        } catch (Exception exc) {
            log("no verify code needed!");
        }

        currentURL = driver.getCurrentUrl();
        if (globals.api.contains("true") && currentURL.contains("code")) {
            log(currentURL);
            currentURL = URLDecoder.decode(currentURL, "UTF-8");
            currentURL = URLDecoder.decode(currentURL, "UTF-8");
            log(currentURL);
            Map<String, String> uriMap = super.parseURIQueryParameters(currentURL);
            log(uriMap.toString());
            globals.auth_code = uriMap.get("code");

            Cookie cookie = driver.manage().getCookieNamed("BrowserId");
            globals.cookie = cookie;
            log("BrowserId Cookie: " + cookie);
            log("name = " + cookie.getName());
            log("value = " + cookie.getValue());
            log("domain = " + cookie.getDomain());
            log("expires = " + cookie.getExpiry());
        }

        return totalTime;
    }

    /**
     * Method doLogout automates log out of Salesforce
     * @param url is Salesforce platform log out url
     */
    public static void doLogout(String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            log("doLogout" + e.getMessage());
        }
    }

    /**
     * Method enterVerifyCode automates entering Verify Code when IP is not whitelisted
     * @return string code inputed by user
     */
    public static String enterVerifyCode() {
        if (code.length() == 0) {
            Scanner scnr = new Scanner(System.in);
            log("Enter Verify Code");
            code = scnr.nextLine();
            log("Verify Code entered: " + code);
        }

        try {
            if (code.length() > 1) {
                e.sendKeys(code);
            }
            e = driver.findElement(By.id("save"));
            e.click();
        } catch (Exception ex) {
            log(ex.getMessage());
        }
        return code;
    }

}
package gov.va.salesforceTests.framework;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

/**
 * Class BrowserUtils contains reusable selenium widget manipulation methods
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class BrowserUtils extends Framework {

    private static WebDriverWait wait;
    private static JavascriptExecutor js = (JavascriptExecutor) driver;
    private long timeout = 0;
    private long polling = 0;

    public BrowserUtils(long timeoutSec, int pollingSec) {
        timeout = (timeoutSec > 0) ? timeoutSec : 90;
        polling = (pollingSec > 0) ? pollingSec : 50;
        wait = new WebDriverWait(driver, timeout, polling);
    }

    /**
     * waitFor method to poll page title
     *
     * @param title String page title
     */
    public static void waitForTitle(String title) {
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.titleContains(title)));
    }

    /**
     * waitForURL method to poll page URL
     *
     * @param url relative url location of page
     */
    public static void waitForURL(String url) {
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.urlContains(url)));
    }

    /**
     * waitForClickable method to poll for clickable
     *
     * @param by Selenium WebElement locating mechanism
     */
    public static void waitForClickable(By by) {
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(by)));
    }

    /**
     * waitForClickableByText method to poll for clickable by Text
     *
     * @param text contained in the the element to wait for
     */
    public static void waitForClickableByText(String text) {
        String byXpath = "//a[text()='" + text + "']";
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(By.xpath(byXpath))));
    }

    /**
     * runJavascript method to run a Javascript script in the browser.
     *
     * @param javascript is the script to be run.
     * @return str is the result output string.
     */
    public String runJavascript(String javascript) {
        String str = ((JavascriptExecutor) driver).executeScript(javascript).toString();
        log(str);
        return str;
    }

    /**
     * click method using JavaScript API click
     *
     * @param by Selenium WebElement locating mechanism
     */
    public static void click(By by) {
        WebElement element = driver.findElement(by);
        js.executeScript("arguments[0].click();", element);
    }

    /**
     * clickByText method using JavaScript API click
     *
     * @param text contains the text of the element to click
     */
    public static void clickByText(String text) {
        String byXpath = "//a[text()='" + text + "']";
        try {
            WebElement element = driver.findElement(By.xpath(byXpath));
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            log(e.getMessage());
            e.printStackTrace();
            globals.status = "FAIL";
        }
    }

    /**
     * byXpath method for selecting WebElement using a xpath string
     *
     * @param xpath Selenium Xpath string for locating WebElement
     * @return WebElement if found, null otherwise
     */
    public WebElement waitByXpath(String xpath) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
            WebElement e = driver.findElement(By.xpath(xpath));
            return e;
        } catch (Exception error) {
            log("BrowserUtils.byXpath: xxx ELEMENT NOT FOUND in " + timeout + " secs!");
            return null;
        }
    }

    /**
     * waitByLinkText method for locating WebElement by text
     *
     * @param text string text contained in the WebElement to be located
     * @return true if WebElement found, false otherwise
     */
    public Boolean waitByLinkText(String text) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(text)));
        } catch (Exception error) {
            log("0X ==> ELEMENT NOT FOUND in " + timeout + " secs X0");
            return false;
        }
        return true;
    }

    /**
     * findByTagAttributeText method for locating WebElement by tag, attr and text
     *
     * @param tag       string for tag name
     * @param attribute string for name of html attribute
     * @param text      string to locate
     * @return WebElement if found, null otherwise
     */
    public WebElement findByTagAttributeText(String tag, String attribute, String text) {

        WebElement e = null;
        List<WebElement> els;

        els = driver.findElements(By.tagName(tag));
        log("FindByTagAttributeText \r\n tag: " + tag +
                "\r\n attribute: " + attribute +
                "\r\n text: " + text +
                "\r\n found tags: " + els.size());
        for (int i = 0; i < els.size(); i++) {
            e = els.get(i);
            if (e.getAttribute(attribute).contains(text) || e.getText().contains(text)) {
                if (e.isEnabled()) {
                    log(e.getAttribute("outerHTML"));
                    break;
                }
            } else {
                e = null;
            }
        }

        return (e == null) ? null : e;
    }

    /**
     * findByTagAttributeText method for locating WebElement by tag, attr and text
     *
     * @param tag       string for tag name
     * @param attribute string for name of html attribute
     * @param text      string to locate
     * @return WebElement if found, null otherwise
     */
    public WebElement findByTagAttributeClassText(String tag, String attribute, String className, String text) {

        WebElement e = null;
        List<WebElement> els;
        String str;

        els = driver.findElements(By.tagName(tag));
        log("FindByTagAttributeText \r\n tag: " + tag +
                "\r\n attribute: " + attribute +
                "\r\n class: " + className +
                "\r\n text: " + text +
                "\r\n found tags: " + els.size());
        for (int i = 0; i < els.size(); i++) {
            e = els.get(i);
            if (e.getAttribute(attribute).contains(className)) {
                str = e.getAttribute("outerHTML");
                if (str.contains(text)) {
                    log(str);
                    break;
                } else {
                    e = null;
                }
            } else {
                e = null;
            }
        }

        return (e == null) ? null : e;
    }

    /**
     * findByClassName method for locating WebElement by tag, attr and text
     *
     * @param className string for class name to find
     * @return WebElement if found, null otherwise
     */
    public WebElement findFirstByClassName(String className) {

        WebElement e = null;
        List<WebElement> els;

        els = driver.findElements(By.className(className));
        if (els.size() > 0) {
            e = els.get(0);
        }
        return (e == null) ? null : e;
    }

    /**
     * pageLoaded method to check if page has been loaded
     *
     * @return true if page is loaded, null otherwise
     */
    public Boolean waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                Boolean flag = false;
                String str = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
                log(str);
                flag = (str.contains("complete")) ? true : false;
                return flag;
            }
        };
        try {
            wait.until(expectation);
        } catch (Throwable error) {
            log(error.getMessage());
            return false;
        }
        return true;
    }

    /**
     * pageDump method to get a page dump
     *
     * @param toScreen true if dump is to screen, false otherwise
     * @return String of the page dump
     */
    public String pageDump(Boolean toScreen) {
        WebElement e;
        String str = "";
        try {
            str = "*******************vvv DUMP vvv*******************";
            e = driver.findElement(By.tagName("html"));
            str += e.getAttribute("outerHTML");
            str += "*******************^^^ DUMP ^^^*******************";
            if (toScreen) log(str);
        } catch (Throwable error) {
            log(error.getMessage());
        }
        return str;
    }

    /**
     * Method checks if browser has crashed
     *
     * @param fileStr is the path / filename of the log file
     */
    public static void check(String fileStr) {

        String content = "";
        File tempFile = new File(fileStr);
        if (tempFile.exists()) {
            try {
                content = new String(Files.readAllBytes(Paths.get(fileStr)));
                log("Checking if browser crashed...");
            } catch (Exception e) {
                log(e.getMessage());
                System.exit(1);
            }
            if (content.contains("MOZ_CRASHREPORTER_SHUTDOWN")) {
                log("Browser CRASHED!");
                System.exit(1);
            }
        } else {
            log("no logfile found. Cannot determine if browser crashed!");
        }
    }
}

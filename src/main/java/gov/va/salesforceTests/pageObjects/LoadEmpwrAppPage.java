package gov.va.salesforceTests.pageObjects;

import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.framework.BrowserUtils;
import org.openqa.selenium.*;

import java.util.*;

/**
 * Class LoadEmpwrAppPage
 *    Loads and Navigates from Home Page utility class
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class LoadEmpwrAppPage extends Framework {

    private WebDriver driver;
    private BrowserUtils browser;
    private static JavascriptExecutor js;

    private By waffleLocator =          By.className("slds-icon-waffle_container");
    private By searchAppLocator =       By.xpath("//input[@placeholder='Search apps and items...']");
    private By menuButtonLocator =      By.xpath("/html/body/div[4]/div[1]/div[2]/header/div[3]/div/div[1]/div[3]/div/button");
    private By homeDropdownLocator =    By.xpath("/html/body/div[4]/div[1]/div[2]/header/div[3]/div/div[1]/div[3]/div/section/div/div/ul/li[1]");
    private By reportsDropDownLocator = By.xpath("/html/body/div[4]/div[1]/div[2]/header/div[3]/div/div[1]/div[3]/div/section/div/div/ul/li[2]");
    private By participantTmpLocator =  By.xpath("/html/body/div[4]/div[1]/div[2]/header/div[3]/div/div[1]/div[3]/div/section/div/div/ul/li[3]");
    private By transDropDownLocator =   By.xpath("/html/body/div[4]/div[1]/div[2]/header/div[3]/div/div[1]/div[3]/div/section/div/div/ul/li[4]");
    private By authQueueLocator =       By.xpath("/html/body/div[4]/div[1]/div[2]/header/div[3]/div/div[1]/div[3]/div/section/div/div/ul/li[5]");

    /**
     * Constructor
     */
    public LoadEmpwrAppPage() {
        driver = super.getInstance().getDriver();
        browser = new BrowserUtils(0, 0);
        js = (JavascriptExecutor) driver;
    }

    /**
     * Method load: automates loading of Home Page
     * @return total time it took to load Home Page in milliseconds
     * @throws Exception type exception
     */
    public long load(String appName) throws Exception {
        WebElement e, waffle;
        List<WebElement> els;
        long appLoadTime = 0;
        long startTime;
        String str;

        log(driver.getCurrentUrl());
        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.getMessage();
        }
        log(driver.getCurrentUrl());

        try {
            browser.waitForTitle("Home | Salesforce");
            closeActiveTabs();
            log(driver.getCurrentUrl());
        } catch (Exception wex) {
            log("Not at home page yet!");
        }

        str = driver.getCurrentUrl();
        while(!str.contains("home")) {
            log("one.app");
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
                log(ex.getMessage());
            }
            closeActiveTabs();
            browser.waitForPageLoaded();
            str = driver.getCurrentUrl();
        }

        els = driver.findElements(waffleLocator);
        log("LoadEmpwrAppPage waffles: " + els.size());
        if (els.size() > 0) {
            waffle = els.get(0);
        } else {
            log("No waffle found!");
            els = driver.findElements(By.tagName("button"));
            for (Integer i = 0; i < els.size(); i++) {
                WebElement el = els.get(i);
                log(el.getAttribute("outerHTML"));
            }
            return 0;
        }

        Thread.sleep(2000);

        try {
            waffle.click();
        } catch (Exception ex) {
            ex.getMessage();
            return 0;
        }

        try {
            e = driver.findElement(searchAppLocator);
        } catch (Exception ie) {
            ie.getMessage();
            return 0;
        }

        Thread.sleep(2000);

        try {
            e.sendKeys(appName);
            e.sendKeys(Keys.ENTER);
            log(appName + " entered!");
            startTime = System.currentTimeMillis();
            browser.waitForPageLoaded();
            appLoadTime = System.currentTimeMillis() - startTime;
            log("appLoadTime: " + appLoadTime);
        } catch (Exception ex) {
            ex.getMessage();
            log("could not enter prototype");
            return 0;
        }

        return appLoadTime;
    }

    /**
     * Method navigateToComponent: automates navigating to an eMPWR-VA component from Home Page.
     * @param component is the string name of the component to navigate to.
     * @return total time it took to navigate and load the component in milliseconds.
     */
    public long navigateToComponent(String component) {

        WebElement e;
        long totalTime, startTime, redirectToHomePageTime;
        String backup_url, str;

        List validComponents = Arrays.asList("Home", "Reports", "eMPWR Participant Temps", "eMPWR Transactions", "Authorization Queue");

        if (!validComponents.contains(component)) {
            component = "Home";
        }

        try {
            Thread.sleep(5000);
            e = browser.findByTagAttributeClassText("div", "class", "oneUtilityBar", "Participant Search");
            while (e != null) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("return arguments[0].setAttribute('style','z-index: -1');", e);
                js.executeScript("return arguments[0].remove();", e);
                log("***************************");
                log("*** REMOVED UTILITY BAR ***");
                log("***************************");
                e = browser.findByTagAttributeClassText("div", "class", "oneUtilityBar", "Participant Search");
            }

        } catch (Exception spinnerErr) {
            System.out.println("**********  COULD NOT REMOVE UTILITY BAR ***************");
        }

        log("component: " + component);
        log(driver.getCurrentUrl());
        str = driver.getCurrentUrl();
        startTime = System.currentTimeMillis();
        while(str.contains("one.app")) {
            log("one.app");
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
                log(ex.getMessage());
            }
            browser.waitForPageLoaded();
            str = driver.getCurrentUrl();
        }
        redirectToHomePageTime = System.currentTimeMillis() - startTime;
        log(driver.getCurrentUrl());

        try {
            Thread.sleep(500);
            e = driver.findElement(menuButtonLocator);
            e.click();
            log("menu button clicked.");
        } catch (Exception err) {
            log(err.getMessage());
            err.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (Exception err) {
            log(err.getMessage());
            err.printStackTrace();
        }

        e = null;
        backup_url = "";
        switch (component) {
            case "Home":
                try {
                    e = driver.findElement(homeDropdownLocator);
                } catch (Exception err) {
                    log("no Home dropdown");
                    e = null;
                }
                backup_url = globals.url + "/lightning/page/home";
                break;
            case "Reports":
                try {
                    e = driver.findElement(reportsDropDownLocator);
                } catch (Exception err) {
                    log("no Reports dropdown");
                    e = null;
                }
                backup_url = globals.url + "/lightning/o/Report/home?queryScope=mru";
                break;
            case "eMPWR Participant Temps":
                try {
                    e = driver.findElement(participantTmpLocator);
                } catch (Exception err) {
                    log("no Participant dropdown");
                    e = null;
                }
                backup_url = globals.url + "/lightning/o/eMPWR_Participant_Temp__c/list?filterName=Recent";
                break;
            case "eMPWR Transactions":
                try {
                    e = driver.findElement(transDropDownLocator);
                } catch (Exception err) {
                    log("no Transaction dropdown");
                    e = null;
                }
                backup_url = globals.url + "/lightning/o/eMPWR_Transaction__c/list?filterName=Recent";
                break;
            case "Authorization Queue":
                try {
                    e = driver.findElement(authQueueLocator);
                } catch (Exception err) {
                    log("no Auth dropdown");
                    e = null;
                }
                backup_url = globals.url + "/lightning/n/Authorization_Queue";
                break;
        }
        totalTime = loadComponent(e, backup_url) + redirectToHomePageTime;
        return totalTime;
    }

    /**
     * Method loadComponent: automates loading of eMPWR-VA component from Home Page.
     * @param e is the web element to click for component navigation.
     * @param backup_url is string of the  url to navigate to in case element is not found clickable.
     * @return total time it took to load the component in milliseconds.
     */
    private long loadComponent(WebElement e, String backup_url) {
        long startTime = 0;
        long totalTime = 0;
        startTime = System.currentTimeMillis();

        log(driver.getCurrentUrl());

        try {
            if (e != null && e.isDisplayed() && e.isEnabled()) {
                try {
                    e.click();
                    log("Component element clicked.");
                } catch (Exception nd) {
                    log("java click error");
                    log(nd.getMessage());
                    nd.printStackTrace();
                    driver.get(backup_url);
                }
            } else {
                log("using back up url");
                driver.get(backup_url);
            }
            browser.waitForPageLoaded();
            totalTime = System.currentTimeMillis() - startTime;
            log(driver.getCurrentUrl());
            log("load component time: " + totalTime);
        } catch (Exception ex) {
            log(ex.getMessage());
            ex.getStackTrace().toString();
            totalTime = 0;
        }
        return totalTime;
    }

}
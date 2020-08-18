package gov.va.salesforceTests.pageObjects.Participants;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import gov.va.salesforceTests.framework.BrowserUtils;
import gov.va.salesforceTests.framework.Framework;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author Oswaldo Plazola
 * @version 1.0
 * Class GenericLookUps
 * Class for Salesforce Objects Navigation
 */
public class Participants extends Framework {

    private WebDriver driver;
    private BrowserUtils browser;
    private WebDriverWait wait;

    private By partIdLocator =          By.xpath("//input[@name='Participant Id']");
    private By searchButtonLocator =    By.xpath("//button[contains(text(),'Search')]");
    private String detailsAnchorXpath = "/html/body/div[4]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/lightning-tab-bar/ul/li[2]/a";
    private String detailsItemXpath =   "/html/body/div[4]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/slot/slot/slot/flexipage-tab2[2]/slot/flexipage-component2/slot/records-lwc-detail-panel/records-base-record-form/div/div/div/records-record-layout-event-broker/slot/records-lwc-record-layout/forcegenerated-detailpanel_contact___012t000000009jaaai___full___view___recordlayout2/force-record-layout-block/slot/force-record-layout-section/div/div/div/slot/force-record-layout-row[5]/slot/force-record-layout-item[1]/div/div/div[2]/span/slot[1]/slot/lightning-formatted-text";
    private String notesAnchorXpath =   "/html/body/div[4]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/lightning-tab-bar/ul/li[3]/a";
    private String notesItemXpath =     "/html/body/div[4]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/slot/slot/slot/flexipage-tab2[3]/slot/flexipage-component2/slot/c-e-m-p-w-r_-notes/div/div/lightning-datatable/div[2]/div/div/div/table/tbody/tr[1]/td[1]/lightning-primitive-cell-factory/span/div/lightning-base-formatted-text";

    /**
     * Method Participants is the Constructor.
     */
    public Participants() {
        driver = getInstance().getDriver();
        browser = new BrowserUtils(globals.implicitWait, globals.polling);
        wait = new WebDriverWait(driver, globals.implicitWait, globals.polling);
    }

    /**
     * Method searchParticipant enters a Participant SSN/TIN/FILE_NUMBER
     * @param part SSN/TIN/FILE_NUMBER
     * @param name string name of the participant
     * @param fallback_url is the url string to navigate if element is not found.
     * @return time in milliseconds it took to load all the object's items on the page.
     */
    public long searchParticipant(String part, String name, String fallback_url) {
        WebElement e;
        long startTime, totalTime = 0;
        String title = name;

        try {
            e = driver.findElement(partIdLocator);
            if (e.isDisplayed() && e.isEnabled()) {
                e.sendKeys(part);
                log("SENT KEYS " + part);
            } else {
                log("Participant input box not enabled!");
                return 0;
            }
        } catch (Exception ex) {
            log("could not find Participant input box");
            return 0;
        }

        try {
            e = driver.findElement(searchButtonLocator);
            if (!e.isDisplayed() && !e.isEnabled()) {
                log("Enabling Participant Search Button");
                String script = " " +
                        "var flag = 0;" +
                        "var count = 0;" +
                        "var content = '';" +
                        "var els = document.getElementsByTagName('button');" +
                        "for (let i=0; i < els.length; i++) { " +
                        "   content = els[i].textContent;" +
                        "   if(content == null) continue; " +
                        "   if(content.indexOf('Search') > -1) { " +
                        "     els[i].removeAttribute('disabled');" +
                        "     break;" +
                        "   }" +
                        "}" +
                        "return content;";
                String str = browser.runJavascript(script);
                log(str);
                Thread.sleep(500);
                e = driver.findElement(searchButtonLocator);
                Thread.sleep(500);
            }
            if (e.isDisplayed() && e.isEnabled()) {
                startTime = System.currentTimeMillis();
                e.click();
                log("Search clicked for participant " + part);
                int i = 0;
                while (!driver.getTitle().contains(title) && i < 200) {
                    Thread.sleep(500);
                    i++;
                }
                if(i > 199) {
                    log("participant search takes too long!");
                    return 0;
                }
                totalTime = System.currentTimeMillis() - startTime;
            } else {
                log("Participant Search button not enabled!");
                log("using backup url");
                startTime = System.currentTimeMillis();
                driver.get(fallback_url);
                browser.waitForTitle(title);
                totalTime = System.currentTimeMillis() - startTime;
            }
            return totalTime;
        } catch (Exception ex) {
            log("Did not find Participant Search button");
            return 0;
        }
    }

    /**
     * Method viewDetails loads detail of the Salesforce items page.
     * @param context string indicating whether you are testing transactions, notes, details, eFolder etc.
     * @return time in milliseconds it took to load the Object's item Details on the page.
     */
    public long viewDetails(String context) {
        By ByXpath = null;
        String itemXpath = "";
        WebElement a;

        switch (context) {
            case "details":
                ByXpath = By.xpath(detailsAnchorXpath);
                itemXpath = detailsItemXpath;
                break;
            case "notes":
                ByXpath = By.xpath(notesAnchorXpath);
                itemXpath = notesItemXpath;
                break;
        }

        long startTime;
        long totalTime;

        try {
            a = driver.findElement(ByXpath);
            log(a.getAttribute("outerHTML"));
            startTime = System.currentTimeMillis();
            if (a.isEnabled() && a.isDisplayed()) {
                a.click();
                log("CLICKED on Participant Context Link");
            } else {
                log("details is not clickable");
            }
            log("context: " + context);
            waitForDetails(itemXpath, context);
            totalTime = System.currentTimeMillis() - startTime;
            log("totaltime: " + totalTime);

        } catch (Exception ex) {
            log("Did not find tab or anchor for details.");
            log(ex.getMessage());
            ex.printStackTrace();
            return 0;
        }

        return totalTime;
    }

    /**
     * Method waitForDetails method to wait for all detail context calls to finish
     * @param itemXpath is the xpath string for the web element to wait for.
     * @param context is the context of the tests (details, notes, efolder, etc)
     */
    public void waitForDetails(String itemXpath, String context) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                String script = "" +
                        "var node = document.evaluate('" + itemXpath + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                        "if(node != null)  { " +
                        "  return node.value;" +
                        "} else { " +
                        "  return 'false';" +
                        "}";
                String result = ((JavascriptExecutor) driver).executeScript(script).toString();
                Boolean flag = (result.contains("false")) ? false : true;
                if (flag) System.out.println("Found context element!");
                return flag;
            }
        };
        try {
            wait.until(expectation);
        } catch (Throwable error) {
            log("WebDriver Wait ERROR");
            log(error.getMessage());
        }
        String script = "" +
                "var node = document.evaluate('" + itemXpath + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                "if(node != null)  { " +
                "  return node.value;" +
                "} else { " +
                "  return 'false';" +
                "}";
        String result = ((JavascriptExecutor) driver).executeScript(script).toString();
        log("******* Participant Detail ********************");
        log("Context detail: " + result);
        log("**********************************************");

        switch (context) {
            case "details":
                globals.participantDetails = result;
                break;
            case "notes":
                globals.notesDetails = result;
                break;
        }
    }
}
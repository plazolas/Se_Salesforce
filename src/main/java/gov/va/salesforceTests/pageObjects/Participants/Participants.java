package gov.va.salesforceTests.pageObjects.Participants;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import gov.va.salesforceTests.framework.BrowserUtils;
import gov.va.salesforceTests.framework.Framework;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;

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

    private By partIdLocator = By.xpath("//input[@name='Participant Id']");
    private By searchButtonLocator = By.xpath("//button[contains(text(),'Search')]");
    private String histXpath =        "/html/body/div[5]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/lightning-tab-bar/ul/li[1]/a";
    private String detailsXpath =     "/html/body/div[5]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/lightning-tab-bar/ul/li[2]/a";
    private String notesXpath =       "/html/body/div[4]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/lightning-tab-bar/ul/li[3]/a";
    //private String historyItemXpath = "/html/body/div[5]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/slot/slot/slot/flexipage-tab2[1]/slot/flexipage-component2/slot/flexipage-aura-wrapper/div/div/div/div[1]/div/div/div/div/div[1]/div[2]/div[2]/div[1]/div/div/table/tbody/tr[1]/th/span/div/a";
    private String historyItemXpath = "/html/body/div[5]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/slot/slot/slot/flexipage-tab2[1]/slot/flexipage-component2/slot/c-empwr_-view-transaction-history/div/article/div[1]/header/div[2]/div/h1";
    private String detailsItemXpath = "/html/body/div[5]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/slot/slot/slot/flexipage-tab2[2]/slot/flexipage-component2/slot/records-lwc-detail-panel/records-base-record-form/div/div/div/records-record-layout-event-broker/slot/records-lwc-record-layout/forcegenerated-detailpanel_contact___012t000000009jaaai___full___view___recordlayout2/force-record-layout-block/slot/force-record-layout-section/div/div/div";
    private String notesItemXpath =   "/html/body/div[4]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div/slot/slot/flexipage-component2/slot/flexipage-tabset2/div/lightning-tabset/div/slot/slot/slot/flexipage-tab2[3]/slot/flexipage-component2/slot/c-e-m-p-w-r_-notes/div/div/lightning-datatable/div[2]/div/div/div/table/tbody/tr[1]/td[1]/lightning-primitive-cell-factory/span/div/lightning-base-formatted-text";
    private String componentXpath =   "/html/body/div[5]/div[1]/div[2]/div[2]/div/div/div/section/div/div[2]/div[1]/div[1]/div/div/div/one-record-home-flexipage2/forcegenerated-adgrollup_component___forcegenerated__flexipage_recordpage___empwr_participant_recordpage___contact___view/forcegenerated-flexipage_empwr_participant_recordpage_contact__view_js/record_flexipage-record-page-decorator/div/slot/flexipage-record-home-single-col-template-desktop2/div/div[2]/div";

    /**
     * Method Participants is the Constructor.
     */
    public Participants() {
        driver = getInstance().getDriver();
        browser = new BrowserUtils(globals.implicitWait, globals.polling);
        wait = new WebDriverWait(driver, 200, 100);
    }

    /**
     * Method searchParticipant enters a Participant SSN/TIN/FILE_NUMBER
     *
     * @param part         SSN/TIN/FILE_NUMBER
     * @param name         string name of the participant
     * @param fallback_url is the url string to navigate if element is not found.
     * @return time in milliseconds it took to load all the object's items on the page.
     */
    public long searchParticipant(String part, String name, String fallback_url) {
        WebElement e;
        long startTime;
        long totalTime;
        String title = name;
        int i = 0;
        int timer;
        String str;
        String script;

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
                script = " " +
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
                str = browser.runJavascript(script);
                log(str);
                Thread.sleep(500);
                e = driver.findElement(searchButtonLocator);
                Thread.sleep(500);
            }
            if (e.isDisplayed() && e.isEnabled()) {
                startTime = System.currentTimeMillis();
                e.click();
                log("Search clicked for participant " + part);
                wait = new WebDriverWait(driver, 300, 100);
                wait.until( ExpectedConditions.refreshed(ExpectedConditions.titleContains(title)) );
                totalTime = System.currentTimeMillis() - startTime;

            } else {
                log("**********************************************");
                log("Participant Search button not enabled!");
                log("USING BACKUP URL");
                log("**********************************************");
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
     * Method viewDetails loads context details from a Participant Page.
     *
     * @param context string indicating whether you are testing transaction history, details, notes, eFolder etc.
     * @return time in milliseconds it took to load the Object's item Details on the page.
     */
    public long viewDetails(String context) {
        By ByXpath = null;
        String itemXpath = "";
        WebElement a = null;
        WebElement e = null;
        String script;
        String str;
        Boolean flag;
        long startTime = 0;
        long totalTime;

        log("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        log(driver.getCurrentUrl());
        log(driver.getTitle());
        log("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

        switch (context) {
            case "history":
                ByXpath = By.xpath(histXpath);
                itemXpath = historyItemXpath;
                break;
            case "details":
                ByXpath = By.xpath(detailsXpath);
                itemXpath = detailsItemXpath;
                break;
            case "notes":
                ByXpath = By.xpath(notesXpath);
                itemXpath = notesItemXpath;
                break;
        }

        if(context.contains("history")) {
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
            try {
                e = browser.findByTagAttributeText("div", "class", "pmcontainer");
                while (e != null) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("return arguments[0].setAttribute('style','z-index: -1');", e);
                    js.executeScript("return arguments[0].remove();", e);
                    log("***************************");
                    log("*** REMOVED pmcontainer ***");
                    log("***************************");
                    e = browser.findByTagAttributeText("div", "class", "pmcontainer");
                }
            } catch (Exception spinnerErr) {
                log("**********  COULD NOT REMOVE pmcontainer ***************");
            }
            try {
                flag = false;
                startTime = System.currentTimeMillis();
                totalTime = 0;
                int counter;
                int prev_counter = 0;
                while(flag == false && totalTime < 100000) {
                    List<WebElement> els = driver.findElements(By.tagName("div"));
                    log("\r\n tag: " + "div" +
                            "\r\n attribute: " + "flexipage-recordhomesinglecoltemplatedesktop2_recordhomesinglecoltemplatedesktop2" +
                            "\r\n text: " + "flexipage_tabset" +
                            "\r\n found flexipage-component2 tags: " + els.size());
                    counter = els.size();
                    if(prev_counter < counter) {
                        prev_counter = counter;
                        for (int i = 0; i < els.size(); i++) {
                            e = els.get(i);
                            if (e.getAttribute("flexipage-recordhomesinglecoltemplatedesktop2_recordhomesinglecoltemplatedesktop2") == "") {
                            //if (e.getAttribute("outerHTML") != null && e.getAttribute("outerHTML").contains("flexipage-recordhomesinglecoltemplatedesktop2_recordhomesinglecoltemplatedesktop2")) {
                                str = e.getAttribute("outerHTML");
                                log(str);
                                flag = true;
                                break;
                            } else {
                                e = null;
                            }
                        }
                    }
                    Thread.sleep(10000);
                    totalTime = System.currentTimeMillis() - startTime;
                    System.out.println(totalTime);
                }
                totalTime = System.currentTimeMillis() - startTime;
                return totalTime;
                /*
                if (e != null) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("return arguments[0].setAttribute('style','z-index: 9999');", e);
                    log("*** CHANGED Z-INDEX TO 9999 ***");
                }
                */
            } catch (Exception spinnerErr) {
                System.out.println("**********  COULD NOT CHANGE Z-INDEX TO 9999 ***************");
            }

            try {
                /*
                script = " " +
                        "var spinner = document.querySelectorAll('.slds-spinner_container');" +
                        "var content = '' ;" +
                        "var str = '' ;" +
                        "for (let i=0; i < spinner.length; i++) { " +
                        "   content = spinner[i].innerText;" +
                        "   if(content == null || content == 'undefined') continue; " +
                        "   str += '|' + content; " +
                        "}" +
                        "str = 'spinners: ' + spinner.length + '--' + str;" +
                        "return str;";
                browser.runJavascript(script);

                script = " " +
                        "var e = document.querySelector('.viewport');" +
                        "var hasVerticalScrollbar = e.scrollHeight > e.clientHeight;" +
                        "var str = ' viewport has vertical scroll: ' + hasVerticalScrollbar;" +
                        "return str;";
                browser.runJavascript(script);

                script = " " +
                        "var e = document.querySelector('.viewport');" +
                        "e.scrollBy(0, 1000);" +
                        "var str = ' scrolled down 1000px ';" +
                        "return str;";
                browser.runJavascript(script);


                script = " " +
                        "var nodes = document.evaluate('" + componentXpath + "', document, null, XPathResult.ANY_TYPE, null);" +
                        "if(nodes == null)  { " +
                        "  return 'nodes is null';" +
                        "}" +
                        "var component = null;" +
                        "var str = typeof nodes.iterateNext;" +
                        "if(str == 'function') { " +
                        "  component = nodes.iterateNext(); " +
                        "  str += 'component.iterateNext() '; " +
                        "} else {" +
                        "  str += ' **************** ';" +
                        "}" +
                        "if(component != null)  { " +
                        "  component.setAttribute('style','z-index: 99999');" +
                        "  return component.innerHTML;" +
                        "} else { " +
                        "  str += ' | component is null!';" +
                        "  return str;" +
                        "}";
                browser.runJavascript(script);
                */

                log("context: " + context);
                startTime = System.currentTimeMillis();
                waitForDetails(itemXpath, context);
                totalTime = System.currentTimeMillis() - startTime;
                log("totaltime: " + totalTime);
                return totalTime;

            } catch (Exception scrollErr) {
                System.out.println("** 0X *********** Exception!");
                log(scrollErr.getMessage());
            }
        }

        // Notes has not been implemented yet
        if (context.contains("notes")) {
            return 0;
        }

        if(!context.contains("history")) {
            try {
                a = driver.findElement(ByXpath);
                log(a.getAttribute("outerHTML"));
            } catch (Exception xpex) {
                log("XXXXXXXXXXXXXXXXXXXXXXX");
                log("Did not find context tab");
                log("XXXXXXXXXXXXXXXXXXXXXXX");
                log(xpex.getMessage());
            }
        } else {

        }


        try {
            if(!context.contains("history")) {
                if (a.isEnabled() && a.isDisplayed()) {
                    flag = false;
                    while (flag == false) {
                        try {
                            Thread.sleep(2000);
                            a.click();
                            flag = true;
                            log("CLICKED on Participant Context tab li");
                        } catch (Exception tabErr) {
                            str = tabErr.getMessage();
                            log(str);
                            setAttribute(a, "style", "background: 000");
                            flag = true;
                        }
                    }

                } else {
                    log("Details tab is not clickable");
                }
            }
            log("context: " + context);
            startTime = System.currentTimeMillis();
            waitForDetails(itemXpath, context);
            totalTime = System.currentTimeMillis() - startTime;
            log("totaltime: " + totalTime);

        } catch (Exception ex) {
            log("**********************************************");
            log("Did not find tab or li for " + context);
            log("**********************************************");
            log(ex.getMessage());
            ex.printStackTrace();
            return 0;
        }

        return totalTime;
    }

    /**
     * Method waitForDetails method to wait for all detail context calls to finish
     *
     * @param itemXpath is the xpath string for the web element to wait for.
     * @param context   is the context of the tests (details, notes, efolder, etc)
     */
    public void waitForDetails(String itemXpath, String context) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                String script = "" +
                        "var node = document.evaluate('" + itemXpath + "', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                        "if(node != null)  { " +
                        "  return node.textContent;" +
                        "} else { " +
                        "  return 'CONTENT DETAILS NOT FOUND: ' + node;" +
                        "}";
                String result = ((JavascriptExecutor) driver).executeScript(script).toString();
                Boolean flag = (result.contains("DETAILS NOT FOUND")) ? false : true;
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
            case "history":
                globals.historyDetails = result;
                break;
            case "details":
                globals.participantDetails = result;
                break;
            case "notes":
                globals.notesDetails = result;
                break;
        }
    }

    public void setAttribute(WebElement element, String attName, String attValue) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);",
                element, attName, attValue);
    }
}
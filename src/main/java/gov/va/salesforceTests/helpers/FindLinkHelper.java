package gov.va.salesforceTests.helpers;

import gov.va.salesforceTests.framework.Framework;
import gov.va.salesforceTests.framework.BrowserUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import java.util.*;

/**
 * Class FindLinkHelper
 *       helper class to find Web Elements in various ways
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class FindLinkHelper extends Framework {

    private BrowserUtils browser;
    private static JavascriptExecutor js = (JavascriptExecutor) driver;

    public FindLinkHelper() {
        driver = super.getInstance().getDriver();
        browser = new BrowserUtils(0, 0);
    }

    /**
     * Method byText
     *       Finds href of anchor inside an article tag given a text. Navigates to anchor's url
     *
     * @param text : inner text of the anchor to find and navigate to.
     * @param fallback_url : url to navigate to in case element is not enabled.
     * @return time in milliseconds it took to navigate to anchor's load page or frame.
     */
    public long byTextInArticle(String text, String fallback_url) {

        Boolean flag = false;
        List<WebElement> els, articles;
        WebElement element;
        String str = "";
        String url = "";
        int i, j = 0;
        long startTime = 0;
        try {
            articles = driver.findElements(By.tagName("article"));
            log("articles found: " + articles.size());
            if (articles.size() > 0) {
                for (i = 0; i < articles.size(); i++) {
                    WebElement article = articles.get(i);
                    els = article.findElements(By.tagName("a"));
                    for (j = 0; j < els.size(); j++) {
                        element = els.get(j);
                        str = element.getAttribute("innerHTML");
                        if (str.contains(text)) {
                            url = element.getAttribute("href");
                            log("findLinkHelper: url: " + url);
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            } else {
                return 0;
            }
        } catch (Exception ex) {
            log("***** Exception: COULD NOT FIND ANCHOR for a TRANSACTION containing " + text);
            log("Error Message: " + ex.getMessage());
            return 0;
        }

        if (!flag) {
            log("could NOT FIND ANCHOR for a LINK containing " + text + ": resorting to hardcoded link");
            url = fallback_url;
        }
        startTime = System.currentTimeMillis();
        driver.get(url);
        browser.waitForPageLoaded();
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Method findTagInArticlesByText
     *       Finds and Navigates to anchor's url given a tag, a web element's inner text.
     *
     * @param tag : html tag to find.
     * @param text : inner text of the tag to find.
     * @param fallback_url : url to navigate to in case the web element is not enabled.
     * @return time in milliseconds it took to navigate to anchor's load page or frame.
     */
    public long findAndNavigateInArticlesByTagText(String tag, String text, String fallback_url) {

        Boolean flag = false;
        List<WebElement> els, articles;
        WebElement e, element;
        String str, url = "";
        int i, j, k = 0;
        long startTime = 0;
        try {
            articles = driver.findElements(By.tagName("article"));
            log("articles found: " + articles.size());
            if (articles.size() > 0) {
                for (i = 0; i < articles.size(); i++) {
                    WebElement article = articles.get(i);
                    els = article.findElements(By.tagName(tag));
                    for (j = 0; j < els.size(); j++) {
                        element = els.get(j);
                        str = element.getAttribute("innerHTML");
                        log("innerHTML: " + str);
                        if (str.contains(text)) {
                            url = element.getAttribute("href");
                            log("findTagInArticle: url: " + url);
                            log("element FOUND with text: " + element.getText());
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            } else {
                return 0;
            }
        } catch (Exception ex) {
            log("***** Exception: COULD NOT FIND ANCHOR for a LINK containing " + text);
            log("Error Message: " + ex.getMessage());
            return 0;
        }

        if (!flag) {
            log("could NOT FIND ANCHOR for a LINK containing " + text + ": resorting to hardcoded link");
            url = fallback_url;
        }
        if(url.length() > 0) {
            startTime = System.currentTimeMillis();
            driver.get(url);
            browser.waitForPageLoaded();
            return System.currentTimeMillis() - startTime;
        } else {
            return 0;
        }
    }

    /**
     * Method findTagInTablesByText
     *       Finds and Navigates to anchor's url given a tag, a web element's inner text
     *       within tables in a page.
     *
     * @param tag : html tag to find.
     * @param text : inner text of the tag to find.
     * @param fallback_url : url to navigate to in case the web element is not enabled.
     * @return time in milliseconds it took to navigate to anchor's load page or frame.
     */
    public long findAndClickTagInTablesByText(String tag, String text, String fallback_url) {

        Boolean flag = false;
        List<WebElement> els, tags;
        WebElement e, table;
        String str = "";
        String url = "";
        int i, j = 0;
        long startTime = 0;
        try {
            els = driver.findElements(By.tagName("table"));
            log("Found tables: " + els.size());
            if (els.size() > 0) {
                for (i = 0; i < els.size(); i++) {
                    table = els.get(i);
                    tags = table.findElements(By.tagName(tag));
                    for (j = 0; j < tags.size(); j++) {
                        e = tags.get(j);
                        str = e.getText();
                        if (str.contains(text)) {
                            url = e.getAttribute("href");
                            log("url: " + url);
                            flag = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log("***** Exception: COULD NOT FIND ANCHOR for a TRANSACTION containing " + text);
            log("Error Message: " + ex.getMessage());
            return 0;
        }

        if (!flag) {
            log("could NOT FIND ANCHOR for a LINK containing " + text + ": resorting to hardcoded link");
            url = fallback_url;
        }
        if(url.length() > 0) {
            startTime = System.currentTimeMillis();
            driver.get(url);
            browser.waitForPageLoaded();
            return System.currentTimeMillis() - startTime;
        } else {
            return 0;
        }
    }

    /**
     * Method viewDetails
     *       Clicks on the Details tab of an Salesforce Object item.
     * @param text is the text of the element to click.
     * @param detail is the string value to check for.
     * @return time in milliseconds it took to load the objects details.
     */
    public long viewDetails(String text, String detail) {

        long startTime, totalTime;

        startTime = System.currentTimeMillis();
        try {
            browser.clickByText(text);
            browser.waitForClickableByText(detail);
            totalTime = System.currentTimeMillis() - startTime;
            return totalTime;
        } catch (Exception e) {
            log(e.getMessage());
            return 0;
        }
    }

}
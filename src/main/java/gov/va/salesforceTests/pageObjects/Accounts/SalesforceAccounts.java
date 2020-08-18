package gov.va.salesforceTests.pageObjects.Accounts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import gov.va.salesforceTests.framework.BrowserUtils;
import gov.va.salesforceTests.framework.Framework;
import org.openqa.selenium.JavascriptExecutor;

import java.util.List;

/**
 * Class SalesforceAccounts
 *       Class for Salesforce Account Object Navigation
 *
 * @author Oswaldo Plazola
 * @version 1.0
 */
public class SalesforceAccounts extends Framework {
    
    private WebDriver driver;
    private BrowserUtils browser;
    private JavascriptExecutor js;

    /**
     * Method SalesforceAccounts is the Constructor.
     */
    public SalesforceAccounts() {
        driver = getInstance().getDriver();
        browser = new BrowserUtils(0,0);
        js = (JavascriptExecutor) driver;
    }

    /**
     * Method viewAll clicks on "View All" link on an Account Object items list page.
     * @return time in milliseconds it took to load all the object's items on the page.
     */
    public long viewAll() {
        Boolean flag = false;
        List<WebElement> els, articles;
        WebElement element = null;
        String str = "";
        int i, j;
        long startTime = 0;
        long totalTime = 0;

        startTime = System.currentTimeMillis();
        browser.clickByText("View All");
        browser.waitForPageLoaded();
        totalTime = System.currentTimeMillis() - startTime;
        if(totalTime > 0){
            return totalTime;
        }

        try {
            log("LOOKING FOR ARTICLES");
            articles = driver.findElements(By.tagName("article"));
            log("articles found: " + articles.size());
            if (articles.size() > 0) {
                for (i = 0; i < articles.size(); i++) {
                    WebElement article = articles.get(i);
                    els = article.findElements(By.tagName("footer"));
                    log("footers: " + els.size());
                    for (j = 0; j < els.size(); j++) {
                        element = els.get(j);
                        str = element.getAttribute("innerHTML");
                        if (str.contains("c-empwr_accountantworkqueue_empwr_accountantworkqueue")) {
                            log("found view all link");
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
            log("***** Exception: COULD NOT FIND View All link ");
            log("Error Message: " + ex.getMessage());
            return 0;
        }

        if (flag == true) {
            startTime = System.currentTimeMillis();
            element.click();
            browser.waitForPageLoaded();
            return System.currentTimeMillis() - startTime;
        } else {
            return 0;
        }

    }

    /**
     * Method viewOne loads on of the Account items in a Salesforce list page.
     * @param Name is the string name of the item to load
     * @param backup_url is the url to load the item.
     * @return time in milliseconds it took to load the Object's item on the page.
     */
    public long viewOne(String Name, String backup_url) {

        long startTime, totalTime;
        WebElement e;

        e = browser.findByTagAttributeText("a", "title", Name);

        if (e != null) {
            try {
                log("Found The One Link for: " + Name);
                startTime = System.currentTimeMillis();
                e.click();
                browser.waitForPageLoaded();
                totalTime = System.currentTimeMillis() - startTime;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                totalTime = 0;
            }
        } else {
            log("no link found by Selenium Java: " + Name);
            startTime = System.currentTimeMillis();
            browser.clickByText(Name);
            browser.waitForPageLoaded();
            totalTime = System.currentTimeMillis() - startTime;

            String str = js.executeScript("return document.title;").toString();
            log(str);

            if(totalTime > 0){
                return totalTime;
            }

            /**
             * last resort use backup_url
             */
            startTime = System.currentTimeMillis();
            driver.get(backup_url);
            browser.waitForPageLoaded();
            totalTime = System.currentTimeMillis() - startTime;
        }

        return totalTime;
    }
    
}

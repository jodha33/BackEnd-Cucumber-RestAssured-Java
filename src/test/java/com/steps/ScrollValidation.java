package com.steps;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class ScrollValidation {


    public static void main(String arg[]) throws InterruptedException {
        int noofTestData=1000;
        int totalNoOfRowInOneScroll=10;
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "E://Jodha- Directories//Jodha-Office//Tools//Selenium//chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://infinite-scroll.com/demo/full-page/page2.html");

        for(int i=1;i<=noofTestData/totalNoOfRowInOneScroll;i++){
            JavascriptExecutor js = (JavascriptExecutor) driver;

            //This will scroll the web page till end.
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            JSWaiter.setDriver(driver);

           /* JSWaiter.waitForJQueryLoad();
            JSWaiter.waitUntilJQueryReady();
            JSWaiter.waitForAngularLoad();
            JSWaiter.waitUntilAngularReady();*/
            JSWaiter.waitUntilJSReady();
            Thread.sleep(1000);

            //Validation of Data
            int currentTotalRowCount=driver.findElements(By.xpath("//table/tr")).size();
            Assert.assertTrue(currentTotalRowCount>=i*noofTestData);

        }
    }
}

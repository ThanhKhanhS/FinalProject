package org.example.base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.testng.annotations.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    private static final String Screenshot_Dir = System.getProperty("user.dir") + File.separator + "src/test/java/resources/screenshot";
    private static int index = 0;
    @BeforeMethod
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext(new Browser.NewContextOptions().setStorageState(null));
        page = context.newPage();
    }

    @AfterMethod
    public void teardown() {
        page.close();
        context.close();
        browser.close();
        playwright.close();
    }
    private String buildFileName(int index){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return String.format("%02d_%s.png",index,timestamp);
    }
    protected void captureScreenshotFullPage(){
        index ++;
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(Screenshot_Dir,buildFileName(index))).setFullPage(true));
    }
    protected void captureScreenshotElement(Locator locator){
        index ++;
        locator.waitFor();
        locator.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(Screenshot_Dir,buildFileName(index))));
    }
    protected void captureScreenshot(){
        index ++;
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(Screenshot_Dir,buildFileName(index))));
    }
    protected void waitForPage(){
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(2_000);
    }
}
package org.example.base;

import com.microsoft.playwright.Page;

import java.nio.file.Paths;

public class BasePage {

    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    public void navigate(String url) {
        page.navigate(url);
    }

    public void fill(String xpath, String value) {
        page.locator(xpath).fill(value);
    }

    public void click(String xpath) {
        page.locator(xpath).click();
    }

    public void check(String xpath) {
        page.locator(xpath).check();
    }

    public void upload(String xpath, String filePath) {
        page.locator(xpath).setInputFiles(Paths.get(filePath));
    }

    public String getText(String xpath) {
        return page.locator(xpath).innerText();
    }

    public void hover(String xpath) {
        page.locator(xpath).hover();
    }

    public void waitForSelector(String xpath){
        page.waitForSelector(xpath);
    }

    public boolean isVisible(String xpath){
        return page.locator(xpath).isVisible();
    }
}
package org.example.pages;

import com.microsoft.playwright.Page;
import org.example.base.BasePage;

public class EcommerceInsightsPage extends BasePage {
    private static final String Examples_Menu          = "//li[@role='treeitem']//div[text()='Examples']";
    private static final String Ecommerce_DashBoard    = "//div[normalize-space(text())='E-commerce Insights']";
    private static final String Deeper_Dive_Section    = "//h2[text()='Deeper Dive']";
    private static final String Widget_Bar_Total_Order = "//div[@data-testid='chart-container']//*[local-name()='path' and @fill='#227FD2' and @stroke-linejoin='bevel']";
    private static final String Tooltip_Pie_Chart      = "//div[@data-testid='echarts-tooltip']//td[text()='Widget']/following-sibling::td[@class='g3M5f hrOS9']";
    private static final String Category_Filter        = "//button[@aria-label='Product Category']";
    private static final String Filter_Option          = "//span[text()='%s']/ancestor::li//input";
    private static final String Checkbox_Select_All    = "//div[text()='Select all']/ancestor::label";
    private static final String Update_Filter_Btn      = "//span[text()='Update filter']/ancestor::button";
    private static final String Revenue_Per_Quarter    = "//div[@data-testid='scalar-container']/span/h1";
    private static final String Revenue_Goal           = "//div[@data-testid='progress-bar']/preceding-sibling::div//div[text()]";
    private static final String Total_Order            = "//div[@data-testid='scalar-container']/div/h1";
    private static final String Q2_2026_Point          = "(//div[@data-testid='chart-container']//*[local-name()='svg']//*[local-name()='path' and @fill='#7172AD'])[5]";
    private static final String Tooltip_Q2             = "//div[@data-testid='echarts-tooltip']//div[text()='Q2 2026']/following-sibling::div//td[text()='%s']/following-sibling::td[@class='g3M5f hrOS9']";
    private static final String Widget_Total_Order     = "//*[local-name()='text' and text()='Total']/preceding-sibling::*[local-name()='text' and @text-anchor='middle']";
    public EcommerceInsightsPage(Page page) {
        super(page);
    }

    public void clickExample() {
        click(Examples_Menu);
        page.waitForLoadState();
    }

    public void clickEcommerceInsights() {
        click(Ecommerce_DashBoard);
        page.waitForLoadState();
    }

    public void scrollToDeepDive() {
        page.locator(Deeper_Dive_Section).scrollIntoViewIfNeeded();
        page.waitForTimeout(1000);
    }
    public void scrollReturn(){
        page.keyboard().press("Home");
        page.waitForTimeout(500);
    }

    public void hoverOnWidgetTotalOrders() {
        hover(Widget_Bar_Total_Order);
        page.waitForTimeout(900);
    }

    public String getTooltipText() {
        return getText(Tooltip_Pie_Chart);
    }

    public void filterByCategory(String categoryName) {
        click(Category_Filter);
        click(Checkbox_Select_All);
        String optionXpath = String.format(Filter_Option, categoryName);
        click(optionXpath);
        click(Update_Filter_Btn);
        page.waitForLoadState();
    }

    public String getRevenuePerQuarter() {
        return getText(Revenue_Per_Quarter);
    }

    public String getRevenueGoal(){
        return getText(Revenue_Goal);
    }

    public String getTotalOrders(){
        return getText(Total_Order);
    }

    public void hoverQ2_2026(){
        hover(Q2_2026_Point);
        page.waitForTimeout(900);
    }

    public String getTooltipTextQ2(String Name){
        String optionXpath = String.format(Tooltip_Q2,Name);
        return getText(optionXpath);
    }

    public String getWidgetTotalOrder(){
        return page.locator(Widget_Total_Order).textContent();
    }
}

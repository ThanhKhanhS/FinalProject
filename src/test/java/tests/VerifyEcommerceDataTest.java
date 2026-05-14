package tests;

import org.example.base.BaseTest;
import org.example.pages.EcommerceInsightsPage;
import org.example.pages.LoginPage;
import org.example.utils.Database;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class VerifyEcommerceDataTest extends BaseTest {
    private LoginPage loginPage;
    private static final String Email    = "khanhsnguyen03@gmail.com";
    private static final String Password = "fanmu567";
    private static final String Metabase_Url = "http://localhost:3000/";

    private String pre_con1;
    private String pre_con2;
    private String pre_con3;
    private String pre_con4;
    @BeforeMethod
    public void preparePreCondition(){
        Database database = new Database();

        List<Map<String,Object>> valueOfCondition1 = database.executeSqlScript("""
                SELECT COUNT(o.ID) AS Total_Orders
                FROM ORDERS o
                JOIN PRODUCTS p ON o.PRODUCT_ID = p.ID
                WHERE p.CATEGORY = 'Widget'
                AND o.CREATED_AT >= '2025-01-01'
                AND o.CREATED_AT <= '2027-01-01';
                """);
        pre_con1 = valueOfCondition1.getFirst().get("TOTAL_ORDERS").toString();

        List<Map<String,Object>> valueOfCondition2 = database.executeSqlScript("""
                SELECT COUNT(o.ID) AS Total_Orders_Q2_2026
                FROM ORDERS o
                JOIN PRODUCTS p ON o.PRODUCT_ID = p.ID
                WHERE p.CATEGORY = 'Widget'
                  AND o.CREATED_AT >= '2026-04-01'
                  AND o.CREATED_AT < '2026-07-01';
                """);
        pre_con2 = valueOfCondition2.getFirst().get("TOTAL_ORDERS_Q2_2026").toString();

        List<Map<String,Object>> valueOfCondition3 = database.executeSqlScript("""
                SELECT SUM(o.TOTAL) AS Total_Revenue_Q2_2026
                FROM ORDERS o
                JOIN PRODUCTS p ON o.PRODUCT_ID = p.ID
                WHERE p.CATEGORY = 'Widget'
                  AND o.CREATED_AT >= '2026-04-01'
                  AND o.CREATED_AT < '2026-07-01';
                """);
        pre_con3 = valueOfCondition3.getFirst().get("TOTAL_REVENUE_Q2_2026").toString();

        List<Map<String,Object>> valueOfCondition4 = database.executeSqlScript("""
                SELECT SUM(o.QUANTITY ) AS Total_Quantity_Sold_Q2_2026
                FROM ORDERS o
                JOIN PRODUCTS p ON o.PRODUCT_ID = p.ID
                WHERE p.CATEGORY = 'Widget'
                  AND o.CREATED_AT >= '2026-04-01'
                  AND o.CREATED_AT < '2026-07-01';
                """);
        pre_con4 = valueOfCondition4.getFirst().get("TOTAL_QUANTITY_SOLD_Q2_2026").toString();
    }
    @Test
    public void TC01_ecommerceInsightsVerification(){
        loginPage = new LoginPage(page);
        EcommerceInsightsPage dashboardPage = new EcommerceInsightsPage(page);

        //Negative to Metabase
        page.navigate(Metabase_Url);
        captureScreenshotFullPage();

        //Login
        loginPage.login(Email,Password);
        waitForPage();
        Assert.assertTrue(loginPage.isOnHomePage());
        captureScreenshotFullPage();

        //Click Examples
        dashboardPage.clickExample();
        waitForPage();
        captureScreenshot();

        //Click E-commerce Insights
        dashboardPage.clickEcommerceInsights();
        waitForPage();
        captureScreenshotFullPage();

        //Scroll down to Deeper Dive section
        dashboardPage.scrollToDeepDive();
        captureScreenshotFullPage();

        //Hover to the Widget part
        dashboardPage.hoverOnWidgetTotalOrders();
        waitForPage();
        captureScreenshot();

        //Verify tooltip matches pre-condition1
        String tooltipText = dashboardPage.getTooltipText();
        int actualTotalOrders = Integer.parseInt(tooltipText.replace(",",""));
        int expectedTotalOrders = Integer.parseInt(pre_con1);
        Assert.assertEquals(actualTotalOrders,expectedTotalOrders);
        captureScreenshotFullPage();

        dashboardPage.scrollReturn();

        //Filter Widget
        dashboardPage.filterByCategory("Widget");
        waitForPage();
        captureScreenshotFullPage();

        //Verify Revenue Per Quarter
        String revenuePerQuarter = dashboardPage.getRevenuePerQuarter();
        int actualRevenue   = Integer.parseInt(revenuePerQuarter.replace(",",""));
        int expectedRevenue = (int) Double.parseDouble(pre_con3);
        Assert.assertEquals(actualRevenue,expectedRevenue);
        captureScreenshotFullPage();

        //Verify Revenue Goal
        String revenueGoal = dashboardPage.getRevenueGoal();
        int actualRevenueGoal   = Integer.parseInt(revenueGoal.replace(",",""));
        int expectedRevenueGoal = (int) Double.parseDouble(pre_con3);
        Assert.assertEquals(actualRevenueGoal,expectedRevenueGoal);
        captureScreenshotFullPage();

        //Verify Total Order
        String totalOrdersQuarter = dashboardPage.getTotalOrders();
        int actualTotalOrder = Integer.parseInt(totalOrdersQuarter);
        int expectedTotalOrder = Integer.parseInt(pre_con2);
        Assert.assertEquals(actualTotalOrder,expectedTotalOrder);
        captureScreenshotFullPage();

        dashboardPage.scrollToDeepDive();

        // Hover Q2 2026 in Revenue and orders over time
        dashboardPage.hoverQ2_2026();
        waitForPage();
        captureScreenshot();

        //Verify Revenue value in Q2 tooltip
        String tooltipTextQ2 = dashboardPage.getTooltipTextQ2("Revenue");
        int uiValue = (int) Double.parseDouble(tooltipTextQ2.replace(",",""));
        int dbValue = (int) Double.parseDouble(pre_con3);
        Assert.assertEquals(uiValue,dbValue);

        //Verify Orders value in Q2 tooltip
        String tooltipTextQ2Order = dashboardPage.getTooltipTextQ2("Orders");
        int uiValueOrder = Integer.parseInt(tooltipTextQ2Order.replace(",",""));
        int dbValueOder = Integer.parseInt(pre_con4);
        Assert.assertEquals(uiValueOrder,dbValueOder);
        captureScreenshotFullPage();

        //Verify Orders by product category
        String widgetTotalOrder = dashboardPage.getWidgetTotalOrder();
        int uiValueTotalOrder = Integer.parseInt(widgetTotalOrder.replace(",",""));
        int dbValueTotalOrder = Integer.parseInt(pre_con1);
        Assert.assertEquals(uiValueTotalOrder,dbValueTotalOrder);
        captureScreenshotFullPage();

        //Logout
        loginPage.logout();
        waitForPage();
        captureScreenshotFullPage();
    }
}

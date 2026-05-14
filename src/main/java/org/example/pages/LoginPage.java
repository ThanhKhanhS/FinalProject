package org.example.pages;
import com.microsoft.playwright.Page;
import org.example.base.BasePage;

public class LoginPage extends BasePage {
    private static final String Email_Input    = "//label[normalize-space()='Email address']/following-sibling::div/input";
    private static final String Password_Input = "//label[normalize-space()='Password']/following-sibling::div/input";
    private static final String Sign_In_Btn    = "//button[@type='submit']";
    private static final String LogOut_Btn     = "//button/div[text()='Sign out']";
    private static final String Account_menu   = "//button[@aria-label='Settings']";

    public LoginPage(Page page) {
        super(page);
    }

    public void login(String email, String password) {
        fill(Email_Input,email);
        fill(Password_Input,password);
        click(Sign_In_Btn);
        page.waitForLoadState();
    }
    public void logout(){
        click(Account_menu);
        click(LogOut_Btn);
    }
    public boolean isOnHomePage(){
        return page.locator("//a/div[text()='Home']").isVisible();
    }
}

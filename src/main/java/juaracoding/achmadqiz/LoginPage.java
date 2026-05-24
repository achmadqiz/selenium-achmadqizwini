package juaracoding.achmadqiz;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    // Locators
    private By usernameField  = By.id("user-name");
    private By passwordField  = By.id("password");
    private By loginButton    = By.id("login-button");
    private By errorMessage   = By.cssSelector("[data-test='error']");
    private By inventoryTitle = By.className("title");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        field.clear();
        field.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void doLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoginSuccessful() {
        try {
            shortWait.until(ExpectedConditions.urlContains("inventory.html"));
            WebElement title = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(inventoryTitle)
            );
            return title.getText().equalsIgnoreCase("Products");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginPageDisplayed() {
        return !driver.findElements(usernameField).isEmpty()
            && !driver.findElements(passwordField).isEmpty()
            && !driver.findElements(loginButton).isEmpty();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getInventoryTitle() {
        try {
            WebElement title = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(inventoryTitle)
            );
            return title.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            WebElement error = wait.until(
                ExpectedConditions.visibilityOfElementLocated(errorMessage)
            );
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isErrorDisplayed() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

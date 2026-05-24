package juaracoding.achmadqiz;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait shortWait;

    private By cartTitle = By.className("title");
    private By cartItems = By.className("cart_item");
    private By itemNames = By.className("inventory_item_name");
    private By itemQuantities = By.className("cart_quantity");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    public boolean isCartPageLoaded() {
        return driver.getCurrentUrl().contains("cart.html")
            && getPageTitle().equalsIgnoreCase("Your Cart");
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(cartTitle)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public boolean isProductDisplayed(String productName) {
        return driver.findElements(itemNames)
            .stream()
            .anyMatch(item -> item.getText().equals(productName));
    }

    public String getProductQuantity(String productName) {
        List<WebElement> names = driver.findElements(itemNames);
        List<WebElement> quantities = driver.findElements(itemQuantities);

        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).getText().equals(productName) && i < quantities.size()) {
                return quantities.get(i).getText();
            }
        }

        return "";
    }
}

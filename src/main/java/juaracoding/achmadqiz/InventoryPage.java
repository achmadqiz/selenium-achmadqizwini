package juaracoding.achmadqiz;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InventoryPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait shortWait;

    private By inventoryContainer = By.id("inventory_container");
    private By inventoryTitle = By.className("title");
    private By cartBadge = By.className("shopping_cart_badge");
    private By cartLink = By.className("shopping_cart_link");
    private By addBackpackButton = By.id("add-to-cart-sauce-labs-backpack");
    private By removeBackpackButton = By.id("remove-sauce-labs-backpack");
    private By addBikeLightButton = By.id("add-to-cart-sauce-labs-bike-light");
    private By removeBikeLightButton = By.id("remove-sauce-labs-bike-light");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
    }

    public boolean isInventoryPageLoaded() {
        return driver.getCurrentUrl().contains("inventory.html")
            && !driver.findElements(inventoryContainer).isEmpty();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        try {
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(inventoryTitle)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public void clickAddToCartFirstProduct() {
        clickAddBackpack();
    }

    public void clickAddBackpack() {
        clickElement(addBackpackButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(removeBackpackButton));
    }

    public void clickRemoveBackpack() {
        clickElement(removeBackpackButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addBackpackButton));
    }

    public void clickAddBikeLight() {
        clickElement(addBikeLightButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(removeBikeLightButton));
    }

    public void clickRemoveBikeLight() {
        clickElement(removeBikeLightButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(addBikeLightButton));
    }

    public void openCart() {
        clickElement(cartLink);
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    public String getCartBadgeCount() {
        try {
            WebElement badge = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(cartBadge)
            );
            return badge.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isCartBadgeDisplayed() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCartBadgeNotDisplayed() {
        try {
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(cartBadge));
            return true;
        } catch (Exception e) {
            return driver.findElements(cartBadge).isEmpty();
        }
    }

    public boolean waitUntilCartBadgeCountIs(String expectedCount) {
        try {
            return shortWait.until(
                ExpectedConditions.textToBe(cartBadge, expectedCount)
            );
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstProductButtonText() {
        return getBackpackButtonText();
    }

    public String getBackpackButtonText() {
        return getProductButtonText(addBackpackButton, removeBackpackButton);
    }

    public String getBikeLightButtonText() {
        return getProductButtonText(addBikeLightButton, removeBikeLightButton);
    }

    private String getProductButtonText(By addButton, By removeButton) {
        if (!driver.findElements(removeButton).isEmpty()) {
            return driver.findElement(removeButton).getText();
        }

        if (!driver.findElements(addButton).isEmpty()) {
            return driver.findElement(addButton).getText();
        }

        return "";
    }

    private void clickElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}

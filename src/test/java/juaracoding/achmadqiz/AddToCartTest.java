package juaracoding.achmadqiz;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AddToCartTest extends BaseTest {

    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String LOCKED_USERNAME = "locked_out_user";
    private static final String ERROR_LOCKED_USER = "Epic sadface: Sorry, this user has been locked out.";
    private static final String BACKPACK_PRODUCT = "Sauce Labs Backpack";
    private static final String BIKE_LIGHT_PRODUCT = "Sauce Labs Bike Light";

    @Test(
        priority = 1,
        groups = {"cart", "positive", "smoke"},
        description = "TC_INV_001 - Add to Cart berhasil dengan user valid",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testAddToCartPositif() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.clickAddToCartFirstProduct();

        assertProductAdded(inventoryPage, "1");
        Assert.assertEquals(
            inventoryPage.getFirstProductButtonText().toLowerCase(),
            "remove",
            "Tombol produk pertama seharusnya berubah menjadi 'Remove' setelah produk ditambahkan"
        );
    }

    @Test(
        priority = 2,
        groups = {"cart", "negative", "regression"},
        description = "TC_INV_002 - Add to Cart gagal karena akun dikunci",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testAddToCartNegatifAkunTerkunci() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.doLogin(LOCKED_USERNAME, VALID_PASSWORD);

        Assert.assertFalse(
            loginPage.isLoginSuccessful(),
            "Login dengan locked_out_user seharusnya gagal"
        );
        Assert.assertTrue(
            loginPage.isErrorDisplayed(),
            "Pesan error seharusnya muncul untuk locked_out_user"
        );
        Assert.assertEquals(
            loginPage.getErrorMessage().trim(),
            ERROR_LOCKED_USER,
            "Pesan error locked_out_user tidak sesuai"
        );
        Assert.assertTrue(
            loginPage.isLoginPageDisplayed(),
            "User terkunci seharusnya tetap berada di halaman login"
        );

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertFalse(
            inventoryPage.isInventoryPageLoaded(),
            "Halaman inventory seharusnya tidak bisa diakses oleh locked_out_user"
        );
        Assert.assertFalse(
            inventoryPage.getCurrentUrl().contains("inventory.html"),
            "User terkunci tidak boleh diarahkan ke inventory. Actual URL: " + inventoryPage.getCurrentUrl()
        );
    }

    @Test(
        priority = 3,
        groups = {"cart", "negative", "regression"},
        description = "TC_INV_003 - Add to Cart gagal karena belum login",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testAddToCartNegatifTanpaLogin() {
        driver.get(BASE_URL + "/inventory.html");

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

        Assert.assertFalse(
            inventoryPage.isInventoryPageLoaded(),
            "Halaman inventory seharusnya tidak tampil tanpa login"
        );
        Assert.assertFalse(
            driver.getCurrentUrl().contains("inventory.html"),
            "User tanpa login tidak boleh tetap berada di inventory. Actual URL: " + driver.getCurrentUrl()
        );
        Assert.assertTrue(
            loginPage.isLoginPageDisplayed(),
            "User tanpa login seharusnya diarahkan ke halaman login"
        );
    }

    @Test(
        priority = 4,
        groups = {"cart", "positive", "regression"},
        description = "TC_INV_004 - Add dua produk berhasil dan badge menjadi 2",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testAddDuaProdukKeCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.clickAddBackpack();
        inventoryPage.clickAddBikeLight();

        Assert.assertTrue(
            inventoryPage.waitUntilCartBadgeCountIs("2"),
            "Badge cart seharusnya bernilai 2 setelah dua produk ditambahkan. Actual: " + inventoryPage.getCartBadgeCount()
        );
        Assert.assertEquals(
            inventoryPage.getBackpackButtonText().toLowerCase(),
            "remove",
            "Tombol Backpack seharusnya berubah menjadi Remove"
        );
        Assert.assertEquals(
            inventoryPage.getBikeLightButtonText().toLowerCase(),
            "remove",
            "Tombol Bike Light seharusnya berubah menjadi Remove"
        );
    }

    @Test(
        priority = 5,
        groups = {"cart", "positive", "regression"},
        description = "TC_INV_005 - Remove produk dari cart berhasil",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testRemoveProdukSetelahAddToCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.clickAddBackpack();
        assertProductAdded(inventoryPage, "1");

        inventoryPage.clickRemoveBackpack();

        Assert.assertTrue(
            inventoryPage.isCartBadgeNotDisplayed(),
            "Badge cart seharusnya hilang setelah satu-satunya produk di-remove"
        );
        Assert.assertEquals(
            inventoryPage.getBackpackButtonText().toLowerCase(),
            "add to cart",
            "Tombol Backpack seharusnya kembali menjadi 'Add to cart' setelah produk di-remove"
        );
    }

    @Test(
        priority = 6,
        groups = {"cart", "positive", "regression"},
        description = "TC_INV_006 - Produk yang ditambahkan tampil di halaman cart",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testProdukTampilDiHalamanCart() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.clickAddBackpack();
        inventoryPage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(
            cartPage.isCartPageLoaded(),
            "Halaman cart seharusnya terbuka setelah klik icon cart. Actual URL: " + cartPage.getCurrentUrl()
        );
        Assert.assertEquals(
            cartPage.getCartItemCount(),
            1,
            "Cart seharusnya berisi tepat 1 produk"
        );
        Assert.assertTrue(
            cartPage.isProductDisplayed(BACKPACK_PRODUCT),
            "Produk Backpack seharusnya tampil di halaman cart"
        );
        Assert.assertEquals(
            cartPage.getProductQuantity(BACKPACK_PRODUCT),
            "1",
            "Quantity produk Backpack di cart seharusnya 1"
        );
    }

    @Test(
        priority = 7,
        groups = {"cart", "positive", "regression"},
        description = "TC_INV_007 - Remove satu dari dua produk memperbarui badge cart",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testRemoveSatuDariDuaProduk() {
        InventoryPage inventoryPage = loginAsStandardUser();

        inventoryPage.clickAddBackpack();
        inventoryPage.clickAddBikeLight();
        Assert.assertTrue(
            inventoryPage.waitUntilCartBadgeCountIs("2"),
            "Precondition gagal: badge cart seharusnya 2 setelah add dua produk. Actual: " + inventoryPage.getCartBadgeCount()
        );

        inventoryPage.clickRemoveBackpack();

        Assert.assertTrue(
            inventoryPage.waitUntilCartBadgeCountIs("1"),
            "Badge cart seharusnya turun menjadi 1 setelah satu produk di-remove. Actual: " + inventoryPage.getCartBadgeCount()
        );
        Assert.assertEquals(
            inventoryPage.getBackpackButtonText().toLowerCase(),
            "add to cart",
            "Tombol Backpack seharusnya kembali menjadi Add to cart setelah di-remove"
        );
        Assert.assertEquals(
            inventoryPage.getBikeLightButtonText().toLowerCase(),
            "remove",
            "Tombol Bike Light seharusnya tetap Remove karena masih ada di cart"
        );
    }

    @Test(
        priority = 8,
        groups = {"cart", "positive", "regression"},
        description = "TC_INV_008 - Cart kosong ketika belum ada produk ditambahkan",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testCartKosongTanpaAddProduk() {
        InventoryPage inventoryPage = loginAsStandardUser();

        Assert.assertTrue(
            inventoryPage.isCartBadgeNotDisplayed(),
            "Badge cart tidak boleh tampil sebelum ada produk yang ditambahkan"
        );

        inventoryPage.openCart();

        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(
            cartPage.isCartPageLoaded(),
            "Halaman cart seharusnya bisa dibuka oleh user yang sudah login"
        );
        Assert.assertEquals(
            cartPage.getCartItemCount(),
            0,
            "Cart seharusnya kosong ketika belum ada produk yang ditambahkan"
        );
        Assert.assertFalse(
            cartPage.isProductDisplayed(BACKPACK_PRODUCT),
            "Produk Backpack tidak boleh tampil di cart kosong"
        );
        Assert.assertFalse(
            cartPage.isProductDisplayed(BIKE_LIGHT_PRODUCT),
            "Produk Bike Light tidak boleh tampil di cart kosong"
        );
    }

    private InventoryPage loginAsStandardUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.doLogin(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(
            loginPage.isLoginSuccessful(),
            "Precondition gagal: login valid seharusnya berhasil"
        );

        InventoryPage inventoryPage = new InventoryPage(driver);
        Assert.assertTrue(
            inventoryPage.isInventoryPageLoaded(),
            "Precondition gagal: halaman inventory seharusnya tampil setelah login valid"
        );
        Assert.assertEquals(
            inventoryPage.getPageTitle(),
            "Products",
            "Precondition gagal: judul halaman inventory tidak sesuai"
        );

        return inventoryPage;
    }

    private void assertProductAdded(InventoryPage inventoryPage, String expectedBadgeCount) {
        Assert.assertTrue(
            inventoryPage.waitUntilCartBadgeCountIs(expectedBadgeCount),
            "Jumlah badge cart tidak sesuai setelah produk ditambahkan. Expected: "
                + expectedBadgeCount + ", Actual: " + inventoryPage.getCartBadgeCount()
        );
    }
}

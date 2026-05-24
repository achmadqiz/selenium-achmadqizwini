package juaracoding.achmadqiz;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    private static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    private static final String ERROR_INVALID_CREDENTIAL = "Epic sadface: Username and password do not match any user in this service";
    private static final String ERROR_LOCKED_USER = "Epic sadface: Sorry, this user has been locked out.";

    @Test(
        priority = 1,
        groups = {"login", "positive", "smoke"},
        description = "TC_LOGIN_001 - Login dengan kredensial valid",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginPositif() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(
            loginPage.isLoginSuccessful(),
            "Login seharusnya berhasil dan halaman Products tampil"
        );
        Assert.assertTrue(
            loginPage.getCurrentUrl().contains("inventory.html"),
            "Setelah login sukses, user seharusnya diarahkan ke inventory. Actual URL: " + loginPage.getCurrentUrl()
        );
        Assert.assertEquals(
            loginPage.getInventoryTitle(),
            "Products",
            "Judul halaman inventory tidak sesuai"
        );
        Assert.assertFalse(
            loginPage.isLoginPageDisplayed(),
            "Form login seharusnya tidak tampil setelah login berhasil"
        );
    }

    @Test(
        priority = 2,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_002 - Login dengan password salah",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginPasswordSalah() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin(VALID_USERNAME, "password_salah");

        assertLoginFailed(loginPage, ERROR_INVALID_CREDENTIAL);
    }

    @Test(
        priority = 3,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_003 - Login dengan username tidak terdaftar",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginUsernameTidakTerdaftar() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin("user_tidak_ada", VALID_PASSWORD);

        assertLoginFailed(loginPage, ERROR_INVALID_CREDENTIAL);
    }

    @Test(
        priority = 4,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_004 - Login dengan akun yang dikunci",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginAkunTerkunci() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin("locked_out_user", VALID_PASSWORD);

        assertLoginFailed(loginPage, ERROR_LOCKED_USER);
    }

    @Test(
        priority = 5,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_005 - Login gagal ketika username kosong",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginUsernameKosong() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin("", VALID_PASSWORD);

        assertLoginFailed(loginPage, ERROR_USERNAME_REQUIRED);
    }

    @Test(
        priority = 6,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_006 - Login gagal ketika password kosong",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginPasswordKosong() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin(VALID_USERNAME, "");

        assertLoginFailed(loginPage, ERROR_PASSWORD_REQUIRED);
    }

    @Test(
        priority = 7,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_007 - Login gagal ketika username dan password kosong",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginUsernameDanPasswordKosong() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin("", "");

        assertLoginFailed(loginPage, ERROR_USERNAME_REQUIRED);
    }

    @Test(
        priority = 8,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_008 - Login gagal ketika username berbeda kapitalisasi",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginUsernameCaseSensitive() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin("STANDARD_USER", VALID_PASSWORD);

        assertLoginFailed(loginPage, ERROR_INVALID_CREDENTIAL);
    }

    @Test(
        priority = 9,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_009 - Login gagal ketika password berbeda kapitalisasi",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginPasswordCaseSensitive() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin(VALID_USERNAME, "SECRET_SAUCE");

        assertLoginFailed(loginPage, ERROR_INVALID_CREDENTIAL);
    }

    @Test(
        priority = 10,
        groups = {"login", "negative", "regression"},
        description = "TC_LOGIN_010 - Login gagal ketika username menggunakan format email tidak terdaftar",
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testLoginUsernameFormatEmailTidakTerdaftar() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.doLogin("standard_user@example.com", VALID_PASSWORD);

        assertLoginFailed(loginPage, ERROR_INVALID_CREDENTIAL);
    }

    private void assertLoginFailed(LoginPage loginPage, String expectedErrorMessage) {
        Assert.assertTrue(
            loginPage.isErrorDisplayed(),
            "Pesan error seharusnya tampil untuk login gagal"
        );
        Assert.assertEquals(
            loginPage.getErrorMessage().trim(),
            expectedErrorMessage,
            "Pesan error login tidak sesuai"
        );
        Assert.assertFalse(
            loginPage.getCurrentUrl().contains("inventory.html"),
            "User tidak boleh diarahkan ke inventory ketika login gagal. Actual URL: " + loginPage.getCurrentUrl()
        );
        Assert.assertTrue(
            loginPage.isLoginPageDisplayed(),
            "Form login seharusnya tetap tampil ketika login gagal"
        );
    }
}

package ru.netology.test;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;
import java.io.IOException;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.SQLHelper.cleanDatabase;

public class AuthorizationTest {
    private LoginPage loginPage;
    public static Process sutProcess;

    @BeforeAll
    static void setupAll() throws IOException {
        sutProcess = Runtime.getRuntime().exec("java -jar artifacts/app-deadline.jar");
        cleanDatabase();
    }
    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }
    @AfterAll
    static void teardownAll() {
        sutProcess.destroy();
        cleanDatabase();
    }
    @Test
    @DisplayName("Should successfully login with exit login and password from sut test data")
    void shouldSuccessfulLogin() {
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }
    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.validLogin(authInfo);
        loginPage.verifyErrorNotificationVisibility();
    }
    @Test
    @DisplayName("Should get error notification if login with exist in base and active user and random verification code")
    void shouldGetErrorNotificationIfLoginWithExistUserAndRandomVerificationCode() {

        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotificationVisibility();
    }
    @Test
    @DisplayName("Should get error notification if user entered the wrong password three times")
    void shouldGetErrorNotificationIfUserEnteredWrongPasswordThreeTimes() {

        var testDataAuthInfo = DataHelper.getAuthInfoWithTestData();
        var validUsername = testDataAuthInfo.getLogin();

        for(int i = 0; i < 4; i++) {
            loginPage.loginField.doubleClick().sendKeys(Keys.BACK_SPACE);
            loginPage.passwordField.doubleClick().sendKeys(Keys.BACK_SPACE);
            var randomPassword = DataHelper.generateRandomPassword();
            var authInfo = new DataHelper.AuthInfo(validUsername, randomPassword);
            var verificationPage = loginPage.validLogin(authInfo);

            if(i < 3) {
                verificationPage.verifyErrorNotificationVisibility();
            } else {
                verificationPage.verifyErrorNotificationVisibilityAndText();
            }
        }
    }
}
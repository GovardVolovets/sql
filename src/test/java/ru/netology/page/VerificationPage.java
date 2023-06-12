package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");
    private SelenideElement errorNotification = $("[data-test-id='error-notification']");
    private SelenideElement errorNotificationBlock = $("[data-test-id='error-notification']");

    public void verifyVerificationPageVisibility() {
        codeField.shouldBe(visible);
    }
    public void verifyErrorNotificationVisibility() {
        errorNotification.shouldBe(visible);
    }
    public DashboardPage validVerify(String verificationCode) {
        verify(verificationCode);
        return new DashboardPage();
    }
    public void verify (String verificationCode) {
        codeField.setValue(verificationCode);
        verifyButton.click();
    }
    public void verifyErrorNotificationVisibilityAndText() {
        errorNotificationBlock.shouldBe(visible).shouldHave(matchText("\\s*Ошибка!\\s*" + "\\s*Неверно указан логин или пароль\\s*"));
    }
}
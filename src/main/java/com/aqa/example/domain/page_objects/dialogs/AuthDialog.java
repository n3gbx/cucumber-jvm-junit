package com.aqa.example.domain.page_objects.dialogs;

import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.element.TextInput;

@Name("User authentication dialog")
@FindBy(xpath = "//ry-auth-popup-container")
public class AuthDialog extends HtmlElement {

    @Name("Sign up switch button")
    @FindBy(css = "button[data-ref='signup_login_signup']")
    private Button signUpSwitchButton;

    @Name("Log in switch button")
    @FindBy(css = "button[data-ref='signup_login_login']")
    private Button logInSwitchButton;

    @Name("Email input")
    @FindBy(css = "input[name='email']")
    private TextInput emailInput;

    @Name("Password input")
    @FindBy(css = "input[name='password']")
    private TextInput passwordInput;

    @Name("Close button")
    @FindBy(css = "icon[class='close__icon']")
    private Button closeButton;

    @Name("Submit button")
    @FindBy(xpath = "//button[contains(@class, 'auth-submit__button')]")
    private Button submitButton;

    public void setEmail(String email) {
        emailInput.sendKeys(email);
    }

    public void setPassword(String password) {
        passwordInput.sendKeys(password);
    }

    public void submit() {
        submitButton.click();
    }

    public boolean waitForOpened() {
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.elementToBeClickable(emailInput.getWrappedElement()));
        return true;
    }
}

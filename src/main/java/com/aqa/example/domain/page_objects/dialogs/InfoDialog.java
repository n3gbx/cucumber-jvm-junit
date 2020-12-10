package com.aqa.example.domain.page_objects.dialogs;

import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("'Info' dialog")
@FindBy(xpath = "//ry-dialog")
public class InfoDialog extends HtmlElement {

    @Name("OK button")
    @FindBy(xpath = ".//div[@class='dialog dialog--desktop']//div[contains(@class, 'actions') or @class='content']//button")
    private Button okButton;

    public void accept() {
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.elementToBeClickable(okButton.getWrappedElement()));
        ElementsHelper.executeClick(okButton);
    }
}

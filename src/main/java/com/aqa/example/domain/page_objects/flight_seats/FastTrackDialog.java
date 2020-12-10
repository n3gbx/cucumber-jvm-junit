package com.aqa.example.domain.page_objects.flight_seats;

import com.aqa.example.environment.ConfigManager;
import com.aqa.example.helpers.ElementsHelper;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import ru.yandex.qatools.htmlelements.annotations.Name;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

@Name("Fast track dialog")
@FindBy(tagName = "ry-enhanced-takeover-beta-desktop")
public class FastTrackDialog extends HtmlElement {

    @Name("Decline button")
    @FindBy(css = "button[data-ref='enhanced-takeover-beta-desktop__dismiss-cta']")
    private Button declineButton;

    public void discard() {
        ElementsHelper.getDefaultWait(ConfigManager.getGeneralConfig().elementTimeout())
                .until(ExpectedConditions.elementToBeClickable(declineButton.getWrappedElement()));
        ElementsHelper.executeClick(declineButton);
    }
}

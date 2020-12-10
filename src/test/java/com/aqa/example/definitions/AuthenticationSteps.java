package com.aqa.example.definitions;

import com.aqa.example.core.DriverManager;
import com.aqa.example.domain.PageUrl;
import com.aqa.example.domain.page_objects.home.HomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;

public class AuthenticationSteps {
    private final WebDriver driver = DriverManager.getDriver();
    private HomePage homePage;

    @Given("I am on the Ryanair home page")
    public void onTheRyanairHomePage() {
        driver.get(PageUrl.BASE_URL);

        homePage = new HomePage(driver);
        homePage.waitForLoading();
        homePage.acceptCookies();
    }

    @And("I am logged in with {string} email using {string} password")
    public void loggedInWithEmailUsingPassword(String email, String password) {
        homePage.logIn(email, password);
    }
}

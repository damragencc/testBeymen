package com.beymen.pages;

import com.beymen.base.BasePage;
import com.beymen.tests.BeymenTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

import static org.bouncycastle.oer.its.etsi102941.CtlCommand.delete;

public class HomePage extends BasePage {
    private final By acceptCookiesButton = By.id("onetrust-accept-btn-handler");
    private final By genderManButton = By.id("genderManButton");
    private final By searchInput = By.xpath("//*[@class='o-header__search--input']");
    private final By searchBox = By.xpath("//*[@id='o-searchSuggestion__input']");
    private final By searchCloseButton = By.xpath("//*[@class='o-header__search--close -hasButton']");
    private final By searchButton = By.xpath("//*[@class='o-header__search--btn']");
    private final By productCards = By.xpath("//*[@class='m-productCard__desc']");
    private final By inputId= By.id("//*[@id='o-searchSuggestion__input']");


    public HomePage(WebDriver driver) {
        super(driver);
    }

    private static final Logger logger = LogManager.getLogger(BeymenTest.class);

    public void navigateToHomePage() {
        driver.get("https://www.beymen.com");
    }

    public void acceptCookiesIfPresent() {
        try {
            clickElement(acceptCookiesButton);
        } catch (TimeoutException e) {
            // Çerez politikası popup'ı görünmedi
        }
    }

    public void selectMaleGender() {
        try {
            Thread.sleep(2000); // Popup’ın yüklenmesi için bekle
            WebElement maleButton = waitForElementClickable(genderManButton);
            clickWithJS(maleButton);
        } catch (TimeoutException | InterruptedException e) {
            // Cinsiyet seçim popup’ı görünmedi veya işlem başarısız
        }
    }

    public void performSearch(String searchTerm) throws InterruptedException {
        WebElement searchBoxElement = waitForElementClickable(searchBox);
        Thread.sleep(2000);
        searchBoxElement.sendKeys(searchTerm);
    }

    public void clearSearch() throws InterruptedException {
        clickElement(searchCloseButton);
        Thread.sleep(2000);
    }

    public void pressEnterKey() {
        WebElement searchBtn = waitForElementClickable(searchButton);
        clickWithJS(searchBtn);
    }





    public void ilkUrun() throws InterruptedException {
        WebElement ilkUrun = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@class='m-productCard__desc'][1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ilkUrun);
        logger.info("Arama sonuçlarından ilk ürün seçildi");

        // Ürün detay sayfasının yüklenmesi için bekle
        Thread.sleep(4000);
    }

   public void searchBoxx() throws InterruptedException {

       
       WebElement delete = driver.findElement(By.xpath("//*[@class='o-header__search--close -hasButton']"));
       delete.click();
       Thread.sleep(2000);
   }


}

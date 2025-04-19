package com.beymen.pages;

import com.beymen.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class BasketPage extends BasePage {
    private final By totalPrice = By.xpath("//li[@class='m-orderSummary__item -grandTotal']//span[@class='m-orderSummary__value']");
    private final By quantitySelect = By.xpath("//select[contains(@class, 'a-selectControl')]");
    private final By removeButton = By.xpath("//button[contains(@class, 'removeItem')] | //button[contains(@class, 'delete')] | //button[contains(@class, 'basket__remove')]");
    private final By emptyBasketMessage = By.xpath("//div[contains(@class, 'empty')]//p | //div[contains(@class, 'empty')]//strong");

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    public String getTotalPrice() throws InterruptedException {
        Thread.sleep(3000);
        return getText(totalPrice);
    }

    public void updateQuantity(String value) throws InterruptedException {
        Thread.sleep(2000);
        WebElement quantityElement = waitForElementClickable(quantitySelect);
        Select select = new Select(quantityElement);
        select.selectByValue(value);
        Thread.sleep(2000);
    }

    public String getSelectedQuantity() {
        WebElement quantityElement = waitForElementClickable(quantitySelect);
        Select select = new Select(quantityElement);
        return select.getFirstSelectedOption().getText();
    }

    public void removeProduct() throws InterruptedException {
        clickElement(removeButton);
        Thread.sleep(2000);
    }

    public boolean isBasketEmpty() throws InterruptedException {
        Thread.sleep(2000);
        try {
            return waitForElementVisible(emptyBasketMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}

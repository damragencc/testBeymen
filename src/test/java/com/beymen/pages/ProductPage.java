package com.beymen.pages;

import com.beymen.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ProductPage extends BasePage {
    private final By productPrice = By.className("m-price__new");
    private final By smallSizeButton = By.xpath("//span[@class='m-variation__item' and (contains(text(),'S') or contains(text(),'Small'))]");
    private final By addToBasketButton = By.xpath("//button[@id='addBasket']");
    private final By basketButton = By.xpath("//a[@title='Sepetim' or @class='o-header__userInfo--bag']");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductPrice() {
        return getText(productPrice);
    }

    public void saveProductInfo(String productName, String price) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("urun_bilgisi.txt"))) {
            writer.write("Ürün Adı: " + productName);
            writer.newLine();
            writer.write("Ürün Fiyatı: " + price);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectSmallSize() throws InterruptedException {
        Thread.sleep(3000);
        clickElement(smallSizeButton);
        Thread.sleep(3000);
    }

    public void addToBasket() {
        clickElement(addToBasketButton);
    }

    public void goToBasket() {
        clickElement(basketButton);
    }
}

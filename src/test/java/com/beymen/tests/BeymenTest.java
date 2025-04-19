package com.beymen.tests;

import com.beymen.pages.BasketPage;
import com.beymen.pages.HomePage;
import com.beymen.pages.ProductPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;



import java.io.FileInputStream;
import java.io.IOException;

public class BeymenTest {
    private static final Logger logger = LogManager.getLogger(BeymenTest.class);
    private WebDriver driver;
    private HomePage homePage;
    private ProductPage productPage;
    private BasketPage basketPage;

    @BeforeAll
    public static void setupClass() {
        final Logger logger = LogManager.getLogger(BeymenTest.class);
        WebDriverManager.chromedriver().setup();
        logger.info("WebDriver kurulumu tamamlandı");
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        
        // Page Objects'leri initialize et
        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        basketPage = new BasketPage(driver);
        
        logger.info("Chrome tarayıcı başlatıldı ve Page Objects oluşturuldu");
    }

    private String[] readDataFromExcel() throws IOException {
        String[] searchTerms = new String[2];
        FileInputStream file = new FileInputStream("src/test/resources/searchData.xlsx");
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        searchTerms[0] = sheet.getRow(0).getCell(0).getStringCellValue(); // short
        searchTerms[1] = sheet.getRow(0).getCell(1).getStringCellValue(); // gömlek

        workbook.close();
        file.close();
        return searchTerms;
    }

    @Test
    public void beymenTest() throws IOException, InterruptedException {
        String[] searchTerms = readDataFromExcel();
        logger.info("Excel'den arama terimleri okundu");

        // Ana sayfaya git ve başlangıç işlemlerini yap
        homePage.navigateToHomePage();
        homePage.acceptCookiesIfPresent();
        homePage.selectMaleGender();


        // İlk arama terimini ara ve temizle
        WebElement element1 = driver.findElement(By.xpath("//*[@class='o-header__search--input']"));
        element1.click();
        homePage.performSearch(searchTerms[0]);
        logger.info("İlk arama terimi girildi: " + searchTerms[0]);
        homePage.clearSearch();

        // İkinci arama terimini ara
        Thread.sleep(2000);
        homePage.performSearch(searchTerms[1]);
        logger.info("İkinci arama terimi girildi: " + searchTerms[1]);

        // Enter tuşuna bas
        homePage.pressEnterKey();
        Thread.sleep(2000);

        

        // ilk ürün seç
        homePage.ilkUrun();

        
        // Ürün bilgilerini kaydet
        String productPrice = productPage.getProductPrice();
        productPage.saveProductInfo("Gömlek", productPrice);
        logger.info("Ürün bilgileri kaydedildi");

        // Ürünü sepete ekle
        productPage.selectSmallSize();
        productPage.addToBasket();
        productPage.goToBasket();
        logger.info("Ürün sepete eklendi ve sepete gidildi");

        // Sepet işlemleri
        String basketPrice = basketPage.getTotalPrice();
        
        // Fiyat karşılaştırması
        if(productPrice.equals(basketPrice)) {
            logger.info("Fiyatlar eşleşiyor!");
        } else {
            logger.warn("UYARI: Fiyatlar farklı! Ürün: " + productPrice + ", Sepet: " + basketPrice);
        }

        // Ürün adedini güncelle
        basketPage.updateQuantity("2");
        String selectedQuantity = basketPage.getSelectedQuantity();
        
        if(selectedQuantity.equals("2 adet")) {
            logger.info("Ürün adedi başarıyla 2 olarak güncellendi");
        } else {
            logger.warn("UYARI: Ürün adedi güncellenemedi! Mevcut değer: " + selectedQuantity);
        }

        // Ürünü sepetten sil
        basketPage.removeProduct();
        
        // Sepetin boş olduğunu kontrol et
        if(basketPage.isBasketEmpty()) {
            logger.info("Sepet başarıyla boşaltıldı");
        } else {
            logger.warn("UYARI: Sepet boşaltılamadı!");
        }
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
            logger.info("Tarayıcı kapatıldı");
        }
    }
}
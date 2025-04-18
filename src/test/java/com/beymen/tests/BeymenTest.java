package com.beymen.tests;

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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;

public class BeymenTest {
    private static final Logger logger = LogManager.getLogger(BeymenTest.class);
    private WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        logger.info("WebDriver kurulumu tamamlandı");
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        logger.info("Chrome tarayıcı başlatıldı");
    }

    private String[] readDataFromExcel() throws IOException {
        String[] searchTerms = new String[2];
        FileInputStream file = new FileInputStream("src/test/resources/searchData.xlsx");
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        searchTerms[0] = sheet.getRow(0).getCell(0).getStringCellValue(); // short
        searchTerms[1] = sheet.getRow(1).getCell(0).getStringCellValue(); // gömlek

        workbook.close();
        file.close();
        return searchTerms;
    }

    @Test
    public void beymenTest() throws IOException, InterruptedException {
        String[] searchTerms = readDataFromExcel();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(45));

        // Ana sayfayı aç
        driver.get("https://www.beymen.com");
        logger.info("Beymen ana sayfası açıldı");

        // Çerez popup'ını kabul et (varsa)
        try {
            WebElement acceptCookies = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("onetrust-accept-btn-handler")));
            acceptCookies.click();
            logger.info("Çerez politikası kabul edildi");
        } catch (TimeoutException e) {
            logger.info("Çerez politikası popup'ı görünmedi");
        }

        // Cinsiyet seçim popup'ında erkek seçeneğini seç (varsa)
        Thread.sleep(2000); // Popup'ın yüklenmesi için bekle
        try {
            WebElement maleButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("genderManButton")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", maleButton);
            logger.info("Cinsiyet seçim popup'ında erkek seçeneği seçildi");
        } catch (TimeoutException e) {
            logger.info("Cinsiyet seçim popup'ı görünmedi");
        }


        // İlk arama terimi (short)
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@placeholder='Ürün, Marka Arayın']")));



        // İkinci arama terimi (gömlek)
        searchBox.sendKeys(searchTerms[1]);
        logger.info("İkinci arama terimi girildi: " + searchTerms[1]);

        // ENTER tuşu ile arama yap
        WebElement araButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@class='o-header__search--btn']")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", araButton);

        // Sayfa yüklenme kontrolü
        Thread.sleep(5000); // Sayfanın yüklenmesi için kısa bir bekleme


        // İlk ürünü seç
        // Ürünü seç
        WebElement ilkUrun = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@class='m-productCard__desc'][1]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ilkUrun);
        logger.info("Arama sonuçlarından ilk ürün seçildi");

        // Ürün detay sayfasının yüklenmesi için bekle
        Thread.sleep(4000);


        // Ürün adı

        WebElement urunAdiElement = driver.findElement(By.xpath("//*[contains(text(),'Kapüşonlu Yün Kaşe')]"));
        String urunAdi = urunAdiElement.getText();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("urun_bilgisi.txt"));
            writer.write("Ürün Adı: " + urunAdi);
            writer.close();
            System.out.println("Ürün adı dosyaya yazıldı.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        WebElement urunFiyatiElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//ins[@class='m-price__new'] | //span[@class='m-price__new'] | //span[contains(@class,'m-price__new')] | //span[@data-price] | //div[contains(@class,'m-price')]//span[contains(@class,'new')]")));
        String urunFiyati = urunFiyatiElement.getText();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("urun_bilgisi.txt", true));
            writer.newLine();
            writer.write("Ürün Fiyatı: " + urunFiyati);
            writer.close();
            System.out.println("Ürün fiyatı dosyaya yazıldı.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread.sleep(3000);

        // Small beden seç
        WebElement smallBeden = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='m-variation__item' and contains(text(),'S') or contains(text(),'Small')]")));
        smallBeden.click();

        Thread.sleep(3000);

        // Sepete ekle butonuna tıkla
        WebElement sepeteEkleBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@id='addBasket']")));
        sepeteEkleBtn.click();



        // Sepete git
        // Sepete ekleme işleminin tamamlanmasını bekle
        WebElement sepetButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@title='Sepetim' or @class='o-header__userInfo--bag']")));
        sepetButton.click();

        // Sepetteki fiyatı al
        Thread.sleep(3000); // Sepet sayfasının yüklenmesi için bekle
        WebElement sepetFiyatElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//li[@class='m-orderSummary__item -grandTotal']//span[@class='m-orderSummary__value']")));
        String sepetFiyati = sepetFiyatElement.getText();
        Thread.sleep(1000); // Fiyatın görüntülenmesi için kısa bir bekleme

        // Fiyatları karşılaştır
        System.out.println("Ürün Sayfasındaki Fiyat: " + urunFiyati);
        System.out.println("Sepetteki Fiyat: " + sepetFiyati);

        if(urunFiyati.equals(sepetFiyati)) {
            System.out.println("Fiyatlar eşleşiyor!");
        } else {
            System.out.println("UYARI: Fiyatlar farklı!");
        }

        // Ürün adedini 2'ye çıkar
        Thread.sleep(2000); // Sayfanın yüklenmesi için bekle
        WebElement quantitySelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'a-selectControl')]")));
        Select select = new Select(quantitySelect);
        select.selectByValue("2");
        Thread.sleep(2000); // Değişikliğin uygulanması için bekle

        // Adet sayısını doğrula
        WebElement updatedQuantitySelect = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[contains(@class, 'a-selectControl')]")));
        Select updatedSelect = new Select(updatedQuantitySelect);
        String selectedValue = updatedSelect.getFirstSelectedOption().getText();
        if(selectedValue.equals("2 adet")) {
            System.out.println("Ürün adedi başarıyla 2 olarak güncellendi");
        } else {
            System.out.println("UYARI: Ürün adedi güncellenemedi! Mevcut değer: " + selectedValue);
        }

        // Ürünü sepetten sil
        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'removeItem')] | //button[contains(@class, 'delete')] | //button[contains(@class, 'basket__remove')]")));
        removeButton.click();
        Thread.sleep(2000); // Silme işleminin tamamlanması için bekle

        // Boş sepet kontrolü
        Thread.sleep(2000); // Sayfanın güncellenmesi için bekle
        try {
            WebElement emptyBasketMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'empty')]//p | //div[contains(@class, 'empty')]//strong")));
            if(emptyBasketMessage.isDisplayed()) {
                System.out.println("Sepet başarıyla boşaltıldı");
            }
        } catch (Exception e) {
            System.out.println("UYARI: Sepet boşaltılamadı!");
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
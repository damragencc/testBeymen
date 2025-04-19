package com.beymen.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {
    protected WebDriver driver;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        logger.info("WebDriver kurulumu tamamlandı");
    }

    @BeforeEach
    public void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        logger.info("Chrome tarayıcı başlatıldı");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
            logger.info("Tarayıcı kapatıldı");
        }
    }
}

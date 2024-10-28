package Test;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import loadApp.ServerLoader;
import variables.ElementLocators;

public class Iframe {
    ServerLoader server = new ServerLoader();
    ElementLocators element = new ElementLocators();

    WebDriver driver;
    public String item, item1 = " ";

    public Iframe() {
        this.driver = server.launch_app();
    }

    @Test(priority = 1) // Go to Home page and ByPass Captcha
    public void testBrowser() throws InterruptedException, IOException, TesseractException {
        ExtractImage();
        while (true) {
            Thread.sleep(1000);
            int display = driver.findElements(ElementLocators.account).size();
            if (display > 0) {
                break;
            } else {
                ExtractImage();
            }
        }
    }

    @Test(priority = 2)
    public void SearchToys() {
        driver.findElement(ElementLocators.search).sendKeys("Toys");
        driver.findElement(ElementLocators.search).sendKeys(Keys.ENTER);
    }

    @Test(priority = 3)
    public void AddProducts_to_Cart() throws InterruptedException {
        for (int i = 4; i < 8; i += 3) {
            Thread.sleep(1000);
            CartAmount(i);
            driver.findElement(By.id("a-autoid-" + i + "-announce")).click();
        }
    }

    @Test(priority = 4)
    public void validatePrice() throws InterruptedException {
        Thread.sleep(10000);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(document.body.scrollWidth, 0);");
        Thread.sleep(10000);

        driver.findElement(ElementLocators.cart).click();
        WebElement pagePrice = driver.findElement(ElementLocators.cartvalue);
        WebElement pagePrice1 = driver.findElement(ElementLocators.cartvalue1);

        String price = pagePrice.getText();
        String price2 = pagePrice1.getText();
        Assert.assertEquals(price2, item);
        Assert.assertEquals(price, item1);
    }

    public void CartAmount(int i) {
        WebElement priceElement = driver.findElement(
                By.xpath("//button[@id='a-autoid-" + i + "-announce']/parent::*/parent::*/parent::*/parent::*/parent::*/parent::*/parent::*/parent::*/preceding-sibling::*[@data-cy='price-recipe']/descendant::span[@class='a-offscreen']")
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", priceElement);

        // Try fetching text with different methods
        String price = priceElement.getText();
        if (price.isEmpty()) {
            price = priceElement.getAttribute("textContent"); // Fallback to textContent if getText() is empty
        }

        if (i == 4) {
            item = price;
        } else {
            item1 = price;
        }
    }

    public void ExtractImage() throws MalformedURLException, IOException, TesseractException {
        WebElement captchaImage = driver.findElement(ElementLocators.captcha_image);
        String imageUrl = captchaImage.getAttribute("src");

        // Download the image
        BufferedImage image = ImageIO.read(new URL(imageUrl));
        File captchaFile = new File("downloadedCaptcha.jpg");
        ImageIO.write(image, "jpg", captchaFile);

        // Initialize Tesseract
        String projectPath = System.getProperty("user.dir");
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(projectPath + "/tessdata"); // Update this path as needed
        tesseract.setLanguage("eng");

        // Perform OCR on the downloaded image
        String result = tesseract.doOCR(captchaFile);
        String cleanedResult = result.replaceAll("[^a-zA-Z0-9]", "");

        driver.findElement(ElementLocators.Entercaptcha).sendKeys(cleanedResult);
        driver.findElement(ElementLocators.submitcaptcha).click();
    }
}

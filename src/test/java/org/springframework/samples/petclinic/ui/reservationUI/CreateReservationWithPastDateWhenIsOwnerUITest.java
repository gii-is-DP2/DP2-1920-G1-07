
package org.springframework.samples.petclinic.ui.reservationUI;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateReservationWithPastDateWhenIsOwnerUITest {

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		// String pathToGeckoDriver = "C:\\Users\\mediamarkt\\Downloads";
		// System.setProperty("webdriver.gecko.driver", pathToGeckoDriver +
		// "\\geckodriver.exe");
		// driver = new FirefoxDriver();
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testCreateReservationWithPastDateWhenIsOwnerUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("owner");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.driver.findElement(By.linkText("Room4")).click();
		this.driver.findElement(By.linkText("Add New Reservation")).click();
		this.driver.findElement(By.id("entryDate")).click();
		this.driver.findElement(By.id("entryDate")).clear();
		this.driver.findElement(By.id("entryDate")).sendKeys("2019/02/02");
		this.driver.findElement(By.id("exitDate")).click();
		this.driver.findElement(By.id("exitDate")).clear();
		this.driver.findElement(By.id("exitDate")).sendKeys("2021/01/02");
		this.driver.findElement(By.xpath("//div/div/div/div/div")).click();
		new Select(driver.findElement(By.id("pet"))).selectByVisibleText("Pet Bird");
		this.driver.findElement(By.xpath("//option[@value='17']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("The entry date must be after the current date",
				this.driver.findElement(By.xpath("//form[@id='add-reservation-form']/div/div/div/span[2]")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}

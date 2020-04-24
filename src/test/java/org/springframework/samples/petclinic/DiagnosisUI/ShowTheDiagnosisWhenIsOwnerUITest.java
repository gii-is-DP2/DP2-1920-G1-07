
package org.springframework.samples.petclinic.DiagnosisUI;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class ShowTheDiagnosisWhenIsOwnerUITest {

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();
	@LocalServerPort
	private int				port;


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		//		String pathToGeckoDriver = "C:\\Users\\mediamarkt\\Downloads";
		//		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		//		driver = new FirefoxDriver();
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testShowTheDiagnosisWhenIsOwnerUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("owner");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.linkText("Add Visit")).click();
		this.driver.findElement(By.xpath("(//a[contains(text(),'Add Visit')])[7]")).click();
		this.driver.findElement(By.xpath("//form[@id='visit']/div")).click();
		this.driver.findElement(By.id("description")).click();
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("revision");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[1]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("vet2");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("12345");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		this.driver.findElement(By.linkText("Add diagnosis")).click();
		this.driver.findElement(By.id("description")).click();
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("Todo bien");
		this.driver.findElement(By.id("date")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a[2]/span")).click();
		this.driver.findElement(By.linkText("20")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[1]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("owner");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.linkText("See my Diagnosis")).click();
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

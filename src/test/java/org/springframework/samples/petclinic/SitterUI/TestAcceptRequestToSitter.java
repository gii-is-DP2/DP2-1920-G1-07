
package org.springframework.samples.petclinic.SitterUI;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class TestAcceptRequestToSitter {

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		//		String pathToGeckoDriver = "C:\\gecko";
		//		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAcceptRequetsToSitter() throws Exception {
		this.driver.get("http://localhost:8080/");
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("owner");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
		this.driver.findElement(By.id("address")).click();
		this.driver.findElement(By.id("address")).clear();
		this.driver.findElement(By.id("address")).sendKeys("Calle a");
		this.driver.findElement(By.id("telephone")).click();
		this.driver.findElement(By.id("telephone")).clear();
		this.driver.findElement(By.id("telephone")).sendKeys("666666666");
		new Select(this.driver.findElement(By.id("type"))).selectByVisibleText("dog");
		this.driver.findElement(By.xpath("//option[@value='dog']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/logout')]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("4dm1n");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("admin1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/admin/request')]")).click();
		this.driver.findElement(By.linkText("Accept request")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/logout')]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("owner");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assert.assertEquals("MY ROOMS", this.driver.findElement(By.xpath("//a[contains(@href, '/sitter/rooms')]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/logout')]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
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

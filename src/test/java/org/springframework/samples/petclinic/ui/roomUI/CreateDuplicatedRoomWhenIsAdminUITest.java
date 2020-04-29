
package org.springframework.samples.petclinic.ui.roomUI;

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
public class CreateDuplicatedRoomWhenIsAdminUITest {

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
	public void testCreateRoomWhereIsAdminUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("admin1");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("4dm1n");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		Assert.assertEquals("Add New Room", this.driver.findElement(By.linkText("Add New Room")).getText());
		this.driver.findElement(By.linkText("Add New Room")).click();
		this.driver.findElement(By.xpath("//h2")).click();
		Assert.assertEquals("New Room", this.driver.findElement(By.xpath("//h2")).getText());
		this.driver.findElement(By.id("name")).click();
		this.driver.findElement(By.id("name")).clear();
		this.driver.findElement(By.id("name")).sendKeys("Room2");
		this.driver.findElement(By.id("capacity")).click();
		this.driver.findElement(By.id("capacity")).clear();
		this.driver.findElement(By.id("capacity")).sendKeys("3");
		new Select(this.driver.findElement(By.id("type"))).selectByVisibleText("bird");
		this.driver.findElement(By.xpath("//option[@value='bird']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("This value is already used", this.driver.findElement(By.xpath("//form[@id='add-room-form']/div/div/div/span[2]")).getText());
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

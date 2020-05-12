
package org.springframework.samples.petclinic.ui.causeUI;

import java.time.LocalDate;
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
public class CheckDateAcceptedCausesUITest {

	@LocalServerPort
	private int				port;
	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();
	private LocalDate		fechaActual;


	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
//		String pathToGeckoDriver = "C:\\Users\\amine";
//		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		this.fechaActual = LocalDate.now();
	}

	@Test
	public void testCheckDateAcceptedCausesUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("0wn3r");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/cause')]")).click();

		String fechaCausa1 = this.driver.findElement(By.xpath("//table[@id='causesTable']/tbody/tr/td[3]")).getText();
		LocalDate fecha1 = LocalDate.parse(fechaCausa1);
		String fechaCausa2 = this.driver.findElement(By.xpath("//table[@id='causesTable']/tbody/tr[2]/td[3]")).getText();
		LocalDate fecha2 = LocalDate.parse(fechaCausa2);
		Assert.assertEquals(true, fecha1.isAfter(this.fechaActual));
		Assert.assertEquals(true, fecha2.isAfter(this.fechaActual));
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

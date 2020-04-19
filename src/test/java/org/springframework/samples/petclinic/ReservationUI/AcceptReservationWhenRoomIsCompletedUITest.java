package org.springframework.samples.petclinic.ReservationUI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

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
public class AcceptReservationWhenRoomIsCompletedUITest {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();
	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() throws Exception {
//		String pathToGeckoDriver = "C:\\Users\\amine\\Downloads";
//		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
//		driver = new FirefoxDriver();
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	@Test
	  public void testAcceptReservationWhenIsAdminUI() throws Exception {
		driver.get("http://localhost:"+port);
		driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("admin1");
	    driver.findElement(By.xpath("//div")).click();
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("4dm1n");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	    driver.findElement(By.linkText("Room2")).click();
	    driver.findElement(By.linkText("PENDING")).click();
	    new Select(driver.findElement(By.id("status"))).selectByVisibleText("ACCEPTED");
	    driver.findElement(By.xpath("//option[@value='ACCEPTED']")).click();
	    driver.findElement(By.id("pet")).click();
	    new Select(driver.findElement(By.id("pet"))).selectByVisibleText("Pet Dog");
	    driver.findElement(By.xpath("//option[@value='16']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("ESTA HABITACION TIENE LAS RESERVAS AGOTADAS", driver.findElement(By.xpath("//h3")).getText());
	    driver.findElement(By.linkText("PENDING")).click();
	    assertEquals("ESTA HABITACION TIENE LAS RESERVAS AGOTADAS", driver.findElement(By.xpath("//form[@id='add-editreservation-form']/div/div[5]/div/div/div/h3")).getText());
	    driver.findElement(By.id("pet")).click();
	    new Select(driver.findElement(By.id("pet"))).selectByVisibleText("Pet Dog");
	    driver.findElement(By.xpath("//option[@value='16']")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("ESTA HABITACION TIENE LAS RESERVAS AGOTADAS", driver.findElement(By.xpath("//h3")).getText());
	  }

	  @AfterEach
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }

	  private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
	  }

	  private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	  }

	  private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	  }

}

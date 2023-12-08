package com.demo.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

	// Intializing web driver
	WebDriver driver;

	private static String readJsonFromFile(String filePath) {
		try {
			Path path = Paths.get(filePath);
			byte[] jsonData = Files.readAllBytes(path);
			return new String(jsonData);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Test
	public void VerifyDataInTable() {

		try {

			// reading data from json file
			String jsonData = readJsonFromFile("C:\\Users\\svaib\\eclipse-workspace\\TableProject\\jsonData.json");

			// creating Object for Objectmapper
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonData);

			// Setting System property
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\svaib\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

			// creating object for chromeDriver Class
			driver = new ChromeDriver();

			// maximizing browser size
			driver.manage().window().maximize();

			// launching url
			driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");

			// locating refresh table table with xpath and clicking
			driver.findElement(By.xpath("//summary[normalize-space()='Table Data']")).click();

			// locating textfield with xpath
			WebElement textField = driver.findElement(By.xpath("//textarea[@id='jsondata']"));

			// clearing previous values
			textField.clear();

			// entering test data
			textField.sendKeys(jsonData);

			// clicking on refresh button
			driver.findElement(By.xpath("//button[@id='refreshtable']")).click();

			// Retrieve the data from the UI table
			WebElement uiTable = driver.findElement(By.xpath("//table[@id='dynamictable']"));
			List<WebElement> tableRows = uiTable.findElements(By.xpath("//tr"));

			// Assert the data dynamically
			for (int i = 1; i < tableRows.size(); i++) {

				List<WebElement> columns = tableRows.get(i).findElements(By.tagName("td"));
				JsonNode node = jsonNode.get(i - 1);

				// Aserting data for name age and gender
				Assert.assertEquals(node.get("name").asText(), columns.get(0).getText());
				Assert.assertEquals(node.get("age").asText(), columns.get(1).getText());
				Assert.assertEquals(node.get("gender").asText(), columns.get(2).getText());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// closing the current browser
		driver.close();
	}
}

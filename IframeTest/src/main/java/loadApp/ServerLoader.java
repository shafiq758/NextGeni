package loadApp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class ServerLoader {
	
	public static WebDriver driver=null;
	
	public WebDriver launch_app() 
	{
		 driver = new ChromeDriver();
	      driver.get("https://www.amazon.com/"); // Update URL to where the captcha is located
	      driver.manage().window().maximize();
		return driver;
	}

}

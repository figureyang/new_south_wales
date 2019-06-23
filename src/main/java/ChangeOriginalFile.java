import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

/**
 * Created by jingfeiyang on 17/1/10.
 */
public class ChangeOriginalFile {
    public static void main(String[] args) throws Exception{


        String readDirection = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/forsydeny/5_newsouthwales.csv";
        String writeDirection = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/forsydeny/5_newSouthWalesResults.csv";
        String notfoundDirection = "/Users/jingfeiyang/Desktop/melburne_university/semester4/finalproject_sydney/forsydeny/5_notfound.csv";


/////////////////  read part  /////////////////////////////////////////////////

        String nextLine[];
        CSVReader reader = new CSVReader(new FileReader(readDirection));
        while((nextLine = reader.readNext())!=null)
        {
            String proNo = nextLine[0];
            String proID = nextLine[1];
            String Lat = nextLine[2];
            String Lng = nextLine[3];
            String proType = nextLine[4];
            String Formated_Address = nextLine[5];

            String frontDeletedArea = Formated_Address.substring(0,Formated_Address.indexOf("/")+1);

            String Changed_Address = Formated_Address.replace(frontDeletedArea,"").replace(" NSW","").replace(", Australia","");



////////////////  crawl information part //////////////////////////////////////


            WebDriver driver;
            System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");

            driver = new ChromeDriver();
            driver.get("https://www.planningportal.nsw.gov.au/find-a-property");

            try
            {





                WebElement textBox = driver.findElement(By.name("address_search"));

                textBox.sendKeys(Changed_Address);

                Thread.sleep(1000);

                WebElement startSearching = driver.findElement(By.className("search-btn"));

                startSearching.click();

                Thread.sleep(5000);

                WebElement confirm = driver.findElement(By.cssSelector(".remodal-confirm.ui-button.ui-widget.ui-state-default.ui-corner-all.ui-button-text-only"));

                confirm.click();

                Thread.sleep(5000);



                try
                {
                    Alert alert = driver.switchTo().alert();
                    alert.accept();
                    Thread.sleep(1000);
                }
                catch (NoAlertPresentException e)
                {
                    System.out.println("no alert");
                    Thread.sleep(1000);
                }



                WebElement landsize = driver.findElement(By.className("searchmarkers-maptip"));

                String detail = landsize.getText();

                String Parcel_Size = detail.substring(detail.indexOf("Parcel Size: ")+13,detail.indexOf("sq")-1);

                driver.quit();


////////////////////////   write part     ////////////////////////////////////////////

                File CSVFile = new File(writeDirection);
                Writer fileWriter = new FileWriter(CSVFile,true);
                CSVWriter writer = new CSVWriter(fileWriter,',');
                String entry[] = {proNo,proID,Lat,Lng,proType,Formated_Address,Changed_Address,Parcel_Size};

                writer.writeNext(entry);
                writer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();

                File CSVFile = new File(notfoundDirection);
                Writer fileWriter = new FileWriter(CSVFile,true);
                CSVWriter writer = new CSVWriter(fileWriter,',');
                String entry[] = {proNo,proID,Lat,Lng,proType,Formated_Address,Changed_Address};

                writer.writeNext(entry);
                writer.close();

                driver.quit();
            }




        }
    }
}



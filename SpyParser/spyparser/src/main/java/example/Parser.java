package example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.opera.OperaDriver;


public final class Parser {

    private static final String STARTLINK = "https://www.olx.ua/uk/nedvizhimost/q-недвижимость/";
    private static final String SAVEFILE = "ads.csv";

    private static Ad[] loadAds(WebDriver driver){
        List<WebElement> offers = driver.findElements(By.cssSelector("#offers_table wrap"));
        Ad[] ads = new Ad[offers.size()];
        for (int i=0; i<ads.length;i++) {
            ads[i] = parseAd(offers.get(i));
        }
        return ads;
    }

    private static Ad parseAd(WebElement offer) {
        String name = offer.findElement(By.className("detailsLink")).getText();
        String priceString = offer.findElement(By.className("price")).getText();
        int price = Integer.parseInt(priceString.substring(0,priceString.length()-5));
        String location = offer.findElement(By.xpath("//i[@data-icon='clock']]..")).getText();
        String timeString = offer.findElement(By.xpath("//i[@data-icon='clock']]..")).getText();
        return new Ad(name, price, location, timeString);
    }

    private static Ad[] loadSavedAds(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(SAVEFILE));
            ArrayList<Ad> ads = new ArrayList<Ad>();
            String line;
            while ((line = br.readLine())!=null){
                ads.add(new Ad(line));
            }
            br.close();
            return ads.toArray(new Ad[ads.size()]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        WebDriver driver = new OperaDriver();
        driver.get(STARTLINK);
        Scanner sc = new Scanner(System.in);
        System.out.println("Input 'stop' when you want the program to stop.");
        for (String response="";!response.equalsIgnoreCase("stop");response = sc.nextLine()){
            Ad[] freshAds = loadAds(driver);
            Ad[] oldAds = loadSavedAds();
        }
        sc.close();
        driver.quit();
    }
}

package example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ad implements Comparable<Ad>{
    public String name;
    public int price;
    public String location;
    public LocalDateTime postedAt;

    public Ad(String name, int price, String location, String postedAt){
        this.name = name;
        this.price = price;
        this.location = location;
        this.postedAt = parseDate(postedAt);
    }

    public Ad(String adString) {
        Pattern p = Pattern.compile("(?<name>.+) за ціною (?<price>\\d+) грн\\.; (?<location>[^-]+) - (?<postedAt>.+)");
        Matcher m = p.matcher(adString);
        if (m.matches()){
            name = m.group("name");
            price = Integer.parseInt(m.group("price"));
            location = m.group("location");
            postedAt = LocalDateTime.parse(m.group("postedAt"));
        }
    }

    /*
    Parses the datetime at which the ad was posted.
    Any ads posted earlier than the day before when the program is run will be considered as those that were posted
    two days prior.
    This is due to the need for more intense date parsing being unneeded for this use case.
    */
    private LocalDateTime parseDate(String dateString){
        LocalDate ldate;
        LocalTime ltime;
        String[] dateParts = dateString.split(" ");
        switch (dateParts[0]){
            case "Сьогодні":
            ldate = LocalDate.now();
            break;
            case "Вчора":
            ldate = LocalDate.now().minusDays(1);
            break;
            default:
            return LocalDateTime.now().minusDays(2);
        }
        ltime = LocalTime.parse(dateParts[1]);
        return LocalDateTime.of(ldate, ltime);
    }

    @Override
    public String toString() {
        return String.format("&s за ціною &d грн.; &s - &tc", name, price, location, postedAt);
    }

    @Override
    public int compareTo(Ad arg0) {
        return postedAt.compareTo(arg0.postedAt);
    }
}

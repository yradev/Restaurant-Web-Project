package source.restaurant_web_project;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class test {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        LocalDateTime nn = localDateTime.plusMinutes(1);
        System.out.println(nn);
    }
}

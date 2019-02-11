package n.kidding.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        for (String a : args) {
            System.out.println(a);
        }
        SpringApplication.run(ServiceApplication.class, args);
    }
}

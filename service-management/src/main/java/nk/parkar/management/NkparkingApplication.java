package nk.parkar.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("nk.parkar.management.mapper")
public class NkparkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(NkparkingApplication.class, args);
    }

}

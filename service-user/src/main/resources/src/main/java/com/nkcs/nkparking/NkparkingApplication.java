package com.nkcs.nkparking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nkcs.nkparking.mapper")
public class NkparkingApplication {

    public static void main(String[] args) {
        SpringApplication.run(NkparkingApplication.class, args);
    }

}

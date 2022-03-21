package com.ja.ioniprog.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@ComponentScans({@ComponentScan("com.ja.ioniprog")})
@SpringBootApplication
public class IoniprogApplication {

	public static void main(String[] args) {
		SpringApplication.run(IoniprogApplication.class, args);

	}

}

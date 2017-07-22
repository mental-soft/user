package com.teammental.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hcguler on 7/8/2017.
 */

/**
 * Config sınıfımız.
 */
@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

  /**
   * main metodur.
   * @param args standart parametre
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
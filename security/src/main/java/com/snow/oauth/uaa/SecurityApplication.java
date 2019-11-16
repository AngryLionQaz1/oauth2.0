package com.snow.oauth.uaa;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("api")
@EnableSwagger2Doc
public class SecurityApplication {


    @GetMapping("user")
    @PreAuthorize("hasAuthority('USER')")
    public String user(){
        return "user";
    }
    @GetMapping("admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin(){
        return "admin";
    }

    @GetMapping("all")
    public String all(){
        return "all";
    }



    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}

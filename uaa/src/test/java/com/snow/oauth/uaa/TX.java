package com.snow.oauth.uaa;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TX {


    @Test
    public void test1(){

        String secret = new BCryptPasswordEncoder().encode("secret");
        System.out.println(secret);

    }



}

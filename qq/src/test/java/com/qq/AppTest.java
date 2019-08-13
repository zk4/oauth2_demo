package com.qq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest
{

    @Autowired
    TestRestTemplate testRestTemplate;
    @Test
    public void loginTest() {
        ResponseEntity<String> forEntity = testRestTemplate.getForEntity("/login", String.class);
        String body = forEntity.getBody();
        assert body .equals("Hello");
    }
}

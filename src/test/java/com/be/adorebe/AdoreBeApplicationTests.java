package com.be.adorebe;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdoreBeApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(AdoreBeApplicationTests.class);

    @Test
    void contextLoads() {
        log.info("test");
    }

}

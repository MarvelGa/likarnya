package com.epam.likarnya;

import com.epam.likarnya.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes ={Application.class})
public class SpringBootContextTest {

        @Autowired
        private ApplicationContext context;

        @Test
        void checkContextForUserService() {
            assertNotNull(context.getBean(UserService.class));
        }

}

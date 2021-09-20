package com.epam.likarnya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomErrorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webappContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webappContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "nurse@gmail.com", roles = {"NURSE"})
    public void shouldGetStatus403whenUserWithNurseRoleTryToGoToAdminPages() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "nurse@gmail.com", roles = {"NURSE"})
    public void shouldGetStatus404whenUserWithNurseRoleTryToGoToWrongAddress() throws Exception {
        this.mockMvc
                .perform(get("/some4455/444"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetStatus302ToLoginPageWhenNotRegisteredUserTryToGoOnNotExistAddress() throws Exception {
        this.mockMvc
                .perform(get("/some4455/444"))
                .andExpect(status().isFound());
    }

    @Test
    public void shouldGetStatus302ToLoginPageWhenNotRegisteredUserTryToGoOnAdminPage() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isFound());
    }

}

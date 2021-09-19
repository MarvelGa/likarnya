package com.epam.likarnya;

import com.epam.likarnya.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
    @WithMockUser(username = "ivan@gmail.com", roles = {"NURSE"})
    public void shouldGetStatus403whenUserWithNurseRoleTryToGoToAdminPages() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isForbidden());
    }



    @Test
    public void shouldReturnLoginFormView() throws Exception {
        this.mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginPage"));
    }


    @Test
    public void shouldReturn404WhenPageIsNotFound() throws Exception {

//         when(customUserDetailsService.loadUserByUsername("admin@gmail.com")).thenReturn()
//        this.mockMvc
//                .perform(get("/somepage"))
//                .andExpect(status().is3xxRedirection())
//                .andReturn(/localhost/login);

    }
    @WithAnonymousUser
    @Test
    void whenNr() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isFound());
    }

    @WithUserDetails("Ella")
    @Test
    void whenNotValidInput_thenReturnsError() throws Exception {
        this.mockMvc
                .perform(get("/admin"))
                .andExpect(status().isFound());
    }

}

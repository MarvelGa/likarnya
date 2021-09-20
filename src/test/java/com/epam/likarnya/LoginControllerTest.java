package com.epam.likarnya;

import com.epam.likarnya.model.User;
import com.epam.likarnya.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private User admin;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webappContext)
                .apply(springSecurity())
                .build();

        admin = new User();
        admin.setId(2L);
        admin.setFirstName("Petro");
        admin.setLastName("Leontev");
        admin.setRole(User.Role.valueOf("ADMIN"));
    }

    @Test
    public void shouldReturn200AndLoginFormView() throws Exception {
        this.mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginPage"));
    }

    @Test
    public void shouldReturn200AndLoginFormErrorView() throws Exception {
        this.mockMvc
                .perform(get("/login-error"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("loginPage"));
    }

    @Test
    public void shouldReturn302ToLoginPage() throws Exception {
        this.mockMvc
                .perform(get("/perform-logout"))
                .andExpect(status().isFound());
    }

    @Test
    @WithMockUser(username = "ivan@gmail.com", roles = {"ADMIN"})
    public void shouldReturn302AndExecutedLogout() throws Exception {
        this.mockMvc
                .perform(post("/perform-logout")
                        .sessionAttr("user", admin))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void shouldReturn302AfterSuccessLogIn() throws Exception {
        when(userService.findByEmail(admin.getEmail())).thenReturn(admin);
        this.mockMvc
                .perform(get("/success"))
                .andExpect(status().isFound());

    }
}

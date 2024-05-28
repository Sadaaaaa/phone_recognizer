package com.example.phone_recognizer;

import com.example.phone_recognizer.controller.CheckPhoneController;
import com.example.phone_recognizer.service.CheckPhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CheckPhoneController.class)
public class CheckPhoneControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private CheckPhoneServiceImpl checkPhoneService;

    @InjectMocks
    private CheckPhoneController checkPhoneController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(checkPhoneController).build();
    }

    @Test
    void checkPhone_invalidNumber_tooShort() throws Exception {
        String phone = "12345";

        mockMvc.perform(get("/check").param("phone", phone))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMessage", "Wrong phone number!"));
    }

    @Test
    void checkPhone_invalidNumber_startsWithZero() throws Exception {
        String phone = "0123456789";

        mockMvc.perform(get("/check").param("phone", phone))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMessage", "Wrong phone number!"));
    }

    @Test
    void checkPhone_invalidNumber_nonNumeric() throws Exception {
        String phone = "12345abcd";

        mockMvc.perform(get("/check").param("phone", phone))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("errorMessage", "Wrong phone number!"));
    }
}

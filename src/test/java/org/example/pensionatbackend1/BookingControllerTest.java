package org.example.pensionatbackend1;

import org.example.pensionatbackend1.controller.BookingController;
import org.example.pensionatbackend1.service.BookingService;
import org.example.pensionatbackend1.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private CustomerService customerService;
}

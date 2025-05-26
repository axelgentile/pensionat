package org.example.pensionatbackend1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PensionatBackend1ApplicationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    ApplicationContext ctx;

    @Test
    void contextLoads() {
        // kontrollerar bara att Context startar utan fel
    }

    @Test
    void controllersAreRegisteredAsBeans() {
        assertThat(ctx.getBean(org.example.pensionatbackend1.controller.RoomController.class)).isNotNull();
        assertThat(ctx.getBean(org.example.pensionatbackend1.controller.CustomerController.class)).isNotNull();
        assertThat(ctx.getBean(org.example.pensionatbackend1.controller.BookingController.class)).isNotNull();
    }

    @Test
    void webServerIsUpAndEndpointReturns200() {
        String url = "http://localhost:" + port + "/rooms/all";
        var response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

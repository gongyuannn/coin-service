package com.example;

import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
public class CoinResourceTest {
    // start app with config
    static final DropwizardAppExtension<CoinConfiguration> EXT =
            new DropwizardAppExtension<>(CoinApplication.class, "config.yml");
    private static Client client;
    private static final GenericType<Map<String, String>> MAP_TYPE = new GenericType<>() {
    };

    @BeforeAll
    public static void setUpClient() {
        client = EXT.client();
    }

    // 1) Success case: valid request
    @Test
    public void testValidRequest() {
        CoinRequest req = new CoinRequest(
                BigDecimal.valueOf(7.03),
                List.of(
                        BigDecimal.valueOf(0.01),
                        BigDecimal.valueOf(0.5),
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(5),
                        BigDecimal.valueOf(10)
                )
        );

        String target = String.format("http://localhost:%d/coins/min", EXT.getLocalPort());
        Response response = client
                .target(target)
                .request()
                .post(Entity.entity(req, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        List<BigDecimal> actual = response.readEntity(new GenericType<>() {
        });
        List<BigDecimal> expected = List.of(
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(5)
        );
        assertEquals(expected, actual);
    }

    // 2) Edge case: 0 target amnt returns empty list
    @Test
    public void testZeroAmount() {
        CoinRequest req = new CoinRequest(BigDecimal.ZERO, List.of(BigDecimal.valueOf(0.01)));

        Response response = client
                .target(String.format("http://localhost:%d/coins/min", EXT.getLocalPort()))
                .request()
                .post(Entity.entity(req, MediaType.APPLICATION_JSON));

        assertEquals(200, response.getStatus());
        List<BigDecimal> actual = response.readEntity(new GenericType<>() {
        });
        assertEquals(List.of(), actual);
    }

    // 3) Out-of-range input: negative target amnt returns 422 + e.msg
    @Test
    public void testNegativeAmount() {
        CoinRequest req = new CoinRequest(BigDecimal.valueOf(-5), List.of(BigDecimal.valueOf(1)));

        Response resp = client.target(
                        String.format("http://localhost:%d/coins/min", EXT.getLocalPort()))
                .request()
                .post(Entity.entity(req, MediaType.APPLICATION_JSON));

        assertEquals(422, resp.getStatus(),
                "Expected HTTP 422 for negative amount");

        // read JSON body into a Map<String,String>
        Map<String, String> body = resp.readEntity(MAP_TYPE);
        assertEquals("Target amount out of range.", body.get("error"),
                "Error message should match exception mapper");
    }

    // Out-of-range input: over max returns 422 + e.msg
    @Test
    public void testOverMaxAmount() {
        CoinRequest req = new CoinRequest(BigDecimal.valueOf(10_000.01), List.of(BigDecimal.valueOf(1)));

        Response resp = client.target(
                        String.format("http://localhost:%d/coins/min", EXT.getLocalPort()))
                .request()
                .post(Entity.entity(req, MediaType.APPLICATION_JSON));

        assertEquals(422, resp.getStatus(),
                "Expected HTTP 422 for over range amount");

        // read JSON body into a Map<String,String>
        Map<String, String> body = resp.readEntity(MAP_TYPE);
        assertEquals("Target amount out of range.", body.get("error"),
                "Error message should match exception mapper");
    }

    // 4) No valid combination: cannot form target amnt
    @Test
    public void testImpossibleChange() {
        CoinRequest req = new CoinRequest(
                BigDecimal.valueOf(0.03),
                List.of(BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.1))
        );

        Response resp = client.target(
                        String.format("http://localhost:%d/coins/min", EXT.getLocalPort()))
                .request()
                .post(Entity.entity(req, MediaType.APPLICATION_JSON));

        assertEquals(422, resp.getStatus(),
                "Expected HTTP 422 when cannot form target");

        // Read and verify the “error” JSON property
        Map<String, String> body = resp.readEntity(MAP_TYPE);
        assertEquals("Cannot form target amount. Remaining: 0.03", body.get("error"),
                "Error message to reflect cannot form target");
    }

}

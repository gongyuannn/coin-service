package com.example;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import java.math.BigDecimal;

@Path("/coins/min")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoinResource {
    private final CoinService service = new CoinService();

    @POST
    public List<BigDecimal> minCoins(CoinRequest req) {
        // run by service
        return service.computeMinCoins(req);
    }
}

package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.util.EnumSet;

public class CoinApplication extends Application<CoinConfiguration> {
    public static void main(String[] args) throws Exception {
        new CoinApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<CoinConfiguration> b){}

    @Override
    public void run(CoinConfiguration cfg, Environment env) throws Exception {
        ObjectMapper mapper = env.getObjectMapper();
        mapper.deactivateDefaultTyping();
        // custom JSON error handler
        env.jersey().register(new JsonProcessingExceptionMapper());

        // enable CORS - permit make request from different origins
        FilterRegistration.Dynamic cors = env.servlets()
                .addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedMethods", "GET, POST, OPTIONS");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        // register resource
        env.jersey().register(new CoinResource());

        // register exception mapper
        env.jersey().register(IllegalArgumentExceptionMapper.class);
    }
}

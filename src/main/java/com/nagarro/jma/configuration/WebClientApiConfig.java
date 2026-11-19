package com.nagarro.jma.configuration;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientApiConfig {

    @Value("${api1.timeout}")
    private int api1Timeout;

    @Value("${api1.readTimeout}")
    private int api1ReadTimeout;

    @Value("${api1.writeTimeout}")
    private int api1WriteTimeout;

    @Value("${api2.timeout}")
    private int api2Timeout;

    @Value("${api2.readTimeout}")
    private int api2ReadTimeout;

    @Value("${api2.writeTimeout}")
    private int api2WriteTimeout;

    @Value("${api3.timeout}")
    private int api3Timeout;

    @Value("${api3.readTimeout}")
    private int api3ReadTimeout;

    @Value("${api3.writeTimeout}")
    private int api3WriteTimeout;

    @Bean("webClientApi1")
    public WebClient.Builder webClientBuilderApi1() {
        return WebClient.builder()
                .baseUrl("https://randomuser.me/api/")
                //helps to process the request and response for en/de
                .exchangeStrategies(ExchangeStrategies.builder()
                		//codecs are for en/de
                		//16 megabytes of data can be buffered in the memory while en/de 
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                		//maximum time user will wait to get response 
                        .responseTimeout(Duration.ofMillis(api1Timeout))
                        //allows customization of timeout behaviour
                        .doOnConnected(conn -> conn
                        		//max time allowed for reading data from server
                                .addHandlerLast(new ReadTimeoutHandler(api1ReadTimeout, TimeUnit.MILLISECONDS))
                                //max time allowed for writing data to the server
                                .addHandlerLast(new WriteTimeoutHandler(api1WriteTimeout, TimeUnit.MILLISECONDS)))));
    }

    @Bean("webClientApi2")
    public WebClient.Builder webClientBuilderApi2() {
        return WebClient.builder()
                .baseUrl("https://api.nationalize.io/")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofMillis(api2Timeout))
                        .doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(api2ReadTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(api2WriteTimeout, TimeUnit.MILLISECONDS)))));
    }

    @Bean("webClientApi3")
    public WebClient.Builder webClientBuilderApi3() {
        return WebClient.builder()
                .baseUrl("https://api.genderize.io/")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .responseTimeout(Duration.ofMillis(api3Timeout))
                        .doOnConnected(conn -> conn
                                .addHandlerLast(new ReadTimeoutHandler(api3ReadTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(api3WriteTimeout, TimeUnit.MILLISECONDS)))));
    }

}


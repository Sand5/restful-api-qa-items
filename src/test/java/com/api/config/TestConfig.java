package com.api.config;

import com.api.endpoints.ItemApi;
import com.api.service.ItemService;
import com.api.utils.ApiTestContext;
import com.api.utils.ConfigReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public ApiTestContext apiTestContext() {
        return new ApiTestContext();
    }

    @Bean
    public ItemApi itemApi() {
        return new ItemApi(ConfigReader.get("base.url"));
    }

    @Bean
    public ItemService itemService(ItemApi itemApi) {
        return new ItemService(itemApi);
    }
}
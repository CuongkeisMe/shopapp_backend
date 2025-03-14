package com.example.shopapp_backend.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    // khac voi repository chi can inject constructor
    // model mapper phai khoi tao rieng
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

package com.yash.ems.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI employeeManagementOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management System API")
                        .description("REST APIs for Employee Management System")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Yash Kondekar")
                                .email("yashkondekar03@gmail.com")
                        ));
    }
}
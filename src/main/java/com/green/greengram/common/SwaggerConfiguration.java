package com.green.greengram.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

import static java.awt.SystemColor.info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "그린그램"
                , description = "Greengram with react"
                , version = "v2"
        )
)
public class SwaggerConfiguration {

}

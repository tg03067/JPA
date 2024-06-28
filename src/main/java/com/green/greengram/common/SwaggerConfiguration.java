package com.green.greengram.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.awt.SystemColor.info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "그린그램"
                , description = "Greengram with react"
                , version = "v2"
        ),
        security = @SecurityRequirement(name = "authorization")
        // EndPoint 마다 자물쇠 아이콘 생성 ( 로그인 가능 )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP
        , name = "authorization"
        , in = SecuritySchemeIn.HEADER
        , bearerFormat = "JWT"
        , scheme = "Bearer"
)
public class SwaggerConfiguration {

}

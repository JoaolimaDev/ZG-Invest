package com.desafio_zg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition( servers= { @Server( url="/", description = "Servidor padrão root") } )
public class DesafioZgApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioZgApplication.class, args);
	}

}

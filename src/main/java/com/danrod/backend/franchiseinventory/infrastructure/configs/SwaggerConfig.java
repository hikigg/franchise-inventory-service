package com.danrod.backend.franchiseinventory.infrastructure.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Franchise Inventory Service API",
                description = "API reactiva para gestionar franquicias, sucursales y el inventario de productos. " +
                        "Esta API sigue los principios de Clean Architecture y utiliza programación reactiva con Spring WebFlux.",
                version = "1.0.0",
                contact = @Contact(
                        name = "Daniel Rodriguez (Backend Developer)",
                        email = "contacto.danrod@gmail.com",
                        url = "https://github.com/danrod" // Puedes poner tu URL de GitHub o portafolio
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class SwaggerConfig {
}

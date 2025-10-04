package com.danrod.backend.franchiseinventory.infrastructure.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /**
     * Configuración personalizada de Jackson para la serialización y deserialización JSON.
     * Esta configuración incluye:
     * - Soporte para las nuevas clases de fecha y hora de Java 8 (JSR-310).
     * - Evitar notación científica para números grandes.
     * - Indentación en la salida JSON.
     * - Inclusión solo de propiedades no nulas en la serialización.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // Soporte para fechas modernas de Java
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Evitar notación científica para números grandes
        mapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);

        // Indentación en la salida (equivalente a INDENT_OUTPUT: true)
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Incluir solo propiedades no nulas (equivale a default-property-inclusion: non_null)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper;
    }
}

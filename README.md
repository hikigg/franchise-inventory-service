# Franchise Inventory Service API

API reactiva para la gestión de inventario de franquicias, construida con Spring Boot 3 y siguiendo los principios de Clean Architecture.

## 1. Descripción General

Este proyecto es una API RESTful que permite manejar una jerarquía de **Franquicias**, **Sucursales** y **Productos**. Ha sido desarrollada utilizando un stack tecnológico moderno, enfocado en el rendimiento, la escalabilidad y la mantenibilidad a través de la programación reactiva y un diseño de software desacoplado.

## 2. Arquitectura del Proyecto (Clean Architecture)

El proyecto está estructurado siguiendo los principios de **Clean Architecture** para garantizar una separación clara de responsabilidades, alta cohesión y bajo acoplamiento entre los componentes. Esto facilita las pruebas, el mantenimiento y la evolución del sistema.

La estructura se divide en tres capas principales:

### Dominio
Esta es la capa central y más importante del sistema. Contiene la lógica y las reglas de negocio puras, representadas a través de las **entidades**.

-   **Responsabilidades:** Define los objetos de negocio (`FranchiseEntity`, `BranchEntity`, `ProductEntity`) y sus comportamientos.
-   **Independencia:** No depende de ninguna otra capa. No tiene conocimiento de la base de datos, la API o cualquier framework.
-   **Reutilización:** Se utilizan clases base como `BaseEntity` y `AuditableEntity` para evitar la repetición de código en campos comunes como `id`, `uuid` y campos de auditoría.

### Aplicacion
Esta capa orquesta el flujo de datos y las interacciones para cumplir con los casos de uso del negocio. Actúa como un intermediario entre la capa de `Infrastructure` y la de `Domain`.

-   **Responsabilidades:**
    -   **Use Cases:** Contiene la lógica específica de la aplicación (ej. `CreateFranchiseUseCase`, `UpdateProductStockUseCase`).
    -   **Interfaces de Servicio:** Define los contratos que la capa de `Infrastructure` debe implementar (ej. `IFranchiseService`).
    -   **DTOs (Data Transfer Objects):** Define los objetos para la comunicación con el exterior (`FranchiseResponseDTO`, `CreateProductCommand`), asegurando que la capa de dominio no se exponga directamente.

###  Infraestructura
Esta capa contiene todos los detalles técnicos y las implementaciones concretas que interactúan con el mundo exterior.

-   **Responsabilidades:**
    -   **Controllers:** Expone los endpoints de la API REST (`FranchiseController`).
    -   **Repositories:** Implementa las interfaces de acceso a datos utilizando R2DBC para PostgreSQL (`FranchiseRepository`).
    -   **Mappers:** Convierte entre entidades del dominio y DTOs (`FranchiseMapper`).
    -   **Configs:** Contiene la configuración del framework, como `SwaggerConfig`, `GlobalExceptionHandler` y la configuración de Docker.

## 3. Stack Tecnológico

-   **Lenguaje:** Java 19+
-   **Framework:** Spring Boot 3 (WebFlux)
-   **Acceso a Datos:** R2DBC (Reactive Relational Database Connectivity)
-   **Base de Datos:** PostgreSQL
-   **Migraciones:** Flyway
-   **Pruebas:** JUnit 5, Mockito, Reactor Test
-   **Contenerización:** Docker / Docker Compose
-   **Documentación API:** SpringDoc (Swagger UI)
-   **Observabilidad:** Spring Boot Actuator

## 4. Ejecución en Entorno Local

Para levantar todo el entorno, solo necesitas tener Docker y Docker Compose instalados.

### Prerrequisitos

-   [Docker](https://www.docker.com/get-started)
-   [Docker Compose](https://docs.docker.com/compose/install/)

### Pasos para la Ejecución

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/hikigg/franchise-inventory-service.git
    cd franchise-inventory-service
    ```

2.  **Crear el archivo de entorno:**
    Copia el archivo `.env.example` y renómbralo a `.env`. Las credenciales por defecto funcionarán sin problemas.
    ```bash
    cp .env.example .env
    ```

3.  **Levantar los contenedores:**
    Este comando realiza todo el proceso:
    - Construye la imagen de la aplicación Spring Boot.
    - Levanta los contenedores para la API, la base de datos PostgreSQL y Redis.
    - Espera a que la base de datos esté saludable antes de iniciar la API.
    ```bash
    docker-compose up --build
    ```
    La API estará disponible en `http://localhost:8080`.

## 5. Documentación y Monitoreo

### Documentación de la API (Swagger)

Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva de la API (Swagger UI), donde podrás explorar y probar todos los endpoints disponibles.

-   **URL:** [**http://localhost:8080/docs**](http://localhost:8080/docs)

### Métricas y Salud de la Aplicación (Actuator)

Se ha implementado Spring Boot Actuator para exponer información útil sobre el estado de la aplicación.

-   **Punto de salud (Health Check):** `http://localhost:8080/actuator/health`
-   **Información de la aplicación:** `http://localhost:8080/actuator/info`
-   **Métricas para Prometheus:** `http://localhost:8080/actuator/prometheus`

## 6. Base de Datos y Migraciones

La persistencia de datos se realiza en una base de datos **PostgreSQL**. El esquema de la base de datos (tablas, índices, relaciones) se gestiona de forma automática y versionada a través de **Flyway**.

Los scripts de migración se encuentran en `src/main/resources/db/migration`. Cada vez que la aplicación se inicia, Flyway verifica si hay nuevas migraciones y actualiza la base de datos según sea necesario, garantizando consistencia entre entornos.

## 7. Despliegue en la Nube (AWS con Terraform)

Además de la ejecución local, este proyecto está completamente configurado para ser desplegado en AWS utilizando un enfoque de Infraestructura como Código (IaC) con Terraform.

Este proceso automatiza la creación de todos los recursos necesarios en la nube, incluyendo:
-   Una red virtual (VPC) con subredes públicas.
-   Una base de datos PostgreSQL gestionada a través de Amazon RDS.
-   Un repositorio de imágenes de Docker (Amazon ECR).
-   Un clúster de contenedores (Amazon ECS) con AWS Fargate para una ejecución sin servidor.
-   Un balanceador de carga de aplicación (ALB) para distribuir el tráfico y exponer la API a internet.

Para obtener las instrucciones detalladas paso a paso sobre cómo realizar el despliegue en la nube, consulta la guía de despliegue:

**[➡️ Guía de Despliegue en AWS con Terraform (DEPLOYMENT.md)](DEPLOYMENT.md)**
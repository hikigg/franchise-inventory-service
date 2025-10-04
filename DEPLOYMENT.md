# Guía de Despliegue en AWS con Terraform

Este documento describe el proceso paso a paso para desplegar la aplicación **Franchise Inventory Service** en Amazon Web Services (AWS) utilizando Docker e Infraestructura como Código (IaC) con Terraform.

## 1. Prerrequisitos

Antes de comenzar, asegúrate de tener instaladas y configuradas las siguientes herramientas en tu máquina local:

-   **Cuenta de AWS:** Acceso a una cuenta de AWS con permisos para crear los recursos necesarios (IAM, VPC, ECR, ECS, RDS, Load Balancer).
-   **AWS CLI:** La interfaz de línea de comandos de AWS. Debes configurarla con tus credenciales ejecutando `aws configure`.
-   **Terraform:** La herramienta de IaC. Verifica la instalación con `terraform -v`.
-   **Docker y Docker Compose:** Necesarios para construir la imagen de la aplicación.

## 2. Archivos de Configuración

Toda la infraestructura de AWS está definida en archivos de Terraform (`.tf`) ubicados en el directorio `/terraform`. Estos archivos describen la red (VPC), la base de datos (RDS), el repositorio de contenedores (ECR), el clúster de ejecución (ECS) y el balanceador de carga (ALB).

## 3. Proceso de Despliegue Paso a Paso

Para garantizar un despliegue exitoso, es crucial seguir estos pasos en el orden correcto.

### Paso 1: Construir la Imagen de Docker

La configuración para producción (`application-prod.yml`) ha sido actualizada para leer las credenciales de la base de datos desde variables de entorno. Es necesario construir una nueva imagen con estos cambios.

1.  Abre una terminal en la **raíz del proyecto**.
2.  Ejecuta el siguiente comando para construir la imagen:
    ```bash
    docker build -t franchise-inventory-service .
    ```

### Paso 2: Preparar el Entorno de Terraform

Este paso inicializa Terraform y limpia cualquier despliegue anterior fallido para asegurar un inicio limpio.

1.  Navega al directorio `terraform`:
    ```bash
    cd terraform
    ```
2.  (Opcional, recomendado) Si tienes un despliegue anterior, destrúyelo:
    ```bash
    terraform destroy -auto-approve
    ```
3.  Inicializa Terraform para descargar los proveedores necesarios:
    ```bash
    terraform init
    ```

### Paso 3: Crear el Repositorio de Contenedores (ECR)

Antes de subir la imagen, necesitamos un lugar en AWS donde almacenarla.

1.  En el directorio `terraform`, ejecuta el siguiente comando para crear *únicamente* el repositorio ECR:
    ```powershell
    terraform apply -target aws_ecr_repository.app -auto-approve
    ```

### Paso 4: Subir la Imagen de Docker a ECR

Con el repositorio ya creado, ahora podemos subir nuestra imagen local.

1.  **Autentica Docker con ECR:** (Comandos para PowerShell)
    ```powershell
    $env:ECR_PASSWORD = (aws ecr get-login-password --region us-east-1)
    docker login --username AWS --password $env:ECR_PASSWORD "$(aws sts get-caller-identity --query Account --output text).dkr.ecr.us-east-1.amazonaws.com"
    ```

2.  **Obtén la URL del repositorio:** En el directorio `terraform`, ejecuta:
    ```powershell
    terraform output -raw ecr_repository_url
    ```
    **Copia la URL** que aparece en la pantalla (ej: `123456789012.dkr.ecr.us-east-1.amazonaws.com/franchise-inventory-service`).

3.  **Navega a la raíz del proyecto** y usa la URL copiada para etiquetar y subir la imagen:
    ```powershell
    cd ..
    # Reemplaza [TU_URL_DE_ECR] con la URL que copiaste
    docker tag franchise-inventory-service:latest [TU_URL_DE_ECR]:latest
    docker push [TU_URL_DE_ECR]:latest
    ```

### Paso 5: Desplegar la Infraestructura Completa

Con la imagen ya en ECR, es hora de lanzar toda la pila de la aplicación, incluyendo la base de datos RDS y el servicio ECS.

1.  Regresa al directorio `terraform`:
    ```bash
    cd terraform
    ```
2.  Ejecuta el comando `apply` final. Este proceso creará todos los recursos restantes y puede tardar entre 5 y 10 minutos.
    ```bash
    terraform apply -auto-approve
    ```

## 4. Verificación y Acceso

Una vez que el comando `apply` se complete, Terraform mostrará una salida (`output`) con el nombre DNS del balanceador de carga (`alb_dns_name`).

-   **URL de la API:** `http://[valor_de_alb_dns_name]`
-   **URL de la Documentación Swagger:** `http://[valor_de_alb_dns_name]/docs`

**Nota:** Después de que Terraform termine, la aplicación puede tardar uno o dos minutos en arrancar y pasar las verificaciones de estado. Si inicialmente recibes un error `503`, espera un momento y refresca la página.

## 5. Limpieza de Recursos

Para evitar costos inesperados en tu cuenta de AWS, es **crucial** destruir toda la infraestructura una vez que hayas terminado tus pruebas.

1.  En el directorio `terraform`, ejecuta:
    ```bash
    terraform destroy -auto-approve
    ```
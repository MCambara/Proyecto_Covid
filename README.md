# Proyecto de Integración de APIs y Persistencia de Datos

## Descripción del Proyecto

Este proyecto fue desarrollado de manera colaborativa y tiene como objetivo integrar múltiples APIs, procesar los datos obtenidos y almacenarlos en una base de datos estructurada. Se utilizó Java con el framework Spring Boot y bibliotecas como Gson y JSON para facilitar el manejo de la información.

## Estructura del Proyecto

El desarrollo se dividió en tres partes principales, cada una implementada por un miembro del equipo:

### 1. Primera API y almacenamiento en HashMap

Uno de los integrantes implementó la **primera API**, que recupera información clave (como una fecha) y la almacena en un `HashMap` para su uso posterior en el flujo del programa.

### 2. Procesamiento intermedio con validación

El segundo integrante utilizó los datos generados por la primera API como entrada para una **segunda API**. Antes de realizar la solicitud, se integró un mecanismo de prueba utilizando **Log4j** para llevar un registro ordenado y verificable de las llamadas y respuestas, permitiendo validar que las solicitudes se ejecutaban correctamente sin necesidad de un framework de testing como JUnit.

### 3. Recolección automatizada con hilos

El tercer integrante implementó un **hilo** que, después de **15 segundos desde el inicio del programa**, llama automáticamente a todos los reportes correspondientes a la fecha generada por la primera API. Esta lógica permite automatizar la recolección de datos desde todas las APIs involucradas.

## Persistencia en Base de Datos

Una vez recibidos los datos desde las APIs, y antes de ser mostrados por consola, el programa los almacena en una **base de datos relacional**. Se utiliza el patrón de diseño **DAO-Service-Model**, con clases separadas para cada una de las tres APIs, y una tabla en la base de datos por cada una.

## Tecnologías Utilizadas

- **Java**
- **Spring Boot**
- **Gson / JSON** para el manejo de datos en formato JSON
- **Hilos (Threads)** para la ejecución automática en segundo plano
- **HashMap** como estructura de almacenamiento temporal
- **Base de datos relacional** (por ejemplo, MySQL)
- **application.properties** para la configuración del entorno y datos sensibles
- **Log4j** para el manejo de logs y seguimiento de ejecución

## Manejo de Configuración

Las configuraciones sensibles, como la conexión a la base de datos y otras propiedades del entorno, están definidas en el archivo `application.properties`. Este archivo es interpretado automáticamente por Spring Boot y permite un manejo seguro y centralizado de la configuración.

## Conclusión

Este proyecto demuestra la integración de múltiples APIs con procesamiento de datos automatizado, uso de hilos para tareas programadas y persistencia en base de datos utilizando buenas prácticas de diseño de software. Cada parte fue desarrollada por un miembro del equipo, permitiendo una división clara de responsabilidades y una colaboración efectiva.

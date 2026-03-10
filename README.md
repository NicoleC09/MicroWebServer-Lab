# MicroWebServer-Lab

Proyecto de laboratorio para construir un micro servidor web en Java con un mini framework IoC basado en anotaciones.

## Requisitos

- Java 17+
- Maven 3.9+

## Estructura Maven

El proyecto sigue la estructura estandar:

- `src/main/java`: codigo fuente del servidor y framework.
- `src/main/resources`: recursos estaticos (HTML/PNG).
- `src/test/java`: pruebas unitarias.

## Compilar y probar

```bash
mvn clean test
```

## Empaquetar

```bash
mvn clean package
```

El artefacto generado queda en `target/`.

## Ejecutar la aplicacion

### Opcion 1: Descubrimiento automatico de controladores

```bash
mvn exec:java -Dexec.mainClass=co.edu.escuelaing.reflexionlab.MicroSpringBoot
```

### Opcion 2: Cargar controlador por linea de comandos

```bash
mvn exec:java -Dexec.mainClass=co.edu.escuelaing.reflexionlab.MicroSpringBoot -Dexec.args="co.edu.escuelaing.reflexionlab.examples.GreetingController"
```

## Endpoints de ejemplo

- `GET /` retorna un mensaje desde `GreetingController`.
- `GET /greeting` retorna `Hola World`.
- `GET /greeting?name=Nicole` retorna `Hola Nicole`.

## Contenido estatico

El servidor resuelve archivos desde `src/main/resources/public`.

- `GET /index.html`.
- Soporte para `.html` y `.png`.

## Alcance implementado

- `@RestController` para identificar componentes web.
- `@GetMapping` para mapear servicios HTTP GET.
- `@RequestParam` para extraer parametros query.
- Descubrimiento por classpath (paquete raiz) o carga explicita por argumento.
- Manejo secuencial de multiples solicitudes (no concurrente).

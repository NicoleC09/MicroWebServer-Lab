# MicroWebServer-Lab

Laboratory project to build a micro web server in Java with a mini IoC (Inversion of Control) framework based on annotations.

---

## Project Information

**Author:** Nicole Dayan Calderón Arevalo  
**Course:** Digital Transformation and Business Solutions  
**University:** Escuela Colombiana de Ingeniería Julio Garavito

---

## Requirements

- Java 17+
- Maven 3.9+

## Project Structure

The project follows the standard Maven structure:

- `src/main/java`: source code for the server and framework.
- `src/main/resources`: static resources (HTML/PNG).
- `src/test/java`: unit tests.

## Compile and Test

```bash
mvn clean test
```

## Package

```bash
mvn clean package
```

The generated artifact is located in `target/`.

## Running the Application

### Option 1: Automatic Controller Discovery

```bash
mvn exec:java -Dexec.mainClass=co.edu.escuelaing.reflexionlab.MicroSpringBoot
```

### Option 2: Load Controller via Command Line

```bash
mvn exec:java -Dexec.mainClass=co.edu.escuelaing.reflexionlab.MicroSpringBoot -Dexec.args="co.edu.escuelaing.reflexionlab.examples.GreetingController"
```

## Example Endpoints

- `GET /` returns a message from `GreetingController`.
- `GET /greeting` returns `Hola World`.
- `GET /greeting?name=Nicole` returns `Hola Nicole`.

## Static Content

The server serves files from `src/main/resources/public`.

- `GET /index.html`
- Support for `.html` and `.png` files.

## Implemented Features

- `@RestController` annotation to identify web components.
- `@GetMapping` annotation to map HTTP GET services.
- `@RequestParam` annotation to extract query parameters.
- Classpath discovery (root package) or explicit loading via command-line argument.
- Sequential handling of multiple requests (non-concurrent).

---

## AWS Deployment Evidence

![alt text](<WhatsApp Image 2026-03-09 at 10.04.17 PM.jpeg>)

![alt text](<WhatsApp Image 2026-03-09 at 10.05.40 PM.jpeg>)

![alt text](<WhatsApp Image 2026-03-09 at 10.05.40 PM-1.jpeg>)

---

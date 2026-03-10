package co.edu.escuelaing.reflexionlab.framework;

import co.edu.escuelaing.reflexionlab.annotations.GetMapping;
import co.edu.escuelaing.reflexionlab.annotations.RequestParam;
import co.edu.escuelaing.reflexionlab.annotations.RestController;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleApplicationContextTest {

    @Test
    void shouldResolveRequestParamDefaultValue() {
        SimpleApplicationContext context = new SimpleApplicationContext();
        context.registerControllerClass(TestController.class);

        Optional<String> response = context.executeGetRoute("/greeting", Map.of());

        assertTrue(response.isPresent());
        assertEquals("Hola World", response.get());
    }

    @Test
    void shouldResolveRequestParamProvidedValue() {
        SimpleApplicationContext context = new SimpleApplicationContext();
        context.registerControllerClass(TestController.class);

        Optional<String> response = context.executeGetRoute("/greeting", Map.of("name", "Nicole"));

        assertTrue(response.isPresent());
        assertEquals("Hola Nicole", response.get());
    }

    @RestController
    public static class TestController {

        @GetMapping("/greeting")
        public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
            return "Hola " + name;
        }
    }
}

package co.edu.escuelaing.reflexionlab.examples;

import co.edu.escuelaing.reflexionlab.annotations.GetMapping;
import co.edu.escuelaing.reflexionlab.annotations.RequestParam;
import co.edu.escuelaing.reflexionlab.annotations.RestController;

@RestController
public class GreetingController {

    @GetMapping("/")
    public String index() {
        return "Greetings from MicroSpringBoot!";
    }

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}

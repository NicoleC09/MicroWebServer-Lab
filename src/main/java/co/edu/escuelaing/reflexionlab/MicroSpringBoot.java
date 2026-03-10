package co.edu.escuelaing.reflexionlab;

import co.edu.escuelaing.reflexionlab.framework.ClassPathScanner;
import co.edu.escuelaing.reflexionlab.framework.SimpleApplicationContext;
import co.edu.escuelaing.reflexionlab.http.MicroWebServer;

import java.io.IOException;
import java.util.List;

public class MicroSpringBoot {

    private static final String ROOT_PACKAGE = "co.edu.escuelaing.reflexionlab";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "35000"));
        SimpleApplicationContext context = new SimpleApplicationContext();

        if (args.length > 0) {
            for (String className : args) {
                context.registerControllerClass(Class.forName(className));
            }
        } else {
            List<Class<?>> controllers = new ClassPathScanner().findRestControllers(ROOT_PACKAGE);
            for (Class<?> controller : controllers) {
                context.registerControllerClass(controller);
            }
        }

        new MicroWebServer(port, context).start();
    }
}

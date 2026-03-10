package co.edu.escuelaing.reflexionlab.framework;

import co.edu.escuelaing.reflexionlab.annotations.RestController;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassPathScanner {

    public List<Class<?>> findRestControllers(String rootPackage) {
        String packagePath = rootPackage.replace('.', '/');
        List<Class<?>> foundControllers = new ArrayList<>();

        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (!"file".equals(resource.getProtocol())) {
                    continue;
                }
                Path rootPath = Paths.get(resource.toURI());
                scanPath(rootPath, rootPackage, foundControllers);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to scan classpath for controllers", e);
        }

        return foundControllers;
    }

    private void scanPath(Path rootPath, String rootPackage, List<Class<?>> foundControllers) throws IOException {
        try (var stream = Files.walk(rootPath)) {
            stream.filter(path -> path.toString().endsWith(".class"))
                    .forEach(path -> addIfController(path, rootPath, rootPackage, foundControllers));
        }
    }

    private void addIfController(Path classFilePath, Path rootPath, String rootPackage,
                                 List<Class<?>> foundControllers) {
        String relativePath = rootPath.relativize(classFilePath).toString();
        String className = rootPackage + "." + relativePath
                .replace('/', '.')
                .replace('\\', '.')
                .replaceAll("\\.class$", "");

        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(RestController.class)) {
                foundControllers.add(clazz);
            }
        } catch (ClassNotFoundException ignored) {
            // Ignore classes that cannot be loaded by name.
        }
    }
}

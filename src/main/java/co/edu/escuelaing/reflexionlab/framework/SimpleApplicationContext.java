package co.edu.escuelaing.reflexionlab.framework;

import co.edu.escuelaing.reflexionlab.annotations.GetMapping;
import co.edu.escuelaing.reflexionlab.annotations.RequestParam;
import co.edu.escuelaing.reflexionlab.annotations.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleApplicationContext {

    private final Map<String, RouteMethod> getRoutes = new ConcurrentHashMap<>();

    public void registerControllerClass(Class<?> controllerClass) {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            return;
        }

        Object controllerInstance = instantiateController(controllerClass);

        for (Method method : controllerClass.getDeclaredMethods()) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            if (mapping == null) {
                continue;
            }
            validateRouteMethod(method);
            getRoutes.put(mapping.value(), RouteMethod.from(controllerInstance, method));
        }
    }

    public Optional<String> executeGetRoute(String path, Map<String, String> queryParams) {
        RouteMethod routeMethod = getRoutes.get(path);
        if (routeMethod == null) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(routeMethod.invoke(queryParams));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Error invoking route: " + path, e);
        }
    }

    private Object instantiateController(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Controller must have a public empty constructor: "
                    + controllerClass.getName(), e);
        }
    }

    private void validateRouteMethod(Method method) {
        if (!method.getReturnType().equals(String.class)) {
            throw new IllegalArgumentException("Only String return type is supported for @GetMapping methods: "
                    + method.getName());
        }

        for (Parameter parameter : method.getParameters()) {
            if (!parameter.getType().equals(String.class)) {
                throw new IllegalArgumentException("Only String parameters are supported in @GetMapping methods: "
                        + method.getName());
            }
            if (!parameter.isAnnotationPresent(RequestParam.class)) {
                throw new IllegalArgumentException("All parameters in @GetMapping methods must use @RequestParam: "
                        + method.getName());
            }
        }
    }

    private record RouteMethod(Object controller, Method method, List<ParameterBinding> bindings) {

        static RouteMethod from(Object controller, Method method) {
            List<ParameterBinding> bindingList = new ArrayList<>();
            for (Parameter parameter : method.getParameters()) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                bindingList.add(new ParameterBinding(requestParam.value(), requestParam.defaultValue()));
            }
            return new RouteMethod(controller, method, bindingList);
        }

        String invoke(Map<String, String> queryParams)
                throws InvocationTargetException, IllegalAccessException {
            Object[] args = new Object[bindings.size()];
            for (int i = 0; i < bindings.size(); i++) {
                ParameterBinding binding = bindings.get(i);
                String value = queryParams.getOrDefault(binding.name(), binding.defaultValue());
                args[i] = value;
            }
            return (String) method.invoke(controller, args);
        }
    }

    private record ParameterBinding(String name, String defaultValue) {
    }
}

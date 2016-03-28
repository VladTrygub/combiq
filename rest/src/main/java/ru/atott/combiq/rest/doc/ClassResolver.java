package ru.atott.combiq.rest.doc;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class ClassResolver {

    private static Map<String, Class> classMap = Collections.synchronizedMap(new HashMap<>());

    private ClassResolver() { }

    public static Class resolveClass(String className) {

        if (StringUtils.isBlank(className)) {
            return null;
        }

        if (classMap.containsKey(className)) {
            return classMap.get(className);
        }

        Class result = null;

        if (!className.contains(".")) {
            result = resolveClassBySimpleName(className);
        }

        if (result == null) {
            result = resolveClassByFullName(className);
        }

        classMap.put(className, result);

        return result;
    }

    private static Class resolveClassBySimpleName(String simpleClassName) {
        String[] packages = {
            "ru.atott.combiq.rest.bean",
            "ru.atott.combiq.rest.request"
        };

        return Arrays.asList(packages).stream()
                .flatMap(packageName -> findPackageStartingWith(packageName).stream())
                .map(p -> {
                    String fullClassName = p.getName() + "." + simpleClassName;
                    return resolveClass(fullClassName);
                })
                .filter(clazz -> clazz != null)
                .findAny().orElse(null);
    }

    private static Class resolveClassByFullName(String className) {

        // Попробовать загрузить Java класс.

        Class result = resolveJavaClassByFullName(className);

        if (result != null) {
            return result;
        }

        return result;
    }

    private static Class resolveJavaClassByFullName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e){
            // not in this package, try another
        }
        return null;
    }

    private static List<Package> findPackageStartingWith(String packageNamePrefix) {
        return Arrays.asList(Package.getPackages()).stream()
                .filter(p -> p.getName().startsWith(packageNamePrefix))
                .collect(Collectors.toList());
    }
}

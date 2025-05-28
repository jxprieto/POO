package com.opensky.utils;


import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Dependency interface and its subclasses to ensure that
 * all subclasses override the static method createInstance.
 */
class DependencyTest {

    private static final Reflections reflections = new Reflections("com.opensky");

    @Test
    void allConstructorsForImplementationsArePrivate(){
        Set<Class<? extends Dependency>> implementations = reflections.getSubTypesOf(Dependency.class);
        implementations.forEach(implementation -> {
            boolean allPrivate = Arrays.stream(implementation.getDeclaredConstructors())
                    .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers()));
            assertTrue(allPrivate, "Not all constructors of " + implementation.getName() + " are private");
        });
    }

    @Test
    void staticMethodInInterfaceMustThrowException() {
        assertThrows(UnsupportedOperationException.class, Dependency::createInstance);
    }

    @Test
    void allSubclassesMustOverrideCreateInstance() {
        var staticMethodsInInterface = Arrays.stream(Dependency.class.getMethods())
                .filter(o-> Modifier.isStatic(o.getModifiers()))
                .toList();

        Set<Class<? extends Dependency>> implementations = reflections.getSubTypesOf(Dependency.class);
        implementations.forEach(implementation ->
            staticMethodsInInterface.forEach(staticMethodInInterface->
                    assertThatStaticMethodIsPresentInImplementation(staticMethodInInterface, implementation)
            )
        );
    }

    private static void assertThatStaticMethodIsPresentInImplementation(Method staticMethodInInterface,
                                                                           Class<? extends Dependency> implementation) {
        try {
            var methodInImplementation = implementation.getMethod(staticMethodInInterface.getName());
            assertTrue(Modifier.isStatic(methodInImplementation.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("the class " + implementation.getName()
                    + " does not override the static method " + staticMethodInInterface.getName()
                    + " from the interface Dependency. All subclasses must override this method.");
        }
    }


}
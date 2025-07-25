package kkdt.keycloak.desktop;

import java.util.function.Consumer;

/**
 * RESTful model to package data from the application to the user.
 */
public class Counter {
    public int value;

    public Counter with(Consumer<Counter> c) {
        c.accept(this);
        return this;
    }
}

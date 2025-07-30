package com.craftinginterpreters.lox;

public class Return extends Break {
    final Object value;

    Return(Object value) {
        super();
        this.value = value;
    }
}

package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

class LoxInstance {
    private LoxClass klass;
    private final Map<String, Object> fields;

    LoxInstance(Map<String, Object> fields) {
        this.fields = fields;
        this.klass = null;
    }

    LoxInstance(LoxClass klass) {
        this.fields = new HashMap<>();
        this.klass = klass;
    }

    Object get(Token name) {

        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }

        if (klass == null) {
            throw new RuntimeError(name,
                    "Undefined static property '" + name.lexeme + "'.");
        }

        LoxFunction method = klass.findMethod(name.lexeme);
        if (method != null) return method.bind(this);

        throw new RuntimeError(name,
            "Undefined property '" + name.lexeme + "'.");
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    @Override
    public String toString() {
        return klass.name + " instance.";
    }
}

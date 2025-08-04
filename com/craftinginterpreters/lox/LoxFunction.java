package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final boolean isInitializer;
    private final Environment closure;
    private final Expr.Function func;
    LoxFunction(Stmt.Function declaration, Environment closure,
        boolean isInitializer) {
        this.closure = closure;
        this.declaration = declaration;
        this.func = (Expr.Function)declaration.lambda;
        this.isInitializer = isInitializer;
    }
    LoxFunction(Expr.Function lambda, Environment closure,
        boolean isInitializer) {
        this.closure = closure;
        this.declaration = new Stmt.Function(lambda.name, lambda);
        this.func = lambda;
        this.isInitializer = isInitializer;
    }

    LoxFunction bind(LoxInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new LoxFunction(declaration, environment, isInitializer);
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        //Expr.Function func = (Expr.Function)declaration.lambda;
        for (int i = 0; i < func.params.size(); i++) {
            environment.define(func.params.get(i).lexeme,
                arguments.get(i));
        }

        try {
            interpreter.executeBlock(func.body, environment);
        } catch (Return returnValue) {
            if (isInitializer) return closure.getAt(0, "this");
            return returnValue.value;
        }

        if (isInitializer) return closure.getAt(0, "this");
        return null;
    }
    @Override
    public int arity() {
        return func.params.size();
    }
    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

}

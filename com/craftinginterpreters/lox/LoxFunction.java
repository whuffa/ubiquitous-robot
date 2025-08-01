package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Environment closure;
    private final Expr.Function func;
    LoxFunction(Stmt.Function declaration, Environment closure) {
        this.closure = closure;
        this.declaration = declaration;
        this.func = (Expr.Function)declaration.lambda;
    }
    LoxFunction(Expr.Function lambda, Environment closure) {
        this.closure = closure;
        this.declaration = new Stmt.Function(lambda.name, lambda);
        this.func = (Expr.Function)lambda;
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
            return returnValue.value;
        }
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

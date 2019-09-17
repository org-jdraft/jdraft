package org.jdraft.macro;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.jdraft._method;

import java.lang.annotation.*;

/**
 * Annotation/Macro to set the default modifier on a {@link org.jdraft._interface} {@link _method}
 *
 * For Interface METHODS,
 * sets the interface to be default and
 * REMOVES the static modifier if it exists
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface _default {

    class Act extends macro<_default, MethodDeclaration> {

        public Act(){
            super(_default.class);
        }

        public Act(_default _d){
            super(_d);
        }

        @Override
        public void expand(MethodDeclaration methodDeclaration) {
            to(methodDeclaration);
        }

        public static MethodDeclaration to(MethodDeclaration methodDeclaration){
            methodDeclaration.setStatic(false);
            methodDeclaration.setDefault(true);
            return methodDeclaration;
        }

        @Override
        public String toString(){
            return "macro[default]";
        }
    }
}

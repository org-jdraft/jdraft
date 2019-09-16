package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.jdraft._method;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to set the default modifier on a {@link org.jdraft._interface} {@link _method}
 *
 * For Interface METHODS,
 * sets the interface to be default and
 * REMOVES the static modifier if it exists
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface _default {

    /*
    Macro $ = new Macro();

    class Macro implements _macro<_method> {

        @Override
        public String toString(){
           return "macro[default]"; 
        }
        
        @Override
        public _method apply(_method _m) {
            return to(_m);
        }

        public static _method to( _method _m ){
            _m.getModifiers().setStatic(false);
            _m.getModifiers().setDefault();
            return _m;
        }
    }
     */

    class Act extends macro<_default, MethodDeclaration> {

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

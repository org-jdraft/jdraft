package org.jdraft.macro;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.jdraft._anno;
import org.jdraft._modifiers;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro for setting the native modifier on a {@link org.jdraft._method}
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface _native {
    Macro $ = new Macro();

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[native]"; 
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _model) {
            return to(_model);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setNative();
            return _model;
        }
    }

    class Act extends macro<_native,MethodDeclaration>{ //} Consumer<MethodDeclaration> {

        public Act(){
            super(_native.class);
        }

        public Act(_native _n){
            super(_n);
        }

        @Override
        public void expand(MethodDeclaration methodDeclaration) {
            methodDeclaration.setNative(true);
        }

        @Override
        public String toString(){
            return "macro[native]";
        }
    }
}

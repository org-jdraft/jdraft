package org.jdraft.macro;

import com.github.javaparser.ast.body.MethodDeclaration;

import java.lang.annotation.*;

/**
 * Annotation/Macro for setting the native modifier on a {@link org.jdraft._method}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface _native {

    class Act extends macro<_native,MethodDeclaration>{

        public Act(){
            super(_native.class);
        }

        public Act(_native _n){
            super(_n);
        }

        @Override
        public void expand(MethodDeclaration methodDeclaration) {
            to(methodDeclaration);
        }

        public static MethodDeclaration to(MethodDeclaration md){
            md.setNative(true);
            return md;
        }

        @Override
        public String toString(){
            return "macro[native]";
        }
    }
}

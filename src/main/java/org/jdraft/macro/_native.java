package org.jdraft.macro;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.jdraft._anno;
import org.jdraft._modifiers;

import java.lang.annotation.*;
import java.util.function.Consumer;

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

    class Act implements Consumer<MethodDeclaration> {

        @Override
        public void accept(MethodDeclaration methodDeclaration) {
            methodDeclaration.setNative(true);
        }
    }
}

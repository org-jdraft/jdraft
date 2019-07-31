package org.jdraft.macro;

import org.jdraft._modifiers;
import org.jdraft._anno;
import org.jdraft._anno._hasAnnos;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface _static  {

    Macro $ = new Macro();

    class Macro implements _macro<_hasAnnos> {

        @Override
        public String toString(){
           return "macro[static]"; 
        }
        
        @Override
        public _hasAnnos apply(_anno._hasAnnos _annotatedModel) {
            return to(_annotatedModel);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setStatic();
            return _model;
        }
    }
}

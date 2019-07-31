package org.jdraft.macro;

import org.jdraft._field;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface _transient {

    Macro $ = new Macro();

    class Macro implements _macro<_field> {

        @Override
        public String toString(){
           return "macro[transient]"; 
        }
        
        @Override
        public _field apply(_field _f) {
            return to(_f);
        }

        public static _field to( _field _f ){
            _f.getModifiers().setTransient();
            return _f;
        }
    }
}

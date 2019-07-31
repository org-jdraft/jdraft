package org.jdraft.macro;

import org.jdraft._anno;
import org.jdraft._named;

import java.lang.annotation.*;

/**
 * Annotation Macro to add imports to a _type
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface _name{
    String value();

    class Macro implements _macro<_anno._hasAnnos> {
        String name;

        public Macro( _name _e ){
            this.name = _e.value();
        }

        @Override
        public String toString(){
           return "macro[name("+name+")]"; 
        }
        
        public _anno._hasAnnos apply( _anno._hasAnnos _a){
            return to( _a, name );
        }

        public static _anno._hasAnnos to( _anno._hasAnnos _a, String name ){
            if( _a instanceof _named ){
                ((_named)_a).name(name);
            }
            return _a;            
        }
    }
}

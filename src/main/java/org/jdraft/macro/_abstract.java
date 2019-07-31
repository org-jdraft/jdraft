package org.jdraft.macro;

import org.jdraft._anno;
import org.jdraft._class;
import org.jdraft._method;

import java.lang.annotation.*;

/**
 * apply the abstract modifier to a {@link _method} or {@link _class}
 * ... if it is a {@link _method}, setting it to be abstract will removeIn the BODY
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface _abstract {

    Macro $ = new Macro();

    public class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[abstract]"; 
        }
        
        public _anno._hasAnnos apply( _anno._hasAnnos _ha ){
            return to( _ha);
        }

        public static _anno._hasAnnos to (_anno._hasAnnos _ha){
            if( _ha instanceof _class){
                return to( (_class)_ha);
            }
            if( _ha instanceof _method ){
                return to( (_method)_ha);
            }
            return _ha;
        }

        public static _method to( _method _m ){
            _m.setAbstract();
            return _m;
        }

        public static _class to( _class t  ) {
            t.setAbstract();
            return t;
        }
    }
}

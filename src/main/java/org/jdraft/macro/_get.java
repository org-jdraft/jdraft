package org.jdraft.macro;

import org.jdraft.*;
import org.jdraft.proto.$method;

import java.lang.annotation.*;
import java.util.List;

/**
 * Builds a getXXX METHODS for all non_static FIELDS on the TYPE
 * Works on {@link _class} and {@link _enum} {@link _type}s
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface _get {

    Macro $ = new Macro();

    class Macro implements _macro<_type> {

        public static final $method $GET = $method.of(
                "public $type$ get$Name$(){ return $name$; }");

        @Override
        public String toString(){
           return "macro[autoGet]"; 
        }
        
        @Override
        public _type apply(_type _t) {
            return to( _t );
        }
        public static <T extends _type> T  to(T t) {
            if (t instanceof _method._hasMethods) {
                List<_field> _fs = t.listFields(_f -> !((_field)_f).isStatic());
                _fs.forEach(f ->
                        ((_method._hasMethods) t).method($GET.construct("type", f.getType(), "name", f.getName() ))
                );
            }
            return t;
        }
    }
}

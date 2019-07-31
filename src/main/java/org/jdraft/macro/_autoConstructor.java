package org.jdraft.macro;

import org.jdraft._constructor;
import org.jdraft._class;
import org.jdraft._field;
import org.jdraft._type;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.Predicate;

/**
 * Builds a constructor based on the final, non_static FIELDS without initializers
 * Works on {@link _class} and {@link org.jdraft._enum} {@link _type}s
 *
 * NOTE: this DOES NOT look into the super class to see if there are final FIELDS that
 * are not initialized
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface _autoConstructor {

    Macro $ = new Macro();

    /** picks _fields of the _class or enum that are required in the constructor */
    Predicate<_field> CTOR_REQUIRED_FIELD = _f -> !_f.isStatic() && _f.isFinal() && !_f.hasInit();

    class Macro implements _macro<_type> {

        public _type apply( _type _t ){
            return to( _t );
        }
        
        @Override
        public String toString(){
           return "macro[autoConstructor]"; 
        }

        public static <T extends _type> T to( T t  ){
            List<_field> _fs = t.listFields(CTOR_REQUIRED_FIELD);
            _constructor _ct = _constructor.of(t.getName() + "(){}");
            if (t instanceof _class) {
                _ct.setPublic(); //assume class CONSTRUCTORS is public
            }
            _fs.forEach(f -> {
                _ct.addParameter(f.getType(), f.getName());
                _ct.add("this." + f.getName() + " = " + f.getName() + ";");
            });
            ((_constructor._hasConstructors) t).constructor(_ct); //add the annotation to the TYPE
            return t;
        }
    }
}

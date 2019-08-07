package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._method;
import org.jdraft._field;
import org.jdraft._type;
import org.jdraft.proto.$method;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Builds a setXXX methods for all non_static, non final FIELDS on the TYPE
 * Works on {@link org.jdraft._class}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _setFluent {

    Macro $ = new Macro();

    class Macro implements _macro<_type> {

        /** picks _fields of the _class that are required in the constructor */
        public static Predicate<_field> SET_REQUIRED = _f -> !_f.isStatic() && !_f.isFinal();

        /** template method for a fluent set method */
        public static $method $SET_FLUENT = $method.of(
                "public $className$ set$Name$($type$ $name$){",
                "    this.$name$ = $name$;",
                "    return this;",
                "}");

        @Override
        public String toString(){
           return "macro[autoSetFluent]"; 
        }
        
        @Override
        public _type apply(_type _t) {
            return to( _t );
        }

        public static <T extends _type> T to(T t) {
            List<_field> _fs = t.listFields(SET_REQUIRED);
            if (t instanceof _method._hasMethods) {
                _fs.forEach(f -> {
                    _method _m = $SET_FLUENT.compose( "className", t.getName(), "name", f.getName(),"type", f.getType());
                    ((_method._hasMethods) t).method(_m);
                });
            }
            return t;
        }
    }

    class Act implements Consumer<TypeDeclaration> {

        /** picks _fields of the _class that are required in the constructor */
        public static Predicate<_field> SET_REQUIRED = _f -> !_f.isStatic() && !_f.isFinal();

        /** template method for a fluent set method */
        public static $method $SET_FLUENT = $method.of(
                "public $className$ set$Name$($type$ $name$){",
                "    this.$name$ = $name$;",
                "    return this;",
                "}");
        @Override
        public void accept(TypeDeclaration typeDeclaration) {
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs = _fs.stream().filter(SET_REQUIRED ).collect(Collectors.toList());
            _fs.forEach(f ->
                    typeDeclaration.addMember(
                            $SET_FLUENT.compose("className", typeDeclaration.getNameAsString(), "name", f.getName(), "type", f.getType()).ast()));
        }
    }
}

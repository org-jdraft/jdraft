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
 * Builds a setXXX METHODS for all non_static, non final FIELDS on the TYPE
 * Works on {@link org.jdraft._class}, {@link org.jdraft._enum}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _set {

    Macro $ = new Macro();

    class Macro implements _macro<_type> {

        public static final Macro INSTANCE = new Macro();

        /** picks _fields of the _class that are required in the constructor */
        public static Predicate<_field> SET_FIELDS = _f -> !_f.isStatic() && !_f.isFinal();

        /** template method for a setXXX() method */
        public static $method $SET = $method.of(
            "public void set$Name$($type$ $name$){ this.$name$ = $name$; }" );

        @Override
        public String toString(){
           return "macro[autoSet]"; 
        }
        
        @Override
        public _type apply(_type _t) {
            return to( _t );
        }

        public static <T extends _type> T to(T t) {
            if (t instanceof _method._hasMethods) {
                List<_field> _fs = t.listFields(SET_FIELDS);
                _fs.forEach(f ->
                        ((_method._hasMethods) t).method($SET.compose("name", f.getName(), "type", f.getType()))
                );
            }
            return t;
        }
    }
    class Act implements Consumer<TypeDeclaration> {

        /** template method for a setXXX() method */
        public static $method $SET = $method.of(
                "public void set$Name$($type$ $name$){ this.$name$ = $name$; }" );
        @Override
        public void accept(TypeDeclaration typeDeclaration) {
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs = _fs.stream().filter(f-> !f.isStatic() && !f.isFinal() ).collect(Collectors.toList());
            _fs.forEach(f ->
                    typeDeclaration.addMember(
                            $SET.compose("name", f.getName(), "type", f.getType()).ast()));
        }
    }
}

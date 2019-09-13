package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;
import org.jdraft.proto.$method;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builds a getXXX METHODS for all non_static FIELDS on the TYPE
 * Works on {@link _class} and {@link _enum} {@link _type}s
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _get {

    Macro $ = new Macro();

    class Macro implements _macro<_type> {

        public static final $method $GET = $method.of(
                "public $type$ get$Name$(){ return $name$; }");

        @Override
        public String toString(){
           return "macro[get]";
        }
        
        @Override
        public _type apply(_type _t) {
            return to( _t );
        }
        public static <T extends _type> T  to(T t) {
            if (t instanceof _method._hasMethods) {
                List<_field> _fs = t.listFields(_f -> !((_field)_f).isStatic());
                _fs.forEach(f ->
                        ((_method._hasMethods) t).method($GET.draft("type", f.getType(), "name", f.getName() ))
                );
            }
            return t;
        }
    }

    class Act extends macro<_get, TypeDeclaration>{

        public Act(){
            super(_get.class);
        }

        public static final $method $GET = $method.of(
                "public $type$ get$Name$(){ return $name$; }");

        public static void to( TypeDeclaration typeDeclaration ){
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs.stream().filter(_f -> !((_field)_f).isStatic())
                    .forEach( _f-> typeDeclaration.addMember(
                            $GET.draft("type", _f.getType(), "name", _f.getName()).ast() ) );
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration);
        }

        @Override
        public String toString(){
            return "macro[get]";
        }
    }
}

package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;
import org.jdraft.pattern.$method;

import java.lang.annotation.*;
import java.util.List;

/**
 * Builds getXXX() METHODS for all non_static FIELDS on the TYPE
 * Works on {@link _class} and {@link _enum} {@link _type}s
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _get {

    class Act extends macro<_get, TypeDeclaration>{

        public Act(){
            super(_get.class);
        }

        public Act(_get _g){
            super(_g);
        }

        public static final $method $GET = $method.of(
                "public $type$ get$Name$(){ return $name$; }");

        public static <T extends TypeDeclaration> T to( T typeDeclaration ){
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs.stream().filter(_f -> !((_field)_f).isStatic())
                    .forEach( _f-> typeDeclaration.addMember(
                            $GET.draft("type", _f.getType(), "name", _f.getName()).ast() ) );
            return typeDeclaration;
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

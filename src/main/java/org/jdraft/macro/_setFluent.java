package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.Ast;
import org.jdraft._field;
import org.jdraft.text.Stencil;
//import org.jdraft.pattern.$method;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Annotation/Macro to add fluent setXXX() methods to a Class or Enum.<BR/>
 *
 * NOTE: "Fluent" means we return the instance after setting to enable "stringing":<BR/>
 *
 * Builds a setXXX methods for all non_static, non final FIELDS on the TYPE
 * Works on {@link org.jdraft._class}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _setFluent {

    /** picks _fields of the _class that are required in the constructor */
    Predicate<_field> SET_REQUIRED = _f -> !_f.isStatic() && !_f.isFinal();

    /** template method for a fluent set method */
    Stencil $SET_FLUENT = Stencil.of(
            "public $className$ set$Name$($type$ $name$){",
            "    this.$name$ = $name$;",
            "    return this;",
            "}");

    class Act extends macro<_setFluent, TypeDeclaration> {

        public Act(){
            super(_setFluent.class);
        }

        public Act(_setFluent sf ){
            super( sf );
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration);
        }

        public static <T extends TypeDeclaration> T to(T typeDeclaration){
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs = _fs.stream().filter(SET_REQUIRED ).collect(Collectors.toList());
            _fs.forEach(f ->
                    typeDeclaration.addMember(
                            Ast.method($SET_FLUENT.draft("className", typeDeclaration.getNameAsString(),
                                    "name", f.getName(), "type", f.getTypeRef()))));
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[setFluent]";
        }
    }
}

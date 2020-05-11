package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.Ast;
import org.jdraft.Statements;
import org.jdraft._field;
import org.jdraft.text.Stencil;
//import org.jdraft.pattern.$method;

import java.lang.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Annotation/Macro to add fluent setXXX() methods to a Class or Enum.<BR/>
 *
 * Builds a setXXX METHODS for all non_static, non final FIELDS on the TYPE
 * Works on {@link org.jdraft._class}, {@link org.jdraft._enum}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _set {

    /** template method for a setXXX() method */
    Stencil $SET = Stencil.of(
            "public void set$Name$($type$ $name$){",
            "    this.$name$ = $name$;",
            "}" );

    class Act extends macro<_set, TypeDeclaration> {

        public Act(){
            super(_set.class);
        }

        public Act(_set _s){
            super(_s);
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration);
        }

        public static <T extends TypeDeclaration> T to(T typeDeclaration){
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs = _fs.stream().filter(f-> !f.isStatic() && !f.isFinal() ).collect(Collectors.toList());

            _fs.forEach(f ->
                    typeDeclaration.addMember(
                            Ast.method($SET.draft("name", f.getName(), "type", f.getTypeRef()))));
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[set]";
        }
    }
}

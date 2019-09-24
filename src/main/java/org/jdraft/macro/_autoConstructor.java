package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.*;

import java.lang.annotation.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Annotation/Macro to create an appropriate {@link org.jdraft._constructor} based on the
 * {@link _field}s of the {@link _type}
 *
 * Builds a constructor based on the final, non_static FIELDS without initializers
 * Works on {@link _class} and {@link org.jdraft._enum} {@link _type}s
 *
 * NOTE: this DOES NOT look into the super class to see if there are final FIELDS that
 * are not initialized
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
public @interface _autoConstructor {

    Predicate<_field> CTOR_REQUIRED_FIELD = _f -> !_f.isStatic() && _f.isFinal() && !_f.hasInit();

    class Act extends macro<_autoConstructor, TypeDeclaration> {

        public Act(){
            super(_autoConstructor.class);
        }

        public Act(_autoConstructor _ac){
            super(_ac);
        }

        @Override
        public void expand(TypeDeclaration node) {
            to(node);
        }

        public static <_T extends TypeDeclaration> _T to(_T node ){
            if( node instanceof EnumDeclaration || ( node instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)node).isInterface() )){
                TypeDeclaration td = (TypeDeclaration)node;
                List<_field> _fs = _field.of( td.getFields() );
                _fs = _fs.stream().filter( CTOR_REQUIRED_FIELD ).collect(Collectors.toList());
                ConstructorDeclaration cd = null;
                if (node instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)node).isInterface()) {
                    cd = td.addConstructor(Modifier.Keyword.PUBLIC);
                } else{
                    cd = td.addConstructor();
                }
                _constructor _ct = _constructor.of( cd );
                _fs.forEach(f -> {
                    _ct.addParameter(f.getType(), f.getName());
                    _ct.add("this." + f.getName() + " = " + f.getName() + ";");
                });
                return node;
            }
            else{
                throw new _draftException( "cannot add a constructor ");
            }
        }

        @Override
        public String toString(){
            return "macro[autoConstructor]";
        }
    }
}

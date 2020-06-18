package org.jdraft.macro;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft.Stmt;
import org.jdraft._method;
import org.jdraft._field;
import org.jdraft._type;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.jdraft.text.Stencil;
//import org.jdraft.pattern.$method;
//import org.jdraft.pattern.$stmt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.function.Predicate;

/**
 * Annotation/Macro to add the toString() method based on the non static fields of the class
 *
 * Builds a toString METHODS for all {@link _field}s on the {@link _type} that
 * are not static Works on {@link org.jdraft._class}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.TYPE_USE})
public @interface _toString {

    /**
     * Decide which FIELDS are to be added into the toString method
     */
    Predicate<_field> TO_STRING_FIELDS = f-> !f.isStatic() && !f.isTransient();

    Stencil $simple = Stencil.of(
            "sb.append(\" $name$: \").append($name$).append(System.lineSeparator());" );

    Stencil $arrayOfPrimitives = Stencil.of(
            "sb.append(\" $name$: \").append(java.util.Arrays.toString($name$)).append(System.lineSeparator());" );

    Stencil $arrayOfObjects = Stencil.of(
            "sb.append(\" $name$: \").append(java.util.Arrays.deepToString($name$)).append(System.lineSeparator());");

    Stencil $TO_STRING = Stencil.of(
            "public String toString(){",
            "    StringBuilder sb = new StringBuilder();",
            "    sb.append( \"$className$\" ).append(\"{\" );",
            "    sb.append( System.lineSeparator() );",
            "    $body$",
            "    sb.append( \"}\" );",
            "    return sb.toString();",
            "}");

    class Act extends macro<_toString,TypeDeclaration> {

        public Act(){
            super(_toString.class);
        }

        public Act(_toString _ts){
            super(_ts);
        }

        @Override
        public void expand(TypeDeclaration typeDeclaration) {
            to(typeDeclaration);
        }

        public static <_T extends _type> _T to( _T _t){
            to( (TypeDeclaration)_t.node());
            return _t;
        }

        public static <T extends TypeDeclaration> T to( T typeDeclaration){
            BlockStmt body = new BlockStmt();
            List<_field> _fs = _field.of(typeDeclaration.getFields());

            //build the body of the toString method based on the fields
            _fs.stream().filter(TO_STRING_FIELDS).forEach( _f  -> {
                if( _f.isArray() ){
                    if( _f.getElementType().isPrimitiveType() ){
                        body.addStatement( Stmt.of($arrayOfPrimitives.fill(_f.getName()) ) );
                    }else{
                        body.addStatement( Stmt.of($arrayOfObjects.fill(_f.getName())) );
                    }
                } else{
                    body.addStatement( Stmt.of($simple.fill(_f.getName())) );
                }
            });
            _method _m = _method.of($TO_STRING.draft("className", typeDeclaration.getName(), "body", body ));
            typeDeclaration.addMember(_m.node());
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[toString]";
        }
    }
}

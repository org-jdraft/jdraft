package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft.Ast;
import org.jdraft._field;
import org.jdraft._jdraftException;
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
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_USE})
public @interface _set {

    /** template method for a setXXX() method */
    Stencil $SET = Stencil.of(
            "public void set$Name$($type$ $name$){",
            "    this.$name$ = $name$;",
            "}" );

    class Act extends macro<_set, Node> {

        public Act(){
            super(_set.class);
        }

        public Act(_set _s){
            super(_s);
        }

        @Override
        public void expand(Node node) {
            if( node instanceof TypeDeclaration) {
                to((TypeDeclaration)node);
            } else{
                to((VariableDeclarator) node);
            }
        }

        public static VariableDeclarator to (VariableDeclarator vari ){
            if( !vari.getParentNode().isPresent() ){
                throw new _jdraftException("variable "+ vari+" must have a parent field to add set method to");
            }
            FieldDeclaration field = (FieldDeclaration)vari.getParentNode().get();

            TypeDeclaration typeDeclaration = (TypeDeclaration)field.getParentNode().get();
            String mm = $SET.draft("name", vari.getName(), "type", vari.getType());
            typeDeclaration.addMember(Ast.methodDeclaration(mm));
            return vari;
        }

        public static <T extends TypeDeclaration> T to(T typeDeclaration){
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs = _fs.stream().filter(f-> !f.isStatic() && !f.isFinal() ).collect(Collectors.toList());

            _fs.forEach(f ->
                    typeDeclaration.addMember(
                            Ast.methodDeclaration($SET.draft("name", f.getName(), "type", f.getType()))));
            return typeDeclaration;
        }

        @Override
        public String toString(){
            return "macro[set]";
        }
    }
}

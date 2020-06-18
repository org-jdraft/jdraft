package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft.*;
import org.jdraft.text.Stencil;
//import org.jdraft.pattern.$method;

import java.lang.annotation.*;
import java.util.List;

/**
 * Builds getXXX() METHODS for all non_static FIELDS on the TYPE
 * Works on {@link _class} and {@link _enum} {@link _type}s
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.TYPE_USE})
public @interface _get {

    class Act extends macro<_get, Node>{

        public Act(){
            super(_get.class);
        }

        public Act(_get _g){
            super(_g);
        }

        public static final Stencil $GET = Stencil.of(
                "public $type$ get$Name$(){ return $name$; }");

        public static <T extends TypeDeclaration> T to( T typeDeclaration ){
            List<_field> _fs = _field.of(typeDeclaration.getFields());
            _fs.stream().filter(_f -> !((_field)_f).isStatic())
                    .forEach( _f-> typeDeclaration.addMember(
                            _method.of($GET.draft("type", _f.getType(), "name", _f.getName()) ).node()) );
            return typeDeclaration;
        }

        public static VariableDeclarator to (VariableDeclarator vari ){
            if( !vari.getParentNode().isPresent() ){
                throw new _jdraftException("variable "+ vari+" must have a parent field to add get method to");
            }
            FieldDeclaration field = (FieldDeclaration)vari.getParentNode().get();

            TypeDeclaration typeDeclaration = (TypeDeclaration)field.getParentNode().get();
            String mm = $GET.draft("name", vari.getName(), "type", vari.getType());
            typeDeclaration.addMember(Ast.methodDeclaration(mm));
            return vari;
        }

        @Override
        public void expand(Node node) {
            if( node instanceof TypeDeclaration) {
                to((TypeDeclaration)node);
            }
            if( node instanceof VariableDeclarator){
                to( (VariableDeclarator)node);
            }
        }

        @Override
        public String toString(){
            return "macro[get]";
        }
    }
}

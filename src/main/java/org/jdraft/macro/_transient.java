package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft._field;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to add the transient modifier to a Field
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface _transient {

    Macro $ = new Macro();

    class Macro implements _macro<_field> {

        @Override
        public String toString(){
           return "macro[transient]"; 
        }
        
        @Override
        public _field apply(_field _f) {
            return to(_f);
        }

        public static _field to( _field _f ){
            _f.getModifiers().setTransient();
            return _f;
        }
    }

    class Act extends macro<_transient, Node> {

        public Act(){
            super(_transient.class);
        }

        @Override
        public String toString(){
            return "macro[transient]";
        }

        @Override
        public void expand(Node node) {
            if( node instanceof FieldDeclaration){
                FieldDeclaration nwp = (FieldDeclaration) node;
                nwp.setTransient(true);
                _macro.removeAnnotation(node, _transient.class);
            }
            else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setTransient(true);
                    _macro.removeAnnotation(fd, _transient.class);
                }
            }
        }
    }

}

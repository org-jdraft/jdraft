package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;

import java.lang.annotation.*;

/**
 * Annotation/Macro to add the transient modifier to a Field
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface _transient {

    class Act extends macro<_transient, Node> {

        public Act(){
            super(_transient.class);
        }

        public Act(_transient _t){
            super(_t);
        }

        @Override
        public String toString(){
            return "macro[transient]";
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <N extends Node> N to(N node){
            if( node instanceof FieldDeclaration){
                FieldDeclaration nwp = (FieldDeclaration) node;
                nwp.setTransient(true);
                //_macro.removeAnnotation(node, _transient.class);
            }
            else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setTransient(true);
                    //_macro.removeAnnotation(fd, _transient.class);
                }
            }
            return node;
        }
    }
}

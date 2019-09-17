package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import org.jdraft._field;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to add the volatile modifier to a Field
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD })
public @interface _volatile {

    class Act extends macro<_volatile, Node> {

        public Act(){
            super(_volatile.class);
        }

        public Act(_volatile _v){
            super( _v);
        }

        @Override
        public String toString(){
            return "macro[volatile]";
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <N extends Node> N to( N node){
            if( node instanceof FieldDeclaration){
                FieldDeclaration nwp = (FieldDeclaration)node;
                nwp.setVolatile(true);
                _macro.removeAnnotation(node, _volatile.class);
            } else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setVolatile(true);
                    _macro.removeAnnotation(fd, _volatile.class);
                }
            }
            return node;
        }
    }
}

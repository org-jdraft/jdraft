package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;

import java.lang.annotation.*;

/**
 * Annotation/Macro to remove the static modifier from {@link org.jdraft._type}, {@link org.jdraft._field}
 * {@link org.jdraft._method}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE})
public @interface _non_static {

    class Act extends macro<_non_static,Node>{

        public Act(){
            super(_non_static.class);
        }

        public Act(_non_static _ns){
            super(_ns);
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <N extends Node> N to(N node ){
            if( node instanceof NodeWithStaticModifier ){
                NodeWithStaticModifier nwsm = (NodeWithStaticModifier)node;
                nwsm.setStatic(false);
                //_macro.removeAnnotation(node, _non_static.class);
            } else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setStatic(false);
                    //_macro.removeAnnotation(fd, _non_static.class);
                }
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[non_static]";
        }
    }
}

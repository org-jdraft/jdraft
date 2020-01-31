package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithProtectedModifier;

import java.lang.annotation.*;

/**
 * Annotation/Macro to add the protected modifier to {@link org.jdraft._type}, {@link org.jdraft._field},
 * {@link org.jdraft._method}, {@link org.jdraft._constructor}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _protected {

    class Act extends macro<_protected,Node> {

        public Act(){
            super(_protected.class);
        }

        public Act(_protected _p){
            super(_p);
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <N extends Node> N to(N node){
            if( node instanceof NodeWithProtectedModifier){
                NodeWithProtectedModifier nwpm = (NodeWithProtectedModifier)node;
                nwpm.setModifier(Modifier.Keyword.PRIVATE, false);
                nwpm.setModifier(Modifier.Keyword.PUBLIC, false);
                nwpm.setProtected(true);
            }else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setProtected(true);
                }
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[protected]";
        }
    }
}

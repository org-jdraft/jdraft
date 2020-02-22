package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPrivateModifier;
import org.jdraft._java;

import java.lang.annotation.*;

/**
 * Annotation/Macro to set the private modifier on {@link org.jdraft._type}, {@link org.jdraft._field},
 * {@link org.jdraft._method}, {@link org.jdraft._constructor}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _private {

    class Act extends macro<_private,Node> {

        public Act(){
            super(_private.class);
        }

        public Act(_private _p){
            super(_p);
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <_N extends _java._multiPart> _N to(_N _n){
            to(_n.ast());
            return _n;
        }

        public static <N extends Node> N to(N node){
            if( node instanceof NodeWithPrivateModifier ){
                NodeWithPrivateModifier nwp = (NodeWithPrivateModifier)node;
                nwp.setModifier(Modifier.Keyword.PUBLIC, false);
                nwp.setModifier(Modifier.Keyword.PROTECTED, false);
                nwp.setPrivate(true);
            } else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setPrivate(true);
                }
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[_private]";
        }
    }
}

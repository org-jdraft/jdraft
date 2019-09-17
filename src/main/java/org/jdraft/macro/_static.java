package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import org.jdraft._modifiers;
import org.jdraft._anno;
import org.jdraft._anno._hasAnnos;
import org.jdraft._node;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to add the static modifier to a {@link org.jdraft._type},
 * {@link org.jdraft._field}, of {@link org.jdraft._method}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE})
public @interface _static  {

     class Act extends macro<_static, Node> {

        public Act(_static _s){
            super(_s);
        }

        @Override
        public String toString(){
            return "macro[static]";
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <_N extends _node> _N to (_N _n){
            to( _n.ast());
            return _n;
        }

        public static <N extends Node> N to(N node){
            if( node instanceof NodeWithStaticModifier){
                NodeWithStaticModifier nwp = (NodeWithStaticModifier)node;
                nwp.setStatic(true);
            }else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setStatic(true);
                }
            }
            return node;
        }
    }
}

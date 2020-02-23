package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import com.github.javaparser.utils.Log;
import org.jdraft._java;
import org.jdraft._modifiers;

import java.lang.annotation.*;

/**
 * Annotation / Macro to add add the final modifier to a
 * {@link org.jdraft._type} {@link org.jdraft._method}, {@link org.jdraft._field}, {@link org.jdraft._parameter}
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface _final {

    class Act extends macro<_final, Node> {

        public Act(){
            super(_final.class);
        }

        public Act( _final _f ){
            super(_f);
        }

        @Override
        public void expand(Node node) {
            to( node );
        }

        public static <N extends Node> N to (N node ){
            if( node instanceof NodeWithFinalModifier){
                NodeWithFinalModifier nwf = (NodeWithFinalModifier)node;
                nwf.setFinal(true);
                //_macro.removeAnnotation(node, _final.class);
            } else if (node instanceof VariableDeclarator ){
                VariableDeclarator vd = (VariableDeclarator)node;
                FieldDeclaration fd = (FieldDeclaration)vd.getParentNode().get();
                fd.setFinal(true);
                //_macro.removeAnnotation(fd, _final.class);
            } else{
                try{
                    ((_modifiers._withModifiers) _java.of(node)).getModifiers().setFinal();
                } catch(Exception e){
                    Log.error("Failed setting final modifier on %s", ()->node);
                }
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[final]";
        }
    }
}

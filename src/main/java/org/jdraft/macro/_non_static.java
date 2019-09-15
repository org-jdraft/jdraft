package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import org.jdraft._anno;
import org.jdraft._modifiers;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to remove the static modifier from {@link org.jdraft._type}, {@link org.jdraft._field}
 * {@link org.jdraft._method}
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE_USE})
public @interface _non_static {

    Macro $ = new Macro();

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[non_static]"; 
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _model) {
            return to(_model);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setStatic(false);
            return _model;
        }
    }
    class Act extends macro<_non_static,Node>{

        public Act(){
            super(_non_static.class);
        }

        public Act(_non_static _ns){
            super(_ns);
        }

        @Override
        public void expand(Node node) {
            if( node instanceof NodeWithStaticModifier ){
                NodeWithStaticModifier nwsm = (NodeWithStaticModifier)node;
                nwsm.setStatic(false);
                _macro.removeAnnotation(node, _non_static.class);
            } else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setStatic(false);
                    _macro.removeAnnotation(fd, _non_static.class);
                }
            }
        }

        @Override
        public String toString(){
            return "macro[non_static]";
        }
    }
}

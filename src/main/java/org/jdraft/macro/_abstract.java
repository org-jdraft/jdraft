package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import org.jdraft._class;
import org.jdraft._draftException;
import org.jdraft._method;

import java.lang.annotation.*;

/**
 * Annotation/Macro to set the abstract modifier on a {@link _method}, {@link _class}
 *
 * apply the abstract modifier to a {@link _method} or {@link _class}
 * ... if it is a {@link _method}, setting it to be abstract will removeIn the BODY
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
public @interface _abstract {

    class Act extends macro<_abstract, Node> {

        public Act(){
            super(_abstract.class);
        }

        public Act(_abstract _a){
            super(_a);
        }

        @Override
        public String toString(){
            return "macro[abstract]";
        }

        public void expand(Node n){
            to(n);
        }

        public static <N extends Node> N to( N n){
            if (n instanceof NodeWithAbstractModifier) {
                NodeWithAbstractModifier nwa = (NodeWithAbstractModifier) n;
                nwa.setAbstract(true);

                if( n instanceof NodeWithOptionalBlockStmt){ //abstract methods need to remove the body
                    NodeWithOptionalBlockStmt  nwb = (NodeWithOptionalBlockStmt)n;
                    //this can only happen to interfaces, so its an implied modifier, no need to set it explicitly
                    nwa.setAbstract(false);

                    nwb.removeBody();
                }
                return n;
            } else {
                throw new _draftException("@_abstract applied to a non abstract-able AST Node " + n.getClass());
            }
        }
    }
}

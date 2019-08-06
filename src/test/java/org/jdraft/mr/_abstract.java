package org.jdraft.mr;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@interface _abstract {
    Consumer<Node> $ = n -> {
        if (n instanceof NodeWithAbstractModifier) {
            NodeWithAbstractModifier nwa = (NodeWithAbstractModifier) n;
            nwa.setAbstract(true);

            if( n instanceof NodeWithOptionalBlockStmt){ //abstract methods need to remove the body
                NodeWithOptionalBlockStmt  nwb = (NodeWithOptionalBlockStmt)n;
                nwb.removeBody();
            }
            _annoMacro.removeAnnotation(n, _abstract.class);
        } else {
            throw new _annoMacro.AnnoMacroException("@_abstract applied to a non abstract-able AST Node " + n.getClass());
        }
    };
}
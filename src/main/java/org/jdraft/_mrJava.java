package org.jdraft;

import com.github.javaparser.ast.Node;

/**
 * Marker interface for categorizing Meta Representations of Java source code entities
 * (objects which represent some Java source code structure)
 * (We have this interface to differentiate between entities/classes that are
 * Ast/syntax entities (i.e.JavaParser Node) and part of the _jdraft API which sits on top of
 * the JavaParser Ast nodes
 *
 * these _mrJava entities are effectively facades or "logical view"s (in database terms)
 * into the data stored in the JavaParser Ast (Syntax) entities.
 *
 * @see _node a one-to-one mapping between an AST (Node) and a <CODE>_javaMetaRepresentation</CODE>>
 */
public interface _mrJava {


}

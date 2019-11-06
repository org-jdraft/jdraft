package org.jdraft;

/**
 * Marker interface for all Java _meta model entity implementations
 * (We have this interface to differentiate between entities/classes that are
 * Ast entities (i.e. Node) and part of the _jdraft API which sits on top of
 * the JavaParser Ast nodes
 *
 * these _draft entities are effectively facades or "logical view"s (in database terms)
 * into the data stored in the JavaParser Ast entities.
 */
public interface _draft {

}

package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * {@link _node} entity that maps directly to an AST {@link Node}
 * for example:
 * <UL>
 * <LI>{@link _declared}s</LI>
 * <UL>
 *     <LI>{@link _type} {@link com.github.javaparser.ast.body.TypeDeclaration}</LI>
 *     <LI>{@link _annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
 *     <LI>{@link _interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}</LI>
 *     <LI>{@link _class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}</LI>
 *     <LI>{@link _enum} {@link com.github.javaparser.ast.body.EnumDeclaration}</LI>
 *     <LI>{@link _constant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}</LI>
 *     <LI>{@link _annotation._entry} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
 *     <LI>{@link _method} {@link com.github.javaparser.ast.body.MethodDeclaration}</LI>
 *     <LI>{@link _constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}</LI>
 *     <LI>{@link _field} {@link com.github.javaparser.ast.body.FieldDeclaration}</LI>
 * </UL>
 * <LI>{@link _member}s</LI>
 * <UL>
 *         <LI>{@link _initBlock} {@link com.github.javaparser.ast.body.InitializerDeclaration}</LI>
 * </UL>
 * //adornment, property
 * <LI>{@link _anno} {@link com.github.javaparser.ast.expr.AnnotationExpr}
 * <LI>{@link _parameter} {@link com.github.javaparser.ast.body.Parameter}</LI>
 * <LI>{@link _receiverParameter} {@link com.github.javaparser.ast.body.ReceiverParameter}</LI>
 * <LI>{@link _typeParameter} {@link com.github.javaparser.ast.type.TypeParameter}</LI>
 * <LI>{@link _typeRef} {@link com.github.javaparser.ast.type.Type}</LI>
 * </UL>
 * @see _java for mappings
 * @param <_N> the jdraft _node type {@link _method}, {@link _field}
 * @param <N> ast node {@link com.github.javaparser.ast.body.MethodDeclaration}, {@link com.github.javaparser.ast.body.FieldDeclaration}
 */
public interface _node<N extends Node, _N extends _node> extends _mrJava, _java._componentized {

    /**
     * Build and return an (independent) copy of this _node entity
     * @return
     */
    _N copy();

    /**
     * Pass a match function to verify based on a lambda predicate
     * @param matchFn a function to match against the entity
     * @return true if the match function is verified, false otherwise
     */
    default boolean is(Predicate<_N> matchFn){
        return matchFn.test((_N)this);
    }

    /**
     * Pass a match function to verify based on a lambda predicate
     * @param astMatchFn a lambda function to match against the ast
     * @return true if the match function is verified, false otherwise
     */
    default boolean isAst(Predicate<N> astMatchFn){
        return astMatchFn.test( ast() );
    }

    /**
     * is the String representation equal to the _node entity
     * (i.e. if we parse the string, does it create an AST entity that
     * is equal to the node?)
     *
     * @param stringRep the string representation of the node
     * (parsed as an AST and compared to this entity to see if equal)
     * @return true if the Parsed String represents the entity
     */
    boolean is(String... stringRep);

    /**
     * Is the AST node representation equal to the underlying entity
     * @param astNode the astNode to compare against
     * @return true if they represent the same _node, false otherwise
     */
    default boolean is(N astNode){
        return Objects.equals(ast(), astNode);
    }

    /**
     * Decompose the entity into smaller named tokens
     * returning a mapping between the name and the constituent part
     * @return a Map with the names mapped to the corresponding components
     */
    default Map<String, Object> tokenize() {
        Map<_java.Component, Object> parts = components();
        Map<String, Object> mdd = new HashMap<>();
        parts.forEach((p, o) -> {
            mdd.put(p.name, o);
        });
        return mdd;
    }

    /**
     * @return the underlying AST Node instance being manipulated
     * by the _model._node facade
     * NOTE: the AST node contains physical information (i.e. location in
     * the file (line numbers) and syntax related parent/child relationships
     */
    N ast();

    /**
     * Pass in the AST Pretty Printer configuration which will determine how the code
     * is formatted and return the formatted String representing the code.
     *
     * @see com.github.javaparser.printer.PrettyPrintVisitor the original visitor for walking and printing nodes in AST
     * @see Ast.PrintNoAnnotations a configurable subclass of PrettyPrintVisitor that will not print ANNOTATIONS
     * @see PrettyPrinterConfiguration the configurations for spaces, Tabs, for printing
     * @see Ast#PRINT_NO_ANNOTATIONS_OR_COMMENTS a premade implementation for
     * @see Ast#PRINT_NO_COMMENTS
     *
     * @param codeFormat the details on how the code will be formatted (for this element and all sub ELEMENTS)
     * @return A String representing the .java code
     */
    default String toString(PrettyPrinterConfiguration codeFormat) {
        return ast().toString(codeFormat);
    }
}

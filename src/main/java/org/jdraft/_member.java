package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;

/**
 * A member component of the {@link _type} (it can be associated with a larger entity or context)
 * NOTE: each {@link _member} maps directly to an AST Node
 * <UL>
 * <LI>{@link _field} {@link com.github.javaparser.ast.body.FieldDeclaration}
 * <LI>{@link _constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}
 * <LI>{@link _method} {@link com.github.javaparser.ast.body.MethodDeclaration}
 * <LI>{@link _enum._constant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}
 * <LI>{@link _annotation._element} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
 * <LI>{@link _type} {@link com.github.javaparser.ast.body.TypeDeclaration}
 * <LI>{@link _class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 * <LI>{@link _enum} {@link com.github.javaparser.ast.body.EnumDeclaration}
 * <LI>{@link _interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 * <LI>{@link _annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
 * </UL>
 *
 * NOTE:
 * <LI>{@link _staticBlock} {@link com.github.javaparser.ast.body.InitializerDeclaration}
 * is NOT a member (primarily because it does not satisfy the {@link _named}
 * {@link _anno._hasAnnos} or {@link _javadoc._hasJavadoc} interfaces
 * (this generally maps to Accessible Java Member things
 * (available via reflection at runtime)
 *
 * @param <N> the node type (i.e. {@link com.github.javaparser.ast.expr.AnnotationExpr})
 * @param <T> the draft type (i.e. {@link _anno})
 */
public interface _member<N extends Node, T extends _node & _named & _anno._hasAnnos & _javadoc._hasJavadoc> 
        extends _node<N, T>, _named<T>, _anno._hasAnnos<T>, _javadoc._hasJavadoc<T> {

    @Override
    default _javadoc getJavadoc() {
        return _javadoc.of((NodeWithJavadoc) this.ast());
    }

    @Override
    default T removeJavadoc() {
        ((NodeWithJavadoc) this.ast()).removeJavaDocComment();
        return (T) this;
    }

    @Override
    default boolean hasJavadoc() {
        return ((NodeWithJavadoc) this.ast()).getJavadoc().isPresent();
    }

    @Override
    default T javadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return (T) this;
    }

    @Override
    default T javadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return (T) this;
    }
    
}

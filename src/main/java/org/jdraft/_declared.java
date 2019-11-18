package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithJavadoc;

/**
 * A {@link _member} defined within a {@link _type} (that is callable/referenceable/reachable) from the outside
 * it can be associated with a larger entity or context)
 * NOTE: each {@link _declared} maps directly to:
 * <UL>
 *     <LI>an AST representation {@link Node}
 *     <LI></LI>a meta-representation {@link _node}
 * </UL>
 * <UL>
 * <LI>{@link _field} {@link com.github.javaparser.ast.body.FieldDeclaration}
 * <LI>{@link _constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}
 * <LI>{@link _method} {@link com.github.javaparser.ast.body.MethodDeclaration}
 * <LI>{@link _constant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}
 * <LI>{@link _annotation._element} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
 * <LI>{@link _type} {@link com.github.javaparser.ast.body.TypeDeclaration}
 * <LI>{@link _class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 * <LI>{@link _enum} {@link com.github.javaparser.ast.body.EnumDeclaration}
 * <LI>{@link _interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 * <LI>{@link _annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
 * </UL>
 *
 * NOTE:
 * <LI>{@link _initBlock} {@link com.github.javaparser.ast.body.InitializerDeclaration}
 * is a {@link _member} but is NOT {@link _declared} (primarily because it is not
 * callable/referenceable/accessible outside of the Class where it is defined and does
 * not satisfy the {@link _named} {@link _anno._hasAnnos} or {@link _javadoc._hasJavadoc} interfaces
 * (Not available via reflection at runtime)
 *
 * @param <N> the AST node type (i.e. {@link com.github.javaparser.ast.body.MethodDeclaration})
 * @param <_D> the meta-representation declaration type (i.e. {@link _method})
 */
public interface _declared<N extends Node, _D extends _node & _named & _anno._hasAnnos & _javadoc._hasJavadoc>
        extends _member<N, _D>, _named<_D>, _anno._hasAnnos<_D>, _javadoc._hasJavadoc<_D> {

    @Override
    default _javadoc getJavadoc() {
        return _javadoc.of((NodeWithJavadoc) this.ast());
    }

    default _D removeJavadoc() {
        ((NodeWithJavadoc) this.ast()).removeJavaDocComment();
        return (_D) this;
    }

    @Override
    default boolean hasJavadoc() {
        return ((NodeWithJavadoc) this.ast()).getJavadoc().isPresent();
    }

    /*
    @Override
    default _D javadoc(String... content) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(Text.combine(content));
        return (_D) this;
    }

    @Override
    default _D javadoc(JavadocComment astJavadocComment) {
        ((NodeWithJavadoc) this.ast()).setJavadocComment(astJavadocComment);
        return (_D) this;
    }
     */
}

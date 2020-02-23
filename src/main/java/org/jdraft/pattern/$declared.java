package org.jdraft.pattern;

import com.github.javaparser.ast.comments.JavadocComment;
import org.jdraft._java;
import org.jdraft._javadoc;

import java.util.function.Predicate;

/**
 *
 *  <LI>{@link $field} {@link com.github.javaparser.ast.body.FieldDeclaration}
 *  <LI>{@link $constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}
 *  <LI>{@link $method} {@link com.github.javaparser.ast.body.MethodDeclaration}
 *  <LI>{@link $enumConstant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}
 *  <LI>{@link $annotationEntry} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
 *  <LI>{@link $type} {@link com.github.javaparser.ast.body.TypeDeclaration}
 *  <LI>{@link $class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 *  <LI>{@link $enum} {@link com.github.javaparser.ast.body.EnumDeclaration}
 *  <LI>{@link $interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 *  <LI>{@link $annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
 *
 * @param <D>
 * @param <$D>
 */
public interface $declared <D extends _java._declaredBodyPart, $D extends $pattern> extends $member<D,$D> {

    $name get$Name();

    /**
     * Sets the name pattern and returns the modified pattern
     * @param name
     * @return
     */
    $D $name(String name);

    /**
     *
     * @param name
     * @return
     */
    $D $name($name name);

    $D $name(Predicate<String> nameMatchFn);

    $comment<JavadocComment> get$javadoc();

    $D $javadoc(  $comment<JavadocComment> javadocComment );

    /* Make these default? */
    $D $javadoc( _javadoc _jd);

    /* Make these default? */
    $D $javadoc( Predicate<JavadocComment> javadocMatchFn );
}

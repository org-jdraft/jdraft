package org.jdraft.proto;

import org.jdraft._declared;
import org.jdraft._type;

import java.util.function.Predicate;

/**
 *  A prototype member component of the {@link _type} (it can be associated with a larger entity or context)
 *  NOTE: each {@link _declared} maps directly to an AST Node
 *  <UL>
 *  <LI>{@link $initBlock} {@link com.github.javaparser.ast.body.InitializerDeclaration}
 *  <LI>{@link $field} {@link com.github.javaparser.ast.body.FieldDeclaration}
 *  <LI>{@link $constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}
 *  <LI>{@link $method} {@link com.github.javaparser.ast.body.MethodDeclaration}
 *  <LI>{@link $enumConstant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}
 *  <LI>{@link $annotationElement} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
 *  <LI>{@link $type} {@link com.github.javaparser.ast.body.TypeDeclaration}
 *  <LI>{@link $class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 *  <LI>{@link $enum} {@link com.github.javaparser.ast.body.EnumDeclaration}
 *  <LI>{@link $interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
 *  <LI>{@link $annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
 *  </UL>
 */
public interface $member<M, $M extends $proto> extends $proto<M,$M> {

    //$getMember()
    /**
     * Is this $member $prototype a $member of another $member<PRE>
     * //all static methods that are the members of an interface
     * $method $staticOnInterface = $method.of($.STATIC).$isMemberOf($interface.of())
     *
     * //all fields that are members of Annotation Types &
     * $field $fieldOnAnnotation = $field.of().$isMember(
     * </PRE>
     * @param $parentMember
     * @return

    default $M $isMemberOf($member...$parentMember ){
        this.$and( (P n) -> {
            //this will help subclasses universally build constraints based on member()
            getMember().test(n)
            return true;
        });
    }
    */
     /**
      * A $member prototype that has a name
      *  <UL>
      *  <LI>{@link $field} {@link com.github.javaparser.ast.body.FieldDeclaration}
      *  <LI>{@link $constructor} {@link com.github.javaparser.ast.body.ConstructorDeclaration}
      *  <LI>{@link $method} {@link com.github.javaparser.ast.body.MethodDeclaration}
      *  <LI>{@link $enumConstant} {@link com.github.javaparser.ast.body.EnumConstantDeclaration}
      *  <LI>{@link $annotationElement} {@link com.github.javaparser.ast.body.AnnotationMemberDeclaration}
      *  <LI>{@link $type} {@link com.github.javaparser.ast.body.TypeDeclaration}
      *  <LI>{@link $class} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
      *  <LI>{@link $enum} {@link com.github.javaparser.ast.body.EnumDeclaration}
      *  <LI>{@link $interface} {@link com.github.javaparser.ast.body.ClassOrInterfaceDeclaration}
      *  <LI>{@link $annotation} {@link com.github.javaparser.ast.body.AnnotationDeclaration}
      *  </UL>
      */
    interface $named<$N extends $named & $proto> {

        /** set the name prototype property and return the modified prototype */
        $N $name($id name);

        /** set the name pattern prototype property and return the modified prototype */
        $N $name(String name);

         /** adds a predicate on the name and return the modified prototype */
        $N $name(Predicate<String> nameMatchFn);
    }

}

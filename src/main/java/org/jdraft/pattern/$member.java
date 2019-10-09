package org.jdraft.pattern;

import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.utils.Log;
import org.jdraft.*;

import java.util.ArrayList;
import java.util.List;
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
public interface $member<M, $M extends $pattern> extends $pattern<M,$M> {

    /**
     * Added this because when we have multi-declared fields in code/ast:
     * <PRE>
     * //multi-field declaration
     * int x, y, z = 100;
     *
     * //this is considered (1) FieldDeclaration, but (3) _field s or (3) $field s
     * so this will allow us to iterate over ALL of the BodyDeclarations/ _members of
     *
     * </PRE>
     * @param bds
     * @return
     */
    public static List<$member> of(List<BodyDeclaration> bds){
        List<$member> $mems = new ArrayList<>();

        bds.forEach(b -> {
            //FIRST REMOVE THE _$not annotation we dont want to match against it
            //b.getAnnotations().removeIf( a -> ((AnnotationExpr)a).getNameAsString().equals(_$not.class.getSimpleName())
            //            || ((AnnotationExpr)a).getNameAsString().equals(_$not.class.getCanonicalName() ));
            if(b instanceof FieldDeclaration){
                FieldDeclaration fd = (FieldDeclaration)b;
                fd.getVariables().forEach(v-> {
                    $field $f = $field.of(v);
                    Log.info( "    adding $field %s ", ()->$f.toString() );
                    $mems.add($f);
                } );
            } else{
                $member $m = of(b);
                Log.info( "    adding member %s ", ()->$m.toString() );
                $mems.add($m);
            }
        });
        return $mems;
    }

    public static $member of( BodyDeclaration _m ){
        if( _m instanceof AnnotationDeclaration){
            return $annotation.of( (AnnotationDeclaration)_m);
        }
        if( _m instanceof AnnotationMemberDeclaration){
            return $annotationElement.of( (AnnotationMemberDeclaration)_m);
        }
        if(_m instanceof ClassOrInterfaceDeclaration){
            ClassOrInterfaceDeclaration coid = (ClassOrInterfaceDeclaration)_m;
            if( coid.isInterface() ){
                return $interface.of(coid);
            }
            return $class.of( coid );
        }
        if(_m instanceof ConstructorDeclaration){
            return $constructor.of( (ConstructorDeclaration)_m);
        }
        if(_m instanceof EnumDeclaration ){
            return $enum.of( (EnumDeclaration)_m);
        }
        if(_m instanceof EnumConstantDeclaration){
            return $enumConstant.of( (EnumConstantDeclaration)_m);
        }
        if(_m instanceof FieldDeclaration){
            return $field.of( (FieldDeclaration)_m);
        }
        if(_m instanceof InitializerDeclaration){
            return $initBlock.of( (InitializerDeclaration) _m);
        }
        if(_m instanceof MethodDeclaration){
            return $method.of( (MethodDeclaration)_m);
        }
        throw new _draftException("Unable to convert _member to $member.. unknown _member "+ _m.getClass());
        /** $type??
         if(_m instanceof _enum._constant){
         return $enumConstant.of( (_enum._constant)_m);
         }
         */
    }
    /**
     * Build and return a $member ($pattern implementation) based on the _member type
     * @return
     */
    public static $member of( _member _m){
        if( _m instanceof _annotation){
            return $annotation.of( (_annotation)_m);
        }
        if( _m instanceof _annotation._element){
            return $annotationElement.of( (_annotation._element)_m);
        }
        if(_m instanceof _class){
            return $class.of( (_class)_m);
        }
        if(_m instanceof _constructor){
            return $constructor.of( (_constructor)_m);
        }
        if(_m instanceof _enum){
            return $enum.of( (_enum)_m);
        }
        if(_m instanceof _enum._constant){
            return $enumConstant.of( (_enum._constant)_m);
        }
        if(_m instanceof _field){
            return $field.of( (_field)_m);
        }
        if(_m instanceof _initBlock){
            return $initBlock.of( (_initBlock)_m);
        }
        if(_m instanceof _interface){
            return $interface.of( (_interface)_m);
        }
        if(_m instanceof _method){
            return $method.of( (_method)_m);
        }
        throw new _draftException("Unable to convert _member to $member.. unknown _member "+ _m.getClass());
        /** $type??
        if(_m instanceof _enum._constant){
            return $enumConstant.of( (_enum._constant)_m);
        }
        */
    }

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
    interface $named<$N extends $named & $pattern> {

        /** set the name prototype property and return the modified prototype */
        $N $name($name name);

        /** set the name pattern prototype property and return the modified prototype */
        $N $name(String name);

         /** adds a predicate on the name and return the modified prototype */
        $N $name(Predicate<String> nameMatchFn);
    }

}

package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import org.jdraft._jdraftException;
import org.jdraft._constructor;
import org.jdraft._method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Annotation _macro to convert a {@link _method} to a {@link _constructor} (removing the
 * _method from its container and adding a _constructor)
 *
 * useful when the BODY of a _class or _enum is defined using an anonymous object
 * and (since anonymous objects don't have CONSTRUCTORS) for example:
 * <PRE>
 *     _class _c = _class.of("aaaa.bbbb.C", new Object(){
 *         public @_final int x;
 *
 *         public @_ctor void m( int x){
 *             this.x = x;
 *         }
 *     }
 *     // _c will be:
 *     package aaaa.bbbb;
 *     public class C{
 *         public final int x;
 *
 *         public C( int x){
 *             this.x = x;
 *         }
 *     }
 * </PRE>
 *
 * @see macro
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface _toCtor {

    class Act extends macro<_toCtor, MethodDeclaration> {

        public Act(){
            super(_toCtor.class);
        }

        public Act( _toCtor _tc){
            super(_tc);
        }

        @Override
        public String toString(){
            return "macro[toCtor]";
        }

        @Override
        public void expand(MethodDeclaration methodDeclaration) {
            to(methodDeclaration);
        }

        public static ConstructorDeclaration fromMethod( MethodDeclaration md ){
            return fromMethod(_method.of(md)).ast();
        }

        /** given a _method turn it into a _constructor */
        public static _constructor fromMethod( _method _m ){
            _constructor _ct = _constructor.of( _m.getModifiers()+" "+_m.getName() +"(){}");
            _m.forParams(p-> _ct.addParam(p) );
            if( _m.hasTypeParams()){
                _ct.setTypeParameters( _m.getTypeParams() );
            }
            if( _m.hasAnnoExprs() ) {
                _ct.addAnnoExprs(_m.ast().getAnnotations() );
            }
            _ct.removeAnnoExprs(_toCtor.class);
            _ct.setBody( _m.getBody() );
            _ct.setThrows(_m.ast().getThrownExceptions());
            _ct.setTypeParameters(_m.getTypeParams());
            if( _m.hasJavadoc() ){
                _ct.ast().setJavadocComment(_m.ast().getJavadocComment().get());
            }
            return _ct;
        }

        public static ConstructorDeclaration to(MethodDeclaration methodDeclaration){
            List<TypeDeclaration>tds = new ArrayList<>();
            //_macro.removeAnnotation(methodDeclaration, _toCtor.class);
            _constructor _ct = fromMethod( _method.of(methodDeclaration));
            Optional<Node> op = methodDeclaration.stream(Node.TreeTraversal.PARENTS).filter(n-> n instanceof EnumDeclaration
                    || (n instanceof ClassOrInterfaceDeclaration && !((ClassOrInterfaceDeclaration)n).isInterface())).findFirst();
            //remove the old method, add the constructor
            if( !op.isPresent()){
                throw new _jdraftException("Could not find parent of "+ methodDeclaration+" to replace as constructor");
            }
            TypeDeclaration td = (TypeDeclaration)op.get();
            //NodeWithConstructors nwm = (NodeWithConstructors) op.get();

            /** OLD REMOVE
            td.getMethods().remove(methodDeclaration);
             */

            //nwm.getMembers().remove(methodDeclaration);
            ConstructorDeclaration cd =
                    td.addConstructor(_ct.getModifiers().ast().stream().map(m -> m.getKeyword()).collect(Collectors.toList()).toArray(new Modifier.Keyword[0]) );

            if( td instanceof EnumDeclaration ){
                cd.setPrivate(true); //enum constructors are ALWAYS private
            }
            //port all the constructor stuff to the AST constructor
            cd.setTypeParameters(_ct.getTypeParams().ast());
            cd.setBody(_ct.getBody().ast());
            cd.setParameters( _ct.getParams().ast());
            cd.setThrownExceptions( _ct.getThrows().ast());
            if( _ct.hasJavadoc() ) {
                cd.setJavadocComment(_ct.getJavadoc().ast());
            }
            cd.setAnnotations( _ct.getAnnoExprs().ast());
            cd.getAnnotations().removeIf( a -> a.getNameAsString().equals(_toCtor.class.getName() ) || a.getNameAsString().equals(_toCtor.class.getCanonicalName()) );

            //remove the old method
            boolean isRemoved = methodDeclaration.remove();
            if( !td.getNameAsString().equals("temp") ){
                cd.setName( td.getNameAsString());
            }
            return cd;
        }
    }
}

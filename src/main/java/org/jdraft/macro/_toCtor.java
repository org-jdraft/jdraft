package org.jdraft.macro;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.jdraft._jDraftException;
import org.jdraft._walk;
import org.jdraft._constructor;
import org.jdraft._method;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Annotation _macro to convert a _method to a _constructor (removing the
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
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface _toCtor {

    Macro $ = new Macro();
    
    class Macro implements _macro<_method>{

        @Override
        public String toString(){
           return "macro[ctor]"; 
        }
        
        /**
         * Given a MethodDeclaration, convert it to a ConstructorDeclaration
         * @param md
         * @return 
         */
        public static ConstructorDeclaration fromMethod( MethodDeclaration md ){
            return fromMethod(_method.of(md)).ast();
        }
        
        /**
         * turn a _method into a _constructor
         * @param _m the method to turn into a _constructor
         * @return the _constructor
         */
        public static _constructor fromMethod( _method _m ){
            _constructor _ct = _constructor.of( _m.getModifiers()+" "+_m.getName() +"(){}");        
            _m.forParameters( p-> _ct.addParameter(p) );
            if( _m.hasTypeParameters()){
                _ct.setTypeParameters( _m.getTypeParameters() );
            }
            if( _m.hasAnnos() ) {
                _ct.annotate(_m.ast().getAnnotations() );
            }
            _ct.setBody( _m.getBody() );
            _ct.setThrows(_m.ast().getThrownExceptions());
            _ct.setTypeParameters(_m.getTypeParameters());
            if( _m.hasJavadoc() ){
                _ct.ast().setJavadocComment(_m.ast().getJavadocComment().get());
            }
            return _ct;
        }
        
        @Override
        public _method apply(_method _m) {
            _m.removeAnnos(_toCtor.class);
            List<TypeDeclaration>tds = new ArrayList<>();
            _walk.parents( _m, TypeDeclaration.class, t-> tds.add(t) );
            if( ! (tds.size() > 0 )){
                throw new _jDraftException("no TypeDeclaration parent for "+_m+" to convert to constructor ");
            }
            TypeDeclaration astParentType = tds.get(0);
            _constructor _ct = fromMethod( _m );
            astParentType.addMember( _ct.ast() );
            boolean removed = astParentType.remove( _m.ast() );
            if( ! removed ){
                throw new _jDraftException("Unable to remove "+_m+" from parent TYPE");
            }
            // set the name of the constructor to the name of 
            // the parent type (since it's no longer a method
            //System.out.println( astParentType.getNameAsString() + " " +astParentType.getClass() + " "+ astParentType.getParentNode().get().getClass() );
            if( !astParentType.getNameAsString().equals("temp") ){
                _ct.name(astParentType.getNameAsString());
            }
            //note this is dangerous, seeing as _m is removed... but we'll return it
            return _m;
        }
    }
}

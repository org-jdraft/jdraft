package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft._field;
import org.jdraft._anno;
import org.jdraft.Ex;
import com.github.javaparser.ast.expr.Expression;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to set the initializer for a {@link _field}
 *
 * NOTE: we can use this to avoid the "double tap"
 * i.e. we do this:
 * <PRE>
 * _class _c = _class.of( new Object(){
 *    class F{
 *        @_static String s = readInAFileContents("C:\\temp\\dat");
 *    }
 * });
 * </PRE>
 * where what we WANT is to create a class that will create a static field that reads in a file
 * ///
 * HOWEVER, in creating the anonymous object the initializer is run before actually calling the _class.of(...)
 * method the "readInAFileContents" is run... which is undesired...
 * <PRE>
 * _class _c = _class.of( new Object(){
 *    class F{
 *        @_static
 *        @_init("readInAFileContents(\"C:\\temp\\dat\")")
 *        String s;
 *    }
 * });
 * ...this will update the fields initializer WITHOUT CALLING the "readInAFileContents()"
 * </PRE>
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface _init {

    String value() default "";

    /*
    class Macro implements _macro<_anno._hasAnnos> {
        Expression init;

        public Macro( _init _p ){ this.init = Ex.of(_p.value() ); }

        public Macro( String init ){
            this.init = Ex.of(init);
        }

        @Override
        public String toString(){
           return "macro[init("+init+")]";
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _f) {
            if( _f instanceof _field) {
                return to( (_field)_f, init);
            }
            return _f;
        }

        public static _field to( _field _f, Expression init ){
            _f.init(init);
            return _f;
        }

        public static _field to( _field _f, String init ){
            if( init == null || init.trim().length() == 0 ){
                _f.ast().removeInitializer();
                return _f;
            }
            _f.init(Ex.of(init) );
            return _f;
        }
    }
    */

    class Act extends macro<_init, Node>{

        public Expression initEx;

        public Act( _init _i ){
            super(_i);
            this.initEx = Ex.of(_i.value());
        }

        public Act( String initEx){
            super(_init.class);
            this.initEx = Ex.of(initEx);
        }

        public Act( Expression e ){
            super(_init.class);
            this.initEx = e;
        }

        @Override
        public void expand(Node node) {
            to(node, this.initEx);
        }

        public static<N extends Node> N to( N node, Expression initEx){
            if( node instanceof FieldDeclaration ){
                FieldDeclaration fieldDeclaration = (FieldDeclaration)node;
                fieldDeclaration.getVariables().forEach( v -> v.setInitializer(initEx));
                //_macro.removeAnnotation(node, _init.class);
            } else if( node instanceof VariableDeclarator) {
                VariableDeclarator vd = (VariableDeclarator)node;
                vd.setInitializer(initEx);
                //_macro.removeAnnotation(vd.getParentNode().get(), _init.class);
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[init("+ initEx +")]";
        }
    }
}

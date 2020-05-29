package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft._field;
import org.jdraft.Expr;
import com.github.javaparser.ast.expr.Expression;

import java.lang.annotation.*;

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
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface _init {

    String value() default "";

    class Act extends macro<_init, Node>{

        public Expression initEx;

        public Act( _init _i ){
            super(_i);
            this.initEx = Expr.of(_i.value());
        }

        public Act( String initEx){
            super(_init.class);
            this.initEx = Expr.of(initEx);
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
            } else if( node instanceof VariableDeclarator) {
                VariableDeclarator vd = (VariableDeclarator)node;
                vd.setInitializer(initEx);
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[init("+ initEx +")]";
        }
    }
}

package org.jdraft.macro;

//import com.github.javaparser.ast.CompilationUnit;
import org.jdraft._field;
import org.jdraft._anno;
import org.jdraft.Expr;
import com.github.javaparser.ast.expr.Expression;
//import draft.java.Expr;

import java.lang.annotation.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;

/**
 * Set the initializer for a field
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
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface _init {

    String value() default "";

    class Macro implements _macro<_anno._hasAnnos> {
        Expression init;

        public Macro( _init _p ){ this.init = Expr.of(_p.value() ); }

        public Macro( String init ){
            this.init = Expr.of(init);
        }

        @Override
        public String toString(){
           return "macro[init(...)]"; 
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
            _f.init(Expr.of(init) );
            return _f;
        }
    }
}

package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithAbstractModifier;
import org.jdraft._anno;
import org.jdraft._class;
import org.jdraft._draftException;
import org.jdraft._method;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * apply the abstract modifier to a {@link _method} or {@link _class}
 * ... if it is a {@link _method}, setting it to be abstract will removeIn the BODY
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.TYPE_USE})
public @interface _abstract {

    Macro $ = new Macro();

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[abstract]"; 
        }
        
        public _anno._hasAnnos apply( _anno._hasAnnos _ha ){
            return to( _ha);
        }

        public static _anno._hasAnnos to (_anno._hasAnnos _ha){
            if( _ha instanceof _class){
                return to( (_class)_ha);
            }
            if( _ha instanceof _method ){
                return to( (_method)_ha);
            }
            return _ha;
        }

        public static _method to( _method _m ){
            _m.setAbstract();
            return _m;
        }

        public static _class to( _class t  ) {
            t.setAbstract();
            return t;
        }
    }

    class Act implements Consumer<Node>{

        @Override
        public String toString(){
            return "macro[abstract]";
        }

        public void accept(Node n){
            if (n instanceof NodeWithAbstractModifier) {
                NodeWithAbstractModifier nwa = (NodeWithAbstractModifier) n;
                nwa.setAbstract(true);

                if( n instanceof NodeWithOptionalBlockStmt){ //abstract methods need to remove the body
                    NodeWithOptionalBlockStmt  nwb = (NodeWithOptionalBlockStmt)n;
                    nwb.removeBody();
                }
                _macro.removeAnnotation(n, _abstract.class);
            } else {
                throw new _draftException("@_abstract applied to a non abstract-able AST Node " + n.getClass());
            }
        }
    }
}

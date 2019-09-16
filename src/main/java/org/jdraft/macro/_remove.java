package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.jdraft.*;
import org.jdraft._draftException;
import org.jdraft._anno._hasAnnos;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation macro to remove some entity (field, method, etc.) that doenst belong in the final model
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _remove {

    /*
    Macro $ = new Macro();

    class Macro implements _macro<_hasAnnos> {

        @Override
        public String toString(){
           return "macro[remove]"; 
        }
        
        @Override
        public _hasAnnos apply(_hasAnnos _model) {
            return to(_model);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            if( _model instanceof _type){
                _type _t = (_type)_model;

                boolean removed = _t.ast().remove();
                if( ! removed ){
                   throw new _draftException("Unable to removeIn _type via annotation Macro "+_model);
                }
                return _model;
            }
            if( _model instanceof _field){
                _field _f = (_field)_model;
                //if( _f.ast() )
                //it may have already been removed
                boolean removed = _f.getFieldDeclaration().remove();
                //n.removeIn();
                if( ! removed ){
                    throw new _draftException("Unable to removeIn _field via annotation Macro "+_model);
                }
                return _model;
            }
            //System.out.println( "Not a _field or _type");
            Node n = ((_node)_model).ast();
            boolean removed = n.remove();
            if( ! removed ){
                throw new _draftException("Unable to removeIn entity via annotation Macro "+_model);
            }
            return _model;
        }
    }
     */

    class Act extends macro<_remove, Node>{

        public Act(){
            super(_remove.class);
        }

        public Act(_remove _r){
            super(_r);
        }

        @Override
        public String toString(){
            return "macro[remove]";
        }

        @Override
        public void expand(Node node) {
            if( node instanceof VariableDeclarator && node.getParentNode().isPresent() ){
                  FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                  fd.remove();
            } else {
                node.remove();
            }
        }
    }
}

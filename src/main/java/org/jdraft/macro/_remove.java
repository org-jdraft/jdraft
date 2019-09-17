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
 * Annotation macro to remove some entity (field, method, etc.) that doesnt belong in the final model
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _remove {

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

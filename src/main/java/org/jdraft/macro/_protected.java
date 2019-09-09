package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithProtectedModifier;
import org.jdraft._anno;
import org.jdraft._modifiers;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to add the protected modifier to {@link org.jdraft._type}, {@link org.jdraft._field},
 * {@link org.jdraft._method}, {@link org.jdraft._constructor}
 *
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _protected {

    Macro $ = new Macro();

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[protected]"; 
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _annotatedModel) {
            return to(_annotatedModel);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setProtected();
            return _model;
        }
    }

    class Act implements Consumer<Node> {

        @Override
        public void accept(Node node) {
            if( node instanceof NodeWithProtectedModifier){
                NodeWithProtectedModifier nwpm = (NodeWithProtectedModifier)node;
                nwpm.setModifier(Modifier.Keyword.PRIVATE, false);
                nwpm.setModifier(Modifier.Keyword.PUBLIC, false);
                nwpm.setProtected(true);
                _macro.removeAnnotation(node, _protected.class);
            }else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setProtected(true);
                    _macro.removeAnnotation(fd, _protected.class);
                }
            }
        }

        @Override
        public String toString(){
            return "macro[protected]";
        }
    }
}

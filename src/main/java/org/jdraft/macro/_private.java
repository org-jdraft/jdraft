package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPrivateModifier;
import org.jdraft._anno;
import org.jdraft._modifiers;

import java.lang.annotation.*;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface _private {

    Macro INSTANCE = new Macro();

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[private]"; 
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _annotatedModel) {
            return to(_annotatedModel);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setPrivate();
            return _model;
        }
    }

    class Act implements Consumer<Node> {

        @Override
        public void accept(Node node) {
            if( node instanceof NodeWithPrivateModifier ){
                NodeWithPrivateModifier nwp = (NodeWithPrivateModifier)node;
                nwp.setModifier(Modifier.Keyword.PUBLIC, false);
                nwp.setModifier(Modifier.Keyword.PROTECTED, false);
                nwp.setPrivate(true);
                _macro.removeAnnotation(node, _private.class);
            } else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setPrivate(true);
                    _macro.removeAnnotation(fd, _private.class);
                }
            }

        }
    }
}

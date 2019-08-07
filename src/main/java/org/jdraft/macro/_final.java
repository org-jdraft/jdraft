package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithFinalModifier;
import org.jdraft._anno;
import org.jdraft._modifiers;

import java.lang.annotation.*;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface _final {

    Macro $ = new Macro();

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[final]"; 
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _model) {
            return to(_model);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setFinal();
            return _model;
        }
    }

    class Act implements Consumer<Node> {

        @Override
        public void accept(Node node) {
            if( node instanceof NodeWithFinalModifier){
                NodeWithFinalModifier nwf = (NodeWithFinalModifier)node;
                nwf.setFinal(true);
                _macro.removeAnnotation(node, _final.class);
            } else if (node instanceof VariableDeclarator ){
                VariableDeclarator vd = (VariableDeclarator)node;
                FieldDeclaration fd = (FieldDeclaration)vd.getParentNode().get();
                fd.setFinal(true);
                _macro.removeAnnotation(fd, _final.class);
            }

        }
    }
}

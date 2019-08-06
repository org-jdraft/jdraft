package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithStaticModifier;
import org.jdraft._modifiers;
import org.jdraft._anno;
import org.jdraft._anno._hasAnnos;

import java.lang.annotation.*;
import java.util.function.Consumer;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface _static  {

    Macro $ = new Macro();

    class Macro implements _macro<_hasAnnos> {

        @Override
        public String toString(){
           return "macro[static]"; 
        }
        
        @Override
        public _hasAnnos apply(_anno._hasAnnos _annotatedModel) {
            return to(_annotatedModel);
        }

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setStatic();
            return _model;
        }
    }

    class Act implements Consumer<Node> {

        @Override
        public void accept(Node node) {
            if( node instanceof NodeWithStaticModifier){
                NodeWithStaticModifier nwp = (NodeWithStaticModifier)node;
                nwp.setStatic(true);
                _macro.removeAnnotation(node, _static.class);
            }else{
                if( node instanceof VariableDeclarator){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setStatic(true);
                    _macro.removeAnnotation(fd, _static.class);
                }
            }
        }
    }
}

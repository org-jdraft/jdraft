package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import org.jdraft._anno;
import org.jdraft._named;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation Macro to update the name of anm entity
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface _name{
    String value();

    class Macro implements _macro<_anno._hasAnnos> {
        String name;

        public Macro( _name _e ){
            this.name = _e.value();
        }

        @Override
        public String toString(){
           return "macro[name("+name+")]"; 
        }
        
        public _anno._hasAnnos apply( _anno._hasAnnos _a){
            return to( _a, name );
        }

        public static _anno._hasAnnos to( _anno._hasAnnos _a, String name ){
            if( _a instanceof _named ){
                ((_named)_a).name(name);
            }
            return _a;            
        }
    }

    /**
     * I Wonder whatll happen if I change the name of a class with constructors
     * ...i.e. will the constructors name change accordingly?
     *
     * ..same thing if I have the name as a return type...or a static instance field
     */
    class Act implements Consumer<Node> {
        String name;
        public Act( String name){
            this.name = name;
        }
        public Act( _name _n ){
            this(_n.value());
        }
        @Override
        public void accept(Node node) {
            if( node instanceof NodeWithName){
                NodeWithName nwn = (NodeWithName)node;
                nwn.setName(name);
            } else if (node instanceof NodeWithSimpleName ){
                NodeWithSimpleName nwsn = (NodeWithSimpleName)node;
                nwsn.setName(name);
            }
        }
    }
}

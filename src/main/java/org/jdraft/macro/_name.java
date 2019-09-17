package org.jdraft.macro;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;

import java.lang.annotation.*;

/**
 * Annotation/Macro to update the name of an entity
 *
 * @see macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface _name{
    String value();

    /**
     * I Wonder whatll happen if I change the name of a class with constructors
     * ...i.e. will the constructors name change accordingly?
     *
     * ..same thing if I have the name as a return type...or a static instance field
     */
    class Act extends macro<_name, Node>{

        String name;

        public Act(String name){
            super(_name.class);
            this.name = name;
        }

        public Act( _name _n ){
            super(_n);
            this.name = _n.value();
        }

        @Override
        public void expand(Node node) {
            to(node, name);
        }

        public static <N extends Node> N to(N node, String name){
            if( node instanceof TypeDeclaration ){
                TypeDeclaration td = (TypeDeclaration) node;
                td.setName(name);
                //also update all constructors of the type
                td.getConstructors().forEach(c -> ((ConstructorDeclaration)c).setName(name));
            }
            else if( node instanceof NodeWithName){
                NodeWithName nwn = (NodeWithName)node;
                nwn.setName(name);
            } else if (node instanceof NodeWithSimpleName ){
                NodeWithSimpleName nwsn = (NodeWithSimpleName)node;
                nwsn.setName(name);
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[name("+name+")]";
        }
    }
}

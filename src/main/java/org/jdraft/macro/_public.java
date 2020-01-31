package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import org.jdraft._node;
import org.jdraft._type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation/Macro to add the public modifier<BR/>
 *
 * Macro annotation to apply the public modifier to a {@link _type}, {@link org.jdraft._field},
 * {@link org.jdraft._method}, or {@link org.jdraft._constructor}.<BR>
 *
 * when processed by
 * {@link macro#to(Class, _type)}
 *
 * example:
 * <PRE>
 * public void testMacroAnno(){
 *     @_public class D{
 *         @_public int x;
 *
 *         @_public D(){}
 *
 *         @_public void print(){}
 *     }
 *     _class _c = _class.of(D.class);
 * }
 *     where _c is:
 *     //  public class D{
 *     //      public int x;
 *     //      public D(){}
 *     //      public void print(){}
 *     //  }
 * }
 * </PRE>
 *
 * @see macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _public {

    class Act extends macro<_public, Node> {

        public Act(){
            super(_public.class);
        }

        public Act(_public _p){
            super(_p);
        }

        @Override
        public void expand(Node node) {
            to(node);
        }

        public static <_N extends _node> _N to(_N _n){
            to(_n.ast());
            return _n;
        }

        public static <N extends Node> N to( N node){
            if( node instanceof NodeWithPublicModifier){
                NodeWithPublicModifier nwp = (NodeWithPublicModifier)node;
                nwp.setModifier(Modifier.Keyword.PRIVATE, false);
                nwp.setModifier(Modifier.Keyword.PROTECTED, false);
                nwp.setPublic(true);
            } else{
                if( node instanceof VariableDeclarator ){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setPublic(true);
                }
            }
            return node;
        }

        @Override
        public String toString(){
            return "macro[public]";
        }
    }
}

package org.jdraft.macro;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.nodeTypes.modifiers.NodeWithPublicModifier;
import org.jdraft._anno;
import org.jdraft._modifiers;
import org.jdraft._node;
import org.jdraft._type;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;

/**
 * Annotation/Macro to add the public modifier<BR/>
 *
 * Macro annotation to apply the public modifier to a {@link _type}, {@link org.jdraft._field},
 * {@link org.jdraft._method}, or {@link org.jdraft._constructor}.<BR>
 *
 * when processed by
 * {@link _macro#to(Class, _type)}
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
 *     _class _c = _macro._class(D.class);
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
 * @see _macro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _public {

    /**
    Macro $ = new Macro();

     * Because this static member class implements _macro, it can be processed by
     * {@link _macro#to(Class, _type)}
     *
     * to _2_template the model

    class Macro implements _macro<_anno._hasAnnos> {

        @Override
        public String toString(){
           return "macro[public]"; 
        }
        
        @Override
        public _anno._hasAnnos apply(_anno._hasAnnos _annotatedModel) {
            return to(_annotatedModel);
        }

         * Apply this Macro directly to the
         * @param _model
         * @param <T>
         * @return

        public static <T extends _anno._hasAnnos> T to( T _model ){
            ((_modifiers._hasModifiers) _model).getModifiers().setPublic();
            return _model;
        }
    }
     */

    class Act extends macro<_public, Node> {

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
                //_macro.removeAnnotation(node, _public.class);
            } else{
                if( node instanceof VariableDeclarator ){
                    FieldDeclaration fd = (FieldDeclaration)node.getParentNode().get();
                    fd.setPublic(true);
                    //_macro.removeAnnotation(fd, _public.class);
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

package org.jdraft.macro;

import org.jdraft.*;
import com.github.javaparser.ast.Node;
import org.jdraft._anno._hasAnnos;

import java.lang.annotation.*;
import java.util.function.Consumer;

/**
 * Annotation/Macro to replace the text of some elements within the entity
 *
 * @see _macro
 */
@Retention( RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.TYPE_USE})
public @interface _replace {
    String[] value() default {};

    class Macro implements _macro<_hasAnnos> {
        String[] replacementKeyValues;

        @Override
        public String toString(){
            StringBuilder body = new StringBuilder();
            for(int i=0;i<replacementKeyValues.length;i+=2){
                body.append("( \"");
                body.append(replacementKeyValues[i]);
                body.append("\" to \"");
                body.append(replacementKeyValues[i+1]);
                body.append("\" )");
            }
            return "macro[replace(" + body.toString()+ ")]"; 
        }
        
        public Macro( _replace _r ){
            this.replacementKeyValues = _r.value();
        }
        public Macro( String[] replacements ){
            this.replacementKeyValues = replacements;
        }

        public _hasAnnos apply( _hasAnnos _a){
            return to( _a, replacementKeyValues);
        }

        public static _anno._hasAnnos to(_anno._hasAnnos _model, String[] replacements ){

            //_model.removeAnnos(_replace.class);
            String str = _model.toString();
            for(int i=0;i<replacements.length; i+=2){
                str = str.replace(replacements[i], replacements[i+1]);
            }
            Node oldNode = null;
            Node newNode = null;
            if( _model instanceof _type ) {
                oldNode = ((_type) _model).ast();
                newNode = _java.node(((_type) _model).ast().getClass(), str);
            }else {
                oldNode = ((org.jdraft._node) _model).ast();
                newNode = _java.node(((org.jdraft._node) _model).ast().getClass(), str);
            }
            //System.out.println( oldNode.getClass() + " "+ newNode.getClass() );
            boolean isReplaced = oldNode.replace(newNode);
            if (!isReplaced) {
                if( oldNode.getParentNode().isPresent()){
                    //System.out.println( "OLDNODE HAS A PARENT");
                } else{
                    //System.out.println( "OLDNODE HAS NO PARENT");
                }
                throw new _draftException(
                        "Unable to replaceIn "+oldNode+" with "+newNode+" in Macro at AST level");
            }
            return (_anno._hasAnnos) _java.of(newNode);
        }
    }

    class Act implements Consumer<Node> {
        String oldValue;
        String newValue;

        public Act(_replace _rep ){
            if( _rep.value() == null || _rep.value().length != 2 ){
                throw new _draftException("FAILURE constructing replace macro, expected (2) values {old, new}");
            }
            this.oldValue = _rep.value()[0];
            this.newValue = _rep.value()[1];
        }
        public Act(String oldValue, String newValue){
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        @Override
        public void accept(Node node) {
            node.walk(_walk.POST_ORDER, n->{ //by walking (in postorder fashion)
                String s = n.toString(Ast.PRINT_NO_COMMENTS);
                if( s.contains(oldValue) ) {
                    Node repNode = _java.node(node.getClass(), s.replace(oldValue, newValue) );
                    n.replace(repNode);
                }
            });
            //node.walk(Node.TreeTraversal.POSTORDER, Node.class, n-> {
            //    String str = n.toString();
        }

        public String toString(){
            return "macro[replace(\""+oldValue+ "\", \""+newValue+"\")]";
        }
    }
}  
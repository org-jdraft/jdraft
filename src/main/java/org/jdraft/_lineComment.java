package org.jdraft;

import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.CharLiteralExpr;

import java.util.Objects;
import java.util.function.Function;

/**
 * Single line comment
 * i.e.
 * <CODE>
 * //prints the name
 * System.out.println( "name" +name);
 * </CODE>
 */
public final class _lineComment implements _comment<LineComment, _lineComment>, _tree._node<LineComment, _lineComment> {

    public static final Function<String, _lineComment> PARSER = s-> _lineComment.of(s);

    public static _lineComment of(LineComment lc ){
        return new _lineComment( lc );
    }

    public static _lineComment of( String... commentContents){
        return new _lineComment( Ast.lineComment(commentContents) );
    }

    public static _lineComment of( String commentContents){
        return new _lineComment( Ast.lineComment(commentContents) );
    }

    public static _feature._one<_lineComment, String> TEXT = new _feature._one<>(_lineComment.class, String.class,
            _feature._id.TEXT,
            a -> a.getText(),
            (_lineComment a, String text) -> a.setText(text), PARSER);

    public static _feature._features<_lineComment> FEATURES = _feature._features.of(_lineComment.class,  PARSER, TEXT);

    public _feature._features<_lineComment> features(){
        return FEATURES;
    }

    public LineComment node;

    public _lineComment(LineComment lc ){
        this.node = lc;
    }

    public _tree._node getAttributedNode(){
        if( node.getCommentedNode().isPresent()){
            return (_tree._node)_java.of( node.getCommentedNode().get());
        }
        return null;
    }

    @Override
    public _lineComment copy() {
        return new _lineComment( this.node.clone());
    }

    @Override
    public LineComment node() {
        return node;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _lineComment replace(LineComment replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean equals(Object o){
        if( o instanceof _lineComment){
            _lineComment _bc = (_lineComment)o;
            return Objects.equals( _bc.node, this.node);
        }
        return false;
    }

    public int hashCode(){
        return this.node.hashCode() * 31;
    }

    public String toString(){
        return this.node.toString();
    }
}

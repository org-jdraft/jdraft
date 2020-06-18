package org.jdraft;

import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.BinaryExpr;

import java.util.Objects;
import java.util.function.Function;

/**
 * Representation of a block comment within code
 * <CODE>
 * /* prints the name * /
 * System.out.println( "name" +name);
 * </CODE>
 */
public final class _blockComment implements _comment<BlockComment, _blockComment>, _tree._node<BlockComment, _blockComment> {

    public static final Function<String, _blockComment> PARSER = s-> _blockComment.of(s);

    public static _blockComment of(BlockComment bc ){
        return new _blockComment( bc );
    }

    public static _blockComment of(){
        return of( new BlockComment());
    }

    public static _blockComment of(String commentContents){
        return new _blockComment( Ast.blockComment( commentContents) );
    }

    public static _blockComment of(String... commentContents){
        return new _blockComment( Ast.blockComment( commentContents) );
    }

    public static _feature._one<_blockComment, String> TEXT = new _feature._one<>(_blockComment.class, String.class,
            _feature._id.TEXT,
            a -> a.getText(),
            (_blockComment a, String text) -> a.setText(text), PARSER);

    public static _feature._features<_blockComment> FEATURES = _feature._features.of(_blockComment.class,  PARSER, TEXT);

    public BlockComment node;

    public _feature._features<_blockComment> features(){
        return FEATURES;
    }

    public _blockComment(BlockComment lc ){
        this.node = lc;
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _blockComment replace(BlockComment replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _tree._node getAttributedNode(){
        if( node.getCommentedNode().isPresent()){
            return (_tree._node)_java.of( node.getCommentedNode().get());
        }
        return null;
    }

    @Override
    public _blockComment copy() {
        return new _blockComment( this.node.clone());
    }

    @Override
    public BlockComment node() {
        return node;
    }

    public boolean equals(Object o){
        if( o instanceof _blockComment){
            _blockComment _bc = (_blockComment)o;
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

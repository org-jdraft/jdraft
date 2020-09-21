package org.jdraft;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.jdraft.text.Stencil;

import java.util.function.Function;

public final class _stringExpr
        implements _expr._literal<StringLiteralExpr, _stringExpr>, _java._withText<_stringExpr> {

    public static final Function<String, _stringExpr> PARSER = s-> _stringExpr.of(s);

    public static _stringExpr of(){
        return new _stringExpr( new StringLiteralExpr());
    }
    public static _stringExpr of(StringLiteralExpr sle){
        return new _stringExpr(sle);
    }

    public static _stringExpr of(String...code){
        return new _stringExpr(Expr.stringLiteralExpr( code));
    }

    public static _feature._one<_stringExpr, String> LITERAL_VALUE = new _feature._one<>(_stringExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_stringExpr a, String value) -> a.node().setValue(value), PARSER);

    public static _feature._features<_stringExpr> FEATURES = _feature._features.of(_stringExpr.class,  PARSER, LITERAL_VALUE);

    public StringLiteralExpr node;

    public _stringExpr(String str){
        this( new StringLiteralExpr(str));
    }

    public _stringExpr(StringLiteralExpr node){
        this.node = node;
    }

    public _feature._features<_stringExpr> features(){
        return FEATURES;
    }

    @Override
    public _stringExpr copy() {
        return new _stringExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _stringExpr replace(StringLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    /**
     * Gets the text of the String
     * @return the text of the String
     */
    public String getText(){
        return this.node.getValue();
    }

    /**
     *
     * @param text the text to be set on the entity
     * @return
     */
    public _stringExpr setText(String text ){
        this.node.setValue(text);
        return this;
    }

    /**
     * Look for matches to matchStencil in the contents of the comment and replace with the replaceStencil
     * for example:
     * <PRE>
     * _comment _c = _comment.of("//<code>System.out.println(getValue());</code>");
     * _c.matchReplace("<code>$content$</code>", "{@code $content$}");
     *
     * //verify we matched the old <code></code> tags to {@code}
     * assertEquals( "{@code System.out.println}", _c.getContents());
     * </PRE>
     * @param matchStencil stencil for matching input pattern
     * @param replaceStencil stencil for drafting the replacement
     * @return
     */
    public _stringExpr matchReplace(String matchStencil, String replaceStencil){
        return matchReplace(Stencil.of(matchStencil), Stencil.of(replaceStencil));
    }

    /**
     * Look for matches to matchStencil in the contents of the comment and replace with the replaceStencil
     * for example:
     * <PRE>
     * Stencil matchStencil = Stencil.of("<code>$content$</code>");
     * Stencil replaceStencil = Stencil.of("{@code $content$}");
     * _comment _c = _comment.of("//<code>System.out.println(getValue());</code>");
     * _c.matchReplace(matchStencil, replaceStencil);
     *
     * //verify we matched the old <code></code> tags to {@code}
     * assertEquals( "{@code System.out.println}", _c.getContents());
     * </PRE>
     * @param matchStencil stencil for matching input pattern
     * @param replaceStencil stencil for drafting the replacement
     * @return
     */
    public _stringExpr matchReplace(Stencil matchStencil, Stencil replaceStencil){
        setText(Stencil.matchReplace( getText(), matchStencil, replaceStencil));
        return this;
    }

    public String valueAsString(){
        return this.node.asString();
    }

    public StringLiteralExpr node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _stringExpr){
            return ((_stringExpr)other).node.equals( this.node);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString(){
        return this.node.toString();
    }
}

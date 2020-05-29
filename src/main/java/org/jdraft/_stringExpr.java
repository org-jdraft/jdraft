package org.jdraft;

import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.jdraft.text.Stencil;

import java.util.function.Function;
import java.util.function.Predicate;

public final class _stringExpr implements _expr._literal<StringLiteralExpr, _stringExpr>, _java._withText<_stringExpr> {

    public static final Function<String, _stringExpr> PARSER = s-> _stringExpr.of(s);

    public static _stringExpr of(){
        return new _stringExpr( new StringLiteralExpr());
    }
    public static _stringExpr of(StringLiteralExpr sle){
        return new _stringExpr(sle);
    }

    public static _stringExpr of(String...code){
        return new _stringExpr(Expr.stringExpr( code));
    }

    public static _feature._one<_stringExpr, String> LITERAL_VALUE = new _feature._one<>(_stringExpr.class, String.class,
            _feature._id.LITERAL_VALUE,
            a -> a.valueAsString(),
            (_stringExpr a, String value) -> a.ast().setValue(value), PARSER);

    public static _feature._meta<_stringExpr> META = _feature._meta.of(_stringExpr.class, LITERAL_VALUE);

    public StringLiteralExpr se;

    public _stringExpr(String str){
        this( new StringLiteralExpr(str));
    }

    public _stringExpr(StringLiteralExpr se){
        this.se = se;
    }

    @Override
    public _stringExpr copy() {
        return new _stringExpr(this.se.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.stringExpr(stringRep));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(StringLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    @Override
    public boolean isText(Predicate<String> textMatchFn ){
        return textMatchFn.test(this.se.asString());
    }

    /**
     * Gets the text of the String
     * @return the text of the String
     */
    public String getText(){
        return this.se.getValue();
    }

    /**
     *
     * @param text the text to be set on the entity
     * @return
     */
    public _stringExpr setText(String text ){
        this.se.setValue(text);
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
        return this.se.asString();
    }

    public StringLiteralExpr ast(){
        return se;
    }

    public boolean equals(Object other){
        if( other instanceof _stringExpr){
            return ((_stringExpr)other).se.equals( this.se);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.se.hashCode();
    }

    public String toString(){
        return this.se.toString();
    }
}

package org.jdraft;

import com.github.javaparser.ast.expr.CharLiteralExpr;
import org.jdraft.text.Text;

import java.util.function.Function;
import java.util.function.Predicate;

public final class _charExpr implements _expr._literal<CharLiteralExpr, _charExpr> {

    public static final Function<String, _charExpr> PARSER = s-> _charExpr.of(s);

    public static _charExpr of(){
        return new _charExpr(new CharLiteralExpr());
    }
    public static _charExpr of(CharLiteralExpr cle){
        return new _charExpr(cle);
    }

    public static _charExpr of(char c){
        //lets create the token
        //System.out.println( c );
        //System.out.println( Character.getType( c) );
        //return new _charExpr(Expr.charLiteralExpr( "'"+c+"'"));
        return new _charExpr(new CharLiteralExpr(c));
    }
    public static _charExpr of(String...code){
        return new _charExpr(Expr.charLiteralExpr(Text.combine(code)));
    }

    public static _feature._one<_charExpr, Character> LITERAL_VALUE = new _feature._one<>(_charExpr.class, Character.class,
            _feature._id.LITERAL_VALUE,
            a -> a.getValue(),
            (_charExpr a, Character b) -> a.setValue(b), PARSER);


    public static _feature._features<_charExpr> FEATURES = _feature._features.of(_charExpr.class,  PARSER, LITERAL_VALUE);

    public CharLiteralExpr node;

    public _feature._features<_charExpr> features(){
        return FEATURES;
    }

    public _charExpr(CharLiteralExpr node){
        this.node = node;
    }

    @Override
    public _charExpr copy() {
        return new _charExpr(this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _charExpr replace(CharLiteralExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(CharLiteralExpr astNode) {
        return this.node( ).equals(astNode);
    }

    public boolean is(char c){
        return this.node.asChar() == c;
    }

    public CharLiteralExpr node(){
        return node;
    }

    public boolean equals(Object other){
        if( other instanceof _charExpr){
            return ((_charExpr)other).node.equals( this.node);
        }
        return false;
    }

    public _charExpr setValue(char c){
        this.node.setValue(c+"");
        return this;
    }

    public boolean isValue( Predicate<Character> characterMatchFn ){
        return characterMatchFn.test(this.node.asChar());
    }

    public char getValue(){
        return this.node.asChar();
    }

    public String valueAsString(){
        return this.node.toString();
    }

    public int hashCode(){
        return 31 * this.node.hashCode();
    }

    public String toString(){
        return this.node.toString();
    }
}

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
        return new _charExpr(new CharLiteralExpr(c));
    }
    public static _charExpr of(String...code){
        return new _charExpr(Expr.charExpr(Text.combine(code)));
    }

    public static _feature._one<_charExpr, Character> LITERAL_VALUE = new _feature._one<>(_charExpr.class, Character.class,
            _feature._id.LITERAL_VALUE,
            a -> a.getValue(),
            (_charExpr a, Character b) -> a.setValue(b), PARSER);


    public static _feature._meta<_charExpr> META = _feature._meta.of(_charExpr.class, LITERAL_VALUE);

    public CharLiteralExpr cle;

    public _charExpr(CharLiteralExpr cle){
        this.cle = cle;
    }

    @Override
    public _charExpr copy() {
        return new _charExpr(this.cle.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Expr.charExpr(Text.combine(stringRep)));
        } catch(Exception e){ }
        return false;
    }

    @Override
    public boolean is(CharLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public boolean is(char c){
        return this.cle.asChar() == c;
    }

    public CharLiteralExpr ast(){
        return cle;
    }

    public boolean equals(Object other){
        if( other instanceof _charExpr){
            return ((_charExpr)other).cle.equals( this.cle);
        }
        return false;
    }

    public _charExpr setValue(char c){
        this.cle.setValue(c+"");
        return this;
    }

    public boolean isValue( Predicate<Character> characterMatchFn ){
        return characterMatchFn.test(this.cle.asChar());
    }

    public char getValue(){
        return this.cle.asChar();
    }

    public String valueAsString(){
        return this.cle.toString();
    }

    public int hashCode(){
        return 31 * this.cle.hashCode();
    }

    public String toString(){
        return this.cle.toString();
    }
}

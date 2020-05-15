package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import org.jdraft.text.Text;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public final class _textBlockExpr implements _expr._literal<TextBlockLiteralExpr, _textBlockExpr>, _java._withText<_textBlockExpr> {

    public static _textBlockExpr of(){
        return new _textBlockExpr( new TextBlockLiteralExpr());
    }
    public static _textBlockExpr of(TextBlockLiteralExpr tb){
        return new _textBlockExpr(tb);
    }
    public static _textBlockExpr of(String...code){
        return new _textBlockExpr(Exprs.textBlockEx( code));
    }


    public static <A extends Object> _textBlockExpr of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _textBlockExpr of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _textBlockExpr of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _textBlockExpr of(Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _textBlockExpr of(Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _textBlockExpr of(Function<A,B> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _textBlockExpr of(BiFunction<A,B,C> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _textBlockExpr of(Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _textBlockExpr from(LambdaExpr le){
        Optional<TextBlockLiteralExpr> ows = le.getBody().findFirst(TextBlockLiteralExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No text block literal found in lambda");
    }

    public TextBlockLiteralExpr tble;

    public _textBlockExpr(TextBlockLiteralExpr tble){
        this.tble = tble;
    }

    @Override
    public _textBlockExpr copy() {
        return new _textBlockExpr(this.tble.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        return is( new TextBlockLiteralExpr(Text.combine(stringRep)));
    }

    @Override
    public boolean is(TextBlockLiteralExpr astNode) {
        return this.ast( ).equals(astNode);
    }

    public TextBlockLiteralExpr ast(){
        return tble;
    }

    public String getText(){
        return this.tble.getValue();
    }

    public String toString(){
        return this.tble.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _textBlockExpr){
            return Objects.equals( ((_textBlockExpr)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }

    public _textBlockExpr setText(String textContent){
        this.tble.setValue(textContent);
        return this;
    }

    @Override
    public String valueAsString() {
        return this.tble.getValue();
    }
}

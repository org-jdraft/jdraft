package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import com.github.javaparser.ast.stmt.WhileStmt;
import org.jdraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _textBlock implements _expression<TextBlockLiteralExpr, _textBlock> {

    public static _textBlock of(){
        return new _textBlock( new TextBlockLiteralExpr());
    }
    public static _textBlock of(TextBlockLiteralExpr tb){
        return new _textBlock(tb);
    }
    public static _textBlock of( String...code){
        return new _textBlock(Ex.textBlockEx( code));
    }


    public static <A extends Object> _textBlock of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _textBlock of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _textBlock of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _textBlock of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _textBlock of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _textBlock of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _textBlock of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _textBlock of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _textBlock from( LambdaExpr le){
        Optional<TextBlockLiteralExpr> ows = le.getBody().findFirst(TextBlockLiteralExpr.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No text block literal found in lambda");
    }

    public TextBlockLiteralExpr ile;

    public _textBlock(TextBlockLiteralExpr ile){
        this.ile = ile;
    }

    @Override
    public _textBlock copy() {
        return new _textBlock(this.ile.clone());
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
        return ile;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put(_java.Component.LITERAL, this.ile.getValue());
        return comps;
    }

    public String getText(){
        return this.ile.getValue();
    }

    public String toString(){
        return this.ile.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _textBlock ){
            return Objects.equals( ((_textBlock)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}

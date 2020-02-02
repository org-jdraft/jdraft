package org.jdraft;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _catch implements _java._node<CatchClause, _catch>,_body._hasBody<_catch> {

    public static _catch of(){
        return new _catch( new CatchClause() );
    }
    public static _catch of( CatchClause cc){
        return new _catch(cc);
    }
    public static _catch of( String...code){
        return new _catch(Ast.catchClause( code));
    }


    public static <A extends Object> _catch of(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _catch of(Consumer<A> c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _catch of(BiConsumer<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _catch of( Ex.TriConsumer<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _catch of( Ex.QuadConsumer<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _catch of( Function<A,B> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _catch of( BiFunction<A,B,C> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _catch of( Ex.TriFunction<A,B,C,D> command ){
        return from(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    private static _catch from( LambdaExpr le){
        Optional<CatchClause> ows = le.getBody().findFirst(CatchClause.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No catch clause found in lambda");
    }

    public CatchClause cc;


    @Override
    public _catch copy() {
        return new _catch( this.cc.clone() );
    }
    public _catch(CatchClause cc){
        this.cc = cc;
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ast.catchClause(stringRep));
        }catch(Exception e){
            return false;
        }
    }

    @Override
    public _body getBody() {
        return _body.of(this.cc );
    }

    @Override
    public _catch setBody(BlockStmt body) {
        this.cc.setBody(body);
        return this;
    }

    @Override
    public _catch clearBody() {
        this.cc.setBody(new BlockStmt());
        return this;
    }

    @Override
    public _catch add(int startStatementIndex, Statement... statements) {
        Statement bd = this.cc.getBody();
        if( bd instanceof BlockStmt){
            for(int i=0;i<statements.length; i++) {
                bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
            }
            return this;
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(bd);
        for(int i=0;i<statements.length; i++) {
            bd.asBlockStmt().addStatement(1+startStatementIndex, statements[i]);
        }
        return this;
    }

    @Override
    public boolean is(CatchClause astNode) {
        return this.cc.equals( astNode );
    }

    @Override
    public CatchClause ast() {
        return this.cc;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> comps = new HashMap<>();
        comps.put( _java.Component.BODY, this.cc.getBody());
        comps.put( _java.Component.PARAMETER, this.cc.getParameter());
        return comps;
    }

    public String toString(){
        return this.cc.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _catch ){
            return Objects.equals( ((_catch)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}

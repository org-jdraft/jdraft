package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.*;

public final class _catch implements _java._multiPart<CatchClause, _catch>,_body._hasBody<_catch> {

    public static _catch of(){
        return new _catch( new CatchClause() );
    }
    public static _catch of( CatchClause cc){
        return new _catch(cc);
    }
    public static _catch of( String...code){
        return new _catch(Ast.catchClause( code));
    }

    public static <A extends Object> _catch of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _catch of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _catch of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _catch of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _catch of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _catch of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _catch of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _catch of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static _catch of( Class<? extends Throwable>...classes ){
        CatchClause cc = new CatchClause();
        if( classes.length == 0 ){
            return of( cc );
        }
        if( classes.length == 1 ){
            cc.setParameter( new Parameter(StaticJavaParser.parseClassOrInterfaceType(classes[0].getCanonicalName()), "e"));
        }
        else {
            UnionType ut = new UnionType();
            NodeList<ReferenceType> types = new NodeList<>();
            for(int i=0;i<classes.length;i++){
                types.add(StaticJavaParser.parseClassOrInterfaceType(classes[i].getCanonicalName() ));
            }
            ut.setElements(types);
            cc.setParameter( new Parameter(ut, "e"));
        }
        return of(cc);
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

    /**
     * Does the single parameter have this type in it (either directly:
     * (i.e. for IOException):
     * <PRE>
     * catch(IOException ioe){
     * }
     * </PRE>
     * or using a UnionType:
     * <PRE>
     * catch(IOException | URISyntaxException e){
     * }
     * </PRE>
     * i.e.
     * then return true
     * @param caughtExceptionType
     * @return
     */
    public boolean hasType( Class<? extends Throwable> caughtExceptionType ){
        return hasType(  StaticJavaParser.parseType(caughtExceptionType.getCanonicalName()) );
        /*
        Type t = this.cc.getParameter().getType();
        Type targetType = StaticJavaParser.parseType(caughtExceptionType.getCanonicalName());
        if( t instanceof UnionType ){
            UnionType ut = t.asUnionType();
            return ut.getElements().stream().anyMatch(tt -> Types.equal(tt, targetType));
        }
        return Types.equal(t, targetType);
         */
    }

    public boolean hasType( _typeRef _t ){
        return hasType(_t.ast());
    }

    public boolean hasType( Type caughtExceptionType ){
        Type t = this.cc.getParameter().getType();
        if( t instanceof UnionType ){
            UnionType ut = t.asUnionType();
            return ut.getElements().stream().anyMatch(tt -> Types.equal(tt, caughtExceptionType));
        }
        return Types.equal(t, caughtExceptionType);
    }

    public _param getParameter(){
        return _param.of(this.cc.getParameter());
    }

    public boolean isParameter( Predicate<_param> matchFn){
        return matchFn.test(this.getParameter());
    }

    public _catch setParameter(String... parameter){
        return setParameter(Ast.parameter( parameter) );
    }

    public _catch setParameter(Parameter parameter){
        this.cc.setParameter(parameter);
        return this;
    }

    public _catch setParameter(_param _p){
        this.cc.setParameter(_p.ast());
        return this;
    }

    public boolean isParameter(String... parameter){
        return isParameter(parameter);
    }

    public boolean isParameter(Parameter parameter){
        return Objects.equals(this.cc.getParameter(), parameter);
    }

    public boolean isParameter(_param _p){
        return Objects.equals(this.cc.getParameter(), _p.ast());
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
    public boolean is(String... stringRep) {
        try{
            return is( Ast.catchClause(stringRep));
        }catch(Exception e){
            return false;
        }
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

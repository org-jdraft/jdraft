package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.UnionType;

import java.util.Objects;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

public final class _catch implements _tree._node<CatchClause, _catch>, _body._withBody<_catch> {

    public static final Function<String, _catch> PARSER = s-> _catch.of(s);

    public static _catch of(){
        return new _catch( new CatchClause() );
    }
    public static _catch of( CatchClause cc){
        return new _catch(cc);
    }
    public static _catch of( String...code){
        return new _catch(Ast.catchClause( code));
    }

    public static <A extends Object> _catch of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _catch of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _catch of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _catch of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _catch of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _catch of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _catch of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _catch of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static _catch of( Class<? extends Throwable>...classes ){
        CatchClause cc = new CatchClause();
        if( classes.length == 0 ){
            return of( cc );
        }
        if( classes.length == 1 ){
            cc.setParameter( new Parameter(
                    Types.of(classes[0].getCanonicalName()), "e"));
                    //StaticJavaParser.parseClassOrInterfaceType(classes[0].getCanonicalName()), "e"));
        }
        else {
            UnionType ut = new UnionType();
            NodeList<ReferenceType> types = new NodeList<>();
            for(int i=0;i<classes.length;i++){
                types.add(
                        (ClassOrInterfaceType)Types.of(classes[i].getCanonicalName()));
                        //StaticJavaParser.parseClassOrInterfaceType(classes[i].getCanonicalName() ));
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

    public static _feature._one<_catch, _param> PARAM = new _feature._one<>(_catch.class, _param.class,
            _feature._id.PARAM,
            a -> a.getParam(),
            (_catch a, _param o) -> a.setParam(o), PARSER);

    public static _feature._one<_catch, _body> BODY = new _feature._one<>(_catch.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_catch a, _body _b) -> a.setBody(_b), PARSER);

    public static _feature._features<_catch> FEATURES = _feature._features.of(_catch.class,  PARSER, PARAM, BODY);

    public CatchClause cc;

    public _feature._features<_catch> features(){
        return FEATURES;
    }

    @Override
    public _catch copy() {
        return new _catch( this.cc.clone() );
    }

    public _catch(CatchClause cc){
        this.cc = cc;
    }

    public boolean isCatch(Class<? extends Throwable> caughtException){
        //UnionType ut = new UnionType();
        //ut.getElements()
        //Type ce = Types.of(caughtException);
        return this.getParam().getType().is(caughtException) ||
                this.getParam().getType().isUnionType( caughtException); //ut -> ((UnionType)ut).getElements().stream().anyMatch(t-> Types.equal(ce, t)));
    }

    public boolean isCatch(_typeRef caughtException){
        //UnionType ut = new UnionType();
        //ut.getElements()
        //Type ce = Types.of(caughtException);
        return this.getParam().getType().is(caughtException.ast()) ||
                this.getParam().getType().isUnionType( caughtException.ast()); //ut -> ((UnionType)ut).getElements().stream().anyMatch(t-> Types.equal(ce, t)));
    }

    public _catch addType(Class<? extends Exception>...clazz){
        Stream.of(clazz).forEach( c -> addType( (ReferenceType)_typeRef.of(c).ast()));
        return this;
    }

    public _catch addType(_typeRef... _ts){
        Stream.of(_ts).forEach( _t -> addType( (ReferenceType)_t.ast()));
        return this;
    }

    public _catch addType(ReferenceType ts){
        Type pt = this.cc.getParameter().getType();
        if( pt.isUnionType() ){
            UnionType ut = pt.asUnionType();
            NodeList<ReferenceType> rts = ut.getElements();
            rts.add( ts );
        } else{
            UnionType ut = new UnionType();
            ut.getElements().add( (ReferenceType) pt );
            ut.getElements().add( ts );
            this.cc.getParameter().setType(ut);
        }
        return this;
    }

    public _catch removeType(Class<? extends Exception>...clazz){
        Stream.of(clazz).forEach( c -> removeType(_typeRef.of(c).ast()));
        return this;
    }

    public _catch removeType(_typeRef _ts){
        Stream.of(_ts).forEach( _t -> removeType(_t.ast()));
        return this;
    }

    public _catch removeType(Type ts){
        Type pt = this.cc.getParameter().getType();
        if( pt.isUnionType() ){
            UnionType ut = pt.asUnionType();

            NodeList<ReferenceType> rts = ut.getElements();
            rts.removeIf( r-> Types.equal(r, ts));

            if( ut.getElements().size() == 1 ){
                //replace unionType with ReferenceType?
                this.cc.getParameter().setType(ut.getElements().get(0));
            }
        } else{
            if( Stream.of(ts).anyMatch(e -> Types.equal(e,pt))){
                throw new _jdraftException("cannot remove the last Exception type"+pt+" from "+this);
            }
        }
        return this;
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
        return hasType(
                //Types.of(caughtExceptionType.getCanonicalName()));
                Types.of(caughtExceptionType.getCanonicalName()));
                //StaticJavaParser.parseType(caughtExceptionType.getCanonicalName()) );
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

    public _param getParam(){
        return _param.of(this.cc.getParameter());
    }

    public boolean isParam(Predicate<_param> matchFn){
        return matchFn.test(this.getParam());
    }

    public _catch setParam(String... parameter){
        return setParam(Ast.parameter( parameter) );
    }

    public _catch setParam(Parameter parameter){
        this.cc.setParameter(parameter);
        return this;
    }

    public _catch setParam(_param _p){
        this.cc.setParameter(_p.ast());
        return this;
    }

    public boolean isParam(String... parameter){
        return isParam(parameter);
    }

    public boolean isParam(Parameter parameter){
        return Objects.equals(this.cc.getParameter(), parameter);
    }

    public boolean isParam(_param _p){
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

    /*
    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Ast.catchClause(stringRep));
        }catch(Exception e){
            return false;
        }
    }
     */

    @Override
    public boolean is(CatchClause astNode) {
        return this.cc.equals( astNode );
    }

    @Override
    public CatchClause ast() {
        return this.cc;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put( _java.Feature.BODY, this.cc.getBody());
        comps.put( _java.Feature.PARAM, this.cc.getParameter());
        return comps;
    }
     */

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

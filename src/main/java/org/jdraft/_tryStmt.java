package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public final class _tryStmt implements _stmt._controlFlow._branching<TryStmt, _tryStmt>,
        _java._multiPart<TryStmt, _tryStmt>{

    public static _tryStmt of(){
        return new _tryStmt( new TryStmt( ));
    }
    public static _tryStmt of(TryStmt ts){
        return new _tryStmt(ts);
    }
    public static _tryStmt of(String...code){
        return new _tryStmt(Stmts.tryStmt(code));
    }

    public static <A extends Object> _tryStmt of(Exprs.Command c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _tryStmt of(Consumer<A> c){
        LambdaExpr le = Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _tryStmt of(BiConsumer<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _tryStmt of( Exprs.TriConsumer<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _tryStmt of( Exprs.QuadConsumer<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _tryStmt of( Function<A,B> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _tryStmt of( BiFunction<A,B,C> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _tryStmt of( Exprs.TriFunction<A,B,C,D> command ){
        return from(Exprs.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }


    private static _tryStmt from( LambdaExpr le){
        Optional<TryStmt> ows = le.getBody().findFirst(TryStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No Try statement found in lambda");
    }

    private TryStmt tryStmt;

    public _tryStmt(TryStmt tryStmt){
        this.tryStmt = tryStmt;
    }

    public TryStmt ast(){
        return tryStmt;
    }

    @Override
    public _tryStmt copy() {
        return new _tryStmt( this.tryStmt.clone());
    }

    @Override
    public boolean is(TryStmt astNode) {
        return this.tryStmt.equals( astNode);
    }

    @Override
    public boolean is(String...str ){
        try{
            return _tryStmt.of(str).equals(this);
        }catch(Exception e){
            return false;
        }
    }

    public _body getTryBody(){
        return _body.of( tryStmt.getTryBlock() );
    }

    public _tryStmt setTryBody( Statement st){
        if(st instanceof BlockStmt){
            return setTryBody( (BlockStmt)st);
        }
        BlockStmt bs = new BlockStmt();
        bs.addStatement(st);
        return setTryBody( bs);
    }

    public _tryStmt setTryBody(BlockStmt body) {
        this.tryStmt.setTryBlock(body);
        return this;
    }

    public _tryStmt setTryBody(_body _b){
        this.tryStmt.setTryBlock(_b.ast());
        return this;
    }

    public  <A extends Object> _tryStmt setTryBody(Exprs.Command lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object> _tryStmt setTryBody(Consumer<A> lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object> _tryStmt setTryBody(Function<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt setTryBody(BiFunction<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt setTryBody(Exprs.TriFunction<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object>  _tryStmt setTryBody(Exprs.QuadFunction<A,B,C,D,E> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object>  _tryStmt setTryBody(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt setTryBody(Exprs.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt setTryBody(Exprs.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return setTryBody(bdy);
    }

    public _tryStmt addTry(_stmt... _sts){
        BlockStmt bs = this.tryStmt.getTryBlock();
        for(int i=0;i<_sts.length; i++){
            bs.addStatement(_sts[i].ast());
        }
        return this;
    }

    public _tryStmt addTry( String...code ){
        BlockStmt bs = Ast.blockStmt(code);
        return addTry( bs.getStatements().toArray(new Statement[0]));
    }

    public _tryStmt addTry(Statement... sts){
        BlockStmt bs = this.tryStmt.getTryBlock();
        for(int i=0;i<sts.length; i++){
            bs.addStatement(sts[i]);
        }
        return this;
    }

    public  <A extends Object> _tryStmt addTry(Exprs.Command lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object> _tryStmt addTry(Consumer<A> lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object> _tryStmt addTry(Function<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt addTry(BiFunction<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt addTry(Exprs.TriFunction<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object>  _tryStmt addTry(Exprs.QuadFunction<A,B,C,D,E> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object>  _tryStmt addTry(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt addTry(Exprs.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt addTry(Exprs.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getBody();
        return addTry(bdy);
    }
    public _tryStmt addTry(int startStatementIndex, _stmt... _sts) {
        return addTry( startStatementIndex, Arrays.stream(_sts).map(_s -> _s.ast()).collect(Collectors.toList()).toArray(new Statement[0]));
    }

    public _tryStmt addTry(int startStatementIndex, Statement... statements) {
        BlockStmt bd = this.tryStmt.getTryBlock();
        for(int i=0;i<statements.length; i++) {
               bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
        }
        return this;
    }

    public _tryStmt clearTryBody() {
        this.tryStmt.setTryBlock(new BlockStmt());
        return this;
    }

    /**
     *
     * @return
     */
    public boolean hasWithResources(){
        return ! this.ast().getResources().isEmpty();
    }

    public boolean hasWithResourceType( Class clazz ){
        return hasWithResources(_newExpr.class, _n-> _n.isTypeRef(clazz))
                || hasWithResources(_variablesExpr.class, _v->_v.listVariables(v-> v.isTypeRef(clazz)).size() > 0 );
    }

    public <_E extends _expr> boolean hasWithResources(Class<_E> exprClass, Predicate<_E> matchFn){
        return listWithResources( e-> {
            if( exprClass.isAssignableFrom(e.getClass())){
                return matchFn.test((_E)e);
            }
            return false;
        }).size() > 0;
    }

    public boolean hasWithResources(Predicate<_expr> matchFn){
        return listWithResources(matchFn) != null;
    }

    public _tryStmt setWithResources(_expr... _exs){
        NodeList<Expression> nle = new NodeList<>();
        Arrays.stream(_exs).forEach( _e -> nle.add(_e.ast()));
        this.ast().setResources(nle);
        return this;
    }

    public _tryStmt addWithResources(String...exs){
        Arrays.stream(exs).forEach( e -> this.ast().getResources().add(Exprs.of(e)));
        return this;
    }

    public _tryStmt addWithResources(Expression...exs){
        Arrays.stream(exs).forEach( e -> this.ast().getResources().add(e));
        return this;
    }


    public _tryStmt addWithResources(_expr..._exs){
        Arrays.stream(_exs).forEach( _e -> this.ast().getResources().add(_e.ast()));
        return this;
    }

    public _tryStmt removeWithResource(_expr..._exs){
        Arrays.stream(_exs).forEach( _e -> this.ast().getResources().remove(_e.ast()));
        return this;
    }

    public _tryStmt removeWithResource(Predicate<_expr> matchFn){
        List<_expr> toRemove = listWithResources(matchFn);
        toRemove.forEach( _e -> this.ast().getResources().remove(_e.ast()));
        return this;
    }

    public List<_expr> listWithResources(){
        List<_expr> exs = new ArrayList<>();
        this.tryStmt.getResources().forEach( e -> exs.add(_expr.of(e)));
        return exs;
    }

    public List<_expr> listWithResources(Predicate<_expr> matchFn){
        List<_expr> exs = listWithResources();
        return exs.stream().filter(matchFn).collect(Collectors.toList());
    }

    public boolean catches( String exceptionClassName){
        return getCatch(exceptionClassName) != null;
    }

    public boolean catches( _typeRef exceptionClass){
        return getCatch(exceptionClass) != null;
    }

    public boolean catches( Type exceptionClass){
        return getCatch(exceptionClass) != null;
    }

    public boolean catches( Class<? extends Throwable> exceptionClass ){
        return getCatch(exceptionClass) != null;
    }

    public _catch getCatch( String exceptionClassName ){
        Type t = Types.of(exceptionClassName);
        return getCatch(t);
    }

    public _catch getCatch( _typeRef _tr){
        return getCatch( _tr.ast());
    }

    public _catch getCatch( Type astType ){
        Optional<_catch> _oc = listCatches().stream().filter( _c->
            _c.getParameter().isTypeRef(astType)
            ||  _c.getParameter().getTypeRef().isUnionType() &&
                    _c.getParameter().getTypeRef().ast().asUnionType().getElements().stream().anyMatch(rt-> Types.equal(rt, astType))
        ).findFirst();
        if( _oc.isPresent() ){
            return _oc.get();
        }
        return null;
    }

    /**
     * Gets the catch associated with this Exception type
     * @param type
     * @return
     */
    public _catch getCatch( Class<? extends Throwable> type ){
        //fully qualified name
        com.github.javaparser.ast.type.ReferenceType astType = Types.of(type).asReferenceType();
        //simple name
        com.github.javaparser.ast.type.ReferenceType astType2 = Types.of(type.getSimpleName()).asReferenceType();

        //_c-> _c.getParameter().isType(IOException.class)).findFirst().get()
        Optional<_catch> _oc = listCatches().stream().filter( _c-> _c.getParameter().isTypeRef(type)
                || _c.getParameter().getTypeRef().isUnionType() && _c.getParameter().getTypeRef().ast().asUnionType().getElements().contains(astType)
                || _c.getParameter().getTypeRef().isUnionType() && _c.getParameter().getTypeRef().ast().asUnionType().getElements().contains(astType2))
                .findFirst();
        if( _oc.isPresent() ){
            return _oc.get();
        }
        return null;
    }

    public boolean hasCatch(){
        return ! this.ast().getCatchClauses().isEmpty();
    }

    public boolean hasCatch(Predicate<_catch> matchFn){
        return listCatches(matchFn).size() > 0;
    }

    /**
     * List the catchClauses
     * @return
     */
    public List<_catch> listCatches(){
        List<_catch> _ccs = new ArrayList<>();
        this.ast().getCatchClauses().forEach(cc -> _ccs.add(_catch.of(cc)));
        return _ccs;
    }

    public List<_catch> listCatches( Predicate<_catch> matchFn){
        return listCatches().stream().filter(matchFn).collect(Collectors.toList());
    }

    public _tryStmt addCatch(String...catchClause){
        CatchClause cc = Ast.catchClause(catchClause);
        return addCatch(cc);
    }

    public _tryStmt addCatch(CatchClause... cc){
        Arrays.stream(cc).forEach( c -> this.tryStmt.getCatchClauses().add(c));
        return this;
    }

    public _tryStmt addCatch(_catch... _c){
        Arrays.stream(_c).forEach( cc -> this.tryStmt.getCatchClauses().add(cc.ast()));
        return this;
    }

    public boolean hasFinally(){
        return this.ast().getFinallyBlock().isPresent();
    }

    //does this try statement have a finally body that is NON-EMPTY
    public boolean hasFinallyBody(){
        return this.ast().getFinallyBlock().isPresent()
                &&  !( this.ast().getFinallyBlock().get().isEmpty());
    }

    public _body getFinallyBody(){
        if( this.tryStmt.getFinallyBlock().isPresent()){
            return _body.of( tryStmt.getFinallyBlock() );
        }
        return null;
    }

    public _tryStmt setFinallyBody(_body _b){
        this.tryStmt.setFinallyBlock(_b.ast());
        return this;
    }

    public _tryStmt setFinallyBody(_blockStmt finallyBlock){
        this.tryStmt.setFinallyBlock(finallyBlock.ast());
        return this;
    }

    public _tryStmt removeFinally(){
        this.tryStmt.removeFinallyBlock();
        return this;
    }

    public _tryStmt setFinallyBody(BlockStmt body) {
        this.tryStmt.setFinallyBlock(body);
        return this;
    }

    public _tryStmt addFinally( String...code ){
        BlockStmt bs = Ast.blockStmt(code);
        return addFinally( bs.getStatements().toArray(new Statement[0]));
    }

    public _tryStmt addFinally(_stmt... _sts){
        Optional<BlockStmt> bd = this.tryStmt.getFinallyBlock();
        BlockStmt bs = null;
        if( !bd.isPresent() ){
            bs = new BlockStmt();
            this.tryStmt.setFinallyBlock(bs);
        } else {
            bs = this.tryStmt.getFinallyBlock().get();
        }
        for(int i=0;i<_sts.length; i++) {
            bs.addStatement(_sts[i].ast());
        }
        return this;
    }

    public _tryStmt addFinally(Statement... sts){
        BlockStmt bs = this.tryStmt.getTryBlock();
        for(int i=0;i<sts.length; i++){
            bs.addStatement(sts[i]);
        }
        return this;
    }

    public _tryStmt addFinally(int startStatementIndex, Statement... statements) {
        Optional<BlockStmt> bd = this.tryStmt.getFinallyBlock();
        BlockStmt bs = null;
        if( !bd.isPresent() ){
            bs = new BlockStmt();
            this.tryStmt.setFinallyBlock(bs);
        } else {
            bs = this.tryStmt.getFinallyBlock().get();
        }
        for(int i=0;i<statements.length; i++) {
            bs.addStatement(i+startStatementIndex, statements[i]);
        }
        return this;
    }

    public String toString(){
        return this.tryStmt.toString();
    }

    @Override
    public Map<_java.Feature, Object> components() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put( _java.Feature.TRY_BODY, tryStmt.getTryBlock());
        comps.put( _java.Feature.WITH_RESOURCES_EXPRS, tryStmt.getResources());
        comps.put(_java.Feature.CATCH_CLAUSES, tryStmt.getCatchClauses());
        if( tryStmt.getFinallyBlock().isPresent() ){
            comps.put(_java.Feature.FINALLY_BODY, tryStmt.getFinallyBlock().get());
        }
        return comps;
    }

    public boolean equals(Object other){
        if( other instanceof _tryStmt ){
            return Objects.equals( ((_tryStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}

package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public final class _tryStmt implements
        _stmt._conditional<TryStmt, _tryStmt> {

    public static final Function<String, _tryStmt> PARSER = s-> _tryStmt.of(s);

    public static _tryStmt of(){
        return new _tryStmt( new TryStmt( ));
    }
    public static _tryStmt of(TryStmt ts){
        return new _tryStmt(ts);
    }
    public static _tryStmt of(String...code){
        return new _tryStmt(Stmt.tryStmt(code));
    }

    public static <A extends Object> _tryStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _tryStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _tryStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _tryStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _tryStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _tryStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _tryStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _tryStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _tryStmt from( LambdaExpr le){
        Optional<TryStmt> ows = le.getBody().findFirst(TryStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No Try statement found in lambda");
    }

    public static _feature._one<_tryStmt, _body> TRY_BODY = new _feature._one<>(_tryStmt.class, _body.class,
            _feature._id.TRY_BODY,
            a -> a.getTryBody(),
            (_tryStmt p, _body _b) -> p.setTryBody(_b), PARSER);

    public static _feature._one<_tryStmt, _body> FINALLY_BODY = new _feature._one<>(_tryStmt.class, _body.class,
            _feature._id.FINALLY_BODY,
            a -> a.getFinallyBody(),
            (_tryStmt p, _body _b) -> p.setFinallyBody(_b), PARSER);

    public static _feature._many<_tryStmt, _expr> WITH_RESOURCES = new _feature._many<>(_tryStmt.class, _expr.class,
            _feature._id.WITH_RESOURCES,
            _feature._id.WITH_RESOURCE,
            a -> a.listWithResources(),
            (_tryStmt p, List<_expr> _ses) -> p.setWithResources(_ses), PARSER, s->_expr.of(s)).setOrdered(false);

    public static _feature._many<_tryStmt, _catch> CATCH_CLAUSES = new _feature._many<>(_tryStmt.class, _catch.class,
            _feature._id.CATCH_CLAUSES,
            _feature._id.CATCH_CLAUSE,
            a -> a.listCatches(),
            (_tryStmt p, List<_catch> _ccs) -> p.setCatchClauses(_ccs), PARSER, s->_catch.of(s)).setOrdered(false);

    public static _feature._features<_tryStmt> FEATURES = _feature._features.of(_tryStmt.class,  PARSER, WITH_RESOURCES, TRY_BODY, CATCH_CLAUSES, FINALLY_BODY );

    private TryStmt node;

    public _tryStmt(TryStmt node){
        this.node = node;
    }

    public _feature._features<_tryStmt> features(){
        return FEATURES;
    }

    public TryStmt node(){
        return node;
    }

    @Override
    public _tryStmt copy() {
        return new _tryStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _tryStmt replace(TryStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public _body getTryBody(){
        return _body.of( node.getTryBlock() );
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
        this.node.setTryBlock(body);
        return this;
    }

    public _tryStmt setTryBody(_body _b){
        Statement st = _b.astStatement();
        if( st == null ){
            this.node.setTryBlock(new BlockStmt());
        }
        else if( st instanceof BlockStmt ){
            this.node.setTryBlock( st.asBlockStmt());
        } else{
            BlockStmt bs = new BlockStmt( );
            bs.addStatement( st);
            this.node.setTryBlock(bs);
        }
        return this;
    }

    public  <A extends Object> _tryStmt setTryBody(Expr.Command lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object> _tryStmt setTryBody(Consumer<A> lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object> _tryStmt setTryBody(Function<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt setTryBody(BiFunction<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt setTryBody(Expr.TriFunction<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object>  _tryStmt setTryBody(Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object>  _tryStmt setTryBody(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt setTryBody(Expr.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt setTryBody(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return setTryBody(bdy);
    }

    public _tryStmt addTry(_stmt... _sts){
        BlockStmt bs = this.node.getTryBlock();
        for(int i=0;i<_sts.length; i++){
            bs.addStatement(_sts[i].node());
        }
        return this;
    }

    public _tryStmt addTry( String...code ){
        BlockStmt bs = Ast.blockStmt(code);
        return addTry( bs.getStatements().toArray(new Statement[0]));
    }

    public _tryStmt addTry(Statement... sts){
        BlockStmt bs = this.node.getTryBlock();
        for(int i=0;i<sts.length; i++){
            bs.addStatement(sts[i]);
        }
        return this;
    }

    public  <A extends Object> _tryStmt addTry(Expr.Command lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object> _tryStmt addTry(Consumer<A> lambdaWithBody){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object> _tryStmt addTry(Function<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt addTry(BiFunction<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt addTry(Expr.TriFunction<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object, E extends Object>  _tryStmt addTry(Expr.QuadFunction<A,B,C,D,E> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object>  _tryStmt addTry(BiConsumer<A,B> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object>  _tryStmt addTry(Expr.TriConsumer<A,B,C> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object>  _tryStmt addTry(Expr.QuadConsumer<A,B,C,D> lambdaWithBody ){
        Statement bdy = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]).getAstStatementBody();
        return addTry(bdy);
    }
    public _tryStmt addTry(int startStatementIndex, _stmt... _sts) {
        return addTry( startStatementIndex, Arrays.stream(_sts).map(_s -> _s.node()).collect(Collectors.toList()).toArray(new Statement[0]));
    }

    public _tryStmt addTry(int startStatementIndex, Statement... statements) {
        BlockStmt bd = this.node.getTryBlock();
        for(int i=0;i<statements.length; i++) {
               bd.asBlockStmt().addStatement(i+startStatementIndex, statements[i]);
        }
        return this;
    }

    public _tryStmt clearTryBody() {
        this.node.setTryBlock(new BlockStmt());
        return this;
    }

    /**
     *
     * @return
     */
    public boolean hasWithResources(){
        return ! this.node().getResources().isEmpty();
    }

    public boolean hasWithResourceType( Class clazz ){
        return hasWithResources(_newExpr.class, _n-> _n.isType(clazz))
                || hasWithResources(_variablesExpr.class, _v->_v.listVariables(v-> v.isType(clazz)).size() > 0 );
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

    public _tryStmt setWithResources(List<_expr> _exs){
        return setWithResources( _exs.toArray(new _expr[0]));
    }

    public _tryStmt setWithResources(_expr... _exs){
        NodeList<Expression> nle = new NodeList<>();
        Arrays.stream(_exs).forEach( _e -> nle.add(_e.node()));
        this.node().setResources(nle);
        return this;
    }

    public _tryStmt addWithResources(String...exs){
        Arrays.stream(exs).forEach( e -> this.node().getResources().add(Expr.of(e)));
        return this;
    }

    public _tryStmt addWithResources(Expression...exs){
        Arrays.stream(exs).forEach( e -> this.node().getResources().add(e));
        return this;
    }

    public _tryStmt addWithResources(_expr..._exs){
        Arrays.stream(_exs).forEach( _e -> this.node().getResources().add(_e.node()));
        return this;
    }

    public _tryStmt removeWithResource(_expr..._exs){
        Arrays.stream(_exs).forEach( _e -> this.node().getResources().remove(_e.node()));
        return this;
    }

    public _tryStmt removeWithResource(Predicate<_expr> matchFn){
        List<_expr> toRemove = listWithResources(matchFn);
        toRemove.forEach( _e -> this.node().getResources().remove(_e.node()));
        return this;
    }

    public List<_expr> listWithResources(){
        List<_expr> exs = new ArrayList<>();
        this.node.getResources().forEach(e -> exs.add(_expr.of(e)));
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
        return getCatch( _tr.node());
    }

    public _catch getCatch( Type astType ){
        Optional<_catch> _oc = listCatches().stream().filter( _c->
            _c.getParam().isType(astType)
            ||  _c.getParam().getType().isUnionType() &&
                    _c.getParam().getType().node().asUnionType().getElements().stream().anyMatch(rt-> Types.equal(rt, astType))
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
        Optional<_catch> _oc = listCatches().stream().filter( _c-> _c.getParam().isType(type)
                || _c.getParam().getType().isUnionType() && _c.getParam().getType().node().asUnionType().getElements().contains(astType)
                || _c.getParam().getType().isUnionType() && _c.getParam().getType().node().asUnionType().getElements().contains(astType2))
                .findFirst();
        if( _oc.isPresent() ){
            return _oc.get();
        }
        return null;
    }

    public boolean hasCatch(){
        return ! this.node().getCatchClauses().isEmpty();
    }

    /**
     * does this try statement have a catch block with this
     * @param thrown
     * @return
     */
    public boolean hasCatch(Class<? extends Throwable> thrown){
        return !listCatches(c-> c.isCatch(thrown) ).isEmpty();
    }

    public boolean hasCatch(Predicate<_catch> matchFn){
        return listCatches(matchFn).size() > 0;
    }

    public _tryStmt setCatchClauses(List<_catch> _ccs){
        this.node().getCatchClauses().clear();
        _ccs.forEach( _cc -> this.node().getCatchClauses().add( _cc.node()));
        return this;
    }

    /**
     * List the catchClauses
     * @return
     */
    public List<_catch> listCatches(){
        List<_catch> _ccs = new ArrayList<>();
        this.node().getCatchClauses().forEach(cc -> _ccs.add(_catch.of(cc)));
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
        Arrays.stream(cc).forEach( c -> this.node.getCatchClauses().add(c));
        return this;
    }

    public _tryStmt addCatch(_catch... _c){
        Arrays.stream(_c).forEach( cc -> this.node.getCatchClauses().add(cc.node()));
        return this;
    }

    public boolean hasFinally(){
        return this.node().getFinallyBlock().isPresent();
    }

    //does this try statement have a finally body that is NON-EMPTY
    public boolean hasFinallyBody(){
        return this.node().getFinallyBlock().isPresent()
                &&  !( this.node().getFinallyBlock().get().isEmpty());
    }

    public _body getFinallyBody(){
        if( this.node.getFinallyBlock().isPresent()){
            return _body.of( node.getFinallyBlock() );
        }
        return null;
    }

    public _tryStmt setFinallyBody(_body _b){
        Statement st = _b.astStatement();
        if( st == null ){
            this.node.setFinallyBlock(new BlockStmt());
        }
        else if( st instanceof BlockStmt ){
            this.node.setFinallyBlock( st.asBlockStmt());
        } else{
            BlockStmt bs = new BlockStmt( );
            bs.addStatement( st);
            this.node.setFinallyBlock(bs);
        }
        return this;
    }

    public _tryStmt setFinallyBody(_blockStmt finallyBlock){
        this.node.setFinallyBlock(finallyBlock.node());
        return this;
    }

    public _tryStmt removeFinally(){
        this.node.removeFinallyBlock();
        return this;
    }

    public _tryStmt setFinallyBody(BlockStmt body) {
        this.node.setFinallyBlock(body);
        return this;
    }

    public _tryStmt addFinally( String...code ){
        BlockStmt bs = Ast.blockStmt(code);
        return addFinally( bs.getStatements().toArray(new Statement[0]));
    }

    public _tryStmt addFinally(_stmt... _sts){
        Optional<BlockStmt> bd = this.node.getFinallyBlock();
        BlockStmt bs = null;
        if( !bd.isPresent() ){
            bs = new BlockStmt();
            this.node.setFinallyBlock(bs);
        } else {
            bs = this.node.getFinallyBlock().get();
        }
        for(int i=0;i<_sts.length; i++) {
            bs.addStatement(_sts[i].node());
        }
        return this;
    }

    public _tryStmt addFinally(Statement... sts){
        BlockStmt bs = this.node.getTryBlock();
        for(int i=0;i<sts.length; i++){
            bs.addStatement(sts[i]);
        }
        return this;
    }

    public _tryStmt addFinally(int startStatementIndex, Statement... statements) {
        Optional<BlockStmt> bd = this.node.getFinallyBlock();
        BlockStmt bs = null;
        if( !bd.isPresent() ){
            bs = new BlockStmt();
            this.node.setFinallyBlock(bs);
        } else {
            bs = this.node.getFinallyBlock().get();
        }
        for(int i=0;i<statements.length; i++) {
            bs.addStatement(i+startStatementIndex, statements[i]);
        }
        return this;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _tryStmt ){
            return Objects.equals( ((_tryStmt)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}

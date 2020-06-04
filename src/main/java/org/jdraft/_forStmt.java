package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;

import java.util.*;
import java.util.function.*;

/**
 *
 */
public final class _forStmt implements
        _stmt._conditional<ForStmt,_forStmt>,
        _stmt._loop<ForStmt, _forStmt>,
        _body._withBody<_forStmt> {

    public static final Function<String, _forStmt> PARSER = s-> _forStmt.of(s);

    public static _forStmt of(){
        return new _forStmt( new ForStmt( ));
    }
    public static _forStmt of(ForStmt fs){
        return new _forStmt(fs);
    }
    public static _forStmt of(String...code){
        return new _forStmt(Stmt.forStmt( code));
    }

    public static <A extends Object> _forStmt of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _forStmt of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _forStmt of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forStmt of( Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forStmt of( Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _forStmt of( Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _forStmt of( BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _forStmt of( Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _forStmt from( LambdaExpr le){
        Optional<ForStmt> ows = le.getBody().findFirst(ForStmt.class);
        if( ows.isPresent() ){
            return of(ows.get());
        }
        throw new _jdraftException("No for statement found in lambda");
    }

    public static _feature._one<_forStmt, _body> BODY = new _feature._one<>(_forStmt.class, _body.class,
            _feature._id.BODY,
            a -> a.getBody(),
            (_forStmt a, _body b) -> a.setBody(b), PARSER);

    public static _feature._one<_forStmt, _expr> COMPARE = new _feature._one<>(_forStmt.class, _expr.class,
            _feature._id.COMPARE,
            a -> a.getCompare(),
            (_forStmt a, _expr _e) -> a.setCompare(_e), PARSER);

    public static _feature._many<_forStmt, _expr> UPDATES = new _feature._many<>(_forStmt.class, _expr.class,
            _feature._id.UPDATES,
            _feature._id.UPDATE,
            a -> a.listUpdates(),
            (_forStmt a, List<_expr> _e) -> a.setUpdates(_e), PARSER, s-> _expr.of(s))
            .setOrdered(true);/** the order of the updates MAY matter */


    public static _feature._one<_forStmt, _variablesExpr> INIT = new _feature._one<>(_forStmt.class, _variablesExpr.class,
          _feature._id.INIT,
          a -> {
            List<Expression> es = a.ast().getInitialization();
            if( es.isEmpty() ){
                return null;
            }
            return _variablesExpr.of( (VariableDeclarationExpr)es.get(0) );
          },
          (_forStmt a, _variablesExpr _e) -> a.setInit(_e), PARSER);

    /*
    public static _feature._many<_forStmt, _expr> INITS = new _feature._many<>(_forStmt.class, _expr.class,
            _feature._id.INITS,
            _feature._id.INIT,
            a -> a.listInits(),
            (_forStmt a, List<_expr> _e) -> a.setInits(_e), PARSER, s-> _expr.of(s))
            .isOrdered(false); /** the order of the initializers doesnt matter (i.e. int i=0; int j=1 === int j=1, int i=0)
    */

    public static _feature._features<_forStmt> FEATURES = _feature._features.of(_forStmt.class, INIT, COMPARE, UPDATES, BODY);

    private ForStmt astStmt;

    public _forStmt(ForStmt rs){
        this.astStmt = rs;
    }

    @Override
    public _forStmt copy() {
        return new _forStmt( this.astStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try{
            return is( Stmt.forStmt(stringRep));
        } catch(Exception e){ }
        return false;
    }

    public boolean hasInit(){
        return !this.astStmt.getInitialization().isEmpty();
    }

    public _variablesExpr getInit(){
        if( hasInit()){
            return _variablesExpr.of( (VariableDeclarationExpr)astStmt.getInitialization().get(0) );
        }
        return null;
    }
    public boolean isInit(_variablesExpr _ve){
        if( !this.hasInit() ){
            return false;
        }
        return Objects.equals( _ve, _variablesExpr.of( (VariableDeclarationExpr)this.astStmt.getInitialization().get(0) ) );
    }

    public boolean isInit(String...code){
        if( !this.hasInit() ){
            return code.length == 0 || code.length == 1 && code[0].trim().length() ==0;
        }
        try{
            _variablesExpr _ve = _variablesExpr.of(code);
            return Objects.equals( _ve, _variablesExpr.of( (VariableDeclarationExpr)this.astStmt.getInitialization().get(0) ) );
        }catch(Exception e){
            return false;
        }
    }

    public boolean isInit(Predicate<_variablesExpr> _varsMatchFn){
        if( !this.hasInit() ){
            return false;
        }
        return _varsMatchFn.test(getInit());
    }

    public _forStmt setInit(_variablesExpr _ve){
        NodeList<Expression> nle = new NodeList<>();
        nle.add( _ve.ast() );
        this.astStmt.setInitialization(nle);
        return this;
    }

    /*
    public _forStmt forInits(Consumer<_expr> consumer){
        listInits().forEach(consumer);
        return this;
    }

    public List<_expr> listInits(){
        List<_expr>init = new ArrayList<>();
        this.astStmt.getInitialization().forEach(i -> init.add(_expr.of(i)));
        return init;
    }

    public <_E extends _expr> List<_E> listInits(Class<_E> expressionClass){
        return listInits( expressionClass, t->true);
    }

    public List<_expr> listInits(Predicate<_expr> matchFn){
        return listInits(_expr.class, matchFn);
    }

    public <_E extends _expr> List<_E> listInits(Class<_E> expressionClass, Predicate<_E> matchFn){
        List<_E> inits = new ArrayList<>();
        this.astStmt.getInitialization().forEach(i -> {
            _expr _e = _expr.of(i);
            if( expressionClass.isAssignableFrom(_e.getClass()) && matchFn.test( (_E)_e)){
                inits.add((_E)_e);
            }
        });
        return inits;
    }

    public _forStmt addInits(_expr... _es){
        Arrays.stream(_es).forEach(_e -> this.astStmt.getInitialization().add(_e.ast()));
        return this;
    }


    public _forStmt setInits(List<_expr> _es){
        return setInits( _es.toArray(new _expr[0]) );
    }

    public _forStmt setInits(_expr... es){
        NodeList<Expression> init = new NodeList<>();
        Arrays.stream(es).forEach(e-> init.add(e.ast()));
        this.astStmt.setInitialization(init);
        return this;
    }
    */

    public boolean hasCompare(){
        return !(this.getCompare() == null);
    }

    public _forStmt removeCompare(){
        this.astStmt.removeCompare();
        return this;
    }

    public _expr getCompare(){
        if( this.astStmt.getCompare().isPresent()) {
            return _expr.of(this.astStmt.getCompare().get());
        }
        return null;
    }

    /**
     * checks if the compare part of the forStmt equals the expression
     * @param expressionCode
     * @return
     */
    public boolean isCompare(String...expressionCode){
        try{
            return Objects.equals( _expr.of(expressionCode), getCompare() );
        } catch(Exception e){
            return false;
        }
    }

    public boolean isCompare(BinaryExpr.Operator bo){
        if(this.astStmt.getCompare().isPresent() && this.astStmt.getCompare().get() instanceof BinaryExpr){
            return ((BinaryExpr) this.astStmt.getCompare().get()).getOperator() == bo;
        }
        return false;
    }

    public boolean isCompare(_expr _e){
        return Objects.equals( getCompare(), _e);
    }

    public boolean isCompare(Class<? extends _expr> expressionClass ){
        if( hasCompare() ){
            return expressionClass.isAssignableFrom( getCompare().getClass() );
        }
        return false;
    }

    public boolean isCompare( Predicate<_expr> matchFn){
        _expr _e = getCompare();
        if( _e == null ){
            try{
                return matchFn.test(null);
            } catch(Exception e){
                return false;
            }
        }
        return matchFn.test(_e);
    }

    public _forStmt setCompare(String...str){
        this.astStmt.setCompare(Expr.of(str));
        return this;
    }

    public _forStmt setCompare(_expr e){
        this.astStmt.setCompare(e.ast());
        return this;
    }

    public _forStmt setCompare(Expression e){
        this.astStmt.setCompare(e);
        return this;
    }

    public boolean hasUpdate(){
        return !this.astStmt.getUpdate().isEmpty();
    }

    public _forStmt removeUpdate(_expr _ex){
        this.astStmt.getUpdate().removeIf(u-> Objects.equals(u, _ex.ast()) );
        return this;
    }

    public _forStmt forUpdates(Consumer<_expr> consumer){
        listUpdates().forEach(consumer);
        return this;
    }

    public _forStmt addUpdates( _expr... _es){
        Arrays.stream(_es).forEach(_e -> this.astStmt.getUpdate().add(_e.ast()));
        return this;
    }

    public List<_expr> listUpdates(){
        List<_expr>update = new ArrayList<>();
        this.astStmt.getUpdate().forEach(i -> update.add(_expr.of(i)));
        return update;
    }

    public List<_expr> listUpdates(Predicate<_expr> matchFn){
        return listUpdates(_expr.class, matchFn);
    }

    public <_E extends _expr> List<_E> listUpdates(Class<_E> expressionClass){
        return listUpdates( expressionClass, t->true);
    }

    public <_E extends _expr> List<_E> listUpdates(Class<_E> expressionClass, Predicate<_E> matchFn){
        List<_E> updates = new ArrayList<>();
        this.astStmt.getUpdate().forEach(i -> {
            _expr _e = _expr.of(i);
            if( expressionClass.isAssignableFrom(_e.getClass()) && matchFn.test( (_E)_e)){
                updates.add((_E)_e);
            }
        });
        return updates;
    }

    public _forStmt setUpdates(List<_expr> es){
        return setUpdates( es.toArray(new _expr[0]));
    }

    public boolean isUpdate( String updateExpr ){
        int x = 0;
        String code = "for(;;"+updateExpr+"){}";
        _forStmt _fs = of(code);
        Set<_expr> updates = new HashSet<>();
        updates.addAll( _fs.listUpdates() );

        Set<_expr> tup = new HashSet<>();
        tup.addAll( listUpdates());
        return Objects.equals(updates, tup);
    }

    public boolean hasUpdate( String updateExpr ){
        String code = "for(;;"+updateExpr+"){}";
        _forStmt _fs = of(code);
        Set<_expr> updates = new HashSet<>();
        updates.addAll( _fs.listUpdates() );

        Set<_expr> tup = new HashSet<>();
        tup.addAll( listUpdates());
        updates.removeAll(tup);
        return updates.isEmpty();
    }

    public boolean hasUpdate( Predicate<_expr> predicate ){
        return listUpdates().stream().anyMatch(predicate);
    }

    public _forStmt setUpdates(_expr... es){
        NodeList<Expression> upd = new NodeList<>();
        Arrays.stream(es).forEach(e-> upd.add(e.ast()));
        this.astStmt.setInitialization(upd);
        return this;
    }

    public _body getBody(){
        return _body.of( this.astStmt.getBody() );
    }

    @Override
    public _forStmt setBody(BlockStmt body) {
        this.astStmt.setBody(body);
        return this;
    }

    public _forStmt setBody(_stmt _st){
        this.astStmt.setBody(_st.ast());
        return this;
    }

    public _forStmt setBody(_body _bd){
        this.astStmt.setBody(_bd.ast());
        return this;
    }

    public _forStmt clearBody(){
        this.astStmt.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _forStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.astStmt.getBody();
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
    public boolean is(ForStmt astNode) {
        return this.toString(Print.PRINT_NO_COMMENTS).equals(astNode.toString(Print.PRINT_NO_COMMENTS));
    }
     */

    public ForStmt ast(){
        return astStmt;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> comps = new HashMap<>();
        comps.put(_java.Feature.INIT_EXPR, astStmt.getInitialization());
        comps.put(_java.Feature.UPDATE_EXPR, astStmt.getUpdate());
        comps.put(_java.Feature.COMPARE_EXPR, astStmt.getCompare());
        comps.put(_java.Feature.BODY, astStmt.getBody());
        return comps;
    }
     */

    public String toString(){
        return this.astStmt.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _forStmt ){
            return Objects.equals( ((_forStmt)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}

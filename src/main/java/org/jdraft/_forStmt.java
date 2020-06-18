package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;

/**
 * <PRE>
 *     //simple no
 *     for(int i=0;i<100;i++)
 *         System.out.println(i);
 * </PRE>
 * <PRE>
 *     for(int i=0, j=100; i<100; i++,j--){
 *           System.out.println(i);
 *     }
 * </PRE>
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
            (_forStmt a, List<_expr> _e) -> a.setUpdate(_e), PARSER, s-> _expr.of(s))
            .setOrdered(true);/** the order of the updates MAY matter */

    public static _feature._one<_forStmt, _variablesExpr> INIT = new _feature._one<>(_forStmt.class, _variablesExpr.class,
          _feature._id.INIT,
          a -> {
            List<Expression> es = a.node().getInitialization();
            if( es.isEmpty() ){
                return null;
            }
            return _variablesExpr.of( (VariableDeclarationExpr)es.get(0) );
          },
          (_forStmt a, _variablesExpr _e) -> a.setInit(_e), PARSER);

    public static _feature._features<_forStmt> FEATURES = _feature._features.of(_forStmt.class,  PARSER, INIT, COMPARE, UPDATES, BODY);

    private ForStmt node;

    public _forStmt(ForStmt rs){
        this.node = rs;
    }

    public _feature._features<_forStmt> features(){
        return FEATURES;
    }

    @Override
    public _forStmt copy() {
        return new _forStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _forStmt replace(ForStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean hasInit(){
        return !this.node.getInitialization().isEmpty();
    }

    /**
     * i.e.
     * <PRE>
     * hasInit("int i=0");
     * </PRE>
     * @param varDeclaration
     * @return
     */
    public boolean hasInit(String varDeclaration){
        if (hasInit()) {
            return hasInit( _variable.of(varDeclaration));
        }
        return false;
    }

    /**
     *
     * @param _v
     * @return
     */
    public boolean hasInit(_variable _v){
        if( hasInit()) {
            return getInit().has(_v);
        }
        return false;
    }

    /**
     * Checks if this forStmt has at least one initializer variable that is of this type (i.e. int.class)
     * <PRE>
     *     _forStmt _fs = _forStmt.of("int i=0;i<100;i++){}");
     *     assertTrue( _fs.hasInit(int.class));
     *     assertFalse( _fs.hasInit(long.class));
     * </PRE>
     * @param clazz
     * @return
     */
    public boolean hasInit(Class clazz){
        if( hasInit()){
            return getInit().has(v-> v.isType(clazz));
        }
        return false;
    }

    /**
     *
     * @param _matchFn
     * @return
     */
    public boolean hasInit( Predicate<_variable> _matchFn){
        if( hasInit()) {
            return getInit().has(_matchFn);
        }
        return false;
    }

    public _variablesExpr getInit(){
        if( hasInit()){
            return _variablesExpr.of( (VariableDeclarationExpr) node.getInitialization().get(0) );
        }
        return null;
    }

    public boolean isInit(_variablesExpr _ve){
        if( !this.hasInit() ){
            return false;
        }
        return Objects.equals( _ve, _variablesExpr.of( (VariableDeclarationExpr)this.node.getInitialization().get(0) ) );
    }

    public boolean isInit(String...code){
        if( !this.hasInit() ){
            return code.length == 0 || code.length == 1 && code[0].trim().length() ==0;
        }
        try{
            _variablesExpr _ve = _variablesExpr.of(code);
            return Objects.equals( _ve, _variablesExpr.of( (VariableDeclarationExpr)this.node.getInitialization().get(0) ) );
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

    /**
     * i.e. setInit("int i=0")
     * NOTE: you can set muliptle variables on init, i.e.
     * <PRE>
     *     setInit("int i, j");
     * </PRE>
     * @param inits initializations for the forStmt
     * @return the modified forStmt
     */
    public _forStmt setInit(String...inits){
        String v = Text.combine(inits);
        if( v.endsWith(";")){
            v = v.substring(0, v.length()-1);
        }
        return setInit( _variablesExpr.of(v));
    }

    public _forStmt setInit(_variablesExpr _ve){
        NodeList<Expression> nle = new NodeList<>();
        nle.add( _ve.node() );
        this.node.setInitialization(nle);
        return this;
    }

    public boolean hasCompare(){
        return !(this.getCompare() == null);
    }

    public _forStmt removeCompare(){
        this.node.removeCompare();
        return this;
    }

    public _expr getCompare(){
        if( this.node.getCompare().isPresent()) {
            return _expr.of(this.node.getCompare().get());
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

    public <_I extends _expr> boolean isCompare(Class<_I> expressionImplementation, Predicate<_I> matchFn){
        _expr _e = getCompare();
        if( expressionImplementation.isAssignableFrom(_e.getClass())){
            return matchFn.test( (_I)_e);
        }
        return false;
    }

    public boolean isCompare(BinaryExpr.Operator bo){
        if(this.node.getCompare().isPresent() && this.node.getCompare().get() instanceof BinaryExpr){
            return ((BinaryExpr) this.node.getCompare().get()).getOperator() == bo;
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
        this.node.setCompare(Expr.of(str));
        return this;
    }

    public _forStmt setCompare(_expr e){
        this.node.setCompare(e.node());
        return this;
    }

    public _forStmt setCompare(Expression e){
        this.node.setCompare(e);
        return this;
    }

    public boolean hasUpdate(){
        return !this.node.getUpdate().isEmpty();
    }

    public _forStmt removeUpdate(Predicate<_expr> _ex){
        this.node.getUpdate().removeIf(u-> _ex.test( _expr.of(u)));
        return this;
    }

    public _forStmt removeUpdate(_expr _ex){
        this.node.getUpdate().removeIf(u-> Objects.equals(u, _ex.node()) );
        return this;
    }

    public _forStmt forUpdates(Consumer<_expr> consumer){
        listUpdates().forEach(consumer);
        return this;
    }

    public _forStmt addUpdates( _expr... _es){
        Arrays.stream(_es).forEach(_e -> this.node.getUpdate().add(_e.node()));
        return this;
    }

    public List<_expr> listUpdates(){
        List<_expr>update = new ArrayList<>();
        this.node.getUpdate().forEach(i -> update.add(_expr.of(i)));
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
        this.node.getUpdate().forEach(i -> {
            _expr _e = _expr.of(i);
            if( expressionClass.isAssignableFrom(_e.getClass()) && matchFn.test( (_E)_e)){
                updates.add((_E)_e);
            }
        });
        return updates;
    }

    public _forStmt setUpdate(String...str ){

        _args _as = _args.of(str);
        return setUpdate( _as.list() );
    }

    public _forStmt setUpdate(List<_expr> es){
        return setUpdate( es.toArray(new _expr[0]));
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

    public _forStmt setUpdate(_expr... es){
        NodeList<Expression> upd = new NodeList<>();
        Arrays.stream(es).forEach(e-> upd.add(e.node()));
        this.node.setUpdate(upd);
        return this;
    }

    public _body getBody(){
        return _body.of( this.node.getBody() );
    }

    @Override
    public _forStmt setBody(BlockStmt body) {
        this.node.setBody(body);
        return this;
    }

    public _forStmt setBody(_stmt _st){
        this.node.setBody(_st.node());
        return this;
    }

    public _forStmt setBody(_body _bd){
        this.node.setBody(_bd.ast());
        return this;
    }

    public _forStmt clearBody(){
        this.node.setBody( new BlockStmt());
        return this;
    }

    @Override
    public _forStmt add(int startStatementIndex, Statement... statements) {
        Statement bd = this.node.getBody();
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

    public ForStmt node(){
        return node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _forStmt ){
            _forStmt _o = (_forStmt)other;
            if( !Objects.equals( getCompare(),_o.getCompare() )){
                return false;
            }
            if( !Objects.equals( getInit(), _o.getInit())){
                return false;
            }
            if( !Objects.equals( getBody(), _o.getBody())){
                return false;
            }
            //doesnt matter the order of updates
            Set<_expr> _tus = new HashSet<>();
            _tus.addAll( listUpdates() );
            Set<_expr> _ous = new HashSet<>();
            _ous.addAll( _o.listUpdates() );
            return Objects.equals(_tus, _ous);
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}

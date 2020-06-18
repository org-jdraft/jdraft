package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class _switchCase implements _tree._node<SwitchEntry, _switchCase> {

    public static final Function<String, _switchCase> PARSER = s-> _switchCase.of(s);

    public static _switchCase of(){
        return new _switchCase(new SwitchEntry());
    }

    /**
     * i.e.
     * _switchEntry.of("case 1: return 1;"); //single case entry
     * _switchEntry.of("case 'd':"); //"Constant Only" switchEntry
     * _switchEntry.of("default: return 1;"); //default entry
     *
     * @param switchEntry
     * @return
     */
    public static _switchCase of(String...switchEntry){
        SwitchStmt ss = Ast.switchStmt("switch(key){"+ Text.combine(switchEntry)+"}");
        return of(ss.getEntry(0));
    }

    public static _switchCase of(SwitchEntry se){
        return new _switchCase(se);
    }

    public static _feature._many<_switchCase, _expr> CASE_EXPRESSIONS = new _feature._many<>(_switchCase.class, _expr.class,
            _feature._id.CASE_EXPRESSIONS,
            _feature._id.CASE_EXPRESSION,
            a -> a.listCaseExpressions(),
            (_switchCase p, List<_expr> _es) -> p.setCaseExpressions(_es), PARSER, s->_expr.of(s))
            .setOrdered(false);

    public static _feature._one<_switchCase, SwitchEntry.Type> BODY_TYPE = new _feature._one<>(_switchCase.class, SwitchEntry.Type.class,
            _feature._id.BODY_TYPE,
            a -> a.getBodyType(),
            (_switchCase p, SwitchEntry.Type t) -> p.node().setType(t), PARSER);

    public static _feature._many<_switchCase, _stmt> STATEMENTS = new _feature._many<>(_switchCase.class, _stmt.class,
            _feature._id.STATEMENTS,
            _feature._id.STATEMENT,
            a -> a.listStatements(),
            (_switchCase p, List<_stmt> _st) -> p.setStatements(_st), PARSER, s->_stmt.of(s)).setOrdered(true);

    public static _feature._features<_switchCase> FEATURES = _feature._features.of(_switchCase.class,  PARSER, CASE_EXPRESSIONS, BODY_TYPE, STATEMENTS );

    public SwitchEntry node;

    public _switchCase(SwitchEntry se){
        this.node = se;
    }

    public _feature._features<_switchCase> features(){
        return FEATURES;
    }

    public _switchCase setCaseExpressions(List<_expr> _es){
        this.node.getLabels().clear();
        _es.forEach( ce -> this.node.getLabels().add( ce.node()));
        return this;
    }

    public List<_expr> listCaseExpressions(){
        List<_expr> caseExpressions = new ArrayList<>();
        this.node.getLabels().forEach(ce -> caseExpressions.add( _expr.of(ce)));
        return caseExpressions;
    }

    public boolean hasCaseConstant(_expr _e){
        return hasCaseConstant(_e.node());
    }

    public boolean hasCaseConstant(Expression caseLabel){
        return this.node.getLabels().stream().anyMatch(c-> c.equals(caseLabel) );
    }

    /**
     * A "constant only" switch entry (no logic tied to this path)
     * i.e.<PRE>
     * case 1:
     * case 'a':
     * case "String" :
     * </PRE>
     * @return
     */
    public boolean isCaseConstantOnly(){
        return this.node.getStatements().isEmpty();
    }

    public SwitchEntry.Type getBodyType(){
        return this.node().getType();
    }

    public _switchCase setBodyType(SwitchEntry.Type bodyType ){
        this.node().setType(bodyType);
        return this;
    }

    public boolean hasCaseConstant(int i){
        return hasCaseConstant( new IntegerLiteralExpr(i) );
    }

    public boolean hasCaseConstant(long l){
        return hasCaseConstant( new LongLiteralExpr(l) );
    }

    public boolean hasCaseConstant(char c){
        return hasCaseConstant( new CharLiteralExpr(c) );
    }

    public boolean hasCaseConstant(float f){
        return hasCaseConstant(new DoubleLiteralExpr(f));
    }

    public boolean hasCaseConstant(double d){
        return hasCaseConstant(new DoubleLiteralExpr(d));
    }

    public _switchCase setCaseConstant(int i){
        return setCaseConstant( new IntegerLiteralExpr(i) );
    }

    public _switchCase setCaseConstant(long l){
        return setCaseConstant( new LongLiteralExpr(l) );
    }

    public _switchCase setCaseConstant(char c){
        return setCaseConstant( new CharLiteralExpr(c) );
    }

    public _switchCase setCaseConstant(float f){
        return setCaseConstant(new DoubleLiteralExpr(f));
    }

    public _switchCase setCaseConstant(double d){
        return setCaseConstant(new DoubleLiteralExpr(d));
    }

    public _switchCase setCaseConstant(Expression caseLabel){
        NodeList<Expression>labels = new NodeList<>();
        labels.add(caseLabel);
        this.node.setLabels(labels);
        return this;
    }

    /**
     * i.e.
     * case 1 : throw new RuntimeException();
     *
     * default : IOException ioe = new IOException();
     *     throw ioe;
     *
     * @return
     */
    public boolean isThrow(){
        SwitchEntry se = node();
        if( se.getStatements().size() > 0 ) {
            if( se.getStatement(se.getStatements().size() - 1) instanceof ThrowStmt ){
                return true;
            }
            if( se.getStatement(se.getStatements().size() - 1) instanceof BlockStmt){
                BlockStmt bs = (BlockStmt)se.getStatement(se.getStatements().size() - 1);
                if( (bs.getStatements().size() > 0) && (bs.getStatement(bs.getStatements().size()) instanceof ThrowStmt ) ){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isThrow(Class<? extends Throwable> clazz){
        return isThrow( Types.of(clazz));
    }

    public boolean isThrow(_typeRef _tr ){
        return isThrow(_tr.node());
    }

    public boolean isThrow(Type thrownType){
        SwitchEntry se = node();
        if( se.getStatements().size() > 0 ) {
            if( se.getStatement(se.getStatements().size() - 1) instanceof ThrowStmt ){
                ThrowStmt ts = (ThrowStmt)se.getStatement(se.getStatements().size() - 1);
                if( ts.getExpression() instanceof ObjectCreationExpr ){
                    ObjectCreationExpr oce = (ObjectCreationExpr)ts.getExpression();
                    return Types.equal(oce.getType(), thrownType);
                }
                return false;
            }
            if( se.getStatement(se.getStatements().size() - 1) instanceof BlockStmt){
                BlockStmt bs = (BlockStmt)se.getStatement(se.getStatements().size() - 1);
                if( (bs.getStatements().size() > 0) && (bs.getStatement(bs.getStatements().size()) instanceof ThrowStmt ) ){
                    ThrowStmt ts = (ThrowStmt)se.getStatement(se.getStatements().size() - 1);
                    if( ts.getExpression() instanceof ObjectCreationExpr ){
                        ObjectCreationExpr oce = (ObjectCreationExpr)ts.getExpression();
                        return Types.equal(oce.getType(), thrownType);
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public boolean hasStatements(){
        return this.node().getStatements().isNonEmpty();
    }

    /**
     * i.e.
     * case 1:
     *     System.out.println(1);
     *     System.out.println(2);
     *     break;
     * @return
     */
    public boolean isStatementGroup(){
        return this.node.getType().equals(SwitchEntry.Type.STATEMENT_GROUP);
    }

    /**
     * case 1: { //a block statement
     *     System.out.println( 1 );
     *     System.out.println( 2 );
     * }
     *
     * @return
     */
    public boolean isBlock(){
        return this.node.getType().equals(SwitchEntry.Type.BLOCK);
    }

    /**
     * used for Java 12 SwitchExpression style stuff:
     * https://docs.oracle.com/en/java/javase/13/language/switch-expressions.html
     *
     * <CODE>
     *     switch(dayOfWeek){
     *          case MONDAY  -> 1;
     *          case TUESDAY -> 2;
     *     }
     * </CODE>
     * @return
     */
    public boolean isExpression(){
        return this.node.getType().equals(SwitchEntry.Type.EXPRESSION);
    }

    public Statement getStatement(int index){
        return this.node.getStatement(index);
    }


    public List<_stmt> listStatements(){
        return this.node.getStatements().stream().map(s-> _stmt.of(s)).collect(Collectors.toList());
    }

    public List<Statement> listAstStatements(){
        return this.node.getStatements();
    }

    private _switchCase setStatements(LambdaExpr le){
        if( le.getExpressionBody().isPresent()){ //an Expression Lambda
            NodeList<Statement>sts = new NodeList<>();
            sts.add( new ExpressionStmt(le.getExpressionBody().get()));
            this.node.setStatements(sts);
            return this;
        }
        Statement st = le.getBody();
        if( !(st instanceof BlockStmt )){
            NodeList<Statement>sts = new NodeList<>();
            sts.add( st);
            this.node.setStatements(sts);
            return this;
        }
        //if its a blockStmt, just pull
        BlockStmt bs = st.asBlockStmt();
        this.node.setStatements( bs.getStatements() );
        return this;
    }

    /**
     * Lambda way of setting the statements
     * @param c a lambdaExpression containing the
     * @return
     */
    public _switchCase setStatements(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2] );
        return setStatements(le);
    }

    public <A extends Object> _switchCase setStatements (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCase setStatements (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public  <A extends Object, B extends Object, C extends Object> _switchCase setStatements(BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchCase setStatements (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchCase setStatements (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCase setStatements(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchCase setStatements(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchCase setStatements(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    /**
     * Sets ALL of the statements inside this SwitchEntry
     * @param sts
     * @return
     */
    public _switchCase setStatements(String...sts){
        BlockStmt bs = Ast.blockStmt(sts);
        bs.getStatements().forEach(s -> this.node.addStatement(s));
        return this;
    }

    public _switchCase setStatements(List<_stmt> sts){
        return setStatements( sts.toArray(new _stmt[0]));
    }

    public _switchCase setStatements(_stmt...st){
        NodeList<Statement>stmts = new NodeList<>();
        Arrays.stream(st).forEach(s -> stmts.add(s.node()));
        this.node.setStatements(stmts);
        return this;
    }

    /**
     * Sets ALL of the statements inside this SwitchEntry
     * @param st
     * @return
     */
    public _switchCase setStatements(Statement...st){
        NodeList<Statement>stmts = new NodeList<>();
        Arrays.stream(st).forEach(s -> stmts.add(s));
        this.node.setStatements(stmts);
        return this;
    }

    /**
     * Lambda way of setting the statements
     * @param c a lambdaExpression containing the
     * @return
     */
    public _switchCase addStatements(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2] );
        return addStatements(le);
    }

    public <A extends Object> _switchCase addStatements (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCase addStatements (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public  <A extends Object, B extends Object, C extends Object> _switchCase addStatements(BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchCase addStatements (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchCase addStatements (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCase addStatements(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchCase addStatements(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchCase addStatements(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    private _switchCase addStatements(LambdaExpr le){
        if( le.getExpressionBody().isPresent()){ //an Expression Lambda
            NodeList<Statement>sts = new NodeList<>();
            sts.add( new ExpressionStmt(le.getExpressionBody().get()));
            this.node.setStatements(sts);
            return this;
        }
        Statement st = le.getBody();
        if( !(st instanceof BlockStmt )){
            //NodeList<Statement>sts = new NodeList<>();
            //sts.add( st);
            this.node.addStatement(st);
            return this;
        }
        //if its a blockStmt, just pull
        BlockStmt bs = st.asBlockStmt();
        bs.getStatements().forEach( s -> this.node.addStatement(s));
        return this;
    }

    public _switchCase addStatements(String...st){
        BlockStmt bs = Ast.blockStmt(st);
        bs.getStatements().forEach(s -> this.node.addStatement(s));
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchCase addStatements(int index, String...st){
        BlockStmt bs = Ast.blockStmt(st);
        //bs.getStatements().forEach(s -> this.switchEntry.addStatement(s));
        for(int i=0;i<bs.getStatements().size();i++){
            this.node.addStatement(index + i, bs.getStatement( i) );
        }
        return this;
    }

    public _switchCase addStatements(Statement...st){
        for(int i=0; i<st.length;i++) {
            this.node.addStatement(st[i]);
        }
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchCase addStatements(int index, Statement...st){
        for(int i=0;i<st.length;i++){
            this.node.addStatement(index + i, st[i] );
        }
        return this;
    }

    /**
     * Default switchEntries have no case labels
     * @return
     */
    public boolean isDefault(){
        return this.node.getLabels().isEmpty();
    }

    @Override
    public _switchCase copy() {
        return new _switchCase( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _switchCase replace(SwitchEntry replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(SwitchEntry astNode) {
        return this.node.equals(astNode);
    }

    @Override
    public SwitchEntry node() {
        return this.node;
    }

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _switchCase){
            return Objects.equals( ((_switchCase)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}

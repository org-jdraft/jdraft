package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class _switchEntry implements _java._compound<SwitchEntry, _switchEntry> {

    public static _switchEntry of(){
        return new _switchEntry(new SwitchEntry());
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
    public static _switchEntry of(String...switchEntry){
        SwitchStmt ss = Ast.switchStmt("switch(key){"+ Text.combine(switchEntry)+"}");
        return of(ss.getEntry(0));
    }

    public static _switchEntry of(SwitchEntry se){
        return new _switchEntry(se);
    }


    public SwitchEntry switchEntry;

    public _switchEntry(SwitchEntry se){
        this.switchEntry = se;
    }

    public boolean isCaseConstant(Expression caseLabel){
        return this.switchEntry.getLabels().stream().anyMatch( c-> c.equals(caseLabel) );
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
        return this.switchEntry.getStatements().isEmpty();
    }

    public boolean isCaseConstant(int i){
        return isCaseConstant( new IntegerLiteralExpr(i) );
    }

    public boolean isCaseConstant(long l){
        return isCaseConstant( new LongLiteralExpr(l) );
    }

    public boolean isCaseConstant(char c){
        return isCaseConstant( new CharLiteralExpr(c) );
    }

    public boolean isCaseConstant(float f){
        return isCaseConstant(new DoubleLiteralExpr(f));
    }

    public boolean isCaseConstant(double d){
        return isCaseConstant(new DoubleLiteralExpr(d));
    }

    public _switchEntry setCaseConstant(int i){
        return setCaseConstant( new IntegerLiteralExpr(i) );
    }

    public _switchEntry setCaseConstant(long l){
        return setCaseConstant( new LongLiteralExpr(l) );
    }

    public _switchEntry setCaseConstant(char c){
        return setCaseConstant( new CharLiteralExpr(c) );
    }

    public _switchEntry setCaseConstant(float f){
        return setCaseConstant(new DoubleLiteralExpr(f));
    }

    public _switchEntry setCaseConstant(double d){
        return setCaseConstant(new DoubleLiteralExpr(d));
    }

    public _switchEntry setCaseConstant(Expression caseLabel){
        NodeList<Expression>labels = new NodeList<>();
        labels.add(caseLabel);
        this.switchEntry.setLabels(labels);
        return this;
    }

    /**
     * i.e.
     * case 1 : throw new RuntimeException();
     *
     * @return
     */
    public boolean isThrows(){
        return this.switchEntry.getType().equals(SwitchEntry.Type.THROWS_STATEMENT);
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
        return this.switchEntry.getType().equals(SwitchEntry.Type.STATEMENT_GROUP);
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
        return this.switchEntry.getType().equals(SwitchEntry.Type.BLOCK);
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
        return this.switchEntry.getType().equals(SwitchEntry.Type.EXPRESSION);
    }

    public Statement getStatement(int index){
        return this.switchEntry.getStatement(index);
    }

    public List<Statement> listStatements(){
        return this.switchEntry.getStatements();
    }

    private _switchEntry setStatements(LambdaExpr le){
        if( le.getExpressionBody().isPresent()){ //an Expression Lambda
            NodeList<Statement>sts = new NodeList<>();
            sts.add( new ExpressionStmt(le.getExpressionBody().get()));
            this.switchEntry.setStatements(sts);
            return this;
        }
        Statement st = le.getBody();
        if( !(st instanceof BlockStmt )){
            NodeList<Statement>sts = new NodeList<>();
            sts.add( st);
            this.switchEntry.setStatements(sts);
            return this;
        }
        //if its a blockStmt, just pull
        BlockStmt bs = st.asBlockStmt();
        this.switchEntry.setStatements( bs.getStatements() );
        return this;
    }

    /**
     * Lambda way of setting the statements
     * @param c a lambdaExpression containing the
     * @return
     */
    public _switchEntry setStatements(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2] );
        return setStatements(le);
    }

    public <A extends Object> _switchEntry setStatements (Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchEntry setStatements (Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public  <A extends Object, B extends Object, C extends Object> _switchEntry setStatements(BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchEntry setStatements (Ex.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchEntry setStatements (Ex.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchEntry setStatements(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchEntry setStatements(Ex.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchEntry setStatements(Ex.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.astLambda);
    }

    /**
     * Sets ALL of the statements inside this SwitchEntry
     * @param sts
     * @return
     */
    public _switchEntry setStatements(String...sts){
        BlockStmt bs = Ast.blockStmt(sts);
        bs.getStatements().forEach(s -> this.switchEntry.addStatement(s));
        return this;
    }

    /**
     * Sets ALL of the statements inside this SwitchEntry
     * @param st
     * @return
     */
    public _switchEntry setStatements(Statement...st){
        NodeList<Statement>stmts = new NodeList<>();
        Arrays.stream(st).forEach(s -> stmts.add(s));
        this.switchEntry.setStatements(stmts);
        return this;
    }

    /**
     * Lambda way of setting the statements
     * @param c a lambdaExpression containing the
     * @return
     */
    public _switchEntry addStatements(Ex.Command c){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2] );
        return addStatements(le);
    }

    public <A extends Object> _switchEntry addStatements (Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchEntry addStatements (Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public  <A extends Object, B extends Object, C extends Object> _switchEntry addStatements(BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchEntry addStatements (Ex.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchEntry addStatements (Ex.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchEntry addStatements(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchEntry addStatements(Ex.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchEntry addStatements(Ex.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.astLambda);
    }

    private _switchEntry addStatements(LambdaExpr le){
        if( le.getExpressionBody().isPresent()){ //an Expression Lambda
            NodeList<Statement>sts = new NodeList<>();
            sts.add( new ExpressionStmt(le.getExpressionBody().get()));
            this.switchEntry.setStatements(sts);
            return this;
        }
        Statement st = le.getBody();
        if( !(st instanceof BlockStmt )){
            //NodeList<Statement>sts = new NodeList<>();
            //sts.add( st);
            this.switchEntry.addStatement(st);
            return this;
        }
        //if its a blockStmt, just pull
        BlockStmt bs = st.asBlockStmt();
        bs.getStatements().forEach( s -> this.switchEntry.addStatement(s));
        return this;
    }

    public _switchEntry addStatements(String...st){
        BlockStmt bs = Ast.blockStmt(st);
        bs.getStatements().forEach(s -> this.switchEntry.addStatement(s));
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchEntry addStatements(int index, String...st){
        BlockStmt bs = Ast.blockStmt(st);
        //bs.getStatements().forEach(s -> this.switchEntry.addStatement(s));
        for(int i=0;i<bs.getStatements().size();i++){
            this.switchEntry.addStatement(index + i, bs.getStatement( i) );
        }
        return this;
    }

    public _switchEntry addStatements(Statement...st){
        for(int i=0; i<st.length;i++) {
            this.switchEntry.addStatement(st[i]);
        }
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchEntry addStatements(int index, Statement...st){
        for(int i=0;i<st.length;i++){
            this.switchEntry.addStatement(index + i, st[i] );
        }
        return this;
    }

    /**
     * Default switchEntries have no case labels
     * @return
     */
    public boolean isDefault(){
        return this.switchEntry.getLabels().isEmpty();
    }

    @Override
    public _switchEntry copy() {
        return new _switchEntry( this.switchEntry.clone());
    }

    @Override
    public boolean is(String... switchEntry ) {
        try {
            return is(Ast.switchEntry(switchEntry));
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean is(SwitchEntry astNode) {
        return this.switchEntry.equals(astNode);
    }

    @Override
    public SwitchEntry ast() {
        return this.switchEntry;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> cs = new HashMap<>();
        cs.put(_java.Component.STATEMENTS, this.switchEntry.getStatements());
        cs.put(_java.Component.SWITCH_BODY_TYPE, this.switchEntry.getType());
        cs.put(_java.Component.SWITCH_LABELS, this.switchEntry.getLabels());
        return cs;
    }

    public String toString(){
        return this.switchEntry.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _switchEntry ){
            return Objects.equals( ((_switchEntry)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}

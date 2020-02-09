package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;

/**
 * A Java 12+ switch expression
 * using yield (Java 12)
 * <CODE>
 * private static int getValueViaYield(String mode) {
 *         int result = switch (mode) {
 *             case "a", "b": //note multi-labels (not case "a" : case "b")
 *                 yield 1;   //note keyword yield
 *             case "c":
 *                 yield 2;
 *             case "d", "e", "f":
 *                 yield 3;
 *             default:
 *                 yield -1;
 *         };
 *         return result;
 *     }
 * </CODE>
 *
 * the SwitchEnties
 * <CODE>
 *     case 1: case 2: return "A"; //this is a SwitchEntry
 *     default: return "C"; //this is a SwitchEntry
 * </CODE>
 *
 * @see _caseGroup a grouping of multiple case statements
 *
 */
public class _switchExpression implements _expression<SwitchExpr, _switchExpression>,
        _java._compound<SwitchExpr, _switchExpression>, _switch {

    public static _switchExpression of(){
        return new _switchExpression(new SwitchExpr());
    }

    public static _switchExpression of(SwitchExpr ss){
        return new _switchExpression(ss);
    }

    public static _switchExpression of(String...code){
        try{
            return of( Ast.switchEx(code) );
        }catch(Exception e){
            throw new _jdraftException("invalid representation of switch expression"+System.lineSeparator()+ Text.combine(code), e);
        }
    }

    public static _switchExpression of(LambdaExpr le ){
        Optional<SwitchExpr> ss = le.findFirst(SwitchExpr.class);
        if( ss.isPresent() ){
            return of(ss.get());
        }
        throw new _jdraftException("No switch expression in lambdaExpr"+System.lineSeparator()+ le);
    }

    public static _switchExpression of(Ex.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object> _switchExpression of (Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switchExpression of (Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object> _switchExpression of (BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchExpression of (Ex.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchExpression of (Ex.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switchExpression of(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object> _switchExpression of(Ex.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _switchExpression of(Ex.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public SwitchExpr switchExpr;

    public _switchExpression(SwitchExpr stt){
        this.switchExpr = stt;
    }

    @Override
    public _switchExpression copy() {
        return new _switchExpression( this.switchExpr.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Ex.switchEx(stringRep));
        }catch(Exception e){ //string could be invalid
            return false;
        }
    }

    @Override
    public boolean is(SwitchExpr astNode) {
        return this.switchExpr.equals(astNode);
    }

    @Override
    public SwitchExpr ast(){
        return switchExpr;
    }

    public _switchEntry getCase(int i){
        return getSwitchEntry(se-> se.isCaseConstant(i) );
    }

    /**
     * List the _caseGroups that exist in this _switch
     * @return
     */
    public List<_caseGroup> listCaseGroups(){
        List<_caseGroup> cgs = new ArrayList<>();
        _caseGroup _cg = new _caseGroup(this.switchExpr);
        List<SwitchEntry> ses = this.switchExpr.getEntries();
        for(int i=0; i< ses.size(); i++){
            if( ses.get(i).getStatements().isEmpty() ){
                _cg.addSwitchEntry(ses.get(i));
            } else{
                _cg.addSwitchEntry(ses.get(i));
                cgs.add(_cg);
                _cg = new _caseGroup(this.switchExpr);
            }
        }
        return cgs;
    }

    /**
     *
     * @param _e
     * @return
     */
    public _switchExpression setSwitchSelector(_expression _e){
        this.switchExpr.setSelector(_e.ast());
        return this;
    }

    /**
     * Sets te switch selector
     * i.e.
     * <PRE><CODE>
     * switch( switchSelector ){
     *    //...
     * }
     * </CODE></PRE>
     * @param switchSelector
     * @return
     */
    public _switchExpression setSwitchSelector(Expression switchSelector){
        this.switchExpr.setSelector(switchSelector);
        return this;
    }

    /**
     *
     * @param switchSelector
     * @return
     */
    public _switchExpression setSwitchSelector(String... switchSelector){
        this.switchExpr.setSelector(Ex.of(switchSelector));
        return this;
    }

    /**
     * Does this switchStatment have a default
     * @return
     */
    public boolean hasDefault(){
        Optional<SwitchEntry> ose = this.switchExpr.getEntries().stream().filter(se-> se.getLabels().isEmpty()).findFirst();
        return ose.isPresent();
    }

    /**
     * Returns the default switchEntry if there is one, or else return null;
     * @return
     */
    public _switchEntry getDefault(){
        Optional<SwitchEntry> ose = this.switchExpr.getEntries().stream().filter(se-> se.getLabels().isEmpty()).findFirst();
        if( ose.isPresent() ){
            return _switchEntry.of(ose.get());
        }
        return null;
    }

    /**
     * return the _expression wrapper on the Switch Selector
     * @return
     */
    public _expression getSwitchSelector(){
        return _expression.of(this.switchExpr.getSelector());
    }

    public int countSwitchEntries(){
        return this.switchExpr.getEntries().size();
    }

    public _switchEntry getSwitchEntry(int index){
        return new _switchEntry(this.switchExpr.getEntries().get(index));
    }

    public _switchEntry getSwitchEntry( Predicate<_switchEntry> matchFn){
        Optional<_switchEntry> ose = this.listSwitchEntries().stream().filter( matchFn ).findFirst();
        if( ose.isPresent() ){
            return ose.get();
        }
        return null;
    }

    public _switchExpression setDefault(Statement... statements){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements(statements);
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(statements);
        this.switchExpr.getEntries().add(newDef.switchEntry);
        return this;
    }

    public _switchExpression setDefault(Expression ex){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements( new ExpressionStmt(ex));
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(new ExpressionStmt(ex));
        this.switchExpr.getEntries().add(newDef.switchEntry);
        return this;
    }

    /**
     * Sets the code to be done for the default: part of the switch statement
     * (if the default does not exist, create one, if one does, then modify it)
     * @return
     */
    public _switchExpression setDefault(String...code){
        //the code COULD be an expression or a statement or a group of statements
        try {
            Expression e = Ex.of(code);
            setDefault(e);
            return this;
        }catch(Exception ex){
            //ignore
        }
        try {
            BlockStmt bs = Ast.blockStmt(code);
            setDefault(bs.getStatements().toArray(new Statement[0]));
            return this;
        } catch (Exception e){

        }
        throw new _jdraftException("unable to set default with "+ System.lineSeparator()+Text.combine(code));
    }


    /**
     *
     * @return
     */
    public List<_switchEntry> listSwitchEntries(){
        List<_switchEntry> _ses = new ArrayList<>();
        this.switchExpr.getEntries().forEach(se -> _ses.add(new _switchEntry(se)));
        return _ses;
    }

    /**
     *
     * @return
     */
    public List<_switchEntry> listSwitchEntries(Predicate<_switchEntry> matchFn){
        List<_switchEntry> _ses = new ArrayList<>();
        this.switchExpr.getEntries().forEach(se -> {
            _switchEntry _se = new _switchEntry(se);
            if( matchFn.test(_se) ) {
                _ses.add(_se);
            }
        });
        return _ses;
    }

    /**
     * Sets the switch entries based on the SwitchEntries
     *
     * i.e.
     * <PRE><CODE>
     * _switch _s = _switch.of().setSelector("key")
     *     .setSwitchEntries("case 1: return 'a'; case 2: case 3: return 'b'; default: return 'c';");
     * </CODE></PRE>
     * @param ses
     * @return
     */
    public _switchExpression setSwitchEntries(String...ses){
        String comb = Text.combine(ses);
        SwitchExpr ss = Ast.switchEx( "switch(key){"+comb+"}");
        ss.getEntries().forEach(se -> addSwitchEntries(se));
        return this;
    }

    public _switchExpression setSwitchEntries(_switchEntry...ses){
        NodeList<SwitchEntry> nses = new NodeList<>();
        Arrays.stream(ses).forEach( se-> nses.add(se.switchEntry));
        this.switchExpr.setEntries(nses);
        return this;
    }

    /**
     * Add one or more caseGroups
     * @param caseGroups
     * @return
     */
    public _switchExpression addCaseGroups(_caseGroup...caseGroups){
        Arrays.stream(caseGroups).forEach( cg-> this.switchExpr.getEntries().addAll(cg.switchEntries));
        return this;
    }

    public _switchExpression addSwitchEntries(SwitchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.switchExpr.getEntries().add(se));
        return this;
    }

    public _switchExpression addSwitchEntries(_switchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.switchExpr.getEntries().add(se.switchEntry));
        return this;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> mc = new HashMap<>();
        mc.put(_java.Component.SWITCH_SELECTOR, this.switchExpr.getSelector());
        mc.put(_java.Component.SWITCH_ENTRIES, this.switchExpr.getEntries());
        return mc;
    }

    public String toString(){
        return this.switchExpr.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _switchExpression ){
            return Objects.equals( ((_switchExpression)other).ast(), this.ast() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
    }
}

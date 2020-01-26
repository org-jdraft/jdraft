package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;

/**
 * The selector
 * <CODE>
 * switch( getValue()){
 *     //...
 * }
 * </CODE>
 *
 * the SwitchEnties
 * <CODE>
 *     case 1: case 2: return "A"; //this is a SwitchEntry
 *     default: return "C"; //this is a SwitchEntry
 * </CODE>
 *
 * _caseGroup a grouping of multiple case statements
 * note: this is a "virtual" thing
 *
 */
public class _switch implements _statement<SwitchStmt, _switch> {

    public static _switch of(){
        return new _switch(new SwitchStmt());
    }

    public static _switch of(SwitchStmt ss){
        return new _switch(ss);
    }

    public static _switch of(String...code){
        try{
            return of( Ast.switchStmt(code) );
        }catch(Exception e){
            throw new _jdraftException("invalid representation of switch statement"+System.lineSeparator()+ Text.combine(code), e);
        }
    }

    public static _switch of(LambdaExpr le ){
        Optional<SwitchStmt> ss = le.findFirst(SwitchStmt.class);
        if( ss.isPresent() ){
            return of(ss.get());
        }
        throw new _jdraftException("No switch statement in lambdaExpr"+System.lineSeparator()+ le);
    }

    public static _switch of(Ex.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object> _switch of (Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switch of (Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object> _switch of (BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switch of (Ex.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switch of (Ex.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switch of(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object> _switch of(Ex.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _switch of(Ex.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public SwitchStmt switchStmt;

    public _switch(SwitchStmt stt){
        this.switchStmt = stt;
    }

    @Override
    public _switch copy() {
        return new _switch( this.switchStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Stmt.switchStmt(stringRep));
        }catch(Exception e){ //string could be invalid
            return false;
        }
    }

    @Override
    public boolean is(SwitchStmt astNode) {
        return this.switchStmt.equals(astNode);
    }

    @Override
    public SwitchStmt ast(){
        return switchStmt;
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
        _caseGroup _cg = new _caseGroup(this.switchStmt);
        List<SwitchEntry> ses = this.switchStmt.getEntries();
        for(int i=0; i< ses.size(); i++){
            if( ses.get(i).getStatements().isEmpty() ){
                _cg.addSwitchEntry(ses.get(i));
            } else{
                //System.out.println( "ADDING "+ ses.get(i));
                _cg.addSwitchEntry(ses.get(i));
                cgs.add(_cg);
                _cg = new _caseGroup(this.switchStmt);
            }
        }
        return cgs;
    }

    /**
     *
     * @param _e
     * @return
     */
    public _switch setSwitchSelector( _expression _e){
        this.switchStmt.setSelector(_e.ast());
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
    public _switch setSwitchSelector( Expression switchSelector){
        this.switchStmt.setSelector(switchSelector);
        return this;
    }

    /**
     *
     * @param switchSelector
     * @return
     */
    public _switch setSwitchSelector( String... switchSelector){
        this.switchStmt.setSelector(Ex.of(switchSelector));
        return this;
    }

    /**
     * Does this switchStatment have a default
     * @return
     */
    public boolean hasDefault(){
        Optional<SwitchEntry> ose = this.switchStmt.getEntries().stream().filter( se-> se.getLabels().isEmpty()).findFirst();
        return ose.isPresent();
    }

    /**
     * Returns the default switchEntry if there is one, or else return null;
     * @return
     */
    public _switchEntry getDefault(){
        Optional<SwitchEntry> ose = this.switchStmt.getEntries().stream().filter( se-> se.getLabels().isEmpty()).findFirst();
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
        return _expression.of(this.switchStmt.getSelector());
    }

    public int countSwitchEntries(){
        return this.switchStmt.getEntries().size();
    }

    public _switchEntry getSwitchEntry(int index){
        return new _switchEntry(this.switchStmt.getEntries().get(index));
    }

    public _switchEntry getSwitchEntry( Predicate<_switchEntry> matchFn){
        Optional<_switchEntry> ose = this.listSwitchEntries().stream().filter( matchFn ).findFirst();
        if( ose.isPresent() ){
            return ose.get();
        }
        return null;
    }

    public _switch setDefault(Statement... statements){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements(statements);
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(statements);
        this.switchStmt.getEntries().add(newDef.switchEntry);
        return this;
    }

    public _switch setDefault(Expression ex){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements( new ExpressionStmt(ex));
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(new ExpressionStmt(ex));
        this.switchStmt.getEntries().add(newDef.switchEntry);
        return this;
    }

    /**
     * Sets the code to be done for the default: part of the switch statement
     * (if the default does not exist, create one, if one does, then modify it)
     * @return
     */
    public _switch setDefault(String...code){
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
        this.switchStmt.getEntries().forEach(se -> _ses.add(new _switchEntry(se)));
        return _ses;
    }

    /**
     *
     * @return
     */
    public List<_switchEntry> listSwitchEntries(Predicate<_switchEntry> matchFn){
        List<_switchEntry> _ses = new ArrayList<>();
        this.switchStmt.getEntries().forEach(se -> {
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
    public _switch setSwitchEntries(String...ses){
        String comb = Text.combine(ses);
        SwitchStmt ss = Ast.switchStmt( "switch(key){"+comb+"}");
        ss.getEntries().forEach(se -> addSwitchEntries(se));
        return this;
    }

    public _switch setSwitchEntries(_switchEntry...ses){
        NodeList<SwitchEntry> nses = new NodeList<>();
        Arrays.stream(ses).forEach( se-> nses.add(se.switchEntry));
        this.switchStmt.setEntries(nses);
        return this;
    }

    /**
     * Add one or more caseGroups
     * @param caseGroups
     * @return
     */
    public _switch addCaseGroups(_caseGroup...caseGroups){
        Arrays.stream(caseGroups).forEach( cg-> this.switchStmt.getEntries().addAll(cg.switchEntries));
        return this;
    }

    public _switch addSwitchEntries(SwitchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.switchStmt.getEntries().add(se));
        return this;
    }

    public _switch addSwitchEntries(_switchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.switchStmt.getEntries().add(se.switchEntry));
        return this;
    }

    @Override
    public Map<_java.Component, Object> components() {
        Map<_java.Component, Object> mc = new HashMap<>();
        mc.put(_java.Component.SWITCH_SELECTOR, this.switchStmt.getSelector());
        mc.put(_java.Component.SWITCH_ENTRIES, this.switchStmt.getEntries());
        return mc;
    }

    public String toString(){
        return this.switchStmt.toString();
    }
}

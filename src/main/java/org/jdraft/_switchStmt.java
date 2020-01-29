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
 * the SwitchEntries
 * <CODE>
 *     case 1: case 2: return "A"; //this is a SwitchEntry
 *     default: return "C"; //this is a SwitchEntry
 * </CODE>
 *
 * _caseGroup a grouping of multiple case statements
 * note: this is a "virtual" thing
 *
 */
public class _switchStmt implements _statement<SwitchStmt, _switchStmt>, _switch {

    public static _switchStmt of(){
        return new _switchStmt(new SwitchStmt());
    }

    public static _switchStmt of(SwitchStmt ss){
        return new _switchStmt(ss);
    }

    public static _switchStmt of(String...code){
        try{
            return of( Ast.switchStmt(code) );
        }catch(Exception e){
            throw new _jdraftException("invalid representation of switch statement"+System.lineSeparator()+ Text.combine(code), e);
        }
    }

    public static _switchStmt of(LambdaExpr le ){
        Optional<SwitchStmt> ss = le.findFirst(SwitchStmt.class);
        if( ss.isPresent() ){
            return of(ss.get());
        }
        throw new _jdraftException("No switch statement in lambdaExpr"+System.lineSeparator()+ le);
    }

    public static _switchStmt of(Ex.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object> _switchStmt of (Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switchStmt of (Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object> _switchStmt of (BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt of (Ex.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt of (Ex.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switchStmt of(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object> _switchStmt of(Ex.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt of(Ex.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public SwitchStmt switchStmt;

    /**
     * How shall we store N to 1 labels in the switch statement
     * (can we specify multiple case labels for each switch? (a Java 12 + language feature))
     * i.e.
     *
     * case 1, 2, 3, 4, 5 -> "weekend"; //multiLabelSwitch = TRUE
     *
     * OR must we provide a new case for each case expression?
     *
     * case 1: case 2: case 3: case 4: case 5: return "weekend"; //multiLabelSwitch = FALSE
     */
    public boolean forceMultiLabelSwitchEntry = false;

    public _switchStmt(SwitchStmt stt){
        this.switchStmt = stt;
    }

    @Override
    public _switchStmt copy() {
        return new _switchStmt( this.switchStmt.clone());
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

    public boolean equals(Object o){
        if( o instanceof _switchStmt){
            _switchStmt _o = (_switchStmt)o;
            return this.ast().equals( _o.ast());
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.ast().hashCode();
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
     * check if the current state of the SwitchStatement HAS at least ONE multiLabel switchEntry
     * (if so, it's safe to assume this is a multi-label switch)
     *
     * can we specify multiple case labels for each switch? (a Java 12 + language feature)
     * i.e.
     *
     * case 1, 2, 3, 4, 5 -> "weekend"; //multiLabelSwitch = TRUE
     *
     * OR must we provide a new case for each case expression?
     *
     * case 1: case 2: case 3: case 4: case 5: return "weekend"; //multiLabelSwitch = FALSE
     *
     * @see #forceMultiLabelSwitchEntry (this will TELL the _switchStmt to populate multi label switchesswitches
     * @return
     */
    public boolean hasMultiLabelSwitchEntry(){
        return this.switchStmt.getEntries().stream().anyMatch(se-> se.getLabels().size() > 1);
    }

    public _switchStmt map(Expression e, NodeList<Statement> nls){
        Optional<SwitchEntry> ose =
                this.switchStmt.getEntries().stream().filter(se -> se.getStatements().equals(nls)).findFirst();
        if( ose.isPresent()){
            //we found a
            SwitchEntry existingSwitchEntry = ose.get();
            //THIS DOESNT HANDLE DUPLICATES
            if( this.forceMultiLabelSwitchEntry || this.hasMultiLabelSwitchEntry() ){
                existingSwitchEntry.getLabels().add(e);
            } else {
                NodeList<Expression> label = new NodeList<>();
                label.add(e);
                SwitchEntry labelOnly = new SwitchEntry();
                labelOnly.setLabels(label);
                this.switchStmt.getEntries().addBefore(labelOnly, existingSwitchEntry);
            }
            return this;
        } //we didnt find an existing mapping to the target statementlist
        SwitchEntry nse = new SwitchEntry();
        NodeList<Expression> label = new NodeList<>();
        label.add(e);
        nse.setLabels(label);
        nse.setStatements(nls);
        this.switchStmt.getEntries().add(nse);
        return this;
    }

    public _switchStmt map(Expression e, Statement... sts) {
        NodeList<Statement> nls = new NodeList<>();
        Arrays.stream(sts).forEach(s -> nls.add(s));
        return map(e, nls);
    }

    public _switchStmt map(boolean b, _statement... _st){
        return map(_boolean.of(b), _st);
    }

    public _switchStmt map(char c, _statement... _st){
        return map(_char.of(c), _st);
    }

    public _switchStmt map(long l, _statement... _st){
        return map(_long.of(l), _st);
    }

    public _switchStmt map(String s, _statement... _st){
        return map(_string.of(s), _st);
    }

    public _switchStmt map(int i, _statement... _st){
        return map(_int.of(i), _st);
    }

    public _switchStmt map(_expression _e, _statement... _st){
        List<Statement>sts = new ArrayList<>();
        Arrays.stream(_st).forEach(_s -> sts.add( _s.ast()));
        return map(_e.ast(), sts.toArray(new Statement[0]));
    }

    public _switchStmt map(boolean b, Statement... st){
        return map(new BooleanLiteralExpr(b), st);
    }

    public _switchStmt map(char c, Statement... st){
        return map(new CharLiteralExpr( c), st);
    }

    public _switchStmt map(long l, Statement... st){
        return map(new LongLiteralExpr(l), st);
    }

    public _switchStmt map(String s, Statement... st){
        return map(new StringLiteralExpr(s), st);
    }

    public _switchStmt map(int i, Statement... st){
        return map(new IntegerLiteralExpr(i), st);
    }

    public _switchStmt map(boolean b, long l){
        return map(new BooleanLiteralExpr(b), _returnStmt.of(l).ast());
    }

    public _switchStmt map(char c, long l){
        return map(new CharLiteralExpr( c), _returnStmt.of(l).ast());
    }

    public _switchStmt map(long l, long l2){
        return map(new LongLiteralExpr(l), _returnStmt.of(l2).ast());
    }

    public _switchStmt map(String s, long l){
        return map(new StringLiteralExpr(s), _returnStmt.of(l).ast());
    }

    public _switchStmt map(int i, long l){
        return map(new IntegerLiteralExpr(i), _returnStmt.of(l).ast());
    }

    public _switchStmt map(boolean b, char c){
        return map(new BooleanLiteralExpr(b), _returnStmt.of(c).ast());
    }

    public _switchStmt map(char c, char c2){
        return map(new CharLiteralExpr( c), _returnStmt.of(c2).ast());
    }

    public _switchStmt map(long l, char c){
        return map(new LongLiteralExpr(l), _returnStmt.of(c).ast());
    }

    public _switchStmt map(String s, char c){
        return map(new StringLiteralExpr(s), _returnStmt.of(c).ast());
    }

    public _switchStmt map(int i, char c){
        return map(new IntegerLiteralExpr(i), _returnStmt.of(c).ast());
    }

    public _switchStmt map(boolean b, int ii){
        return map(new BooleanLiteralExpr(b), _returnStmt.of(ii).ast());
    }

    public _switchStmt map(char c, int ii){
        return map(new CharLiteralExpr( c), _returnStmt.of(ii).ast());
    }

    public _switchStmt map(long l, int ii){
        return map(new LongLiteralExpr(l), _returnStmt.of(ii).ast());
    }

    public _switchStmt map(String s, int ii){
        return map(new StringLiteralExpr(s), _returnStmt.of(ii).ast());
    }

    public _switchStmt map(int i, int ii){
        return map(new IntegerLiteralExpr(i), _returnStmt.of(ii).ast());
    }

    public _switchStmt map(boolean b, String s){
        return map(new BooleanLiteralExpr(b), _returnStmt.ofString(s).ast());
    }

    public _switchStmt map(char c, String s){
        return map(new CharLiteralExpr( c), _returnStmt.ofString(s).ast());
    }

    public _switchStmt map(long l, String s){
        return map(new LongLiteralExpr(l), _returnStmt.ofString(s).ast());
    }

    public _switchStmt map(String s, String s2){
        return map(new StringLiteralExpr(s), _returnStmt.ofString(s2).ast());
    }

    public _switchStmt map(int i, String s){
        return map(new IntegerLiteralExpr(i), _returnStmt.ofString(s).ast());
    }

    /**
     * sets the selector for the switch (i.e. the selector is the content within the() i.e. "switch(selector)"
     * @param _selectorExpression
     * @return
     */
    public _switchStmt setSwitchSelector(_expression _selectorExpression){
        this.switchStmt.setSelector(_selectorExpression.ast());
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
    public _switchStmt setSwitchSelector(Expression switchSelector){
        this.switchStmt.setSelector(switchSelector);
        return this;
    }

    /**
     *
     * @param switchSelector
     * @return
     */
    public _switchStmt setSwitchSelector(String... switchSelector){
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

    public _switchStmt setDefault(LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return setDefault( le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return setDefault(le.getBody());
    }

    public _switchStmt setDefault(Ex.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object> _switchStmt setDefault (Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt setDefault (Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt setDefault (BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt setDefault (Ex.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt setDefault (Ex.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt setDefault(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt setDefault(Ex.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt setDefault(Ex.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
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

    public _switchStmt setDefault(Statement... statements){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements(statements);
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(statements);
        this.switchStmt.getEntries().add(newDef.switchEntry);
        return this;
    }

    public _switchStmt setDefault(Expression ex){
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
    public _switchStmt setDefault(String...code){
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
    public _switchStmt setSwitchEntries(String...ses){
        String comb = Text.combine(ses);
        SwitchStmt ss = Ast.switchStmt( "switch(key){"+comb+"}");
        ss.getEntries().forEach(se -> addSwitchEntries(se));
        return this;
    }

    public _switchStmt setSwitchEntries(_switchEntry...ses){
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
    public _switchStmt addCaseGroups(_caseGroup...caseGroups){
        Arrays.stream(caseGroups).forEach( cg-> this.switchStmt.getEntries().addAll(cg.switchEntries));
        return this;
    }

    public _switchStmt addSwitchEntries(SwitchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.switchStmt.getEntries().add(se));
        return this;
    }

    public _switchStmt addSwitchEntries(_switchEntry...ses){
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

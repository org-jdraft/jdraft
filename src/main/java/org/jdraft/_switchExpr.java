package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

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
 * @see _switchCases a grouping of multiple case statements
 *
 */
public final class _switchExpr implements _expr<SwitchExpr, _switchExpr>,
        _tree._node<SwitchExpr, _switchExpr>, _switch<_switchExpr> {

    public static final Function<String, _switchExpr> PARSER = s-> _switchExpr.of(s);

    public static _switchExpr of(){
        return new _switchExpr(new SwitchExpr());
    }

    public static _switchExpr of(SwitchExpr ss){
        return new _switchExpr(ss);
    }

    public static _switchExpr of(String...code){
        try{
            return of( Ast.switchExpr(code) );
        }catch(Exception e){
            throw new _jdraftException("invalid representation of switch expression"+System.lineSeparator()+ Text.combine(code), e);
        }
    }

    public static _switchExpr of(LambdaExpr le ){
        Optional<SwitchExpr> ss = le.findFirst(SwitchExpr.class);
        if( ss.isPresent() ){
            return of(ss.get());
        }
        throw new _jdraftException("No switch expression in lambdaExpr"+System.lineSeparator()+ le);
    }

    public static _switchExpr of(Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object> _switchExpr of (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object> _switchExpr of (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object, C extends Object> _switchExpr of (BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchExpr of (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchExpr of (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object> _switchExpr of(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object,C extends Object> _switchExpr of(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _switchExpr of(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    /**
     * Expression to be selected on (i.e. "a" in "switch(a){ ... }"
     */
    public static _feature._one<_switchExpr, _expr> SELECTOR = new _feature._one<>(_switchExpr.class, _expr.class,
            _feature._id.SELECTOR,
            a -> a.getSwitchSelector(),
            (_switchExpr p, _expr _es) -> p.setSwitchSelector(_es), PARSER);

    public static _feature._many<_switchExpr, _switchCase> SWITCH_ENTRIES = new _feature._many<>(_switchExpr.class, _switchCase.class,
            _feature._id.SWITCH_ENTRIES,
            _feature._id.SWITCH_ENTRY,
            a -> a.listSwitchEntries(),
            (_switchExpr p, List<_switchCase> _ses) -> p.setSwitchEntries(_ses), PARSER, s-> _switchCase.of(s))
            .setOrdered(true); //this is true because of fall-through

    public static _feature._features<_switchExpr> FEATURES = _feature._features.of(_switchExpr.class, PARSER, SELECTOR, SWITCH_ENTRIES );


    public _feature._features<_switchExpr> features(){
        return FEATURES;
    }

    public SwitchExpr node;

    public _switchExpr(SwitchExpr stt){
        this.node = stt;
    }

    @Override
    public _switchExpr copy() {
        return new _switchExpr( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _switchExpr replace(SwitchExpr replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    @Override
    public boolean is(SwitchExpr astNode) {
        return this.node.equals(astNode);
    }

    @Override
    public SwitchExpr node(){
        return node;
    }

    @Override
    public SwitchExpr switchNode(){
        return node;
    }

    public _switchCase getCase(int i){
        return getSwitchEntry(se-> se.hasCaseConstant(i) );
    }

    /**
     * List the _caseGroups that exist in this _switch
     * @return
     */
    public List<_switchCases> listCaseGroups(){
        List<_switchCases> cgs = new ArrayList<>();
        _switchCases _cg = new _switchCases(this.node);
        List<SwitchEntry> ses = this.node.getEntries();
        for(int i=0; i< ses.size(); i++){
            if( ses.get(i).getStatements().isEmpty() ){
                _cg.addSwitchEntry(ses.get(i));
            } else{
                _cg.addSwitchEntry(ses.get(i));
                cgs.add(_cg);
                _cg = new _switchCases(this.node);
            }
        }
        return cgs;
    }

    /**
     *
     * @param matchFn
     * @return
     */
    public List<_switchCases> listCaseGroups(Predicate<_switchCases> matchFn){
        return listCaseGroups().stream().filter(matchFn).collect(Collectors.toList());
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
     * @return
     */
    public boolean hasMultiLabelSwitchEntry(){
        return this.node.getEntries().stream().anyMatch(se-> se.getLabels().size() > 1);
    }

    public boolean isSwitchSelector(String... selector){
        return Objects.equals( this.node.getSelector(), Expr.of(selector));
    }

    public boolean isSwitchSelector(_expr e){
        return Objects.equals( this.node.getSelector(), e.node());
    }

    public boolean isSwitchSelector(Expression e){
        return Objects.equals( this.node.getSelector(), e);
    }

    /**
     *
     * @param _e
     * @return
     */
    public _switchExpr setSwitchSelector(_expr _e){
        this.node.setSelector(_e.node());
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
    public _switchExpr setSwitchSelector(Expression switchSelector){
        this.node.setSelector(switchSelector);
        return this;
    }

    /**
     *
     * @param switchSelector
     * @return
     */
    public _switchExpr setSwitchSelector(String... switchSelector){
        this.node.setSelector(Expr.of(switchSelector));
        return this;
    }

    /**
     * Does this switchStatment have a default
     * @return
     */
    public boolean hasDefault(){
        Optional<SwitchEntry> ose = this.node.getEntries().stream().filter(se-> se.getLabels().isEmpty()).findFirst();
        return ose.isPresent();
    }

    /**
     * Returns the default switchEntry if there is one, or else return null;
     * @return
     */
    public _switchCase getDefault(){
        Optional<SwitchEntry> ose = this.node.getEntries().stream().filter(se-> se.getLabels().isEmpty()).findFirst();
        if( ose.isPresent() ){
            return _switchCase.of(ose.get());
        }
        return null;
    }

    /**
     * return the _expression wrapper on the Switch Selector
     * @return
     */
    public _expr getSwitchSelector(){
        return _expr.of(this.node.getSelector());
    }

    public int countSwitchEntries(){
        return this.node.getEntries().size();
    }

    public _switchCase getSwitchEntry(int index){
        return new _switchCase(this.node.getEntries().get(index));
    }

    public _switchCase getSwitchEntry(Predicate<_switchCase> matchFn){
        Optional<_switchCase> ose = this.listSwitchEntries().stream().filter( matchFn ).findFirst();
        if( ose.isPresent() ){
            return ose.get();
        }
        return null;
    }

    public _switchExpr setDefault(Statement... statements){
        _switchCase se = getDefault();
        if( se != null ){
            se.setStatements(statements);
            return this;
        }
        _switchCase newDef = _switchCase.of(new SwitchEntry()).setStatements(statements);
        this.node.getEntries().add(newDef.node);
        return this;
    }

    public _switchExpr setDefault(Expression ex){
        _switchCase se = getDefault();
        if( se != null ){
            se.setStatements( new ExpressionStmt(ex));
            return this;
        }
        _switchCase newDef = _switchCase.of(new SwitchEntry()).setStatements(new ExpressionStmt(ex));
        this.node.getEntries().add(newDef.node);
        return this;
    }

    /**
     * Sets the code to be done for the default: part of the switch statement
     * (if the default does not exist, create one, if one does, then modify it)
     * @return
     */
    public _switchExpr setDefault(String...code){
        //the code COULD be an expression or a statement or a group of statements
        try {
            Expression e = Expr.of(code);
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
    public List<_switchCase> listSwitchEntries(){
        List<_switchCase> _ses = new ArrayList<>();
        this.node.getEntries().forEach(se -> _ses.add(new _switchCase(se)));
        return _ses;
    }

    /**
     *
     * @return
     */
    public List<_switchCase> listSwitchEntries(Predicate<_switchCase> matchFn){
        List<_switchCase> _ses = new ArrayList<>();
        this.node.getEntries().forEach(se -> {
            _switchCase _se = new _switchCase(se);
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
    public _switchExpr setSwitchEntries(String...ses){
        String comb = Text.combine(ses);
        SwitchExpr ss = Ast.switchExpr( "switch(key){"+comb+"}");
        ss.getEntries().forEach(se -> addSwitchEntries(se));
        return this;
    }

    public _switchExpr setSwitchEntries(List<_switchCase> ses){
        return setSwitchEntries( ses.toArray(new _switchCase[0]));
    }

    public _switchExpr setSwitchEntries(_switchCase...ses){
        NodeList<SwitchEntry> nses = new NodeList<>();
        Arrays.stream(ses).forEach( se-> nses.add(se.node));
        this.node.setEntries(nses);
        return this;
    }

    /**
     * Add one or more caseGroups
     * @param caseGroups
     * @return
     */
    public _switchExpr addCaseGroups(_switchCases...caseGroups){
        Arrays.stream(caseGroups).forEach( cg-> this.node.getEntries().addAll(cg.switchEntries));
        return this;
    }

    public _switchExpr addSwitchEntries(SwitchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.node.getEntries().add(se));
        return this;
    }

    public _switchExpr addSwitchEntries(_switchCase...ses){
        Arrays.stream(ses).forEach( se-> this.node.getEntries().add(se.node));
        return this;
    }

    /*
    public Map<_java.Feature, Object> features() {
        Map<_java.Feature, Object> mc = new HashMap<>();
        mc.put(_java.Feature.SWITCH_SELECTOR_EXPR, this.switchExpr.getSelector());
        mc.put(_java.Feature.SWITCH_ENTRIES, this.switchExpr.getEntries());
        return mc;
    }
     */

    public String toString(){
        return this.node.toString();
    }

    public boolean equals(Object other){
        if( other instanceof _switchExpr){
            return Objects.equals( ((_switchExpr)other).node(), this.node() );
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }
}

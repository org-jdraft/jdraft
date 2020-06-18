package org.jdraft;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

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
public final class _switchStmt implements _stmt._conditional<SwitchStmt, _switchStmt> {

    public static final Function<String, _switchStmt> PARSER = s-> _switchStmt.of(s);

    public static _switchStmt ofSelector(String selectorExpression){
        return ofSelector(Expr.of(selectorExpression));
    }

    public static _switchStmt ofSelector(Expression switchSelector){
        return of().setSwitchSelector(switchSelector);
    }

    public static _switchStmt of(){
        return new _switchStmt(new SwitchStmt());
    }

    public static _switchStmt of(SwitchStmt ss){
        return new _switchStmt(ss);
    }

    /**
     * Creates the switch based on the ENTIRE string...
     * accept if the String
     * @param code
     * @return
     */
    public static _switchStmt of(String...code){
        //first check if they just passed in the name or expression... a common thing to pass in the variable
        // name to set as the SwitchSelector, if it's a single token, then just try treating it as a name
        String comb = Text.combine(code);
        String [] split = comb.split(" " );
        if( split.length == 1 && !split[0].contains("switch") ){
            try {
                return of().setSwitchSelector(comb);
            }catch(Exception e){

            }
        }
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

    public static _switchStmt of(Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object> _switchStmt of (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object> _switchStmt of (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object, C extends Object> _switchStmt of (BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt of (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt of (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object> _switchStmt of(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object,C extends Object> _switchStmt of(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt of(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.node);
    }

    /**
     * Expression to be selected on (i.e. "a" in "switch(a){ ... }"
     */
    public static _feature._one<_switchStmt, _expr> SELECTOR = new _feature._one<>(_switchStmt.class, _expr.class,
            _feature._id.SELECTOR,
            a -> a.getSwitchSelector(),
            (_switchStmt p, _expr _es) -> p.setSwitchSelector(_es), PARSER);

    public static _feature._many<_switchStmt, _switchCase> SWITCH_ENTRIES = new _feature._many<>(_switchStmt.class, _switchCase.class,
            _feature._id.SWITCH_ENTRIES,
            _feature._id.SWITCH_ENTRY,
            a -> a.listSwitchEntries(),
            (_switchStmt p, List<_switchCase> _ses) -> p.setSwitchEntries(_ses), PARSER, s-> _switchCase.of(s))
            .setOrdered(true);

    public static _feature._features<_switchStmt> FEATURES = _feature._features.of(_switchStmt.class,  PARSER, SELECTOR, SWITCH_ENTRIES );

    public SwitchStmt node;

    public _feature._features<_switchStmt> features(){
        return FEATURES;
    }

    /**
     * if SET to true, checks when statements are added to SwitchEntries that
     * end in either<UL>
     *     <LI>A break statement</LI>
     *     <LI>a return statement</LI>
     *     <LI>a throw exception</LI>
     * </UL>
     */
    public boolean autoBreakStatements = true;

    public _switchStmt autoBrake(boolean toAutoBreak){
        this.autoBreakStatements = toAutoBreak;
        return this;
    }

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
        this.node = stt;
    }

    @Override
    public _switchStmt copy() {
        return new _switchStmt( this.node.clone());
    }

    /**
     * Replace the underlying node within the AST (if this node has a parent)
     * and return this (now pointing to the new node)
     * @param replaceNode the node instance to swap in for the old node that this facade was pointing to
     * @return the modified this (now pointing to the replaceNode which was swapped into the AST)
     */
    public _switchStmt replace(SwitchStmt replaceNode){
        this.node.replace(replaceNode);
        this.node = replaceNode;
        return this;
    }

    public boolean equals(Object o){
        if( o instanceof _switchStmt){
            _switchStmt _o = (_switchStmt)o;
            return this.node().equals( _o.node());
        }
        return false;
    }

    public int hashCode(){
        return 31 * this.node().hashCode();
    }

    @Override
    public SwitchStmt node(){
        return node;
    }

    public SwitchStmt switchNode() { return node; }

    public _switchCases getCaseGroup(String s){
        return getCaseGroup( new StringLiteralExpr(s));
    }

    public _switchCases getCaseGroup(char c){
        return getCaseGroup( new CharLiteralExpr(c));
    }

    public _switchCases getCaseGroup(int i){
        return getCaseGroup( new IntegerLiteralExpr(i));
    }

    public _switchCases getCaseGroup(Enum e ){
        return getCaseGroup( new NameExpr(e.name()));
    }

    public _switchCases getCaseGroup(_expr _e){
        return getCaseGroup( _e.node() );
    }

    public _switchCases getCaseGroup(Expression e){
        List<_switchCases> lc = listCaseGroups(cg -> cg.hasCase(e));
        if( lc.isEmpty() ){
            return null;
        }
        return lc.get(0);
    }

    /**
     * gets the _switchEntry by the caseConstant
     * @param e
     * @return
     */
    public _switchCase getCase(Enum e){
        return getSwitchEntry(se-> se.hasCaseConstant( new NameExpr(e.name()) ) );
    }

    /**
     * Find and return the case based on the case expression
     * @param _e
     * @return
     */
    public _switchCase getCase(_expr _e ){
        return getSwitchEntry(se-> se.hasCaseConstant( _e.node() ) );
    }

    public _switchCase getCase(String caseString){
        return getSwitchEntry(se-> se.hasCaseConstant( new StringLiteralExpr(caseString) ) );
    }

    public _switchCase getCase(char c){
        return getSwitchEntry(se-> se.hasCaseConstant(c) );
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
                //System.out.println( "ADDING "+ ses.get(i));
                _cg.addSwitchEntry(ses.get(i));
                cgs.add(_cg);
                _cg = new _switchCases(this.node);
            }
        }
        return cgs;
    }

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
     * @see #forceMultiLabelSwitchEntry (this will TELL the _switchStmt to populate multi label switchesswitches
     * @return
     */
    public boolean hasMultiLabelSwitchEntry(){
        return this.node.getEntries().stream().anyMatch(se-> se.getLabels().size() > 1);
    }

    public _switchStmt mapCode( char c, Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object> _switchStmt mapCode (char c, Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (char c,Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt  mapCode (char c,BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (char c, Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (char c, Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (char c, BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (char c, Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (char c, Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.node);
    }

    public _switchStmt mapCode(char c, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(c, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(c, le.getBody());
    }

    public _switchStmt mapCode( String s, Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object> _switchStmt mapCode (String s, Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt mapCode (String s,Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt mapCode (String s,BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (String s, Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (String s, Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (String s, BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (String s, Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (String s, Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.node);
    }

    public _switchStmt mapCode(String s, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(s, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(s, le.getBody());
    }

    public _switchStmt mapCode( int i, Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object> _switchStmt mapCode (int i, Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (int i,Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt  mapCode (int i,BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (int i, Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (int i, Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (int i, BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (int i, Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (int i, Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.node);
    }

    public _switchStmt mapCode(int i, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(i, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(i, le.getBody());
    }

    public <EN extends Enum> _switchStmt mapCode( EN e, Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object> _switchStmt mapCode (EN e, Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object, B extends Object> _switchStmt  mapCode (EN e,Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object, B extends Object, C extends Object> _switchStmt  mapCode (EN e,BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (EN e, Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (EN e, Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object, B extends Object> _switchStmt  mapCode (EN e, BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum,A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (EN e, Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public <EN extends Enum, A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (EN e, Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.node);
    }

    public  <EN extends Enum>_switchStmt mapCode(EN e, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(e, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(e, le.getBody());
    }

    public _switchStmt mapCode(char c, _stmt... _st){
        return mapCode(_charExpr.of(c), _st);
    }

    public _switchStmt mapCode(String s, _stmt... _st){
        return mapCode(_stringExpr.of(s), _st);
    }

    public _switchStmt mapCode(int i, _stmt... _st){
        return mapCode(_intExpr.of(i), _st);
    }

    public <E extends Enum> _switchStmt mapCode(E e, Statement...st){
        return mapCode(Expr.nameExpr(e.name()), st);
    }

    public <E extends Enum> _switchStmt mapCode(E e, _stmt..._st){
        return mapCode(_nameExpr.of(e.name()), _st);
    }

    public _switchStmt mapCode(_expr _e, _stmt... _st){
        List<Statement>sts = new ArrayList<>();
        Arrays.stream(_st).forEach(_s -> sts.add( _s.node()));
        return mapCode(_e.node(), sts.toArray(new Statement[0]));
    }

    public _switchStmt mapCode(Expression e, _stmt... _st){
        List<Statement>sts = new ArrayList<>();
        Arrays.stream(_st).forEach(_s -> sts.add( _s.node()));
        return mapCode(e, sts.toArray(new Statement[0]));
    }

    public _switchStmt mapCode(Expression e, Statement... sts) {
        NodeList<Statement> nls = new NodeList<>();
        Arrays.stream(sts).forEach(s -> nls.add(s));
        return mapCode(e, nls);
    }

    public _switchStmt mapCode(Expression e, NodeList<Statement> nls){
        Optional<SwitchEntry> ose =
                this.node.getEntries().stream().filter(se -> se.getStatements().equals(nls)).findFirst();
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
                this.node.getEntries().addBefore(labelOnly, existingSwitchEntry);
            }
            return this;
        } //we didnt find an existing mapping to the target statementlist
        SwitchEntry nse = new SwitchEntry();
        NodeList<Expression> label = new NodeList<>();
        label.add(e);
        nse.setLabels(label);
        if( autoBreakStatements ){ //verify that the last statement
            Statement lastStatement = nls.get(nls.size()-1);
            //if the last statement is neither a break, throws or returnStmt
            if( !( lastStatement.isBreakStmt() || lastStatement.isThrowStmt() || lastStatement.isReturnStmt() )){
                nls.add(new BreakStmt().removeLabel());
            }
        }
        nse.setStatements(nls);
        this.node.getEntries().add(nse);
        return this;
    }

    public _switchStmt mapCode(char c, Statement... st){
        return mapCode(new CharLiteralExpr( c), st);
    }

    public _switchStmt mapCode(String s, Statement... st){
        return mapCode(new StringLiteralExpr(s), st);
    }

    public _switchStmt mapCode(int i, Statement... st){
        return mapCode(new IntegerLiteralExpr(i), st);
    }


    public _switchStmt mapReturn(int[] vals, int i){
        return mapReturn(vals, _intExpr.of(i));
    }

    public _switchStmt mapReturn(char[] cs, int i){
        return mapReturn(cs, _intExpr.of(i));
    }

    public _switchStmt mapReturn(String[] s, int i){
        return mapReturn(s, _intExpr.of(i));
    }

    public _switchStmt mapReturn(Enum[] e, int i){
        return mapReturn(e, _intExpr.of(i));
    }

    public _switchStmt mapReturn(Enum[] e, char c){
        return mapReturn(e, _charExpr.of(c));
    }

    public _switchStmt mapReturn(int[] vals, char c){
        return mapReturn(vals, _charExpr.of(c));
    }

    public _switchStmt mapReturn(char[] cs, char c){
        return mapReturn(cs, _charExpr.of(c));
    }

    public _switchStmt mapReturn(String[] s, char c){
        return mapReturn(s, _charExpr.of(c));
    }


    public _switchStmt mapReturn(Enum[] e, String s){
        return mapReturn(e, _stringExpr.of(s));
    }

    public _switchStmt mapReturn(int[] vals, String s){
        return mapReturn(vals, _stringExpr.of(s));
    }

    public _switchStmt mapReturn(char[] cs, String s){
        return mapReturn(cs, _stringExpr.of(s));
    }

    public _switchStmt mapReturn(String[] s, String st){
        return mapReturn(s, _stringExpr.of(st));
    }

    public _switchStmt mapReturn(Enum[] e, Enum ev){
        return mapReturn(e, _nameExpr.of(ev.name()));
    }

    public _switchStmt mapReturn(int[] vals, Enum ev){
        return mapReturn(vals, _nameExpr.of(ev.name()));
    }

    public _switchStmt mapReturn(char[] cs,Enum ev){
        return mapReturn(cs, _nameExpr.of(ev.name()));
    }

    public _switchStmt mapReturn(String[] s, Enum ev){
        return mapReturn(s, _nameExpr.of(ev.name()));
    }

    public _switchStmt mapReturn(Enum[] vals, _expr returnValue){
        _switchCases _cg = _switchCases.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<vals.length;i++){
            _cg.addCase(vals[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }

    public _switchStmt mapReturn(String[] vals, _expr returnValue){
        _switchCases _cg = _switchCases.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<vals.length;i++){
            _cg.addCase(vals[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }

    public _switchStmt mapReturn(int[] vals, _expr returnValue){
        _switchCases _cg = _switchCases.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<vals.length;i++){
            _cg.addCase(vals[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }

    public _switchStmt mapReturn(char[] cs, _expr returnValue){
        _switchCases _cg = _switchCases.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<cs.length;i++){
            _cg.addCase(cs[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, long l){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(l).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, long l){
        return mapCode(s, _returnStmt.of(l).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, long l){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(l).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expr _e, long ll){
        return mapReturn(_e.node(), ll);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, long ll){
        return mapCode(e, _returnStmt.of(ll).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, long ll){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.of(ll).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, char c2){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(c2).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, char c){
        return mapCode( s, _returnStmt.of(c).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, char c){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(c).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expr _e, char c){
        return mapReturn(_e.node(), c);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, char c){
        return mapCode(e, _returnStmt.of(c).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, char c){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.of(c).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, int ii){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(ii).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, int ii){
        return mapCode( s, _returnStmt.of(ii).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, int ii){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(ii).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expr _e, int ii){
        return mapReturn(_e.node(), ii);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, int ii){
        return mapCode(e, _returnStmt.of(ii).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, int ii){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.of(ii).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, String s){
        return mapCode(new CharLiteralExpr( c), _returnStmt.ofString(s).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, String s2){
        return mapCode( s, _returnStmt.ofString(s2).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, String s){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.ofString(s).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expr _e, String s){
        return mapReturn(_e.node(), s);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, String s){
        return mapCode(e, _returnStmt.ofString(s).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, String str){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.ofString(str).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, double dd){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(dd).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, double dd){
        return mapCode( s, _returnStmt.of(dd).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, double dd){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(dd).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expr _e, double dd){
        return mapReturn(_e.node(), dd);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, double dd){
        return mapCode(e, _returnStmt.of(dd).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, double dd){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.of(dd).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, float ff){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(ff).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, float ff){
        return mapCode( s, _returnStmt.of(ff).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, float ff){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(ff).node());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expr _e, float ff){
        return mapReturn(_e.node(), ff);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, float ff){
        return mapCode(e, _returnStmt.of(ff).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, float ff){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.of(ff).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(char c, E e){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(e).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(String s, E e){
        return mapCode( s, _returnStmt.of(e).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(int i, E e){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(e).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(_expr _e, E e){
        return mapCode(_e.node(), _returnStmt.of(e).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(Expression e, E en){
        return mapCode(e, _returnStmt.of(en).node());
    }

    /** map the case constant to the following return value */
    public <E extends Enum, EE extends Enum> _switchStmt mapReturn(E e, EE ee){
        return mapCode(_nameExpr.of(e.name()).node(), _returnStmt.of(ee).node());
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
     * sets the selector for the switch (i.e. the selector is the content within the() i.e. "switch(selector)"
     * @param _selectorExpression
     * @return
     */
    public _switchStmt setSwitchSelector(_expr _selectorExpression){
        this.node.setSelector(_selectorExpression.node());
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
        this.node.setSelector(switchSelector);
        return this;
    }

    /**
     *
     * @param switchSelector
     * @return
     */
    public _switchStmt setSwitchSelector(String... switchSelector){
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

    public _switchStmt setDefault(LambdaExpr le ){
        //if( le.getExpressionBody().isPresent()){
        //    return setDefault(_returnStmt.of(le.getExpressionBody().get()));
        //}
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return setDefault( le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return setDefault(le.getBody());
    }

    public _switchStmt setDefault(Expr.Command lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object> _switchStmt setDefault (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt setDefault (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt setDefault (BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt setDefault (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt setDefault (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object> _switchStmt setDefault(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt setDefault(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt setDefault(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.node);
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

    private Statement[] handleBrake(Statement... stmts){
        if( autoBreakStatements ){
            Statement st = stmts[stmts.length-1];
            if( !( st.isBreakStmt() || st.isReturnStmt() || st.isThrowStmt() ) ){
                Statement[] stt = new Statement[stmts.length+1];
                System.arraycopy(stmts, 0, stt, 0, stmts.length);
                stt[stt.length-1] = new BreakStmt().removeLabel();
                return stt;
            }
        }
        return stmts;
    }

    public _switchStmt setDefault(_stmt... _sts){
        Statement[] sts = new Statement[_sts.length];
        for(int i=0;i<_sts.length; i++){
            sts[i] = _sts[i].node();
        }
        return setDefault(handleBrake(sts));
    }

    public _switchStmt setDefault(Statement... statements){
        _switchCase se = getDefault();
        if( se != null ){
            se.setStatements(handleBrake(statements));
            return this;
        }
        _switchCase newDef = _switchCase.of(new SwitchEntry()).setStatements(
                handleBrake(statements) );
        this.node.getEntries().add(newDef.node);
        return this;
    }

    public _switchStmt setDefault(Expression ex){
        _switchCase se = getDefault();
        if( se != null ){
            se.setStatements( handleBrake(new ExpressionStmt(ex)));
            return this;
        }
        _switchCase newDef = _switchCase.of(new SwitchEntry()).setStatements(handleBrake(new ExpressionStmt(ex)));
        this.node.getEntries().add(newDef.node);
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
            Expression e = Expr.of(code);
            setDefault(e);
            return this;
        }catch(Exception ex){
            //ignore
        }
        try {
            BlockStmt bs = Ast.blockStmt(code);
            setDefault(handleBrake(bs.getStatements().toArray(new Statement[0])));
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

    public _switchStmt setSwitchEntries(List<_switchCase> ses){
        return setSwitchEntries( ses.toArray(new _switchCase[0]));
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
    public _switchStmt setSwitchEntries(String...ses){
        String comb = Text.combine(ses);
        SwitchStmt ss = Ast.switchStmt( "switch(key){"+comb+"}");
        ss.getEntries().forEach(se -> addSwitchEntries(se));
        return this;
    }

    public _switchStmt setSwitchEntries(_switchCase...ses){
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
    public _switchStmt addCaseGroups(_switchCases...caseGroups){
        Arrays.stream(caseGroups).forEach( cg-> this.node.getEntries().addAll(cg.switchEntries));
        return this;
    }

    public _switchStmt addSwitchEntries(SwitchEntry...ses){
        Arrays.stream(ses).forEach( se-> this.node.getEntries().add(se));
        return this;
    }

    public _switchStmt addSwitchEntries(_switchCase...ses){
        Arrays.stream(ses).forEach( se-> this.node.getEntries().add(se.node));
        return this;
    }

    public String toString(){
        return this.node.toString();
    }
}

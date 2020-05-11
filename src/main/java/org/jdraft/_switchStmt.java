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
public final class _switchStmt implements _statement._controlFlow._branching<SwitchStmt, _switchStmt>,
        _java._multiPart<SwitchStmt, _switchStmt>, _switch<_switchStmt> {

    public static _switchStmt ofSelector(String selectorExpression){
        return ofSelector(Expressions.of(selectorExpression));
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

    public static _switchStmt of(Expressions.Command lambdaContainer){
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

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt of (Expressions.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt of (Expressions.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object> _switchStmt of(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object> _switchStmt of(Expressions.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public static <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt of(Expressions.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return of( _l.astLambda);
    }

    public SwitchStmt switchStmt;

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
        this.switchStmt = stt;
    }

    @Override
    public _switchStmt copy() {
        return new _switchStmt( this.switchStmt.clone());
    }

    @Override
    public boolean is(String... stringRep) {
        try {
            return is(Statements.switchStmt(stringRep));
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

    public _caseGroup getCaseGroup(String s){
        return getCaseGroup( new StringLiteralExpr(s));
    }

    public _caseGroup getCaseGroup( char c){
        return getCaseGroup( new CharLiteralExpr(c));
    }

    public _caseGroup getCaseGroup( int i){
        return getCaseGroup( new IntegerLiteralExpr(i));
    }

    public _caseGroup getCaseGroup( Enum e ){
        return getCaseGroup( new NameExpr(e.name()));
    }

    public _caseGroup getCaseGroup(_expression _e){
        return getCaseGroup( _e.ast() );
    }

    public _caseGroup getCaseGroup(Expression e){
        List<_caseGroup> lc = listCaseGroups(cg -> cg.hasCase(e));
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
    public _switchEntry getCase(Enum e){
        return getSwitchEntry(se-> se.isCaseConstant( new NameExpr(e.name()) ) );
    }

    /**
     * Find and return the case based on the case expression
     * @param _e
     * @return
     */
    public _switchEntry getCase( _expression _e ){
        return getSwitchEntry(se-> se.isCaseConstant( _e.ast() ) );
    }

    public _switchEntry getCase( String caseString){
        return getSwitchEntry(se-> se.isCaseConstant( new StringLiteralExpr(caseString) ) );
    }

    public _switchEntry getCase(char c){
        return getSwitchEntry(se-> se.isCaseConstant(c) );
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

    public List<_caseGroup> listCaseGroups( Predicate<_caseGroup> matchFn){
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
        return this.switchStmt.getEntries().stream().anyMatch(se-> se.getLabels().size() > 1);
    }

    public _switchStmt mapCode( char c, Expressions.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object> _switchStmt mapCode (char c, Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (char c,Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt  mapCode (char c,BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (char c, Expressions.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (char c, Expressions.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (char c, BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (char c, Expressions.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (char c, Expressions.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(c, _l.astLambda);
    }

    public _switchStmt mapCode(char c, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(c, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(c, le.getBody());
    }

    public _switchStmt mapCode( String s, Expressions.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object> _switchStmt mapCode (String s, Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt mapCode (String s,Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt mapCode (String s,BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (String s, Expressions.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (String s, Expressions.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (String s, BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (String s, Expressions.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (String s, Expressions.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(s, _l.astLambda);
    }

    public _switchStmt mapCode(String s, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(s, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(s, le.getBody());
    }


    public _switchStmt mapCode( int i, Expressions.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object> _switchStmt mapCode (int i, Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (int i,Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object> _switchStmt  mapCode (int i,BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (int i, Expressions.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (int i, Expressions.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt  mapCode (int i, BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (int i, Expressions.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (int i, Expressions.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(i, _l.astLambda);
    }

    public _switchStmt mapCode(int i, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(i, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(i, le.getBody());
    }

    public <EN extends Enum> _switchStmt mapCode( EN e, Expressions.Command lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object> _switchStmt mapCode (EN e, Consumer<A> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object, B extends Object> _switchStmt  mapCode (EN e,Function<A,B> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object, B extends Object, C extends Object> _switchStmt  mapCode (EN e,BiFunction<A,B,C> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt  mapCode (EN e, Expressions.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt  mapCode (EN e, Expressions.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object, B extends Object> _switchStmt  mapCode (EN e, BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum,A extends Object, B extends Object,C extends Object> _switchStmt  mapCode (EN e, Expressions.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public <EN extends Enum, A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt  mapCode (EN e, Expressions.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return mapCode(e, _l.astLambda);
    }

    public  <EN extends Enum>_switchStmt mapCode(EN e, LambdaExpr le ){
        if( le.getBody().isBlockStmt() ){
            if( le.getBody().asBlockStmt().getStatements().size() == 1 ){
                return mapCode(e, le.getBody().asBlockStmt().getStatement(0));
            }
        }
        return mapCode(e, le.getBody());
    }

    public _switchStmt mapCode(char c, _statement... _st){
        return mapCode(_char.of(c), _st);
    }

    public _switchStmt mapCode(String s, _statement... _st){
        //try{
        //    return mapCode( Expressions.of(s), _st);
        //}catch(Exception e){
            return mapCode(_string.of(s), _st);
        //}
    }

    public _switchStmt mapCode(int i, _statement... _st){
        return mapCode(_int.of(i), _st);
    }

    public <E extends Enum> _switchStmt mapCode(E e, Statement...st){
        return mapCode(Expressions.nameEx(e.name()), st);
    }

    public <E extends Enum> _switchStmt mapCode(E e, _statement..._st){
        return mapCode(_nameExpression.of(e.name()), _st);
    }

    public _switchStmt mapCode(_expression _e, _statement... _st){
        List<Statement>sts = new ArrayList<>();
        Arrays.stream(_st).forEach(_s -> sts.add( _s.ast()));
        return mapCode(_e.ast(), sts.toArray(new Statement[0]));
    }

    public _switchStmt mapCode(Expression e, _statement... _st){
        List<Statement>sts = new ArrayList<>();
        Arrays.stream(_st).forEach(_s -> sts.add( _s.ast()));
        return mapCode(e, sts.toArray(new Statement[0]));
    }

    public _switchStmt mapCode(Expression e, Statement... sts) {
        NodeList<Statement> nls = new NodeList<>();
        Arrays.stream(sts).forEach(s -> nls.add(s));
        return mapCode(e, nls);
    }

    public _switchStmt mapCode(Expression e, NodeList<Statement> nls){
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
        if( autoBreakStatements ){ //verify that the last statement
            Statement lastStatement = nls.get(nls.size()-1);
            //if the last statement is neither a break, throws or returnStmt
            if( !( lastStatement.isBreakStmt() || lastStatement.isThrowStmt() || lastStatement.isReturnStmt() )){
                nls.add(new BreakStmt().removeLabel());
            }
        }
        nse.setStatements(nls);
        this.switchStmt.getEntries().add(nse);
        return this;
    }

    public _switchStmt mapCode(char c, Statement... st){
        return mapCode(new CharLiteralExpr( c), st);
    }

    public _switchStmt mapCode(String s, Statement... st){
        //try{
            //they might have passed a String as the expression
        //    Expression ex = Expressions.of(s);
        //    return mapCode(ex, st);
        //} catch(Exception e) {
        return mapCode(new StringLiteralExpr(s), st);
        //}
    }

    public _switchStmt mapCode(int i, Statement... st){
        return mapCode(new IntegerLiteralExpr(i), st);
    }


    public _switchStmt mapReturn(int[] vals, int i){
        return mapReturn(vals, _int.of(i));
    }

    public _switchStmt mapReturn(char[] cs, int i){
        return mapReturn(cs, _int.of(i));
    }

    public _switchStmt mapReturn(String[] s, int i){
        return mapReturn(s, _int.of(i));
    }

    public _switchStmt mapReturn(Enum[] e, int i){
        return mapReturn(e, _int.of(i));
    }

    public _switchStmt mapReturn(Enum[] e, char c){
        return mapReturn(e, _char.of(c));
    }

    public _switchStmt mapReturn(int[] vals, char c){
        return mapReturn(vals, _char.of(c));
    }

    public _switchStmt mapReturn(char[] cs, char c){
        return mapReturn(cs, _char.of(c));
    }

    public _switchStmt mapReturn(String[] s, char c){
        return mapReturn(s, _char.of(c));
    }


    public _switchStmt mapReturn(Enum[] e, String s){
        return mapReturn(e, _string.of(s));
    }

    public _switchStmt mapReturn(int[] vals, String s){
        return mapReturn(vals, _string.of(s));
    }

    public _switchStmt mapReturn(char[] cs, String s){
        return mapReturn(cs, _string.of(s));
    }

    public _switchStmt mapReturn(String[] s, String st){
        return mapReturn(s, _string.of(st));
    }



    public _switchStmt mapReturn(Enum[] e, Enum ev){
        return mapReturn(e, _nameExpression.of(ev.name()));
    }

    public _switchStmt mapReturn(int[] vals, Enum ev){
        return mapReturn(vals, _nameExpression.of(ev.name()));
    }

    public _switchStmt mapReturn(char[] cs,Enum ev){
        return mapReturn(cs, _nameExpression.of(ev.name()));
    }

    public _switchStmt mapReturn(String[] s, Enum ev){
        return mapReturn(s, _nameExpression.of(ev.name()));
    }

    public _switchStmt mapReturn(Enum[] vals, _expression returnValue){
        _caseGroup _cg = _caseGroup.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<vals.length;i++){
            _cg.addCase(vals[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }

    public _switchStmt mapReturn(String[] vals, _expression returnValue){
        _caseGroup _cg = _caseGroup.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<vals.length;i++){
            _cg.addCase(vals[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }

    public _switchStmt mapReturn(int[] vals, _expression returnValue){
        _caseGroup _cg = _caseGroup.of();

        _cg.setStatements(_returnStmt.of(returnValue));

        for(int i=0;i<vals.length;i++){
            _cg.addCase(vals[i]);
        }
        _cg.removeDefault();
        addCaseGroups(_cg);
        return this;
    }


    public _switchStmt mapReturn(char[] cs, _expression returnValue){
        _caseGroup _cg = _caseGroup.of();

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
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(l).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, long l){
        return mapCode(s, _returnStmt.of(l).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, long l){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(l).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expression _e, long ll){
        return mapReturn(_e.ast(), ll);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, long ll){
        return mapCode(e, _returnStmt.of(ll).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, long ll){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.of(ll).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, char c2){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(c2).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, char c){
        return mapCode( s, _returnStmt.of(c).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, char c){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(c).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expression _e, char c){
        return mapReturn(_e.ast(), c);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, char c){
        return mapCode(e, _returnStmt.of(c).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, char c){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.of(c).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, int ii){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(ii).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, int ii){
        return mapCode( s, _returnStmt.of(ii).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, int ii){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(ii).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expression _e, int ii){
        return mapReturn(_e.ast(), ii);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, int ii){
        return mapCode(e, _returnStmt.of(ii).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, int ii){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.of(ii).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, String s){
        return mapCode(new CharLiteralExpr( c), _returnStmt.ofString(s).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, String s2){
        return mapCode( s, _returnStmt.ofString(s2).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, String s){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.ofString(s).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expression _e, String s){
        return mapReturn(_e.ast(), s);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, String s){
        return mapCode(e, _returnStmt.ofString(s).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, String str){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.ofString(str).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, double dd){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(dd).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, double dd){
        return mapCode( s, _returnStmt.of(dd).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, double dd){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(dd).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expression _e, double dd){
        return mapReturn(_e.ast(), dd);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, double dd){
        return mapCode(e, _returnStmt.of(dd).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, double dd){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.of(dd).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(char c, float ff){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(ff).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(String s, float ff){
        return mapCode( s, _returnStmt.of(ff).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(int i, float ff){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(ff).ast());
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(_expression _e, float ff){
        return mapReturn(_e.ast(), ff);
    }

    /** map the case constant to the following return value */
    public _switchStmt mapReturn(Expression e, float ff){
        return mapCode(e, _returnStmt.of(ff).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum> _switchStmt mapReturn(E e, float ff){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.of(ff).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(char c, E e){
        return mapCode(new CharLiteralExpr( c), _returnStmt.of(e).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(String s, E e){
        return mapCode( s, _returnStmt.of(e).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(int i, E e){
        return mapCode(new IntegerLiteralExpr(i), _returnStmt.of(e).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(_expression _e, E e){
        return mapCode(_e.ast(), _returnStmt.of(e).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum>_switchStmt mapReturn(Expression e, E en){
        return mapCode(e, _returnStmt.of(en).ast());
    }

    /** map the case constant to the following return value */
    public <E extends Enum, EE extends Enum> _switchStmt mapReturn(E e, EE ee){
        return mapCode(_nameExpression.of(e.name()).ast(), _returnStmt.of(ee).ast());
    }

    public boolean isSwitchSelector(String... selector){
        return Objects.equals( this.switchStmt.getSelector(), Expressions.of(selector));
    }

    public boolean isSwitchSelector(_expression e){
        return Objects.equals( this.switchStmt.getSelector(), e.ast());
    }

    public boolean isSwitchSelector(Expression e){
        return Objects.equals( this.switchStmt.getSelector(), e);
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
        this.switchStmt.setSelector(Expressions.of(switchSelector));
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

    public _switchStmt setDefault(Expressions.Command lambdaContainer){
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

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchStmt setDefault (Expressions.TriFunction<A,B,C, D> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchStmt setDefault (Expressions.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object> _switchStmt setDefault(BiConsumer<A,B> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object> _switchStmt setDefault(Expressions.TriConsumer<A,B,C> lambdaContainer ){
        _lambda _l = _lambda.from( Thread.currentThread().getStackTrace()[2]);
        return setDefault( _l.astLambda);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchStmt setDefault(Expressions.QuadConsumer<A,B,C,D> lambdaContainer ){
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

    public _switchStmt setDefault(_statement... _sts){
        Statement[] sts = new Statement[_sts.length];
        for(int i=0;i<_sts.length; i++){
            sts[i] = _sts[i].ast();
        }
        return setDefault(handleBrake(sts));
    }

    public _switchStmt setDefault(Statement... statements){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements(handleBrake(statements));
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(
                handleBrake(statements) );
        this.switchStmt.getEntries().add(newDef.switchEntry);
        return this;
    }

    public _switchStmt setDefault(Expression ex){
        _switchEntry se = getDefault();
        if( se != null ){
            se.setStatements( handleBrake(new ExpressionStmt(ex)));
            return this;
        }
        _switchEntry newDef = _switchEntry.of(new SwitchEntry()).setStatements(handleBrake(new ExpressionStmt(ex)));
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
            Expression e = Expressions.of(code);
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

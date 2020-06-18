package org.jdraft;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.SwitchNode;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.text.Stencil;
import org.jdraft.text.Text;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * <P>A "virtual" object to simplify logically interacting (mutating) multiple {@link SwitchEntry} s in a
 * {@link SwitchStmt} or {@link SwitchExpr}, specifically if you have the scenario where multiple "cases" map to a
 * single action or (we call this a {@link _switchCases}) of actions, i.e.:
 * <CODE>
 *     switch(dayOfWeek){
 *          case 1: case 2: case 3: case 4: case 5: //{@link _switchCases}(1)
 *              System.out.println("Week Day !");
 *          break;
 *          case 6: case 7:                        //{@link _switchCases}(2)
 *              System.out.println("Week End !");
 *          break;
 *          default:                              //{@link _switchCases}(3)
 *                  throw new RuntimeException("bad day");
 *     }
 * </CODE>
 * Above, we have (3) _caseGroups
 *
 * <P>NOTE: this only exists "virtually" for organization
 * (there is no corresponding JavaParser AST element)
 *
 * ALSO this is only ONE way of interacting with the {@link SwitchStmt}, or {@link SwitchExpr} you might want to
 * interact directly each {@link SwitchEntry} individually
 */
public final class _switchCases implements _tree._view<_switchCases>, _tree._orderedGroup<SwitchEntry, _switchCase, _switchCases> {

    public static final Function<String, _switchCases> PARSER = s-> _switchCases.of(s);

    public static _feature._many<_switchCases, _switchCase> SWITCH_ENTRIES = new _feature._many<>(_switchCases.class, _switchCase.class,
            _feature._id.SWITCH_ENTRIES,
            _feature._id.SWITCH_ENTRY,
            a -> a.list(),
            (_switchCases cg, List<_switchCase> _ses) -> cg.setSwitchEntries(_ses), PARSER, s-> _switchCase.of(s))
            .setOrdered(false);

    public static _feature._features<_switchCases> FEATURES = _feature._features.of(_switchCases.class, PARSER, SWITCH_ENTRIES);

    public static _switchCases of(){
        return new _switchCases(new SwitchStmt());
    }

    public _feature._features features(){
        return FEATURES;
    }

    public _switchCases copy(){
        _switchCases _cg = new _switchCases(this.parentSwitch);
        _cg.switchEntries = NodeList.nodeList(this.switchEntries);
        return _cg;
    }

    /**
     * Build a caseGroup from scratch (it uses a SwitchStmt by default)
     * @param code the code representing the cases and body
     * @return
     */
    public static _switchCases of(String...code){
         _switchStmt _ss = _switchStmt.of("switch(unknown){" +System.lineSeparator()+
                 Text.combine(code)+System.lineSeparator()
                 +"}");
         return _ss.listCaseGroups().get(0);
    }

    public static _switchCases of(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object> _switchCases of(Consumer<A> c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        return from(le);
    }

    public static <A extends Object, B extends Object> _switchCases of(BiConsumer<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _switchCases of(Expr.TriConsumer<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchCases of(Expr.QuadConsumer<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> _switchCases of(Function<A,B> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> _switchCases of(BiFunction<A,B,C> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> _switchCases of(Expr.TriFunction<A,B,C,D> command ){
        return from(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    private static _switchCases from(LambdaExpr le){
        Optional<SwitchStmt> ows = le.getBody().findFirst(SwitchStmt.class);
        if( ows.isPresent() ){
            SwitchStmt ss = ows.get();
            _switchStmt _ss = _switchStmt.of(ss);
            return _ss.listCaseGroups().get(0);
        }
        Optional<SwitchExpr> ses = le.getBody().findFirst(SwitchExpr.class);
        if( ses.isPresent() ){
            SwitchExpr se = ses.get();
            _switchExpr _se = _switchExpr.of(se);
            return _se.listCaseGroups().get(0);
        }
        throw new _jdraftException("No switch/case found in lambda");
    }

    /**
     * NOTE the parent switchNode COULD BE a <CODE>SwitchExpr</CODE> or a <CODE>SwitchStmt</CODE>, implementation
     */
    public final SwitchNode parentSwitch;

    public NodeList<SwitchEntry> switchEntries = new NodeList<>();

    public <N extends Node> N anchorNode(){
        return (N)parentSwitch;
    }

    public _switchCases(SwitchNode parentSwitch){
        this.parentSwitch = parentSwitch;
    }

    public _switchCases addSwitchEntry(SwitchEntry se){
        this.switchEntries.add( se );
        return this;
    }

    //note this
    public List<_stmt> getStatements(){
        Optional<SwitchEntry> ose =
                switchEntries.stream().filter( se -> !se.getStatements().isEmpty()).findFirst();
        if( !ose.isPresent()){
            throw new RuntimeException("Invalid _caseGroup in switch... no active entry (witch statement(s)/expression) ");
        }
        return ose.get().getStatements().stream().map(s -> _stmt.of(s)).collect(Collectors.toList());
    }

    /**
     * Gets the "active" SwitchEntry, the one that has associated Statement(s) or Expression
     * i.e. in the below, "case 4:" is the active entry (cases 1, 2, 3 are all case labels only)
     * <CODE>
     * case 1: case 2: case 3: case 4: System.out.println( 4 );
     * </CODE>
     * return NULL if
     * 1) there are no switchEntries
     * 2) NONE of the switchEntries present have associated Statements
     * @return the active SwitchEntry (the one with statement(s)) or null if none are found
     */
    public SwitchEntry getActiveEntry(){
        Optional<SwitchEntry> ose =
                switchEntries.stream().filter( se -> !se.getStatements().isEmpty()).findFirst();
        //if( !ose.isPresent()){
         //   throw new RuntimeException("Invalid _caseGroup in switch... no active entry (witch statement(s)/expression) ");
        //}
        if( ose.isPresent() ) {
            return ose.get();
        }
        //if we have (either NO switch Entries, or No switch entries that have Statement/Expressions)
        return null;
    }

    public List<_switchCase> list(){
        return this.switchEntries.stream().map(se -> _switchCase.of(se)).collect(Collectors.toList());
    }

    @Override
    public NodeList<SwitchEntry> astList() {
        return this.switchEntries;
    }

    @Override
    public String toString(PrettyPrinterConfiguration prettyPrinter) {
        return null;
    }

    public _switchCases setSwitchEntries(List<_switchCase> ses){
        //this.switchEntries.forEach(se -> se.remove());
        int index = parentSwitch.getEntries().size();
        if( !this.switchEntries.isEmpty()) {
            index = this.parentSwitch.getEntries().indexOf(this.switchEntries.get(0));
        }
        //remove the old ones
        this.switchEntries.forEach(se-> se.remove());

        //add all of the new ones
        for(int i=0; i<ses.size(); i++){
            parentSwitch.getEntries().add(i + index, ses.get(i).node() );
        }
        return this;
    }

    /**
     * Find the Active Entry (the SwitchEntry associated with some Statement(s))
     * and returns it
     * (or the LAST SwitchEntry in the List if there are no SwitchEntries that contain Statements)
     * or creates, adds (to switchEntries) and returns new SwitchEntry
     * @return
     */
    public SwitchEntry getOrCreateActiveEntry(){
        if( this.switchEntries.isEmpty() ){
            SwitchEntry newEntry = new SwitchEntry();
            this.parentSwitch.getEntries().add(newEntry);
            this.switchEntries.add( newEntry);
            return newEntry;
        }
        SwitchEntry se = getActiveEntry();
        if( se != null ){
            return se;
        }
        //here I have one or more SwitchEntries, but NONE of them have any Statements, so return
        //the LAST one
        return this.switchEntries.get(this.switchEntries.size() -1);
    }

    /**
     * Sets ALL of the statements inside this SwitchEntry
     * @param sts
     * @return
     */
    public _switchCases setStatements(String...sts){
        BlockStmt bs = Ast.blockStmt(sts);
        bs.getStatements().forEach(s -> this.getOrCreateActiveEntry().addStatement(s));
        return this;
    }

    /**
     * Lambda way of setting the statements
     * @param c a lambdaExpression containing the
     * @return
     */
    public _switchCases setStatements(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2] );
        return setStatements(le);
    }

    public <A extends Object> _switchCases setStatements (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCases setStatements (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public  <A extends Object, B extends Object, C extends Object> _switchCases setStatements(BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchCases setStatements (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchCases setStatements (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCases setStatements(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchCases setStatements(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchCases setStatements(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return setStatements( _l.node);
    }

    private _switchCases setStatements(LambdaExpr le){
        if( le.getExpressionBody().isPresent()){ //an Expression Lambda
            NodeList<Statement>sts = new NodeList<>();
            sts.add( new ExpressionStmt(le.getExpressionBody().get()));
            this.getActiveEntry().setStatements(sts);
            return this;
        }
        Statement st = le.getBody();
        if( !(st instanceof BlockStmt )){
            NodeList<Statement>sts = new NodeList<>();
            sts.add( st);
            this.getActiveEntry().setStatements(sts);
            return this;
        }
        //if its a blockStmt, just pull
        BlockStmt bs = st.asBlockStmt();
        this.getActiveEntry().setStatements( bs.getStatements() );
        return this;
    }

    /**
     * Sets ALL of the statements inside this SwitchEntry
     * @param st
     * @return
     */
    public _switchCases setStatements(Statement...st){
        NodeList<Statement>stmts = new NodeList<>();
        Arrays.stream(st).forEach(s -> stmts.add(s));
        this.getOrCreateActiveEntry().setStatements(stmts);
        return this;
    }

    public _switchCases setStatements(_stmt...st){
        NodeList<Statement>stmts = new NodeList<>();
        Arrays.stream(st).forEach(s -> stmts.add(s.node()));
        this.getOrCreateActiveEntry().setStatements(stmts);
        return this;
    }

    /**
     * Lambda way of setting the statements
     * @param c a lambdaExpression containing the
     * @return
     */
    public _switchCases addStatements(Expr.Command c){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2] );
        return addStatements(le);
    }

    public <A extends Object> _switchCases addStatements (Consumer<A> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCases addStatements (Function<A,B> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public  <A extends Object, B extends Object, C extends Object> _switchCases addStatements(BiFunction<A,B,C> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object> _switchCases addStatements (Expr.TriFunction<A,B,C, D> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object> _switchCases addStatements (Expr.QuadFunction<A,B,C, D,E> lambdaContainer){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object> _switchCases addStatements(BiConsumer<A,B> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object> _switchCases addStatements(Expr.TriConsumer<A,B,C> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    public <A extends Object, B extends Object,C extends Object, D extends Object> _switchCases addStatements(Expr.QuadConsumer<A,B,C,D> lambdaContainer ){
        _lambdaExpr _l = _lambdaExpr.from( Thread.currentThread().getStackTrace()[2]);
        return addStatements( _l.node);
    }

    private _switchCases addStatements(LambdaExpr le){
        if( le.getExpressionBody().isPresent()){ //an Expression Lambda
            //NodeList<Statement>sts = new NodeList<>();
            //sts.add( new ExpressionStmt(le.getExpressionBody().get()));
            this.getOrCreateActiveEntry().addStatement( new ExpressionStmt(le.getExpressionBody().get()) );
            return this;
        }
        Statement st = le.getBody();
        if( !(st instanceof BlockStmt )){
            this.getOrCreateActiveEntry().addStatement(st);
            return this;
        }
        //if its a blockStmt, just pull
        BlockStmt bs = st.asBlockStmt();
        SwitchEntry se = this.getOrCreateActiveEntry();
        bs.getStatements().forEach(s -> se.addStatement(s));
        //se.addStatements(bs.getStatements());
        return this;
    }

    /**
     * Adds statements to be used in this _caseGroup
     * @param st
     * @return
     */
    public _switchCases addStatements(String...st){
        BlockStmt bs = Ast.blockStmt(st);
        bs.getStatements().forEach(s -> this.getOrCreateActiveEntry().addStatement(s));
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchCases addStatements(int index, String...st){
        BlockStmt bs = Ast.blockStmt(st);
        //bs.getStatements().forEach(s -> this.getActionEntry().addStatement(s));
        for(int i=0;i<bs.getStatements().size();i++){
            this.getOrCreateActiveEntry().addStatement(index + i, bs.getStatement( i) );
        }
        return this;
    }

    public _switchCases addStatements(Statement...st){
        for(int i=0; i<st.length;i++) {
            this.getOrCreateActiveEntry().addStatement(st[i]);
        }
        return this;
    }

    public _switchCases addStatements(_stmt...st){
        for(int i=0; i<st.length;i++) {
            this.getOrCreateActiveEntry().addStatement(st[i].node());
        }
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchCases addStatements(int index, Statement...st){
        for(int i=0;i<st.length;i++){
            this.getOrCreateActiveEntry().addStatement(index + i, st[i] );
        }
        return this;
    }

    /**
     * Adds statements to this SwitchEntry
     * @param index
     * @param st
     * @return
     */
    public _switchCases addStatements(int index, _stmt...st){
        for(int i=0;i<st.length;i++){
            this.getOrCreateActiveEntry().addStatement(index + i, st[i].node() );
        }
        return this;
    }

    /**
     * i.e.
     * case 1 : throw new RuntimeException();
     *
     * @return
     */
    public boolean isThrows(){
        return this.getOrCreateActiveEntry().getType().equals(SwitchEntry.Type.THROWS_STATEMENT);
    }

    /**
     * is the LAST statement in the case body-logic a Return statement
     * @return
     */
    public boolean isReturn(){
        SwitchEntry se = this.getOrCreateActiveEntry();
        if( se.getStatements().size() > 0 ) {
            if( se.getStatement(se.getStatements().size() - 1) instanceof ReturnStmt){
                return true;
            }
            if( se.getStatement(se.getStatements().size() - 1) instanceof BlockStmt){
                BlockStmt bs = (BlockStmt)se.getStatement(se.getStatements().size() - 1);
                if( (bs.getStatements().size() > 0) && (bs.getStatement(bs.getStatements().size()) instanceof ReturnStmt ) ){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isReturn(double d){
        return isReturn( Expr.of(d));
    }

    public boolean isReturn(float f){
        return isReturn( Expr.of(f));
    }

    public boolean isReturn(boolean b){
        return isReturn( Expr.of(b));
    }

    public boolean isReturn(char c){
        return isReturn( Expr.of(c));
    }

    public boolean isReturn(long l){
        return isReturn( Expr.of(l));
    }

    public boolean isReturn(int i){
        return isReturn( Expr.of(i));
    }

    public boolean isReturn(String str){
        try{
            return isReturn( Expr.of(str));
        } catch(Exception e){
            return false;
        }
    }

    public boolean isReturn(_expr e){
        return isReturn( e.node());
    }

    public boolean isReturn(Expression e){
        SwitchEntry se = this.getOrCreateActiveEntry();
        if( se.getStatements().size() > 0 ) {
            if( se.getStatement(se.getStatements().size() - 1) instanceof ReturnStmt){
                ReturnStmt rs = (ReturnStmt)se.getStatement(se.getStatements().size() - 1);
                if( rs.getExpression().isPresent() ){
                    return Expr.equal(rs.getExpression().get(), e);
                }
                return e == null;
            }
            if( se.getStatement(se.getStatements().size() - 1) instanceof BlockStmt){
                BlockStmt bs = (BlockStmt)se.getStatement(se.getStatements().size() - 1);
                if( (bs.getStatements().size() > 0) && (bs.getStatement(bs.getStatements().size()) instanceof ReturnStmt ) ){
                    ReturnStmt rs = (ReturnStmt)bs.getStatement(se.getStatements().size() - 1);
                    if( rs.getExpression().isPresent() ){
                        return Expr.equal(rs.getExpression().get(), e);
                    }
                    return e == null;
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
        SwitchEntry se = this.getOrCreateActiveEntry();
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

    /**
     * i.e.
     * case 1:
     *     System.out.println(1);
     *     System.out.println(2);
     *     break;
     * @return
     */
    public boolean isStatementGroup(){
        return this.getOrCreateActiveEntry().getType().equals(SwitchEntry.Type.STATEMENT_GROUP);
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
        return this.getOrCreateActiveEntry().getType().equals(SwitchEntry.Type.BLOCK);
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
        return this.getOrCreateActiveEntry().getType().equals(SwitchEntry.Type.EXPRESSION);
    }

    public _switchCases addCase(String str){
        return addCase( new StringLiteralExpr(str) );
    }

    public _switchCases addCase(int i){
        return addCase( new IntegerLiteralExpr(i) );
    }

    public _switchCases addCase(char c){
        return addCase( new CharLiteralExpr(c) );
    }

    public _switchCases addCase(Enum e){
        return addCase( new NameExpr(e.name()));
    }

    public _switchCases addCase(_expr _e){
        return addCase(_e.node());
    }

    public _switchCases removeDefault(){
        Optional<SwitchEntry> defaultSwitchEntry =
                this.switchEntries.stream().filter( se -> se.getLabels().isEmpty() ).findFirst();
        if( defaultSwitchEntry.isPresent() ) {
            NodeList<Statement> sts = defaultSwitchEntry.get().getStatements();
            int defaultIndex = this.switchEntries.indexOf(defaultSwitchEntry.get());
            if( defaultIndex < 1 ){
                throw new _jdraftException("Cant remove default as only element in casegroup");
            }
            else{
                this.switchEntries.get(defaultIndex - 1).setStatements(sts);
            }
            this.switchEntries.remove(defaultSwitchEntry.get());
        }
        return this;
    }

    /**
     * TODO: what I SHOULD do is to remove the Statement(s) from the ActiveEntry
     * and create a new switchEntry with the new expression and the statement(s)
     *
     *
     * @param e
     * @return
     */
    public _switchCases addCase(Expression e){

        if(!this.switchEntries.stream().anyMatch(se -> se.getLabels().stream().anyMatch(ex -> ex.equals(e)))){
            NodeList<Expression>nle = new NodeList<>();
            nle.add(e);
            SwitchEntry nse = new SwitchEntry().setLabels(nle);

            if( this.switchEntries.isEmpty() ){
                this.switchEntries.add( nse );
                this.parentSwitch.getEntries().add( nse );
                return this;
            }
            // Now find (on the parentSwitchStmt) the last
            SwitchEntry activeEntry = this.getActiveEntry();

            this.switchEntries.add( this.switchEntries.indexOf(activeEntry), nse );

            //add a new case BEFORE the active entry
            this.parentSwitch.getEntries().addBefore(nse,activeEntry);
        }
        return this;
    }

    public boolean hasCase(Enum e){
        return hasCase( new NameExpr( e.name() ) );
    }

    public boolean hasCase(_expr _e){
        return hasCase(_e.node());
    }

    public boolean hasCase(Expression expr){
        return this.switchEntries.stream().anyMatch(se -> se.getLabels().stream().anyMatch(ex -> ex.equals(expr)));
    }

    public boolean hasCase(int i){
        return hasCase( new IntegerLiteralExpr(i) );
    }

    public boolean hasCase(char c){
        return hasCase( new CharLiteralExpr(c) );
    }

    public boolean hasCase(String str){
        return hasCase( new StringLiteralExpr(str) );
    }

    /**
     * Does this caseGroup contain a default?
     * @return
     */
    public boolean isDefault(){
        return this.switchEntries.stream().anyMatch(se -> se.getLabels().isEmpty());
    }

    /**
     *
     * @return
     */
    public List<_expr> listCases(){
        //return this.switchEntries.stream().flatMap(se -> se.getLabels().stream()).map(e -> _expression.of(e)).collect(Collectors.toList());
        return listCases( t->true);
    }

    public List<_expr> listCases(Predicate<_expr> matchFn){
        return this.switchEntries.stream().flatMap(se -> se.getLabels().stream()).map(e -> _expr.of(e)).filter(matchFn).collect(Collectors.toList());
    }

    public List<_stmt> listStatements(){
        return listStatements(t->true);
    }

    public List<_stmt> listStatements(Predicate<_stmt> _matchFn){
        SwitchEntry se = this.getActiveEntry();
        if( se == null ){
            return new ArrayList<>();
        }
        return se.getStatements().stream().map(s -> _stmt.of(s)).filter(_matchFn).collect(Collectors.toList());
    }

    public _stmt getStatement(int index){
        SwitchEntry se = this.getActiveEntry();
        if( se == null ){
            return null;
        }
        return _stmt.of(se.getStatement(index));
    }

    public _switchCases sort(Comparator<? super SwitchEntry> cse){

        //AtomicInteger firstIndex = new AtomicInteger( -1);
        NodeList<SwitchEntry> ses = parentSwitch.getEntries();
        int firstIndex = this.switchEntries.stream().map(se -> ses.indexOf(se) ).min(Integer::compareTo).get();

        List<_stmt> sts = getStatements();
        //System.out.println("THE FIRST INDEX IS "+firstIndex);

        this.switchEntries.sort(cse);
        //first remove all of
        for(int i=0;i<switchEntries.size();i++){
            ses.remove(switchEntries.get(i));
        }
        //now add them in, in order
        for(int i=0;i<switchEntries.size();i++){
            SwitchEntry stripped =  switchEntries.get(i).setStatements(new NodeList<>());
            ses.add(firstIndex+i,stripped);
            if( i == switchEntries.size() -1 ){
                _switchCase.of(stripped).setStatements(sts.toArray(new _stmt[0]));
            }
        }
        return this;
    }

    public boolean is(String code){
        return is( new String[]{code});
    }

    public boolean is(String...code){
        String all = Text.combine(code);
        if( all.startsWith("$") && all.endsWith("$")){
            Stencil st = Stencil.of( all );
            if( st.isMatchAny() ){
                return true;
            }
        }
        _switchCases _o = of(all);
        //TODO: Feature parameterization
        return equals( _o);
    }

    public boolean equals(Object o){
        if(! (o instanceof _switchCases)){
            return false;
        }
        _switchCases cg = (_switchCases)o;
        Set<_expr> cct = new HashSet<>();
        Set<_expr> cco = new HashSet<>();
        cct.addAll(this.listCases());
        cco.addAll(cg.listCases());

        return Objects.equals( cct, cco) &&
                Objects.equals(this.listStatements(), cg.listStatements());
    }

    public int hashCode(){
        //order doesnt matter
        //i.e. these are the same logically
        // case 1: case 2: case 3:
        // case 3: case 2: case 1:
        Set<_expr> caseConstants = new HashSet<>();
        caseConstants.addAll(listCases());
        return Objects.hash( caseConstants, listStatements());
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        this.switchEntries.forEach( se-> sb.append(se) );
        return sb.toString();
    }
}

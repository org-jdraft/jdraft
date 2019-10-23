package org.jdraft.pattern;

import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

import org.jdraft.*;
import org.jdraft.Ex.QuadConsumer;
import org.jdraft.Ex.TriConsumer;

/**
 * Pattern of a Java {@link Statement} that provides operations for
 * constructing, analyzing, matching, extracting, removing, replacing / etc. Statement
 * type nodes within the AST.
 * <PRE>
 * NOTE: In the future I might create individual implementations for each Statement type
 * i.e. $assertStmt, $blockStmt, $continueStmt...
 * rather than:
 * $stmt<AssertStmt>, $stmt<BlockStmt>, $stmt<ContinueStmt>...
 *
 * </PRE>
 * <PRE>
 * $stmt
 * CONSTRUCT
 *     .construct([Translator], Tokens) build & return 
 *     .fill([Translator], values)
 * PARAMETERIZE
 *     .$(Tokens)
 *     .hardcode$([translator], target, value)
 * MATCH
 *     .constraint(Predicate<T>) //set the matching constraint
 *     .matches(Statement)
 *     .select(Statement)
 *     .deconstruct( Statement )
 * QUERY       
 *     .first/.firstIn(_node, proto) find the first matching statement in
 *     .list/.listIn(_node, proto, Predicate<>) list all matches in        
 *     .selectFirst/.selectFirstIn(_node, proto) return the first "selection" match
 *     .selectList/.selectListIn(_node, proto) return a list of selection matches
 * MODIFY
 *     .remove/.removeIn(_node, proto)
 *     .replace/.replaceIn(_node, protoTarget, protoReplacement)
 *     .forIn(_node, Consumer<T>)
 *     .forSelectedIn(_node, Consumer<T>) 
 *</PRE>      
 * @param <T> underlying Statement implementation type
 */
public class $stmt<T extends Statement>
    implements Template<T>, $pattern<T, $stmt<T>>, $body.$part, $method.$part, $constructor.$part {
        
    /**
     * 
     * @param ste
     * @return 
     */
    private static <S extends Statement> $stmt<S> from(StackTraceElement ste ){
        Statement st = Stmt.from( ste );
        return new $stmt( (S)st );
    }

    /**
     *
     * @param stmtClass
     * @param pattern
     * @param <S>
     * @return
     */
    public static <S extends Statement> $stmt<S> of(Class<S> stmtClass, String...pattern){
        return new $stmt<S>(stmtClass, Text.combine(pattern));
    }

    /**
     * 
     * @param <S>
     * @param proto
     * @return 
     */
    public static <S extends Statement> $stmt<S> of( Ex.Command proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }
    
    /**
     * 
     * @param <T>
     * @param <S>
     * @param proto
     * @return 
     */
    public static <T extends Object, S extends Statement> $stmt<S> of( Consumer<T> proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    } 
    
    /**
     * 
     * @param <T>
     * @param <U>
     * @param <S>
     * @param proto
     * @return 
     */
    public static <T extends Object, U extends Object, S extends Statement> $stmt<S> of( BiConsumer<T,U> proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * 
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <S>
     * @param proto
     * @return 
     */
    public static <T extends Object, U extends Object, V extends Object, S extends Statement> $stmt<S> of(TriConsumer<T, U, V> proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * 
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <X>
     * @param <S>
     * @param proto
     * @return 
     */
    public static <T extends Object, U extends Object, V extends Object, X extends Object, S extends Statement> $stmt<S> of(QuadConsumer<T, U, V, X> proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }
    
    /** 
     * Will match ANY statement, or empty statemen
     * @param <S>
     * @return 
     */
    public static <S extends Statement> $stmt<S> of(){
        return new $stmt( Statement.class, t-> true );
    }

    /**
     * $stmt.of( new Object(){
     *     if( $a$ != $b$){
     *         $then$:{}
     *     }else{
     *         $else$:{}
     *     }
     * });
     * @param anonymousObjectWithStatement
     * @param <S>
     * @return
     */
    public static <S extends Statement> $stmt<S> of( Object anonymousObjectWithStatement ){
        ObjectCreationExpr oce = Ex.anonymousObjectEx( Thread.currentThread().getStackTrace()[2]);
        BlockStmt bs = oce.findFirst(com.github.javaparser.ast.stmt.BlockStmt.class).get();

        //?? do I want to do anything with $label$:{} ?
        //return new $stmt<S>( );
        return of( bs.getStatement(0) );
    }

    public static <S extends Statement> $stmt<S> of( String prototypePattern ){
        return of( new String[] {prototypePattern});
    }

    /**
     *
     * @param pattern
     * @return 
     */
    public static <S extends Statement> $stmt<S> of(String...pattern ){
        S st = (S)Stmt.of(pattern);
        return new $stmt<S>(st);
    }
    
    /**
     *
     * @param astProto
     * @return 
     */ 
    public static $stmt of(Statement astProto ){
        return new $stmt<>(astProto);
    }

    public static $stmt.Or or( Statement... _protos ){
        $stmt[] arr = new $stmt[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $stmt.of( _protos[i]);
        }
        return or(arr);
    }

    public static $stmt.Or or( $stmt...$tps ){
        return new $stmt.Or($tps);
    }

    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<AssertStmt> assertStmt(){
        return new $stmt( AssertStmt.class, "$assertStmt$" );
    } 
    
    /**
     * i.e."assert(1==1);"
     * @param pattern
     * @return and AssertStmt with the code
     */
    public static $stmt<AssertStmt> assertStmt(String... pattern ) {
        return new $stmt( Stmt.assertStmt(pattern));
    }

    /**
     * i.e."assert(1==1);"
     * @param pattern
     * @param constraint
     * @return and AssertStmt with the code
     */
    public static $stmt<AssertStmt> assertStmt(String pattern, Predicate<AssertStmt> constraint) {
        return new $stmt( Stmt.assertStmt(pattern) ).$and(constraint);
    }
    
    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<BlockStmt> blockStmt(){
        return new $stmt( BlockStmt.class, "$blockStmt$" );
    }

    /**
     *
     * @param block
     * @return
     */
    public static $stmt<BlockStmt> blockStmt(BlockStmt block){
        return new $stmt( block );
    }

    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param pattern the code making up the blockStmt
     * @return the BlockStmt
     */
    public static $stmt<BlockStmt> blockStmt(String... pattern ) {
        return new $stmt( Stmt.blockStmt(pattern));
    }
    
    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param pattern the code making up the blockStmt
     * @param constraint
     * @return the BlockStmt
     */
    public static $stmt<BlockStmt> blockStmt(String pattern, Predicate<BlockStmt> constraint) {
        return new $stmt( Stmt.blockStmt(pattern)).$and(constraint);
    }

    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<BreakStmt> breakStmt(){
        return new $stmt( BreakStmt.class, "$breakStmt$" );
    } 

    /**
     * i.e."break;" or "break outer;"
     * @param pattern String representing the break of
     * @return the breakStmt
     */
    public static $stmt<BreakStmt> breakStmt(String... pattern ) {
        return new $stmt( Stmt.breakStmt(pattern));
    }

    /**
     * i.e."break;" or "break outer;"
     * @param pattern String representing the break of
     * @param constraint
     * @return the breakStmt
     */
    public static $stmt<BreakStmt> breakStmt(String pattern, Predicate<BreakStmt> constraint) {
        return new $stmt( Stmt.breakStmt(pattern)).$and(constraint);
    }
        
    /**
     * Returns a prototype that matches ANY continueStmt
     * @return 
     */
    public static $stmt<ContinueStmt> continueStmt(){
        return new $stmt( ContinueStmt.class, "$continueStmt$" );
    }
    
    /** 
     * i.e."continue outer;" 
     * @param pattern
     * @return 
     */
    public static $stmt<ContinueStmt> continueStmt(String... pattern ) {
        return new $stmt( Stmt.continueStmt(pattern));
    }

    
    /** 
     * i.e."continue outer;" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ContinueStmt> continueStmt(String pattern, Predicate<ContinueStmt> constraint) {
        return new $stmt( Stmt.continueStmt(pattern)).$and(constraint);
    }
    
    
    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<DoStmt> doStmt(){
        return new $stmt( DoStmt.class, "$DoStmt$" );
    }

    /**
     * i.e."do{ System.out.println(1); }while( a < 100 );"
     * @param ds
     * @return
     */
    public static $stmt<DoStmt> doStmt( DoStmt ds) {
        return new $stmt( ds);
    }

    /** 
     * i.e."do{ System.out.println(1); }while( a < 100 );" 
     * @param pattern
     * @return 
     */
    public static $stmt<DoStmt> doStmt(String... pattern ) {
        return new $stmt( Stmt.doStmt(pattern));
    }

    /** 
     * i.e."do{ System.out.println(1); }while( a < 100 );" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<DoStmt> doStmt(String pattern, Predicate<DoStmt> constraint) {
        return new $stmt( Stmt.doStmt(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<EmptyStmt> emptyStmt(){
        return new $stmt( new EmptyStmt() );
    }
    
    
    /**
     *
     * Returns a pattern that matches ANY thisOrSuperCallStmt
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> thisCallStmt(){
        return new $stmt( ExplicitConstructorInvocationStmt.class, "$thisCallStmt$" );
    } 

    /** 
     * i.e."this(100,2900);" 
     * @param pattern
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> thisCallStmt(String... pattern ) {
        return new $stmt( Stmt.thisOrSuperCallStmt(pattern));
    }

    /**
     * i.e."this(100,2900);"
     * @param cts
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt> thisCallStmt(ExplicitConstructorInvocationStmt cts) {
        return new $stmt( cts );
    }

    /** 
     * i.e."this(100,2900);" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> thisCallStmt(String pattern, Predicate<ExplicitConstructorInvocationStmt> constraint) {
        return new $stmt( Stmt.thisOrSuperCallStmt(pattern)).$and(constraint);
    }

    /**
     *
     * Returns a pattern that matches ANY thisOrSuperCallStmt
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt> superCallStmt(){
        return new $stmt( ExplicitConstructorInvocationStmt.class, "$superCallStmt$" );
    }

    /**
     * i.e."this(100,2900);"
     * @param pattern
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt> superCallStmt(String... pattern ) {
        return new $stmt( Stmt.thisOrSuperCallStmt(pattern));
    }

    /**
     * i.e."this(100,2900);"
     * @param cts
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt> superCallStmt(ExplicitConstructorInvocationStmt cts) {
        return new $stmt( cts );
    }

    /**
     * i.e."this(100,2900);"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt> superCallStmt(String pattern, Predicate<ExplicitConstructorInvocationStmt> constraint) {
        return new $stmt( Stmt.thisOrSuperCallStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."s += t;" 
     * @return 
     */
    public static $stmt<ExpressionStmt> expressionStmt() {
        return new $stmt( ExpressionStmt.class, "$expressionStmt$");
    }

    /**
     * i.e."s += t;"
     * @param es
     * @return
     */
    public static $stmt<ExpressionStmt> expressionStmt( ExpressionStmt es) {
        return new $stmt( es);
    }

    /** 
     * i.e."s += t;" 
     * @param pattern
     * @return 
     */
    public static $stmt<ExpressionStmt> expressionStmt( String... pattern ) {
        return new $stmt( Stmt.expressionStmt(pattern));
    }
    
    /** 
     * i.e."s += t;" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ExpressionStmt> expressionStmt( String pattern, Predicate<ExpressionStmt> constraint) {
        return new $stmt( Stmt.expressionStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."s += t;" 
     * @return 
     */
    public static $stmt<ForStmt> forStmt( ) {
        return new $stmt( ForStmt.class, "$forStmt$");
    }

    /** 
     * i.e."for(int i=0; i<100;i++) {...}" 
     * @param pattern
     * @return 
     */
    public static $stmt<ForStmt> forStmt( String... pattern ) {
        return new $stmt( Stmt.forStmt(pattern));
    }
    
    /** 
     * i.e."for(int i=0; i<100;i++) {...}" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ForStmt> forStmt( String pattern, Predicate<ForStmt> constraint ) {
        return new $stmt( Stmt.forStmt(pattern)).$and(constraint);
    }

    /**
     * i.e."s += t;"
     * @return
     */
    public static $stmt<ForEachStmt> forEachStmt() {
        return new $stmt( ForEachStmt.class, "$forEachStmt$");
    }

    /**
     * i.e."s += t;"
     * @return
     */
    public static $stmt<ForEachStmt> forEachStmt(ForEachStmt fes) {
        return new $stmt( fes);
    }

    /** 
     * i.e."for(String element:arr){...}" 
     * @param pattern
     * @return 
     */
    public static $stmt<ForEachStmt> forEachStmt( String... pattern ) {
        return new $stmt( Stmt.forEachStmt(pattern));
    }
    
    /** 
     * i.e."for(String element:arr){...}" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ForEachStmt> forEachStmt( String pattern, Predicate<ForEachStmt> constraint) {
        return new $stmt( Stmt.forEachStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."if(a==1){...}" 
     * @return 
     */
    public static $stmt<IfStmt> ifStmt( ) {
        return new $stmt( IfStmt.class, "$ifStmt$");
    }

    /**
     * i.e."if(a==1){...}"
     * @return
     */
    public static $stmt<IfStmt> ifStmt( IfStmt is) {
        return new $stmt( is );
    }
    
    /** 
     * i.e."if(a==1){...}" 
     * @param pattern
     * @return 
     */
    public static $stmt<IfStmt> ifStmt( String... pattern ) {
        return new $stmt( Stmt.ifStmt(pattern));
    }
    
    /** 
     * i.e."if(a==1){...}" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<IfStmt> ifStmt( String pattern, Predicate<IfStmt> constraint) {
        return new $stmt( Stmt.ifStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."outer:   start = getValue();" 
     * @return 
     */
    public static $stmt<LabeledStmt> labeledStmt( ) {
        return new $stmt( LabeledStmt.class, "$labeledStmt$");
    }

    /**
     * i.e."outer:   start = getValue();"
     * @param ls
     * @return
     */
    public static $stmt<LabeledStmt> labeledStmt( LabeledStmt ls) {
        return new $stmt( ls );
    }

    /** 
     * i.e."outer:   start = getValue();" 
     * @param pattern
     * @return 
     */
    public static $stmt<LabeledStmt> labeledStmt( String... pattern ) {
        return new $stmt( Stmt.labeledStmt(pattern));
    }

    /** 
     * i.e."outer:   start = getValue();" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<LabeledStmt> labeledStmt( String pattern, Predicate<LabeledStmt> constraint) {
        return new $stmt( Stmt.labeledStmt(pattern)).$and(constraint);
    }
    
    /**
     * i.e."class C{ int a, b; }"
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt() {
        return new $stmt( LocalClassDeclarationStmt.class, "$localClass$");
    }

    /**
     *
     * @param lcs
     * @return
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt(LocalClassDeclarationStmt lcs) {
        return new $stmt( lcs );
    }

    /**
     * Converts from a String to a LocalClass
     * i.e. "class C{ int a, b; }"
     * @param pattern the code that represents a local class
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt( String... pattern ) {
        return new $stmt( Stmt.localClassStmt(pattern));
    }
    
    /**
     * Converts from a String to a LocalClass
     * i.e."class C{ int a, b; }"
     * @param pattern the code that represents a local class
     * @param constraint
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt( String pattern, Predicate<LocalClassDeclarationStmt> constraint) {
        return new $stmt( Stmt.localClassStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<ReturnStmt> returnStmt() {
        return new $stmt(ReturnStmt.class, "$returnStmt$");
    }

    /**
     *
     * @param rs
     * @return
     */
    public static $stmt<ReturnStmt> returnStmt(ReturnStmt rs){
        return new $stmt(rs);
    }

    /** 
     * i.e."return VALUE;" 
     * @param pattern
     * @return 
     */
    public static $stmt<ReturnStmt> returnStmt( String... pattern ) {
        return new $stmt( Stmt.returnStmt(pattern));
    }
    
    /** 
     * i.e."return VALUE;" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ReturnStmt> returnStmt( String pattern, Predicate<ReturnStmt> constraint ) {
        return new $stmt( Stmt.returnStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<SwitchStmt> switchStmt() {
        return new $stmt(SwitchStmt.class, "$switchStmt$");
    }

    /**
     *
     * @param ss
     * @return
     */
    public static $stmt<SwitchStmt> switchStmt(SwitchStmt ss) {
        return new $stmt(ss);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $stmt<SwitchStmt> switchStmt( String... pattern ) {
        return new $stmt( Stmt.switchStmt(pattern));
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<SwitchStmt> switchStmt( String pattern, Predicate<SwitchStmt> constraint) {
        return new $stmt( Stmt.switchStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<SynchronizedStmt> synchronizedStmt() {
        return new $stmt(SynchronizedStmt.class, "$synchronizedStmt$" );
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $stmt<SynchronizedStmt> synchronizedStmt( String... pattern ) {
        return new $stmt( Stmt.synchronizedStmt(pattern));
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<SynchronizedStmt> synchronizedStmt( String pattern, Predicate<SynchronizedStmt> constraint ) {
        return new $stmt( Stmt.synchronizedStmt(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<ThrowStmt> throwStmt( ) {
        return new $stmt(ThrowStmt.class, "$throwStmt$");
    }


    /**
     *
     * @param ts
     * @return
     */
    public static $stmt<ThrowStmt> throwStmt( ThrowStmt ts ){
        return new $stmt( ts);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $stmt<ThrowStmt> throwStmt( String... pattern ) {
        return new $stmt( Stmt.throwStmt(pattern));
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ThrowStmt> throwStmt( String pattern, Predicate<ThrowStmt> constraint) {
        return new $stmt( Stmt.throwStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<TryStmt> tryStmt( ) {
        return new $stmt(TryStmt.class, "$tryStmt$" );
    }

    /**
     *
     * @param ts
     * @return
     */
    public static $stmt<TryStmt> tryStmt(TryStmt ts ){
        return new $stmt(ts);
    }

    /** 
     * i.e."try{ clazz.getMethod("fieldName"); }" 
     * @param pattern
     * @return 
     */
    public static $stmt<TryStmt> tryStmt( String... pattern ) {
        return new $stmt( Stmt.tryStmt(pattern));
    }

    /** 
     * i.e."try{ clazz.getMethod("fieldName"); }" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<TryStmt> tryStmt( String pattern, Predicate<TryStmt> constraint ) {
        return new $stmt( Stmt.tryStmt(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<WhileStmt> whileStmt( ) {
        return new $stmt(WhileStmt.class, "$whileStmt$");
    }
    
    /** 
     * i.e."while(i< 1) { ...}"
     * @param pattern
     * @return 
     */
    public static $stmt<WhileStmt> whileStmt( String... pattern ) {
        return new $stmt( Stmt.whileStmt(pattern));
    }

    /**
     *
     * @param ws
     * @return
     */
    public static $stmt<WhileStmt> whileStmt( WhileStmt ws){
        return new $stmt( ws);
    }

    /** 
     * i.e."while(i< 1) { ...}"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<WhileStmt> whileStmt( String pattern, Predicate<WhileStmt> constraint ) {
        return new $stmt( Stmt.whileStmt(pattern)).$and(constraint);
    }

    /**
     * Optional matching predicate applied to matches to ensure 
     * not only pattern match
     */
    public Predicate<T> constraint = t-> true;
    
    /** The stencil representing the statement */
    public Stencil stmtStencil;

    /** the class of the statement */
    public Class<T> statementClass;

    private $stmt( Class<T> statementClass ){
        this( statementClass, "$any$"); 
    }
    
    private $stmt( Class<T> statementClass, String pattern){
        this.constraint = t->true;
        this.statementClass = statementClass;
        this.stmtStencil = Stencil.of(pattern);
    }
    
    private $stmt( Class<T> statementClass, Predicate<T> constraint ){
        this.statementClass = statementClass;
        this.stmtStencil = Stencil.of("$any$");
        this.constraint = constraint;        
    }
    
    public $stmt( T st ){
        this.statementClass = (Class<T>)st.getClass();
        this.stmtStencil = Stencil.of( st.toString(NO_COMMENTS) );
    }

    public $stmt<T> $and(Predicate<T> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public T fill(Object...values){
        String str = stmtStencil.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (T)Stmt.of( str);
    }

    @Override
    public $stmt $(String target, String $paramName) {
        this.stmtStencil = this.stmtStencil.$(target, $paramName);
        return this;
    }

    /**
     * Convert (normalize) the expr to an Expression, then to a String
     * @param expr
     * @param $name
     * @return
     */
    public $stmt $expr( String expr, String $name){
        return $(Ex.of(expr).toString(), $name);
    }

    /**
     * 
     * @param expr
     * @param $name
     * @return 
     */
    public $stmt $(Expression expr, String $name ){
        String exprString = expr.toString();
        return $(exprString, $name);
    }
    
    /**
     * WE need to have custom logic to find the statements WITHIN statements...
     * for example if we have a if statement with a body: <PRE>
     * if( a ){
     *     doSomething();
     * }</PRE>
     * //and we want to parameterize the "doSomething();" method,
     * we need to handle all of the whitespace/indents, line feeds so that our 
     * Template looks like this:<PRE>
     * if(a){$name$}
     * </PRE>
     * 
     * ... Note that this will match ALL of the following :<PRE>
     * if(a){}
     * if(a){
     *     singleStatement();
     * }
     * if(a){
     *     multi(); 
     *     statement();
     * }
     * </PRE>
     * 
     * IF we DID NOT first treat the whitespace around the statement as a part
     * of the parameter, then our template Would look like this:<PRE>
     * 
     * if(a){
     *     $name$
     * }
     * </PRE>
     * and it would only match:<PRE>
     * if(a) {
     *     singleStatement();
     * }
     * </PRE>
     * 
     * @param stmt the statement to parameterize
     * @param $name the name used for the statement parameter
     * @return the modified $stmt
     */
    public $stmt $(Statement stmt, String $name ){
        String stmtString = stmt.toString( Ast.PRINT_NO_COMMENTS );
        
        List<String> stringsToReplace = new ArrayList<>();
        String fixedText  = this.stmtStencil.getTextForm().getFixedText();
        int nextInd = fixedText.indexOf(stmtString);        
        while( nextInd >= 0 ){
            String padded = Text.matchNextPaddedTarget(fixedText, stmtString, nextInd );
            stringsToReplace.add( padded );
            nextInd = fixedText.indexOf(stmtString, nextInd + stmtString.length() );
        }
        for(int i=0;i<stringsToReplace.size();i++){
            int indexOfAssert = this.stmtStencil.getTextForm().getFixedText().indexOf( stringsToReplace.get(i) );
            this.stmtStencil = this.stmtStencil.$( stringsToReplace.get(i), $name);
        }
        return this;
    }

    @Override
    public T fill(Translator t, Object...values){

        List<String> keys = list$Normalized();
        if( values.length < keys.size() ){
            throw new _draftException("not enough values("+values.length+") to fill ("+keys.size()+") variables "+ keys);
        }
        Map<String,Object> kvs = new HashMap<>();
        for(int i=0;i<values.length;i++){
            kvs.put( keys.get(i), values[i]);
        }
        return draft( t, kvs );
    }

    @Override
    public T draft(Object...keyValues ){
        Tokens tokens = Tokens.of(keyValues);
        return (T) parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( tokens )), tokens );
    }
    
    @Override
    public T draft(Translator t, Object...keyValues ){
        Tokens tokens = Tokens.of(keyValues);
        return (T) parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( t, tokens )), tokens );
    }

    @Override
    public T draft(Map<String,Object> tokens ){
        return (T) parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( tokens )), tokens );
    }
    
    /**
     * 
     * @param _n
     * @return 
     */
    public T draft(_node _n ){
        Map<String,Object> decons = _n.tokenize();
        return (T) draft( decons );
    }

    @Override
    public T draft(Translator t, Map<String,Object> tokens ){
        return (T) parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( t, tokens )), tokens );
    }

    public boolean match( Node node ) {
        if (node instanceof Statement) {
            return matches((Statement) node);
        }
        return false;
    }

    /**
     * 
     * @param stmt
     * @return 
     */
    public boolean matches( String...stmt ){
        return matches( Stmt.of(stmt));
    }

    /**
     * 
     * @param astStmt
     * @return 
     */
    public boolean matches( Statement astStmt ){        
        Select sel = select(astStmt);
        return sel != null;
    }

    @Override
    public List<String> list$(){        
        return this.stmtStencil.list$();
    }

    @Override
    public List<String> list$Normalized(){
        return this.stmtStencil.list$Normalized();
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $stmt hardcode$( Translator translator, Tokens kvs ) {
        this.stmtStencil = this.stmtStencil.hardcode$(translator, kvs);
        return this;
    }

    public Select<T> select(String s){
        return select( new String[]{s});
    }

    /**
     * 
     * @param stmt
     * @return 
     */
    public Select<T> select(String...stmt){
        try{
            return select(Stmt.of(stmt));
        }catch(Exception e){
            return null;
        }
    }
    
    public boolean isMatchAny(){
        try{
            return this.constraint.test(null) 
                && this.statementClass == Statement.class 
                && this.stmtStencil.isMatchAny();
        }catch(Exception e){
            return false;
        }
    }
    
    /**
     * 
     * @param astStmt
     * @return 
     */
    public Select<T> select( Statement astStmt ){
        if( astStmt == null ){
            return null;
        }
        if( !statementClass.isAssignableFrom(astStmt.getClass())){
            return null;
        }
        T t = (T)astStmt;
        if( ! constraint.test(t)){
            return null;
        }
        Tokens st = this.stmtStencil.parse(astStmt.toString(NO_COMMENTS));
        if( st == null ){
            return null;
        }      
        return new Select( astStmt, $tokens.of(st) );
    }

    /**
     * Returns the first Statement that matches the 
     * @param _j
     * @return 
     */
    @Override
    public Select<T> selectFirstIn( _model _j){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return selectFirstIn(_t.ast());
        }
        if( _j instanceof _body ){
            return selectFirstIn( ((_body)_j).ast() );
        }
        return selectFirstIn( ((_node) _j).ast() );
    }
     

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @return a Select containing the Statement and the key value pairs from the pattern
     */
    @Override
    public Select<T> selectFirstIn( Node astNode ){
        Optional<T> f = astNode.findFirst(this.statementClass, s -> this.matches(s) );         
        if( f.isPresent()){
            return this.select(f.get());
        }
        return null;
    }

    /**
     * Returns the first Statement that matches the 
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public Select<T> selectFirstIn(_model _n, Predicate<Select<T>> selectConstraint ){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return selectFirstIn( ((_code) _n).astCompilationUnit(), selectConstraint );
            }
            return selectFirstIn( ((_type)_n).ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_n).ast(), selectConstraint );
    }

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @param selectConstraint 
     * @return a Select containing the Statement and the key value pairs from the pattern
     */
    public Select<T> selectFirstIn( Node astNode, Predicate<Select<T>> selectConstraint ){
        Optional<T> f = astNode.findFirst(this.statementClass, s -> {
            Select<T> sel = this.select(s); 
            return sel != null && selectConstraint.test(sel);
            });                         
                //s -> this.matches(s) );         
        if( f.isPresent()){
            return this.select(f.get());
        }
        return null;
    }
    
    /**
     * Returns the first Statement that matches the 
     * @param astStartNode the
     * @param statementMatchFn 
     * @return 
     */
    @Override
    public T firstIn(Node astStartNode, Predicate<T> statementMatchFn ){
        Optional<T> f = astStartNode.findFirst(this.statementClass, s ->{
            Select sel = select(s);
            return sel != null && statementMatchFn.test((T)sel.astStatement);
        });         
        if( f.isPresent()){
            return f.get();
        }
        return null;
    }    
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<T> statementMatchFn, Consumer<T> statementActionFn){
        astNode.walk(this.statementClass, e-> {
            Select sel = select(e);
            if( sel != null && statementMatchFn.test((T)sel.astStatement) ) {
                statementActionFn.accept( e);
            }
        });
        return astNode;
    }
    
    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectedActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select<T>> selectedActionFn){
        astNode.walk(this.statementClass, e-> {
            Select<T> sel = select( e );
            if( sel != null ){
                selectedActionFn.accept( sel );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectedActionFn
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select<T>> selectedActionFn){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectedActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Consumer<Select<T>> selectedActionFn){
        _walk.in(_j, this.statementClass, e->{
            Select<T> sel = select( e );
            if( sel != null ){
                selectedActionFn.accept( sel );
            }
        });
        return _j;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectedActionFn){
        astNode.walk(this.statementClass, e-> {
            Select<T> sel = select( e );
            if( sel != null  && selectConstraint.test(sel) ){
                selectedActionFn.accept( sel );
            }
        });
        return astNode;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <CT extends _type> CT forSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectedActionFn){
        return forSelectedIn((CT)_java.type(clazz), selectConstraint, selectedActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _model> _J forSelectedIn(_J _j, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectedActionFn){
        _walk.in(_j, this.statementClass, e->{
            Select<T> sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectedActionFn.accept( sel );
            }
        });
        return _j;
    }
    
    /** Write the Statements without comments (for matching, comparison) */
    public static final PrettyPrinterConfiguration NO_COMMENTS = 
        new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false);

    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public List<Select<T>> listSelectedIn(Class clazz){
        return listSelectedIn((_type)_java.type(clazz));
    }
    
    @Override
    public List<Select<T>> listSelectedIn(Node astNode ){
        List<Select<T>>sts = new ArrayList<>();
        astNode.walk(this.statementClass, st-> {
            Select sel = select(st);
            if( sel != null ){
                sts.add( sel); //new Select( (T)st, tokens) );
            }
        });
        return sts;
    }

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select<T>> listSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint ){
        return listSelectedIn( (_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select<T>> listSelectedIn(Node astNode, Predicate<Select<T>> selectConstraint ){
        List<Select<T>>sts = new ArrayList<>();
        astNode.walk(this.statementClass, st-> {
            Select sel = select(st);
            if( sel != null && selectConstraint.test(sel)){
                sts.add( sel); //new Select( (T)st, tokens) );
            }
        });
        return sts;
    }
    
    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select<T>> listSelectedIn(_model _j, Predicate<Select<T>> selectConstraint ){
        List<Select<T>>sts = new ArrayList<>();
        _walk.in(_j, this.statementClass, st->{
            Select sel = select(st);
            if (sel != null && selectConstraint.test(sel)){
                sts.add(sel);
            }
        });
        return sts;
    }
    
    /**
     * 
     * @param clazz
     * @param $repl
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $stmt $repl){
        return (_CT)replaceIn( (_type)_java.type(clazz), $repl);
    }
    
    /**
     * 
     * @param clazz
     * @param replacement
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn( Class clazz, String...replacement){
        return (_CT)replaceIn( (_type)_java.type(clazz), replacement);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $repl
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, $stmt $repl ){
        $stmts $sn = new $stmts($repl);
        return replaceIn(_j, $sn);
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param statment_s
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, String... statment_s ){
        $stmts $sn = $stmts.of(statment_s);
        return replaceIn(_j, $sn);
    }    

    /**
     * 
     * @param <_J>
     * @param _j
     * @param $protoReplacement
     * @return 
     */
    public <_J extends _model> _J replaceIn(_J _j, $stmts $protoReplacement ){
        AtomicInteger ai = new AtomicInteger(0);
        _walk.in(_j, this.statementClass, st->{
            $stmt.Select sel = select( st );
            if( sel != null ){
                //construct the replacement snippet
                List<Statement> replacements = $protoReplacement.draft(sel.tokens);

                //Statement firstStmt = sel.statements.get(0);
                //Node par = firstStmt.getParentNode().get();
                //NodeWithStatements parentNode = (NodeWithStatements)par;
                //int addIndex = par.getChildNodes().indexOf( firstStmt );
                LabeledStmt ls = Stmt.labeledStmt("$replacement$:{}");
                // we want to add the contents of the replacement to a labeled statement,
                // because, (if we did it INLINE, we could  end up in an infinite loop, searching the
                // tree up to a cursor, then adding some code AT the cursor, then finding a match within the added
                // code, then adding more code, etc. etc.
                // this way, WE ADD A SINGLE LABELED STATEMENT AT THE LOCATION OF THE FIRST MATCH (which contains multiple statements)
                // then, we move to the next statement
                for(int i=0;i<replacements.size(); i++){
                    ls.getStatement().asBlockStmt().addStatement( replacements.get(i) );
                }
                sel.astStatement.replace( ls );
                //parentNode.addStatement(addIndex +1, ls);
                //removeIn all but the first statement
                //sel.statements.forEach( s-> s.removeIn() );
                //System.out.println("PAR AFTER Remove "+ par );
            }
        });
        if( _j instanceof _node ){
            Ast.flattenLabel( ((_node) _j).ast(), "$replacement$");
        }
        return (_J) _j;
    }
    
    @Override
    public String toString(){
        return "$stmt{ ("+this.statementClass.getSimpleName()+") : \""+ this.stmtStencil +"\" }";
    }

    /**
     * After we've constructed the body from the String based Stencil...
     * 
     * we look for Labeled Statements with label starts with $, i.e.<PRE>
     * 
     * $callSuperEquals: eq = super.typesEqual(proxy) && eq;
     * </PRE>
     * 
     * when we encounter the first type (a labeled statement with code...)
     * <PRE>$callSuperEquals: eq = super.typesEqual($b$) && eq;</PRE>
     * we look know to look for the parameter "callSuperEquals" when we are 
     * constructing the _body...
     * 
     * IF the value for "callSuperEquals" is null, or FALSE
     * then there is no trace of it in the output
     * 
     * IF the value "callSuperEquals" is a Statement or List<Statement> we replace
     * the contents of the labelStatement "callSuperEquals" with the statement/(s)
     * 
     * IF the value of "callSuperEquals" is anything else, we add the statment(s)
     * 
     * <PRE>super.typesEqual(proxy) && eq;</PRE>
     * 
     * and we remove/flatten the label where only the statement remains in the body
     * 
     * @param stmt
     * @param tokens
     * @return the (potentially modified) statement
     */
    public static Statement construct$LabelStmt( Statement stmt, Map<String,Object> tokens ){
        if( stmt instanceof LabeledStmt && stmt.asLabeledStmt().getLabel().asString().startsWith("$") ){
            return labelStmtReplacement(stmt.asLabeledStmt(), tokens);
        }
        return parameterize$LabeledStmt( stmt, tokens);
    } 
    
    /**
     * Walks AST nodes looking for a $labeledStmt, if found will replace
     * the labeled stmt with a parameter
     * @param <N>
     * @param node
     * @param tokens
     * @return 
     */
    public static <N extends Node> N parameterize$LabeledStmt(N node, Map<String,Object> tokens ){
        //separate into (2) operations, (dont WALK and MUTATE at the same time)
        List<LabeledStmt> lss  = _walk.list(node, LabeledStmt.class, ls-> ls.getLabel().asString().startsWith("$") );
        lss.forEach(ls-> {
            //System.out.println( "  Found "+ ls+" in "+ node);
            Statement st = labelStmtReplacement(ls, tokens);
            //System.out.println( "  REPLACING WITH "+ st);
            if( st.isEmptyStmt() || (st.isBlockStmt() && st.asBlockStmt().isEmpty()) ){
                //System.out.println( "  ST IS EMPTY REMOVE FORCED !!!");
                //ls.removeForced();

                boolean rem = ls.remove(); //getParentNode().get().remove(ls);
                if( !rem ){
                    //System.out.println( "    COULDNT REMOVE!!!!!!!!!!!!");
                    ls.replace( st );
                }
                //System.out.println( "  AFTERWARDS "+ node);

            } else{
                LabeledStmt $TO_REPLACE = Stmt.labeledStmt("$TO_REPLACE: {}");
                $TO_REPLACE.setStatement(st);
                ls.replace( $TO_REPLACE );
                Ast.flattenLabel(node, "$TO_REPLACE");
            }
        });
        return node;
    }
    
    private static Statement labelStmtReplacement(LabeledStmt ls, Map<String,Object> tokens){
        //System.out.println("Found labeled Statenm " +ls.getLabel() );
        String name = ls.getLabel().asString().substring(1);
        Object value = tokens.get(name);
        
        //HIDE:
        if( value == null || value == Boolean.FALSE ){
            return new EmptyStmt();
        }
        //OVERRIDE: with a static Statement 
        else if( value instanceof Statement) {
            return (Statement) value;
        }
        //OVERRIDE: with a String Statement
        else if( value instanceof String ){
            return Stmt.of( (String)value);
        }
        //OVERRIDE: with a proto Statement
        else if( value instanceof $stmt ){ 
            return (($stmt)value).draft(tokens);
        }
        //SHOW (just remove the $label:)
        return ls.getStatement();        
    }

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are suppressed.
     */
    public static class Or extends $stmt {

        final List<$stmt>ors = new ArrayList<>();

        public Or($stmt...$as){
            super(Statement.class);
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $stmt hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$stmt.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param n
         * @return
         */
        public $stmt.Select select(Statement n){
            $stmt $a = whichMatch(n);
            if( $a != null ){
                return $a.select(n);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $method that matches the Method or null if none of the match
         * @param stmt
         * @return
         */
        public $stmt whichMatch(Statement stmt){
            if( !this.constraint.test(stmt ) ){
                return null;
            }
            Optional<$stmt> orsel  = this.ors.stream().filter( $p-> $p.matches(stmt) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * 
     * @param <T> 
     */
    public static class Select<T extends Statement> implements $pattern.selected,
            selectAst<T> {
        
        public T astStatement;
        public $tokens tokens;
        
        public Select( T astStatement, $tokens tokens){
            this.astStatement = astStatement;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$stmt.Select{"+ System.lineSeparator()+
                Text.indent(astStatement.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public T ast() {
            return astStatement;
        }
    }
}
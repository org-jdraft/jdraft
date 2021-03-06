package org.jdraft.pattern;

import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.printer.PrettyPrintVisitor;
import com.github.javaparser.printer.PrettyPrinterConfiguration;

import java.util.*;
import java.util.function.*;

import org.jdraft.*;
import org.jdraft.Expr.QuadConsumer;
import org.jdraft.Expr.TriConsumer;
import org.jdraft.text.*;
import org.jdraft.walk.Walk;

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
 * @param <S> AST Statement implementation type
 * @param <_S> _java._domain implementation type
 */
public class $stmt<S extends Statement, _S extends _stmt>
    implements Template<_S>, $pattern.$java<_S, $stmt<S, _S>>, $body.$part, $method.$part, $constructor.$part {

    public Class<_S> _modelType(){
        return (Class<_S>) _stmt.class;
    }

    /**
     * This allows Statements to be commented out or uncommented in a conventional way.
     * the "convention" is
     * the "comment" MUST START with the text "<code&rt;" (preliminary line feeds and white space allowed)
     * the "comment" MUST END with the text "</code&rt;" (line feeds and white space allowed after the end)
     *
     * Specifically, if we have:<PRE>
     * class MyClass{
     *     void m(){
     *         /*<code>System.out.println(123);</code>* /
     *         //<code>assert(1==1);</code>
     *         /**<code>System.exit(-1);</code>* /
     *     }
     * }
     *
     * // uncomment ALL Statements in code (that follow the convention)
     * _class _c = $stmt.of().unComment(MyClass.class);
     *  // since all of the commented out Statements match $stmt.of(), they are all uncommented
     * // RESULTS:
     * class MyClass{
     *     void m(){
     *         System.out.println(123);
     *         assert(1==1);
     *         System.exit(-1);
     *     }
     * }
     * // UNcomment Statements that are System.out.println() Statements
     * _class _c = $stmt.of("System.out.println($any$);").unComment(MyClass.class);
     *
     *  //RESULTS : (ONLY uncomment System.out.println() statements )
     * class MyClass{
     *     void m(){
     *         System.out.println(123);
     *         //<code>assert(1==1);</code>
     *         /**<code>System.exit(-1);</code>* /
     *     }
     * }
     * </PRE>

    public static $comment STATEMENT_COMMENT = $comment.STATEMENT_COMMENT;
    */

    /**
     * 
     * @param ste
     * @return 
     */
    private static <S extends Statement, _S extends _stmt> $stmt<S, _S> from(StackTraceElement ste ){
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
    public static <S extends Statement, _S extends _stmt> $stmt<S, _S> of(Class<S> stmtClass, String...pattern){
        return new $stmt<S, _S>(stmtClass, Text.combine(pattern));
    }

    /**
     * 
     * @param <S>
     * @param proto
     * @return 
     */
    public static <S extends Statement, _S extends _stmt> $stmt<S, _S> of(Expr.Command proto ){
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
    public static <T extends Object, S extends Statement, _S extends _stmt> $stmt<S, _S> of(Consumer<T> proto ){
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
    public static <T extends Object, U extends Object, S extends Statement, _S extends _stmt> $stmt<S, _S> of(BiConsumer<T,U> proto ){
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
    public static <T extends Object, U extends Object, V extends Object, S extends Statement, _S extends _stmt> $stmt<S, _S> of(TriConsumer<T, U, V> proto ){
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
    public static <T extends Object, U extends Object, V extends Object, X extends Object, S extends Statement, _S extends _stmt> $stmt<S, _S> of(QuadConsumer<T, U, V, X> proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Will match ANY statement, or empty statemen
     * @param stmtClasses the classes accepted for the Statement
     * @return
     */
    public static $stmt<Statement, _stmt> not(Class<? extends Statement>... stmtClasses){
        Set<Class<? extends Statement>> notClasses = new HashSet<>();
        Arrays.stream(stmtClasses).forEach( s -> notClasses.add(s));
        Predicate<Statement> ps = s-> notClasses.contains(s.getClass());
        return ($stmt<Statement, _stmt>) new $stmt( Statement.class, t-> true )
                .$not( ps );
    }

    /**
     * A Statement that contains this expression
     * @param $ex the expression contained within the Statement
     * @return the new Statement matching only Statements that contain this expression
     */
    public static $stmt<Statement, _stmt> of($ex $ex ){
        return $stmt.of().$and( s-> $ex.isIn(s) );
    }

    /**
     * A Statement
     * @param $exprs
     * @return
     */
    public static $stmt<Statement, _stmt> hasAny($ex...$exprs ){
        return $stmt.of().$and( s-> Arrays.stream($exprs).anyMatch( e -> e.isIn(s) ));
    }

    /**
     * A Statement
     * @param $exprs
     * @return
     */
    public static $stmt<Statement, _stmt> hasAll($ex...$exprs ){
        return $stmt.of().$and( s-> Arrays.stream($exprs).allMatch( e -> e.isIn(s) ));
    }

    /**
     * Match ONLY statements of these classes
     * @param stmtClasses the classes accepted for the Statement
     * @return
     */
    public static $stmt<Statement, _stmt> of(Class<? extends Statement>... stmtClasses){
        Set<Class<? extends Statement>> notClasses = new HashSet<>();
        Arrays.stream(stmtClasses).forEach( s -> notClasses.add(s));
        Predicate<Statement> ps = s-> notClasses.contains(s.getClass());
        return ($stmt<Statement, _stmt>) new $stmt( Statement.class, t-> true )
                .$and( ps );
    }

    /** 
     * Will match ANY statement, or empty statemen
     * @param <S>
     * @return 
     */
    public static <S extends Statement, _S extends _stmt> $stmt<S, _S> of(){
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
    public static <S extends Statement, _S extends _stmt> $stmt<S,_S> of(Object anonymousObjectWithStatement ){
        ObjectCreationExpr oce = Expr.newExpr( Thread.currentThread().getStackTrace()[2]);
        BlockStmt bs = oce.findFirst(com.github.javaparser.ast.stmt.BlockStmt.class).get();

        //?? do I want to do anything with $label$:{} ?
        //return new $stmt<S>( );
        return of( bs.getStatement(0) );
    }

    public static <S extends Statement, _S extends _stmt> $stmt<S, _S> of(String prototypePattern ){
        return of( new String[] {prototypePattern});
    }

    /**
     *
     * @param pattern
     * @return 
     */
    public static <S extends Statement, _S extends _stmt> $stmt<S,_S> of(String...pattern ){
        S st = (S) Stmt.of(pattern);
        return new $stmt<S, _S>(st);
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
    public static $stmt<AssertStmt, _assertStmt> assertStmt(){
        return new $stmt( AssertStmt.class, "$assertStmt$" );
    } 
    
    /**
     * i.e."assert(1==1);"
     * @param pattern
     * @return and AssertStmt with the code
     */
    public static $stmt<AssertStmt, _assertStmt> assertStmt(String... pattern ) {
        return new $stmt( Stmt.assertStmt(pattern));
    }

    /**
     * i.e."assert(1==1);"
     * @param pattern
     * @param constraint
     * @return and AssertStmt with the code
     */
    public static $stmt<AssertStmt, _assertStmt> assertStmt(String pattern, Predicate<AssertStmt> constraint) {
        return new $stmt( Stmt.assertStmt(pattern) ).$and(constraint);
    }
    
    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<BlockStmt, _blockStmt> blockStmt(){
        return new $stmt( BlockStmt.class, "$blockStmt$" );
    }

    /**
     *
     * @param block
     * @return
     */
    public static $stmt<BlockStmt, _blockStmt> blockStmt(BlockStmt block){
        return new $stmt( block );
    }

    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param pattern the code making up the blockStmt
     * @return the BlockStmt
     */
    public static $stmt<BlockStmt, _blockStmt> blockStmt(String... pattern ) {
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
    public static $stmt<BlockStmt, _blockStmt> blockStmt(String pattern, Predicate<BlockStmt> constraint) {
        return new $stmt( Stmt.blockStmt(pattern)).$and(constraint);
    }

    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<BreakStmt, _breakStmt> breakStmt(){
        return new $stmt( BreakStmt.class, "$breakStmt$" );
    } 

    /**
     * i.e."break;" or "break outer;"
     * @param pattern String representing the break of
     * @return the breakStmt
     */
    public static $stmt<BreakStmt, _breakStmt> breakStmt(String... pattern ) {
        return new $stmt( Stmt.breakStmt(pattern));
    }

    /**
     * i.e."break;" or "break outer;"
     * @param constraint
     * @return the breakStmt
     */
    public static $stmt<BreakStmt, _breakStmt> breakStmt(Predicate<_breakStmt> constraint) {
        return breakStmt().$and(constraint);
    }

    /**
     * i.e."break;" or "break outer;"
     * @param pattern String representing the break of
     * @param constraint
     * @return the breakStmt
     */
    public static $stmt<BreakStmt, _breakStmt> breakStmt(String pattern, Predicate<BreakStmt> constraint) {
        return new $stmt( Stmt.breakStmt(pattern)).$and(constraint);
    }
        
    /**
     * Returns a prototype that matches ANY continueStmt
     * @return 
     */
    public static $stmt<ContinueStmt, _continueStmt> continueStmt(){
        return new $stmt( ContinueStmt.class, "$continueStmt$" );
    }
    
    /** 
     * i.e."continue outer;" 
     * @param pattern
     * @return 
     */
    public static $stmt<ContinueStmt, _continueStmt> continueStmt(String... pattern ) {
        return new $stmt( Stmt.continueStmt(pattern));
    }

    
    /** 
     * i.e."continue outer;" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ContinueStmt, _continueStmt> continueStmt(String pattern, Predicate<ContinueStmt> constraint) {
        return new $stmt( Stmt.continueStmt(pattern)).$and(constraint);
    }

    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $doStmt doStmt(){
        return $doStmt.of();

    }

    /**
     * i.e."do{ System.out.println(1); }while( a < 100 );"
     * @param ds
     * @return
     */
    public static $stmt<DoStmt, _doStmt> doStmt( DoStmt ds) {
        return new $stmt( ds);
    }

    /** 
     * i.e."do{ System.out.println(1); }while( a < 100 );" 
     * @param pattern
     * @return 
     */
    public static $stmt<DoStmt, _doStmt> doStmt(String... pattern ) {
        return new $stmt( Stmt.doStmt(pattern));
    }

    /** 
     * i.e."do{ System.out.println(1); }while( a < 100 );" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<DoStmt, _doStmt> doStmt(String pattern, Predicate<DoStmt> constraint) {
        return new $stmt( Stmt.doStmt(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<EmptyStmt, _emptyStmt> emptyStmt(){
        return new $stmt( new EmptyStmt() );
    }
    

    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> constructorCallStmt(){
        return new $stmt( ExplicitConstructorInvocationStmt.class, "$constructorCallStmt$" );
    }

    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> constructorCallStmt(String... pattern ) {
        return new $stmt( Stmt.of(pattern));
    }

    /**
     * i.e."this(100,2900);"
     * @param cts
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> constructorCallStmt(ExplicitConstructorInvocationStmt cts) {
        return new $stmt( cts );
    }

    /**
     *
     * Returns a pattern that matches ANY thisOrSuperCallStmt
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> thisCallStmt(){
        return new $stmt( ExplicitConstructorInvocationStmt.class, "$thisCallStmt$" );
    } 

    /** 
     * i.e."this(100,2900);" 
     * @param pattern
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> thisCallStmt(String... pattern ) {
        return new $stmt( Stmt.constructorCallStmt(pattern));
    }

    /**
     * i.e."this(100,2900);"
     * @param cts
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> thisCallStmt(ExplicitConstructorInvocationStmt cts) {
        return new $stmt( cts );
    }

    /** 
     * i.e."this(100,2900);" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> thisCallStmt(String pattern, Predicate<ExplicitConstructorInvocationStmt> constraint) {
        return new $stmt( Stmt.constructorCallStmt(pattern)).$and(constraint);
    }

    /**
     *
     * Returns a pattern that matches ANY thisOrSuperCallStmt
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> superCallStmt(){
        return new $stmt( ExplicitConstructorInvocationStmt.class, "$superCallStmt$" );
    }

    /**
     * i.e."this(100,2900);"
     * @param pattern
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> superCallStmt(String... pattern ) {
        return new $stmt( Stmt.constructorCallStmt(pattern));
    }

    /**
     * i.e."this(100,2900);"
     * @param cts
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> superCallStmt(ExplicitConstructorInvocationStmt cts) {
        return new $stmt( cts );
    }

    /**
     * i.e."this(100,2900);"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt, _constructorCallStmt> superCallStmt(String pattern, Predicate<ExplicitConstructorInvocationStmt> constraint) {
        return new $stmt( Stmt.constructorCallStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."s += t;" 
     * @return 
     */
    public static $stmt<ExpressionStmt, _exprStmt> expressionStmt() {
        return new $stmt( ExpressionStmt.class, "$expressionStmt$");
    }

    /**
     * i.e."s += t;"
     * @param es
     * @return
     */
    public static $stmt<ExpressionStmt, _exprStmt> expressionStmt(ExpressionStmt es) {
        return new $stmt( es);
    }

    /** 
     * i.e."s += t;" 
     * @param pattern
     * @return 
     */
    public static $stmt<ExpressionStmt, _exprStmt> expressionStmt(String... pattern ) {
        return new $stmt( Stmt.expressionStmt(pattern));
    }
    
    /** 
     * i.e."s += t;" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ExpressionStmt, _exprStmt> expressionStmt(String pattern, Predicate<ExpressionStmt> constraint) {
        return new $stmt( Stmt.expressionStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."s += t;" 
     * @return 
     */
    public static $stmt<ForStmt, _forStmt> forStmt( ) {
        return new $stmt( ForStmt.class, "$forStmt$");
    }

    /** 
     * i.e."for(int i=0; i<100;i++) {...}" 
     * @param pattern
     * @return 
     */
    public static $stmt<ForStmt, _forStmt> forStmt( String... pattern ) {
        return new $stmt( Stmt.forStmt(pattern));
    }
    
    /** 
     * i.e."for(int i=0; i<100;i++) {...}" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ForStmt, _forStmt> forStmt( String pattern, Predicate<ForStmt> constraint ) {
        return new $stmt( Stmt.forStmt(pattern)).$and(constraint);
    }

    /**
     * i.e."s += t;"
     * @return
     */
    public static $stmt<ForEachStmt, _forEachStmt> forEachStmt() {
        return new $stmt( ForEachStmt.class, "$forEachStmt$");
    }

    /**
     * i.e."s += t;"
     * @return
     */
    public static $stmt<ForEachStmt, _forEachStmt> forEachStmt(ForEachStmt fes) {
        return new $stmt( fes);
    }

    /** 
     * i.e."for(String element:arr){...}" 
     * @param pattern
     * @return 
     */
    public static $stmt<ForEachStmt, _forEachStmt> forEachStmt( String... pattern ) {
        return new $stmt( Stmt.forEachStmt(pattern));
    }
    
    /** 
     * i.e."for(String element:arr){...}" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ForEachStmt, _forEachStmt> forEachStmt( String pattern, Predicate<ForEachStmt> constraint) {
        return new $stmt( Stmt.forEachStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."if(a==1){...}" 
     * @return 
     */
    public static $stmt<IfStmt, _ifStmt> ifStmt( ) {
        return new $stmt( IfStmt.class, "$ifStmt$");
    }

    /**
     * i.e."if(a==1){...}"
     * @return
     */
    public static $stmt<IfStmt, _ifStmt> ifStmt( IfStmt is) {
        return new $stmt( is );
    }
    
    /** 
     * i.e."if(a==1){...}" 
     * @param pattern
     * @return 
     */
    public static $stmt<IfStmt, _ifStmt> ifStmt( String... pattern ) {
        return new $stmt( Stmt.ifStmt(pattern));
    }
    
    /** 
     * i.e."if(a==1){...}" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<IfStmt, _ifStmt> ifStmt( String pattern, Predicate<IfStmt> constraint) {
        return new $stmt( Stmt.ifStmt(pattern)).$and(constraint);
    }

    /** 
     * i.e."outer:   start = getValue();" 
     * @return 
     */
    public static $stmt<LabeledStmt, _labeledStmt> labeledStmt( ) {
        return new $stmt( LabeledStmt.class, "$labeledStmt$");
    }

    /**
     * i.e."outer:   start = getValue();"
     * @param ls
     * @return
     */
    public static $stmt<LabeledStmt, _labeledStmt> labeledStmt( LabeledStmt ls) {
        return new $stmt( ls );
    }

    /** 
     * i.e."outer:   start = getValue();" 
     * @param pattern
     * @return 
     */
    public static $stmt<LabeledStmt, _labeledStmt> labeledStmt( String... pattern ) {
        return new $stmt( Stmt.labeledStmt(pattern));
    }

    /** 
     * i.e."outer:   start = getValue();" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<LabeledStmt, _labeledStmt> labeledStmt( String pattern, Predicate<LabeledStmt> constraint) {
        return new $stmt( Stmt.labeledStmt(pattern)).$and(constraint);
    }
    
    /**
     * i.e."class C{ int a, b; }"
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt, _localClassStmt> localClassStmt() {
        return new $stmt( LocalClassDeclarationStmt.class, "$localClass$");
    }

    /**
     *
     * @param lcs
     * @return
     */
    public static $stmt<LocalClassDeclarationStmt, _localClassStmt> localClassStmt(LocalClassDeclarationStmt lcs) {
        return new $stmt( lcs );
    }

    /**
     * Converts from a String to a LocalClass
     * i.e. "class C{ int a, b; }"
     * @param pattern the code that represents a local class
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt, _localClassStmt> localClassStmt( String... pattern ) {
        return new $stmt( Stmt.localClassDeclarationStmt(pattern));
    }
    
    /**
     * Converts from a String to a LocalClass
     * i.e."class C{ int a, b; }"
     * @param pattern the code that represents a local class
     * @param constraint
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt, _localClassStmt> localClassStmt( String pattern, Predicate<LocalClassDeclarationStmt> constraint) {
        return new $stmt( Stmt.localClassDeclarationStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<ReturnStmt, _returnStmt> returnStmt() {
        return new $stmt(ReturnStmt.class, "$returnStmt$");
    }

    /**
     *
     * @param rs
     * @return
     */
    public static $stmt<ReturnStmt, _returnStmt> returnStmt(ReturnStmt rs){
        return new $stmt(rs);
    }

    /** 
     * i.e."return VALUE;" 
     * @param pattern
     * @return 
     */
    public static $stmt<ReturnStmt, _returnStmt> returnStmt( String... pattern ) {
        return new $stmt( Stmt.returnStmt(pattern));
    }
    
    /** 
     * i.e."return VALUE;" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ReturnStmt, _returnStmt> returnStmt( String pattern, Predicate<ReturnStmt> constraint ) {
        return new $stmt( Stmt.returnStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<SwitchStmt, _switchStmt> switchStmt() {
        return new $stmt(SwitchStmt.class, "$switchStmt$");
    }

    /**
     *
     * @param ss
     * @return
     */
    public static $stmt<SwitchStmt, _switchStmt> switchStmt(SwitchStmt ss) {
        return new $stmt(ss);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $stmt<SwitchStmt, _switchStmt> switchStmt( String... pattern ) {
        return new $stmt( Stmt.switchStmt(pattern));
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<SwitchStmt, _switchStmt> switchStmt( String pattern, Predicate<SwitchStmt> constraint) {
        return new $stmt( Stmt.switchStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<SynchronizedStmt, _synchronizedStmt> synchronizedStmt() {
        return new $stmt(SynchronizedStmt.class, "$synchronizedStmt$" );
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $stmt<SynchronizedStmt, _synchronizedStmt> synchronizedStmt( String... pattern ) {
        return new $stmt( Stmt.synchronizedStmt(pattern));
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<SynchronizedStmt, _synchronizedStmt> synchronizedStmt( String pattern, Predicate<SynchronizedStmt> constraint ) {
        return new $stmt( Stmt.synchronizedStmt(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<ThrowStmt, _throwStmt> throwStmt( ) {
        return new $stmt(ThrowStmt.class, "$throwStmt$");
    }


    /**
     *
     * @param ts
     * @return
     */
    public static $stmt<ThrowStmt, _throwStmt> throwStmt( ThrowStmt ts ){
        return new $stmt( ts);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $stmt<ThrowStmt, _throwStmt> throwStmt( String... pattern ) {
        return new $stmt( Stmt.throwStmt(pattern));
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ThrowStmt, _throwStmt> throwStmt( String pattern, Predicate<ThrowStmt> constraint) {
        return new $stmt( Stmt.throwStmt(pattern)).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<TryStmt, _tryStmt> tryStmt( ) {
        return new $stmt(TryStmt.class, "$tryStmt$" );
    }

    /**
     *
     * @param ts
     * @return
     */
    public static $stmt<TryStmt, _tryStmt> tryStmt(TryStmt ts ){
        return new $stmt(ts);
    }

    /** 
     * i.e."try{ clazz.getMethod("fieldName"); }" 
     * @param pattern
     * @return 
     */
    public static $stmt<TryStmt, _tryStmt> tryStmt( String... pattern ) {
        return new $stmt( Stmt.tryStmt(pattern));
    }

    /** 
     * i.e."try{ clazz.getMethod("fieldName"); }" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<TryStmt, _tryStmt> tryStmt( String pattern, Predicate<TryStmt> constraint ) {
        return new $stmt( Stmt.tryStmt(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<WhileStmt, _whileStmt> whileStmt( ) {
        return new $stmt(WhileStmt.class, "$whileStmt$");
    }
    
    /** 
     * i.e."while(i< 1) { ...}"
     * @param pattern
     * @return 
     */
    public static $stmt<WhileStmt, _whileStmt> whileStmt( String... pattern ) {
        return new $stmt( Stmt.whileStmt(pattern));
    }

    /**
     *
     * @param ws
     * @return
     */
    public static $stmt<WhileStmt, _whileStmt> whileStmt( WhileStmt ws){
        return new $stmt( ws);
    }

    /** 
     * i.e."while(i< 1) { ...}"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<WhileStmt, _whileStmt> whileStmt( String pattern, Predicate<WhileStmt> constraint ) {
        return new $stmt( Stmt.whileStmt(pattern)).$and(constraint);
    }

    /**
     * Match predicate tested against the AST {@link Statement} syntax implementation
     */
    public Predicate<_S> astMatch = s -> true;

    /**
     * Match predicate tested against the domain {@link _stmt} implementation

    public Predicate<_S> domainMatch = _s -> true;
     */
    
    /** The stencil representing the statement */
    public Stencil stmtStencil;

    /** the class of the statement */
    public Class<S> statementClass;

    protected $stmt( Class<S> statementClass ){
        this( statementClass, "$any$"); 
    }
    
    protected $stmt(Class<S> statementClass, String pattern){
        this.astMatch = s ->true;
        this.statementClass = statementClass;
        this.stmtStencil = Stencil.of(pattern);
    }
    
    protected $stmt(Class<S> statementClass, Predicate<_S> _matchFn){
        this.statementClass = statementClass;
        this.stmtStencil = Stencil.of("$any$");
        this.astMatch = _matchFn;
    }
    
    public $stmt( S st ){
        this.statementClass = (Class<S>)st.getClass();
        this.stmtStencil = Stencil.of( st.toString(PRINT_$LABELED_AS_EMBED) );
        //Hmm, I could just have a specific visitor that transforms
        // $label: assert($value$);
        //        TO

        /** New... for Embeds
        st.walk(LabeledStmt.class, ls-> {
            String labelName = ls.getLabel().asString();
            if( labelName.startsWith("$") ){
                //this is MEANT to be a Stencil.Embed
                Stencil embedBody = ls.getStatement()
            }
        });
        */
    }

    public $stmt<S, _S> $and(Predicate<_S> constraint ){
        this.astMatch = this.astMatch.and(constraint);
        return this;
    }

    /*
    public $stmt<S, _S> $and(Predicate<S> constraint ){
        this.astMatch = this.astMatch.and(constraint);
        return this;
    }
     */


    @Override
    public $stmt $(String target, String $paramName) {
        this.stmtStencil = this.stmtStencil.$(target, $paramName);
        return this;
    }

    @Override
    public Template<_S> $hardcode(Translator translator, Map<String, Object> keyValues) {
        return null;
    }

    /**
     * Convert (normalize) the expr to an Expression, then to a String
     * @param expr
     * @param $name
     * @return
     */
    public $stmt $expr( String expr, String $name){
        return $(Expr.of(expr).toString(), $name);
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
        String stmtString = stmt.toString( Print.PRINT_NO_COMMENTS );
        
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
    public _S fill(Object...values){
        String str = stmtStencil.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (_S) _stmt.of((S) Stmt.of( str));
    }


    @Override
    public _S fill(Translator t, Object...values){

        List<String> keys = $listNormalized();
        if( values.length < keys.size() ){
            throw new _jdraftException("not enough values("+values.length+") to fill ("+keys.size()+") variables "+ keys);
        }
        Map<String,Object> kvs = new HashMap<>();
        for(int i=0;i<values.length;i++){
            kvs.put( keys.get(i), values[i]);
        }
        return draft( t, kvs );
    }

    @Override
    public _S draft(Object...keyValues ){
        Tokens tokens = Tokens.of(keyValues);
        return (_S) _stmt.of( parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( tokens )), tokens ));
    }
    
    @Override
    public _S draft(Translator t, Object...keyValues ){
        Tokens tokens = Tokens.of(keyValues);
        return (_S) _stmt.of(parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( t, tokens )), tokens ));
    }

    @Override
    public _S draft(Map<String,Object> tokens ){
        return (_S) _stmt.of((S) parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( tokens )), tokens ));
    }

    @Override
    public _S draft(Translator t, Map<String,Object> tokens ){
        return (_S) _stmt.of(parameterize$LabeledStmt( Stmt.of(stmtStencil.draft( t, tokens )), tokens ));
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
    public List<String> $list(){
        return this.stmtStencil.$list();
    }

    @Override
    public List<String> $listNormalized(){
        return this.stmtStencil.$listNormalized();
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $stmt $hardcode(Translator translator, Tokens kvs ) {
        this.stmtStencil = this.stmtStencil.$hardcode(translator, kvs);
        return this;
    }

    public Select<S, _S> select(String s){
        return select( new String[]{s});
    }

    /**
     * 
     * @param stmt
     * @return 
     */
    public Select<S, _S> select(String...stmt){
        try{
            return select(Stmt.of(stmt));
        }catch(Exception e){
            return null;
        }
    }
    
    public boolean isMatchAny(){
        try{
            return this.astMatch.test(null)
                && this.statementClass == Statement.class 
                && this.stmtStencil.isMatchAny();
        }catch(Exception e){
            return false;
        }
    }


    public Select<S, _S> select(_stmt _s){
        if( _s == null ){
            return null;
        }
        if( !statementClass.isAssignableFrom(_s.node().getClass())){
            return null;
        }
        S s = (S)_s.node();
        if( ! astMatch.test((_S) _stmt.of(s))){
            return null;
        }
        Tokens st = this.stmtStencil.parse(_s.node().toString(NO_COMMENTS));
        if( st == null ){
            return null;
        }
        return new Select( _s.node(), $tokens.of(st) );
    }

    /**
     * 
     * @param astStmt
     * @return 
     */
    public Select<S, _S> select(Statement astStmt ){
        if( astStmt == null ){
            return null;
        }
        if( !statementClass.isAssignableFrom(astStmt.getClass())){
            return null;
        }
        S s = (S)astStmt;
        if( ! astMatch.test((_S) _stmt.of(s))){
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
    public Select<S, _S> selectFirstIn(_java._domain _j){
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return selectFirstIn(_t.node());
        }
        if( _j instanceof _body ){
            return selectFirstIn( ((_body)_j).ast() );
        }
        return selectFirstIn( ((_tree._node) _j).node() );
    }
     

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @return a Select containing the Statement and the key value pairs from the pattern
     */
    @Override
    public Select<S, _S> selectFirstIn(Node astNode ){
        Optional<S> f = astNode.findFirst(this.statementClass, s -> this.matches(s) );
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
    public Select<S, _S> selectFirstIn(_java._domain _n, Predicate<Select<S, _S>> selectConstraint ){
        if( _n instanceof _codeUnit){
            if( ((_codeUnit) _n).isTopLevel()){
                return selectFirstIn( ((_codeUnit) _n).astCompilationUnit(), selectConstraint );
            }
            return selectFirstIn( ((_type)_n).node(), selectConstraint);
        }
        return selectFirstIn( ((_tree._node)_n).node(), selectConstraint );
    }

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @param selectConstraint 
     * @return a Select containing the Statement and the key value pairs from the pattern
     */
    public Select<S, _S> selectFirstIn(Node astNode, Predicate<Select<S, _S>> selectConstraint ){
        Optional<S> f = astNode.findFirst(this.statementClass, s -> {
            Select<S, _S> sel = this.select(s);
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
    public _S firstIn(Node astStartNode, Predicate<_S> statementMatchFn ){
        Optional<S> f = astStartNode.findFirst(this.statementClass, s ->{
            Select sel = select(s);
            return sel != null && statementMatchFn.test((_S)sel._s);
        });         
        if( f.isPresent()){
            return (_S) _stmt.of(f.get());
        }
        return null;
    }    
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_S> statementMatchFn, Consumer<_S> statementActionFn){
        astNode.walk(this.statementClass, e-> {
            Select sel = select(e);
            if( sel != null && statementMatchFn.test((_S)sel._s) ) {
                statementActionFn.accept((_S)sel._s);
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
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select<S, _S>> selectedActionFn){
        astNode.walk(this.statementClass, e-> {
            Select<S,_S> sel = select( e );
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
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select<S, _S>> selectedActionFn){
        return (_CT)forSelectedIn((_type) _type.of(clazz), selectedActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select<S, _S>> selectedActionFn){
        Walk.in(_j, this.statementClass, e->{
            Select<S, _S> sel = select( e );
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
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<S, _S>> selectConstraint, Consumer<Select<S, _S>> selectedActionFn){
        astNode.walk(this.statementClass, e-> {
            Select<S, _S> sel = select( e );
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
    public <CT extends _type> CT forSelectedIn(Class clazz, Predicate<Select<S, _S>> selectConstraint, Consumer<Select<S, _S>> selectedActionFn){
        return forSelectedIn((CT) _type.of(clazz), selectConstraint, selectedActionFn);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select<S, _S>> selectConstraint, Consumer<Select<S,_S>> selectedActionFn){
        Walk.in(_j, this.statementClass, e->{
            Select<S, _S> sel = select( e );
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
    public List<Select<S, _S>> listSelectedIn(Class clazz){
        return listSelectedIn((_type) _type.of(clazz));
    }
    
    @Override
    public List<Select<S, _S>> listSelectedIn(Node astNode ){
        List<Select<S, _S>>sts = new ArrayList<>();
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
    public List<Select<S, _S>> listSelectedIn(Class clazz, Predicate<Select<S, _S>> selectConstraint ){
        return listSelectedIn( (_type) _type.of(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select<S, _S>> listSelectedIn(Node astNode, Predicate<Select<S, _S>> selectConstraint ){
        List<Select<S, _S>>sts = new ArrayList<>();
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
    public List<Select<S, _S>> listSelectedIn(_java._domain _j, Predicate<Select<S, _S>> selectConstraint ){
        List<Select<S, _S>>sts = new ArrayList<>();
        Walk.in(_j, this.statementClass, st->{
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
        return (_CT)replaceIn( (_type) _type.of(clazz), $repl);
    }

    /**
     * 
     * @param clazz
     * @param replacement
     * @return 
     */
    public <_CT extends _type> _CT  replaceIn( Class clazz, String...replacement){
        return (_CT)replaceIn( (_type) _type.of(clazz), replacement);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $repl
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $stmt $repl ){
        $stmts $sn = new $stmts($repl);
        return replaceIn(_j, $sn);
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param statements
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String... statements ){
        $stmts $sn = $stmts.of(statements);
        return replaceIn(_j, $sn);
    }

    public static final Consumer<_stmt> REPLACE_WITH_EMPTY_COMMENT_BLOCK = (st)->{
        BlockStmt bs = Ast.blockStmt("{/*<code>"+st.toString(Print.PRINT_NO_COMMENTS)+"</code>*" + "/}");
        /**
         * Check if you are in this situation (replacing System.out.println()) which is already in an empty block
         * <PRE>
         * void m() {
         *     {
         *         System.out.println(1);
         *     }
         * }
         * </PRE>
         */
        if( st.node().getParentNode().isPresent()
                && st.node().getParentNode().get() instanceof BlockStmt
                && ((BlockStmt)st.node().getParentNode().get()).getParentNode().isPresent()
                && !(((BlockStmt)st.node().getParentNode().get()).getParentNode().get() instanceof BodyDeclaration) ){
            BlockStmt par = ((BlockStmt)st.node().getParentNode().get());
            if( par.getStatements().size() == 1 ){
                bs = Ast.blockStmt("{/*<code>"+st.toString(Print.PRINT_NO_COMMENTS)+"</code>" + "*/}");
                par.replace( bs );
            } else{
                st.node().replace(bs);
            }
        }
    };

    public static final Consumer<_stmt> REPLACE_WITH_EMPTY_STMT_COMMENT = (st)->{
        Statement es = new EmptyStmt(); //create a new empty statement
        es.setComment( new BlockComment("<code>"+st.toString(Print.PRINT_NO_COMMENTS)+"</code>") );
        st.node().replace( es );
    };

    /** comments out the matching code
     * as a BlockStmt containing the code
     * i.e.
     * //comment out all assertStmts
     * class A{
     *     void m(){
     *         assert(1==1);
     *     }
     * }
     * _class _c = $.assertStmt().commentOut(A.class);
     * class A{
     *     void m(){
     *         { /* assert(1==1); * / }
     *     }
     * }
     */
    public <N extends Node> N commentOut( N ast ){
        return commentOut(ast, REPLACE_WITH_EMPTY_STMT_COMMENT);
    }

    //comments out the matching code
    public <_CT extends _type> _CT commentOut( Class clazz){
        return (_CT)commentOut( _class.of(clazz), REPLACE_WITH_EMPTY_STMT_COMMENT);
    }

    public _project commentOut(_project _codeProvider){
        return commentOut(_codeProvider, REPLACE_WITH_EMPTY_STMT_COMMENT);
    }

    public _project commentOut(_project _codeProvider, Consumer<_stmt> commenter){
        forEachIn(_codeProvider, n-> commenter.accept(n));
        return _codeProvider;
    }

    //comments out the matching code
    public <_J extends _java._domain> _J commentOut(_J _j){
        return commentOut(_j, REPLACE_WITH_EMPTY_STMT_COMMENT);
    }

    //
    public <N extends Node> N commentOut( N ast, Consumer<_stmt> commenter){
        return forEachIn(ast, n-> commenter.accept(n));
    }

    // comments out the matching code
    public <_CT extends _type> _CT commentOut( Class clazz, Consumer<_stmt> commenter){
        return (_CT)commentOut( _class.of(clazz), commenter);
    }

    /** comments out the matching code */
    public <_J extends _java._domain> _J commentOut(_J _j, Consumer<_stmt>commenter){
        return forEachIn(_j, s-> commenter.accept(s) );
    }

    public static final $comment<com.github.javaparser.ast.comments.Comment> $COMMENTED_STATEMENT = $comment.as("<code>$statement$</code>");

    /**
     *
     * @param ast
     * @param <N>
     * @return
     */
    public <N extends Node> N unComment( N ast ){
        $COMMENTED_STATEMENT.forSelectedIn( ast, ($comment.Select sel)-> {
            try {
                Statement st = Stmt.of( sel.get("statement").toString() );
                Select ssel = this.select(st);

                if( ssel != null ){
                    //if it's a comment on an EmptyStmt ";", lets replace the statement
                    Optional<Node> oc = sel.comment.getCommentedNode();
                    if( oc.isPresent() && ( oc.get() instanceof EmptyStmt)) {
                        //System.out.println("Empty Stmt");
                        oc.get().replace(st);
                    } else { //TODO handle Empty Block Comments
                        /*
                        if( !oc.isPresent() ){
                            Optional<Node> oparent = sel.comment.getParentNode();
                            if( oparent.isPresent() &&  )
                            oparent.get().is
                        }
                         */
                        Comments.replace(sel.comment, st);
                    }
                }
            } catch( Exception e ){
                //couldnt parse comment statement
            }
        } );
        return ast;
    }

    /**
     *
     * @param clazz
     * @param <_CT>
     * @return
     */
    public <_CT extends _type> _CT unComment( Class clazz){
        return (_CT)unComment( _class.of(clazz));
    }

    /**
     *
     * @param _n a node to be uncommented
     * @param <_N> the node type
     * @return the modified node
     */
    public <_N extends _tree._node> _N unComment(_N _n){
        unComment( _n.node() );
        return _n;
    }

    public <N extends Node> N replaceIn( N node, $stmt $pat){
        return replaceIn(node, $stmts.of($pat));
    }

    public <N extends Node> N replaceIn( N node, $stmts $pat){
        Walk.in(node, this.statementClass, st->{
            $stmt.Select sel = select( st );
            if( sel != null ){
                //construct the replacement snippet
                List<Statement> replacements = $pat.draft(sel.tokens);

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
                sel._s.node().replace( ls );
                //parentNode.addStatement(addIndex +1, ls);
                //removeIn all but the first statement
                //sel.statements.forEach( s-> s.removeIn() );
                //System.out.println("PAR AFTER Remove "+ par );
            }
        });
        Walk.deLabel(node, "$replacement$");
        return node;
    }
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $protoReplacement
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $stmts $protoReplacement ){
        //AtomicInteger ai = new AtomicInteger(0);
        Walk.in(_j, this.statementClass, st->{
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
                sel._s.node().replace( ls );
                //parentNode.addStatement(addIndex +1, ls);
                //removeIn all but the first statement
                //sel.statements.forEach( s-> s.removeIn() );
                //System.out.println("PAR AFTER Remove "+ par );
            }
        });
        if( _j instanceof _tree._node){
            Walk.deLabel( ((_tree._node) _j).node(), "$replacement$");
        }
        return (_J) _j;
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $stmt<S, _S> $isAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_S> prev = e -> $pattern.BodyScope.findPrevious(e.node(), patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $stmt<S, _S> $isNotAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_S> prev = e -> $pattern.BodyScope.findPrevious(e.node(), patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $stmt<S, _S> $isBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_S> prev = e -> $pattern.BodyScope.findNext(e.node(), patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $stmt<S, _S> $isNotBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_S> prev = e -> $pattern.BodyScope.findNext(e.node(), patternsOccurringAfterThisNode) != null;
        return $not(prev);
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
     * Walks AST nodes looking for a $labeledStmt,
     *
     * if found will replace the labeled stmt with a parameter
     * @param <N>
     * @param node
     * @param tokens
     * @return 
     */
    public static <N extends Node> N parameterize$LabeledStmt(N node, Map<String,Object> tokens ){
        //separate into (2) operations, (dont WALK and MUTATE at the same time)
        List<LabeledStmt> lss  = Walk.list(node, LabeledStmt.class, ls-> ls.getLabel().asString().startsWith("$") );
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
                Walk.deLabel(node, "$TO_REPLACE");
            }
        });
        return node;
    }
    
    public static Statement labelStmtReplacement(LabeledStmt ls, Map<String,Object> tokens){
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
            return (($stmt)value).draft(tokens).node();
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
        public $stmt $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
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
            if( !this.astMatch.test(stmt ) ){
                return null;
            }
            Optional<$stmt> orsel  = this.ors.stream().filter( $p-> $p.matches(stmt) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }


    public static final PrettyPrinterConfiguration PRINT_$LABELED_AS_EMBED = new PrettyPrinterConfiguration()
            .setPrintComments(false).setPrintJavadoc(false) //MED ADDED
            .setVisitorFactory(Print$LabeledStatementsAsStencilEmbed::new);

    public static class Print$LabeledStatementsAsStencilEmbed extends PrettyPrintVisitor {
        //MAKE THIS A STENCIL??
        public Predicate<String> labelMatcher = l-> l.startsWith("$");

        public Function<String,String> labelToName = l-> {
            if( l.startsWith("$") ){
                return l.substring(1);
            }
            return null;
        };

        public Print$LabeledStatementsAsStencilEmbed(PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
        }

        public Print$LabeledStatementsAsStencilEmbed(Function<String,String> labelToName, PrettyPrinterConfiguration prettyPrinterConfiguration) {
            super(prettyPrinterConfiguration);
            this.labelToName = labelToName;
        }

        //convert $label: assert($condition$);
        //        to
        //        $$label: assert($condition$);:$$
        public void visit(LabeledStmt ls, Void arg){
            String name = labelToName.apply(ls.getLabel().asString());
            if( name != null ){
                printer.print("$$"+name+":"+ls.getStatement().toString(Print.PRINT_NO_COMMENTS)+":$$");
            } else{
                super.visit(ls, arg);
            }
        }
    }

    /**
     * 
     * @param <T> 
     */
    public static class Select<T extends Statement, _S extends _stmt> implements $pattern.selected,
            selectAst<T>, select_java<_S> {

        public _S _s;
        public $tokens tokens;
        
        public Select( T astStatement, $tokens tokens){
            this._s = (_S) _stmt.of( astStatement); //this.astStatement = astStatement;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$stmt.Select{"+ System.lineSeparator()+
                Text.indent(_s.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public T ast() {
            return (T)_s.node();
        }

        @Override
        public _S _node() {
            return _s;
        }
    }
}
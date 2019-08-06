package org.jdraft.proto;

import org.jdraft._code;
import org.jdraft._java;
import org.jdraft._type;
import org.jdraft._walk;
import org.jdraft.Expr;
import org.jdraft.Ast;
import org.jdraft.Stmt;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import org.jdraft.*;
import org.jdraft.Expr.QuadConsumer;
import org.jdraft.Expr.TriConsumer;
import org.jdraft._node;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;

/**
 * Prototype of a Java {@link Statement} that provides operations for 
 * constructing, analyzing, extracting, removing, replacing / etc. Statement
 * type nodes within the AST.
 *
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
public final class $stmt<T extends Statement>
    implements Template<T>, $proto<T> {
        
    /**
     * 
     * @param ste
     * @return 
     */
    private static <S extends Statement> $stmt<S> fromStackTrace( StackTraceElement ste ){
        Statement st = Stmt.from( ste );
        return new $stmt( (S)st );
    }

    /**
     * 
     * @param <S>
     * @param proto
     * @return 
     */
    public static <S extends Statement> $stmt<S> of( Expr.Command proto ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return fromStackTrace( ste );
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
        return fromStackTrace( ste );
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
        return fromStackTrace( ste );
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
        return fromStackTrace( ste );
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
        return fromStackTrace( ste );
    }

    /**
     * 
     * @return 
     */
    public static $stmt<Statement> any(){
        return of();
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
     * 
     * @param <S>
     * @param pattern
     * @return 
     */
    public static <S extends Statement> $stmt<S> of(String...pattern ){
        Statement st = Stmt.of(pattern);
        return new $stmt( st );
    }
    
    /**
     * 
     * @param <S>
     * @param astProto
     * @return 
     */ 
    public static <S extends Statement> $stmt of(Statement astProto ){
        return new $stmt<>(astProto);
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
        return new $stmt( Stmt.assertStmt(pattern) ).addConstraint(constraint);
    }
    
    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<BlockStmt> blockStmt(){
        return new $stmt( BlockStmt.class, "$blockStmt$" );
    } 
    
    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param constraint
     * @return the BlockStmt
     */
    public static $stmt<BlockStmt> blockStmt(Predicate<BlockStmt> constraint) {
        return new $stmt( Stmt.block("{ a(); }"))
                .$(Stmt.block("{ a(); }"), "any")
                .addConstraint(constraint);
    }
    
    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param pattern the code making up the blockStmt
     * @return the BlockStmt
     */
    public static $stmt<BlockStmt> blockStmt(String... pattern ) {
        return new $stmt( Stmt.block(pattern));
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
        return new $stmt( Stmt.block(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return the breakStmt
     */
    public static $stmt<BreakStmt> breakStmt(Predicate<BreakStmt> constraint) {
        return new $stmt( Stmt.breakStmt("break;"))
                .$(Stmt.breakStmt("break;"), "any")
                .addConstraint(constraint);
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
        return new $stmt( Stmt.breakStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<ContinueStmt> continueStmt(Predicate<ContinueStmt> constraint) {
        return new $stmt( Stmt.continueStmt("continue r;")).
                $(Stmt.continueStmt("continue r;"), "any").addConstraint(constraint);
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
        return new $stmt( Stmt.continueStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<DoStmt> doStmt(Predicate<DoStmt> constraint) {
        return new $stmt( Stmt.doStmt("do{ a(); } while(a==1);") )
                .$(Stmt.doStmt("do{ a(); } while(a==1);"), "any").addConstraint(constraint);
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
        return new $stmt( Stmt.doStmt(pattern)).addConstraint(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $stmt<EmptyStmt> emptyStmt(){
        return new $stmt( new EmptyStmt() );
    }
    
    
    /**
     * Returns a prototype that matches ANY assertStmt
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> ctorInvocationStmt(){
        return new $stmt( ExplicitConstructorInvocationStmt.class, "$ctorInvocationStmt$" );
    } 
    
    /** 
     * i.e."this(100,2900);" 
     * @param constraint
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> ctorInvocationStmt(Predicate<ExplicitConstructorInvocationStmt> constraint) {
        return new $stmt( Stmt.ctorInvocationStmt("this(a);"))
            .$(Stmt.ctorInvocationStmt("this(a);"), "any")
            .addConstraint(constraint);
    }
    
    /** 
     * i.e."this(100,2900);" 
     * @param pattern
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> ctorInvocationStmt(String... pattern ) {
        return new $stmt( Stmt.ctorInvocationStmt(pattern));
    }

    /**
     * i.e."this(100,2900);"
     * @param cts
     * @return
     */
    public static $stmt<ExplicitConstructorInvocationStmt> ctorInvocationStmt(ExplicitConstructorInvocationStmt cts) {
        return new $stmt( cts );
    }

    /** 
     * i.e."this(100,2900);" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $stmt<ExplicitConstructorInvocationStmt> ctorInvocationStmt(String pattern, Predicate<ExplicitConstructorInvocationStmt> constraint) {
        return new $stmt( Stmt.ctorInvocationStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<ExpressionStmt> expressionStmt( Predicate<ExpressionStmt> constraint) {
        return new $stmt( Stmt.expressionStmt("a += t;"))
            .$("a += t;", "any")
            .addConstraint(constraint);
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
        return new $stmt( Stmt.expressionStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<ForStmt> forStmt( Predicate<ForStmt> constraint ) {
        return new $stmt( Stmt.forStmt("for(int i=0;i<1;i++){ a(); }"))
            .$(Stmt.forStmt("for(int i=0;i<1;i++){ a(); }"), "any")
            .addConstraint(constraint);
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
        return new $stmt( Stmt.forStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<ForEachStmt> forEachStmt( Predicate<ForEachStmt> constraint) {
        return new $stmt( Stmt.forEachStmt("for(int i:arr){}"))
            .$(Stmt.forEachStmt("for(int i:arr){}"), "any")
            .addConstraint(constraint);
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
        return new $stmt( Stmt.forEachStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<IfStmt> ifStmt(Predicate<IfStmt> constraint) {
        return new $stmt( Stmt.ifStmt("if(a){ b(); }"))
            .$(Stmt.of("if(a){ b();}"), "any")
            .addConstraint(constraint);
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
        return new $stmt( Stmt.ifStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<LabeledStmt> labeledStmt( Predicate<LabeledStmt> constraint) {
        return new $stmt( Stmt.labeledStmt("l: a();"))
                .$(Stmt.labeledStmt("l:a();"), "any").addConstraint(constraint);
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
        return new $stmt( Stmt.labeledStmt(pattern)).addConstraint(constraint);
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
     * i.e."class C{ int a, b; }"
     * @param constraint
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt( Predicate<LocalClassDeclarationStmt> constraint) {
        return new $stmt( Stmt.localClass( "class C{}"))
                .$(Stmt.localClass("class C{}"), "any")
                .addConstraint(constraint);
    }
    
    /**
     * Converts from a String to a LocalClass
     * i.e. "class C{ int a, b; }"
     * @param pattern the code that represents a local class
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt( String... pattern ) {
        return new $stmt( Stmt.localClass(pattern));
    }
    
    /**
     * Converts from a String to a LocalClass
     * i.e."class C{ int a, b; }"
     * @param pattern the code that represents a local class
     * @param constraint
     * @return the AST implementation
     */
    public static $stmt<LocalClassDeclarationStmt> localClassStmt( String pattern, Predicate<LocalClassDeclarationStmt> constraint) {
        return new $stmt( Stmt.localClass(pattern)).addConstraint(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $stmt<ReturnStmt> returnStmt() {
        return new $stmt(ReturnStmt.class, "$returnStmt$");
    }
    
    /** 
     * i.e."return VALUE;" 
     * @param constraint
     * @return 
     */
    public static $stmt<ReturnStmt> returnStmt( Predicate<ReturnStmt> constraint ) {
        return new $stmt( Stmt.returnStmt("return a;"))
                .$(Stmt.of("return a;"), "any").addConstraint(constraint);
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
        return new $stmt( Stmt.returnStmt(pattern)).addConstraint(constraint);
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
        return new $stmt( Stmt.switchStmt(pattern)).addConstraint(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $stmt<SwitchStmt> switchStmt(Predicate<SwitchStmt> constraint) {
        return new $stmt( Stmt.switchStmt("switch(a){ default : a(); }"))
                .$(Stmt.of("switch(a){ default : a(); }"), "any")
                .addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<SynchronizedStmt> synchronizedStmt( Predicate<SynchronizedStmt> constraint ) {
        return new $stmt( Stmt.synchronizedStmt("synchronized(a){ b();}") ).
            $(Stmt.synchronizedStmt("synchronized(a){ b();}"), "any")
            .addConstraint(constraint);
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
        return new $stmt( Stmt.synchronizedStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<ThrowStmt> throwStmt( Predicate<ThrowStmt> constraint ) {
        return new $stmt( Stmt.throwStmt("throw a;")).$(Stmt.throwStmt("throw a;"), "any")
                .addConstraint(constraint);
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
        return new $stmt( Stmt.throwStmt(pattern)).addConstraint(constraint);
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
     * @param constraint
     * @return 
     */
    public static $stmt<TryStmt> tryStmt( Predicate<TryStmt> constraint ) {
        return new $stmt( Stmt.tryStmt("try{ a(); }"))
            .$(Stmt.tryStmt("try{ a();}"), "any")
            .addConstraint(constraint);
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
        return new $stmt( Stmt.tryStmt(pattern)).addConstraint(constraint);
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
        return new $stmt( Stmt.whileStmt(pattern)).addConstraint(constraint);
    }

    /** 
     * i.e."while(i< 1) { ...}"
     * @param constraint
     * @return 
     */
    public static $stmt<WhileStmt> whileStmt( Predicate<WhileStmt> constraint ) {
        return new $stmt( Stmt.whileStmt("while(true){a();}") ).
                $(Stmt.whileStmt("while(true){a();}").toString(), "any").addConstraint(constraint);
    }

    /**
     * Optional matching predicate applied to matches to ensure 
     * not only pattern match
     */
    public Predicate<T> constraint = t-> true;
    
    /** The stencil representing the statement */
    public Stencil stmtPattern;

    /** the class of the statement */
    public Class<T> statementClass;

    private $stmt( Class<T> statementClass ){
        this( statementClass, "$any$"); 
    }
    
    private $stmt( Class<T> statementClass, String pattern){
        this.constraint = t->true;
        this.statementClass = statementClass;
        this.stmtPattern = Stencil.of(pattern);
    }
    
    private $stmt( Class<T> statementClass, Predicate<T> constraint ){
        this.statementClass = statementClass;
        this.stmtPattern = Stencil.of("$any$");
        this.constraint = constraint;        
    }
    
    public $stmt( T st ){
        this.statementClass = (Class<T>)st.getClass();
        this.stmtPattern = Stencil.of( st.toString(NO_COMMENTS) );
    }

    public $stmt<T> addConstraint( Predicate<T> constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public T fill(Object...values){
        String str = stmtPattern.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (T)Stmt.of( str);
    }

    @Override
    public $stmt $(String target, String $name ) {
        this.stmtPattern = this.stmtPattern.$(target, $name);
        return this;
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
        String fixedText  = this.stmtPattern.getTextBlanks().getFixedText();
        int nextInd = fixedText.indexOf(stmtString);        
        while( nextInd >= 0 ){
            String padded = Text.matchNextPaddedTarget(fixedText, stmtString, nextInd );
            //System.out.println( "FOUND "+ padded );
            //String prefix = Text.getLeadingSpaces( fixedText, nextInd );
            //String postfix = Text.getTrailingSpaces(fixedText, stmtString.length() + nextInd);
            //System.out.println( "FOUND \""+ (prefix+stmtString+postfix) +"\"");
            stringsToReplace.add( padded );
            nextInd = fixedText.indexOf(stmtString, nextInd + stmtString.length() );
        }
        for(int i=0;i<stringsToReplace.size();i++){
            int indexOfAssert = this.stmtPattern.getTextBlanks().getFixedText().indexOf( stringsToReplace.get(i) );
            System.out.println( indexOfAssert );
            //System.out.println( "PATTERN " + this.stmtPattern );
            this.stmtPattern = this.stmtPattern.$( stringsToReplace.get(i), $name);
            //System.out.println( "PATTERN " + this.stmtPattern );
        }
        return this;
    }

    @Override
    public T fill(Translator t, Object...values){
        //if( this.commentPattern != null ){
        //    return (T)Stmt.of(Stencil.of(commentPattern, stmtPattern).fill(t, values) );
        //}
        List<String> keys = list$Normalized();
        if( values.length < keys.size() ){
            throw new _jDraftException("not enough values("+values.length+") to fill ("+keys.size()+") variables "+ keys);
        }
        Map<String,Object> kvs = new HashMap<>();
        for(int i=0;i<values.length;i++){
            kvs.put( keys.get(i), values[i]);
        }
        return compose( t, kvs );
    }

    @Override
    public T compose(Object...keyValues ){
        Tokens tokens = Tokens.of(keyValues);
        return (T)walkCompose$LabeledStmt( Stmt.of(stmtPattern.compose( tokens )), tokens );
    }
    
    @Override
    public T compose(Translator t, Object...keyValues ){
        Tokens tokens = Tokens.of(keyValues);
        return (T)walkCompose$LabeledStmt( Stmt.of(stmtPattern.compose( t, tokens )), tokens );
    }

    @Override
    public T compose(Map<String,Object> tokens ){
        return (T)walkCompose$LabeledStmt( Stmt.of(stmtPattern.compose( tokens )), tokens );
    }
    
    /**
     * 
     * @param _n
     * @return 
     */
    public T compose(_node _n ){
        Map<String,Object> decons = _n.deconstruct();
        return (T) compose( decons );
    }

    @Override
    public T compose(Translator t, Map<String,Object> tokens ){
        return (T)walkCompose$LabeledStmt( Stmt.of(stmtPattern.compose( t, tokens )), tokens );
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
        return this.stmtPattern.list$();
    }

    @Override
    public List<String> list$Normalized(){
        return this.stmtPattern.list$Normalized();
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param kvs the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $stmt hardcode$( Tokens kvs ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, kvs );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $stmt hardcode$( Object... keyValues ) {
        return hardcode$( Translator.DEFAULT_TRANSLATOR, Tokens.of( keyValues ) );
    }

    /**
     * Hardcode parameterized values
     * (i.e. what was once a parameter, now is static text)
     *
     * @param translator translates values to be hardcoded into the Stencil
     * @param keyValues the key parameter NAME and String VALUE to assign to the
     * @return the modified Stencil
     */
    public $stmt hardcode$( Translator translator, Object... keyValues ) {
        return hardcode$( translator, Tokens.of( keyValues ) );
    }

    /**
     * 
     * @param translator
     * @param kvs
     * @return 
     */
    public $stmt hardcode$( Translator translator, Tokens kvs ) {
        this.stmtPattern = this.stmtPattern.hardcode$(translator, kvs);
        return this;
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
                && this.stmtPattern.isMatchAny();
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
        Tokens st = this.stmtPattern.decompose(astStmt.toString(NO_COMMENTS));
        if( st == null ){
            return null;
        }      
        return new Select( astStmt, $args.of(st) );        
    }

    /**
     * Returns the first Statement that matches the 
     * @param _j
     * @return 
     */
    @Override
    public Select<T> selectFirstIn( _java _j){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return selectFirstIn(_t.ast());
        }
        return selectFirstIn( ((_node) _j).ast() );
    }
     

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @return a Select containing the Statement and the key value pairs from the prototype
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
    public Select<T> selectFirstIn( _java _n, Predicate<Select<T>> selectConstraint ){
        if( _n instanceof _code ){
            if( ((_code) _n).isTopLevel()){
                return selectFirstIn( ((_code) _n).astCompilationUnit(), selectConstraint );
            }
            return selectFirstIn( ((_type)_n).ast(), selectConstraint);
        }
        return selectFirstIn( ((_node)_n).ast(), selectConstraint );        
        /*
        Optional<T> f = _n.ast().findFirst(this.statementClass, s -> {
            Select<T> sel = this.select(s); 
            return sel != null && selectConstraint.test(sel);
            });         
        if( f.isPresent()){
            return select( f.get() );
        }
        return null;
        */
    }

    /**
     * Returns the first Statement that matches the 
     * @param astNode the 
     * @param selectConstraint 
     * @return a Select containing the Statement and the key value pairs from the prototype
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
     * @param astNode the 
     * @param statementMatchFn 
     * @return 
     */
    @Override
    public T firstIn( Node astNode, Predicate<T> statementMatchFn ){
        Optional<T> f = astNode.findFirst(this.statementClass, s ->{
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
    public _type forSelectedIn(Class clazz, Consumer<Select<T>> selectedActionFn){
        return forSelectedIn(_java.type(clazz), selectedActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectedActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Consumer<Select<T>> selectedActionFn){
        _walk.in(_n, this.statementClass, e->{
            Select<T> sel = select( e );
            if( sel != null ){
                selectedActionFn.accept( sel );
            }
        });
        return _n;
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
    public _type forSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectedActionFn){
        return forSelectedIn(_java.type(clazz), selectConstraint, selectedActionFn);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param selectConstraint
     * @param selectedActionFn
     * @return 
     */
    public <N extends _java> N forSelectedIn(N _n, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectedActionFn){
        _walk.in(_n, this.statementClass, e->{
            Select<T> sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectedActionFn.accept( sel );
            }
        });
        return _n;
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
        return listSelectedIn(_java.type(clazz));
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
    
    /*
    @Override
    public List<Select<T>> listSelectedIn(_node _n ){
        List<Select<T>>sts = new ArrayList<>();
        _walk.in(_n, this.statementClass, st->{
            //$args tokens = deconstruct(st);
            Select sel = select(st);
            if (sel != null) {
                sts.add(sel);
            }
        });
        return sts;
    }
    */

    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select<T>> listSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint ){
        return listSelectedIn(_java.type(clazz), selectConstraint);
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
            //$args tokens = deconstruct( st );
            Select sel = select(st);
            if( sel != null && selectConstraint.test(sel)){
                sts.add( sel); //new Select( (T)st, tokens) );
            }
        });
        return sts;
    }
    
    /**
     * 
     * @param _n
     * @param selectConstraint
     * @return 
     */
    public List<Select<T>> listSelectedIn(_java _n, Predicate<Select<T>> selectConstraint ){
        List<Select<T>>sts = new ArrayList<>();
        _walk.in(_n, this.statementClass, st->{
            //$args tokens = deconstruct(st);
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
    public _type replaceIn( Class clazz, $stmt $repl){
        return replaceIn(_java.type(clazz), $repl);
    }
    
    /**
     * 
     * @param clazz
     * @param replacement
     * @return 
     */
    public _type replaceIn( Class clazz, String...replacement){
        return replaceIn(_java.type(clazz), replacement);
    }
    
    /**
     * 
     * @param <N>
     * @param _n
     * @param $repl
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, $stmt $repl ){
        $snip $sn = new $snip($repl);
        return replaceIn(_n, $sn);
    }

    /**
     * 
     * @param <N>
     * @param _n
     * @param statment_s
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, String... statment_s ){
        $snip $sn = $snip.of(statment_s);
        return replaceIn(_n, $sn);
    }    

    /**
     * 
     * @param <N>
     * @param _n
     * @param $protoReplacement
     * @return 
     */
    public <N extends _java> N replaceIn(N _n, $snip $protoReplacement ){
        AtomicInteger ai = new AtomicInteger(0);
        _walk.in(_n, this.statementClass, st->{
            $stmt.Select sel = select( st );
            if( sel != null ){
                //construct the replacement snippet
                List<Statement> replacements = $protoReplacement.compose(sel.args );

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
        if( _n instanceof _node ){
            Ast.flattenLabel( ((_node) _n).ast(), "$replacement$");
        }
        /*
        if( _n instanceof _body._hasBody){
            for(int i=0;i< ai.get(); i++){
                ((_body._hasBody)_n).flattenLabel("$replacement$");
            }
        } else if( _n instanceof _type ){
            ((_type)_n).flattenLabel("$replacement$");
            for(int i=0;i< ai.get(); i++){
                ((_type)_n).flattenLabel("$replacement$");
            }
        }
         */
        return (N)_n;
    }
    
    @Override
    public String toString(){
        return "("+this.statementClass.getSimpleName()+") : \""+ this.stmtPattern+"\"";
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
        return walkCompose$LabeledStmt( stmt, tokens);
    } 
    
    /**
     * Walks AST nodes looking for a $labeledStmt, if found will compose and replace
     * the labeled stmt
     * @param <N>
     * @param node
     * @param tokens
     * @return 
     */
    public static <N extends Node> N walkCompose$LabeledStmt(N node, Map<String,Object> tokens ){
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
        /*
        _walk.in(node,
            LabeledStmt.class, 
            ls-> ls.getLabel().asString().startsWith("$"),
            ls -> {
                Statement st = labelStmtReplacement(ls, tokens);
                if( st.isEmptyStmt() || (st.isBlockStmt() && st.asBlockStmt().isEmpty()) ){
                    System.out.println( "REMOVE FORCED !!!");
                    ls.removeForced();

                    boolean rem = ls.remove();
                    if( !rem ){
                        ls.replace( st );
                    }

                } else{
                    //always 1) replace it with a labeled statement ...2)then Flatten the label
                    LabeledStmt $TO_REPLACE = Stmt.labeledStmt("$TO_REPLACE: {}");
                    $TO_REPLACE.setStatement(st);
                    ls.replace( $TO_REPLACE );
                    Ast.flattenLabel(node, "$TO_REPLACE");
                }                
            });        
        return node;
         */
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
            return (($stmt)value).compose(tokens);
        }
        //SHOW (just remove the $label:)
        return ls.getStatement();        
    }
    
    /**
     * 
     * @param <T> 
     */
    public static class Select<T extends Statement> implements $proto.selected,
            $proto.selectedAstNode<T> {
        
        public T astStatement;
        public $args args;
        
        public Select( T astStatement, $args tokens){
            this.astStatement = astStatement;
            this.args = tokens;
        }
        
        @Override
        public $args args(){
            return args;
        }
        
        @Override
        public String toString(){
            return "$stmt.Select{"+ System.lineSeparator()+
                Text.indent(astStatement.toString() )+ System.lineSeparator()+
                Text.indent("$args : " + args) + System.lineSeparator()+
                "}";
        }

        @Override
        public T ast() {
            return astStatement;
        }
    }
}
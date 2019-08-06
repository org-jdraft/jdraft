package org.jdraft;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.function.*;

/**
 * Translator for converting from Strings to AST {@link Statement} nodes
 */
public enum Stmt {

    ;

    /**
     * Returns the Statement associated from the lambda expression
     * (i.e. without the PARAMETERS)
     * NOTE: if the Statement is a BlockStmt with a single Statement, a single Statement
     * is returned, rather than a BlockStmt
     * @param le the lamba expression
     * @return the statement associated with the lambda
     */
    public static Statement from( LambdaExpr le ){
        Statement st = le.getBody();
        if( st.isBlockStmt() && st.asBlockStmt().getStatements().size() == 1){
            return st.asBlockStmt().getStatement(0);
        }
        return st;
    }

    /**
     * Resolves and returns the Ast Statement representing body of the lambda 
     * located at the particular lambdaStackTrace element passed in for example:
     * <PRE>
     * 
     * LambdaExpr le = Expr.of( ()-> System.out.println(1) );
     * assertEquals( Stmt.of("System.out.println(1);"), le.getBody().getStatement(0) );
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * @param lambdaStackTrace StackTrace identifying the runtime Lambda Expression
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static Statement from(StackTraceElement lambdaStackTrace ){
        return from( Expr.lambda( lambdaStackTrace ) );
    }

    /**
     * Resolves and returns the AST Statement representing the body of the 
     * lambda expression.for example:<PRE>
     * //call the method f with a lambdaExpression
     * Statement st = Stmt.of( (String s) -> System.out.println(s) );
     * assertEquals( Stmt.of("System.out.println(s);"), st );    
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @param <T>
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object> Statement of( Expr.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Resolves and returns the AST Statement representing the body of the 
     * lambda expression.for example:<PRE>
     * //call the method f with a lambdaExpression
     * Statement st = Stmt.of( (String s) -> System.out.println(s) );
     * assertEquals( Stmt.of("System.out.println(s);"), st );    
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @param <T>
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object> Statement of( Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Resolves and returns the AST Statement representing the body of the 
     * lambda expression.for example:<PRE>
     * //call the method f with a lambdaExpression
     * Statement st = Stmt.of( (String s) -> System.out.println(s) );
     * assertEquals( Stmt.of("System.out.println(s);"), st );    
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @param <T>
     * @param <U>
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object>  Statement of( BiConsumer<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Resolves and returns the AST Statement representing the body of the 
     * lambda expression.for example:<PRE>
     * //call the method f with a lambdaExpression
     * Statement st = Stmt.of( (String s) -> System.out.println(s) );
     * assertEquals( Stmt.of("System.out.println(s);"), st );    
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @param <T>
     * @param <U>
     * @param <V>
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object, V extends Object>  Statement of( Expr.TriConsumer<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * Resolves and returns the AST Statement representing the body of the 
     * lambda expression.for example:<PRE>
     * //call the method f with a lambdaExpression
     * Statement st = Stmt.of( (String s) -> System.out.println(s) );
     * assertEquals( Stmt.of("System.out.println(s);"), st );    
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <W>
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static <T extends Object, U extends Object, V extends Object, W extends Object>  Statement of( Expr.QuadConsumer<T, U, V, W> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * convert the String code into a single Stmt AST nod
     *
     * @param code
     * @return a Stmt
     */
    public static Statement of(String... code ) {
        String str = Text.combine(code).trim();
        if( str.length() == 0 ) {
            return new EmptyStmt();
        }
        if( !str.endsWith(";") && !str.endsWith("}") ){
            str = str + ";";
        }
        if( str.startsWith("/*") || str.startsWith("//") ){
            return block( str ).getStatement(0);
        }        
        return StaticJavaParser.parseStatement( str );
    }

    public static final Class<AssertStmt> ASSERT = AssertStmt.class;

    /**
     * i.e."assert(1==1);"
     * @param code
     * @return and AssertStmt with the code
     */
    public static AssertStmt assertStmt(String... code ) {
        return of( code ).asAssertStmt();
    }

    public static final Class<BlockStmt> BLOCK = BlockStmt.class;
    
    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param code the code making up the blockStmt
     * @return the BlockStmt
     */
    public static BlockStmt block(String... code ) {

        String combined = Text.combine(code).trim();
        if( combined.startsWith("/*")){
            int endCommentIndex = combined.indexOf("*/");
            if( endCommentIndex < 0 ){
                throw new _draftException("Unclosed comment");
            }
            String startM = combined.substring(endCommentIndex+2).trim();
            if(!startM.startsWith("{")){
                startM = "{"+ startM;
            }
            int count = 0;
            for(int i=0;i<combined.length();i++){
                if( combined.charAt(i) == '{'){
                    count ++;
                }
                if( combined.charAt(i) == '{'){
                    count --;
                }
            }
            if( ! startM.endsWith("}") || count < 0 ){
                startM = startM + "}";
            }

            String comb = combined.substring(0, endCommentIndex+2)+startM;
            return StaticJavaParser.parseBlock( comb );
        }

        if( !combined.startsWith("{") ){
            combined = "{"+ combined;
        }
        int count = 0;
        for(int i=0;i<combined.length();i++){
            if( combined.charAt(i) == '{'){
                count ++;
            }
            if( combined.charAt(i) == '}'){
                count --;
            }
        }
        if( !combined.endsWith("}") || count > 0 ){
            combined = combined +System.lineSeparator()+ "}";
        }
        return StaticJavaParser.parseBlock( combined );
    }

    /**
     * Builds a BlockStmt from the Lambda expression code found based on the location of the
     * StackTraceElement.  i.e.
     * Stmt.block( ()-> { System.out.println(1}; System.out.println(2); } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static BlockStmt block( StackTraceElement ste ){
        LambdaExpr le = Expr.lambda( ste );
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static BlockStmt block( Expr.Command command ){
        LambdaExpr le = Expr.lambda( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object> BlockStmt block( Consumer<A> command ){
        LambdaExpr le = Expr.lambda( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object> BlockStmt block( BiConsumer<A,B> command ){
        LambdaExpr le = Expr.lambda( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object, C extends Object> BlockStmt block( Expr.TriConsumer<A,B,C> command ){
        LambdaExpr le = Expr.lambda( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> BlockStmt block( Expr.QuadConsumer<A,B,C,D> command ){
        LambdaExpr le = Expr.lambda( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    /** i.e. "break;" "break outer;" */
    public static final Class<BreakStmt> BREAK = BreakStmt.class;

    /**
     * i.e."break;" or "break outer;"
     * @param code String representing the break of
     * @return the breakStmt
     */
    public static BreakStmt breakStmt(String... code ) {
        return of( code ).asBreakStmt();
    }

    /** i.e. "continue outer;" */
    public static final Class<ContinueStmt> CONTINUE = ContinueStmt.class;

    /** 
     * i.e."continue outer;" 
     *
     * @param code
     * @return 
     */
    public static ContinueStmt continueStmt(String... code ) {
        return of( code ).asContinueStmt();
    }

    /** i.e. "do{ System.out.println(1); }while( a < 100 );" */
    public static final Class<DoStmt> DO = DoStmt.class;

    /** 
     *  i.e."do{ System.out.println(1); }while( a < 100 );"
     * @param code
     * @return  
     */
    public static DoStmt doStmt(String... code ) {
        return of( code ).asDoStmt();
    }

    /** 
     * i.e. "this(100,2900);" 
     */
    public static final Class<ExplicitConstructorInvocationStmt> CONSTRUCTOR_INVOCATION
            = ExplicitConstructorInvocationStmt.class;

    /** 
     * i.e."this(100,2900);" 
     * 
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt ctorInvocationStmt(String... code ) {
        return of( code ).asExplicitConstructorInvocationStmt();
    }

    /** i.e. "s += t;" */
    public static final Class<ExpressionStmt> EXPRESSION = ExpressionStmt.class;

    /** 
     * i.e."s += t;" 
     * 
     * @param code
     * @return 
     */
    public static ExpressionStmt expressionStmt( String... code ) {
        String str = Text.combine( code );
        if(str.endsWith(";") ){
            //Expression ex = ;
            return new ExpressionStmt( Expr.of(str.substring(0, str.length() -1) ) );
        }
        return of( code ).asExpressionStmt();
    }

    /** i.e. "for(int i=0; i<100;i++) {...}" */
    public static final Class<ForStmt> FOR = ForStmt.class;

    /** 
     * i.e."for(int i=0; i<100;i++) {...}"
     * @param code
     * @return 
     */
    public static ForStmt forStmt( String... code ) {
        return of( code ).asForStmt();
    }

    /** i.e. "for(String element:arr){...}" */
    public static final Class<ForEachStmt> FOR_EACH = ForEachStmt.class;

    /** 
     * i.e."for(String element:arr){...}" 
     * 
     * @param code
     * @return 
     */
    public static ForEachStmt forEachStmt( String... code ) {
        return of( code ).asForEachStmt(); //.asForeachStmt();
    }

    /** i.e. "if(a==1){...}" */
    public static final Class<IfStmt> IF = IfStmt.class;

    /** 
     * i.e."if(a==1){...}" 
     * @param code
     * @return 
     */
    public static IfStmt ifStmt( String... code ) {
        return of( code ).asIfStmt();
    }

    /** i.e. "outer:   start = getValue();" */
    public static final Class<LabeledStmt> LABELED = LabeledStmt.class;

    /** 
     * i.e."outer:   start = getValue();" 
     * @param code
     * @return 
     */
    public static LabeledStmt labeledStmt( String... code ) {
        return of( code ).asLabeledStmt();
    }

    /** i.e. "class C{ int a, b; }" */
    public static final Class<LocalClassDeclarationStmt> LOCAL_CLASS = LocalClassDeclarationStmt.class;

    /**
     * Converts from a String to a LocalClass
     * i.e. "class C{ int a, b; }"
     * @param code the code that represents a local class
     * @return the AST implementation
     */
    public static LocalClassDeclarationStmt localClass( String... code ) {
        return of( code ).asLocalClassDeclarationStmt();
    }

    /** i.e. "return VALUE;" */
    public static final Class<ReturnStmt> RETURN = ReturnStmt.class;

    /** 
     * i.e."return VALUE;" 
     * 
     * @param code
     * @return 
     */
    public static ReturnStmt returnStmt( String... code ) {
        return of( code ).asReturnStmt();
    }

    /** i.e. "switch(a) { case 1: break; default : doMethod(a); }" */ 
    public static final Class<SwitchStmt> SWITCH = SwitchStmt.class;
    
    /** 
     * i.e."switch(a) { case 1: break; default : doMethod(a); }" 
     * 
     * @param code
     * @return 
     */    
    public static SwitchStmt switchStmt( String... code ) {

        return of( code ).asSwitchStmt();
    }

    /** i.e. "synchronized(e) { ...}" */
    public static final Class<SynchronizedStmt> SYNCHRONIZED = SynchronizedStmt.class;
    
    /**
     * i.e. "synchronized(e) { ...}"
     * @param code
     * @return 
     */
    public static SynchronizedStmt synchronizedStmt( String... code ) {
        return of( code ).asSynchronizedStmt();
    }

    /**
     * i.e. "throw new RuntimeException("SHOOT");"
     */
    public static final Class<ThrowStmt> THROW = ThrowStmt.class;
    
    /**
     * i.e."throw new RuntimeException("SHOOT");"
     * 
     * @param code
     * @return 
     */
    public static ThrowStmt throwStmt( String... code ) {
        return of( code ).asThrowStmt();
    }

    /** i.e. "try{ clazz.getMethod("fieldName"); }" */
    public static final Class<TryStmt> TRY = TryStmt.class;
    
    /** 
     * i.e. "try{ clazz.getMethod("fieldName"); }" 
     * @param code
     * @return 
     */
    public static TryStmt tryStmt( String... code ) {
        return of( code ).asTryStmt();
    }

    /** i.e. "while(i< 1) { ... }"*/
    public static final Class<WhileStmt> WHILE = WhileStmt.class;
    
    /** 
     * i.e."while(i< 1) { ...}"
     * 
     * @param code
     * @return 
     */    
    public static WhileStmt whileStmt( String... code ) {
        return of( code ).asWhileStmt();
    }

    /** an empty statement i.e. ";" */
    public static final Class<EmptyStmt> EMPTY = EmptyStmt.class;
}


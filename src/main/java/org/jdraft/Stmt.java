package org.jdraft;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.Optional;
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
        return from( Ex.lambdaEx( lambdaStackTrace ) );
    }

    /**
     * Resolves and returns the AST Statement representing the body of the 
     * lambda expression.for example:<PRE>
     * //call the method f with a lambdaExpression
     * Statement st = Stmt.of( (String s) -> System.out.println(s) );
     * assertEquals( Stmt.of("System.out.println(s);"), st );    
     * </PRE>
     * NOTE: the source of the calling method must be resolveable via draft
     * @see org.jdraft.io._io#addInFilePath(java.lang.String)
     * @see org.jdraft.io._io#addInProjectPath(java.lang.String)
     * 
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static  Statement of( Ex.Command c ){
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
    public static <T extends Object, U extends Object, V extends Object>  Statement of( Ex.TriConsumer<T, U, V> c ){
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
    public static <T extends Object, U extends Object, V extends Object, W extends Object> Statement of( Ex.QuadConsumer<T, U, V, W> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    /**
     * convert the String code into a single JavaParser Ast Statement instance
     *
     * @param code the String code
     * @return a Statement based on the code (or throw an
     * @throws ParseProblemException if the code does not represent valid Java
     */
    public static Statement of( String code ){
        return of(new String[]{code});
    }

    /**
     * convert the String code into a single JavaParser Ast Statement instance
     *
     * @param code the String code
     * @return a Statement based on the code (or throw an
     * @throws ParseProblemException if the code does not represent valid Java
     */
    public static Statement of(String... code ) throws ParseProblemException {
        String str = Text.combine(code).trim();
        if( str.length() == 0 ) {
            return new EmptyStmt();
        }
        if( !str.endsWith(";") && !str.endsWith("}") ){
            str = str + ";";
        }
        if( str.startsWith("/*") || str.startsWith("//") ){
            return blockStmt( str ).getStatement(0);
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

    /**
     * i.e."assert(1==1);"
     * @param code
     * @return and AssertStmt with the code
     */
    public static AssertStmt assertStmt( String code ){
        return assertStmt( new String[]{code});
    }

    /**
     * Builds a AssertStmt from the first AssertStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.assertStmt( (Integer i)-> assert(1==1) );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static AssertStmt assertStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(AssertStmt.class).get();
    }

    public static AssertStmt assertStmt( LambdaExpr le ){
        Optional<AssertStmt> ods = le.findFirst(AssertStmt.class);
        if( ods == null ){
            throw new _draftException("No AssertStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static AssertStmt assertStmt( Ex.Command command ){
        return assertStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> AssertStmt assertStmt( Consumer<A> command ){
        return assertStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> AssertStmt assertStmt( BiConsumer<A,B> command ){
        return assertStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> AssertStmt assertStmt( Ex.TriConsumer<A,B,C> command ){
        return assertStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> AssertStmt assertStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return assertStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static final Class<BlockStmt> BLOCK = BlockStmt.class;
    
    /**
     * NOTE: If you omit the opening and closing braces { }, they will be added
     *
     * i.e."{ int i=1; return i;}"
     * @param code the code making up the blockStmt
     * @return the BlockStmt
     */
    public static BlockStmt blockStmt(String... code ) {

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
    public static BlockStmt blockStmt(StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static BlockStmt blockStmt(Ex.Command command ){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object> BlockStmt blockStmt(Consumer<A> command ){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object> BlockStmt blockStmt(BiConsumer<A,B> command ){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object, C extends Object> BlockStmt blockStmt(Ex.TriConsumer<A,B,C> command ){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> BlockStmt blockStmt(Ex.QuadConsumer<A,B,C,D> command ){
        LambdaExpr le = Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]);
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
     *  i.e."do{ System.out.println(1); }while( a < 100 );"
     * @param code
     * @return
     */
    public static DoStmt doStmt( String code ){
        return doStmt( new String[]{code});
    }

    /**
     * Builds a DoStmt from the first DoStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> { do{ System.out.println(1}; System.out.println(2); }while(i==1) } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static DoStmt doStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(DoStmt.class).get();
    }

    public static DoStmt doStmt( LambdaExpr le ){
        Optional<DoStmt> ods = le.findFirst(DoStmt.class);
        if( ods == null ){
            throw new _draftException("No DoStmt in lambda "+ le );
        }
        return ods.get();
    }
    public static DoStmt doStmt( Ex.Command command ){
        return doStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> DoStmt doStmt( Consumer<A> command ){
        return doStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> DoStmt doStmt( BiConsumer<A,B> command ){
        return doStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> DoStmt doStmt( Ex.TriConsumer<A,B,C> command ){
        return doStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> DoStmt doStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return doStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    /** 
     * i.e. "this(100,2900);" 
     */
    public static final Class<ExplicitConstructorInvocationStmt> THIS_CONSTRUCTOR
            = ExplicitConstructorInvocationStmt.class;

    /** 
     * i.e."this(100,2900);" 
     * 
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt thisConstructorStmt(String... code ) {
        return of( code ).asExplicitConstructorInvocationStmt();
    }

    /** i.e. "s += t;" */
    public static final Class<ExpressionStmt> EXPRESSION_STMT = ExpressionStmt.class;

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
            return new ExpressionStmt( Ex.of(str.substring(0, str.length() -1) ) );
        }
        return of( code ).asExpressionStmt();
    }

    /**
     * i.e."for(int i=0; i<100;i++) {...}"
     * @param code
     * @return
     */
    public static ExpressionStmt expressionStmt( String code ){
        return expressionStmt( new String[]{code});
    }

    /**
     * Builds a ExpressionStmt from the first ExpressionStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.expressionStmt( (Integer s)-> s+=5; );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static ExpressionStmt expressionStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(ExpressionStmt.class).get();
    }

    public static ExpressionStmt expressionStmt( LambdaExpr le ){
        Optional<ExpressionStmt> ods = le.findFirst(ExpressionStmt.class);
        if( ods == null ){
            throw new _draftException("No ExpressionStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ExpressionStmt expressionStmt( Ex.Command command ){
        return expressionStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ExpressionStmt expressionStmt( Consumer<A> command ){
        return expressionStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ExpressionStmt expressionStmt( BiConsumer<A,B> command ){
        return expressionStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ExpressionStmt expressionStmt( Ex.TriConsumer<A,B,C> command ){
        return expressionStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ExpressionStmt expressionStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return expressionStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     * i.e."for(int i=0; i<100;i++) {...}"
     * @param code
     * @return
     */
    public static ForStmt forStmt( String code ){
        return forStmt( new String[]{code});
    }

    /**
     * Builds a ForStmt from the first ForStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.forStmt( (Integer s)-> { for(int i=s;i<s;i++){ System.out.println(1};  } } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static ForStmt forStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(ForStmt.class).get();
    }

    public static ForStmt forStmt( LambdaExpr le ){
        Optional<ForStmt> ods = le.findFirst(ForStmt.class);
        if( ods == null ){
            throw new _draftException("No ForStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ForStmt forStmt( Ex.Command command ){
        return forStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ForStmt forStmt( Consumer<A> command ){
        return forStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ForStmt forStmt( BiConsumer<A,B> command ){
        return forStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ForStmt forStmt( Ex.TriConsumer<A,B,C> command ){
        return forStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ForStmt forStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return forStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     * i.e."for(String element:arr){...}"
     *
     * @param code
     * @return
     */
    public static ForEachStmt forEachStmt( String code ){
        return forEachStmt( new String[]{code});
    }

    /**
     * Builds a ForEachStmt from the first ForEachStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e. <PRE>{@code
     * Stmt.forEachStmt( (List<Integer>arr)-> for(String element:arr){...} );
     * }</PRE>
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static ForEachStmt forEachStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(ForEachStmt.class).get();
    }

    public static ForEachStmt forEachStmt( LambdaExpr le ){
        Optional<ForEachStmt> ods = le.findFirst(ForEachStmt.class);
        if( ods == null ){
            throw new _draftException("No ForEachStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ForEachStmt forEachStmt( Ex.Command command ){
        return forEachStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ForEachStmt forEachStmt( Consumer<A> command ){
        return forEachStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ForEachStmt forEachStmt( BiConsumer<A,B> command ){
        return forEachStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ForEachStmt forEachStmt( Ex.TriConsumer<A,B,C> command ){
        return forEachStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ForEachStmt forEachStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return forEachStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     * i.e."if(a==1){...}"
     * @param code
     * @return
     */
    public static IfStmt ifStmt( String code ){
        return ifStmt( new String[]{code});
    }

    /**
     * Builds a IfStmt from the first IfStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.ifStmt( (Integer a)-> { if(a==1){ System.out.prnitln(1); } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static IfStmt ifStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(IfStmt.class).get();
    }

    public static IfStmt ifStmt( LambdaExpr le ){
        Optional<IfStmt> ods = le.findFirst(IfStmt.class);
        if( ods == null ){
            throw new _draftException("No IfStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static IfStmt ifStmt( Ex.Command command ){
        return ifStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> IfStmt ifStmt( Consumer<A> command ){
        return ifStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> IfStmt ifStmt( BiConsumer<A,B> command ){
        return ifStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> IfStmt ifStmt( Ex.TriConsumer<A,B,C> command ){
        return ifStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> IfStmt ifStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return ifStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     *
     * @param code
     * @return
     */
    public static LabeledStmt labeledStmt( String code ){
        return labeledStmt( new String[]{code});
    }

    /**
     * Builds a LabeledStmt from the first LabeledStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> {
     *    label: System.out.println(i};
     *    } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static LabeledStmt labeledStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(LabeledStmt.class).get();
    }

    public static LabeledStmt labeledStmt( LambdaExpr le ){
        Optional<LabeledStmt> ods = le.findFirst(LabeledStmt.class);
        if( ods == null ){
            throw new _draftException("No IfStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static LabeledStmt labeledStmt( Ex.Command command ){
        return labeledStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> LabeledStmt labeledStmt( Consumer<A> command ){
        return labeledStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> LabeledStmt labeledStmt( BiConsumer<A,B> command ){
        return labeledStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> LabeledStmt labeledStmt( Ex.TriConsumer<A,B,C> command ){
        return labeledStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> LabeledStmt labeledStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return labeledStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    /** i.e. "class C{ int a, b; }" */
    public static final Class<LocalClassDeclarationStmt> LOCAL_CLASS = LocalClassDeclarationStmt.class;

    /**
     * Converts from a String to a LocalClass
     * i.e. "class C{ int a, b; }"
     * @param code the code that represents a local class
     * @return the AST implementation
     */
    public static LocalClassDeclarationStmt localClassStmt(String... code ) {
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

    /**
     * Finds the first ReturnStmt in this Function
     * @param s
     * @return
     */
    public static ReturnStmt returnStmt( Function<? extends Object, ? extends Object> s ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return returnStmt(ste);
    }

    public static ReturnStmt returnStmt( Supplier<? extends Object> s ) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return returnStmt(ste);
    }

    public static ReturnStmt returnStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx(ste);
        if( le.getExpressionBody().isPresent() ){
            return new ReturnStmt(le.getExpressionBody().get());
        }
        return le.getBody().findFirst(ReturnStmt.class).get();
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

    public static SwitchStmt switchStmt( String code ){
        return switchStmt( new String[]{code});
    }

    /**
     * Builds a DoStmt from the first DoStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> { do{ System.out.println(1}; System.out.println(2); }while(i==1) } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static SwitchStmt switchStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(SwitchStmt.class).get();
    }

    public static SwitchStmt switchStmt( LambdaExpr le ){
        Optional<SwitchStmt> ods = le.findFirst(SwitchStmt.class);
        if( ods == null ){
            throw new _draftException("No SwitchStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static SwitchStmt switchStmt( Ex.Command command ){
        return switchStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> SwitchStmt switchStmt( Consumer<A> command ){
        return switchStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> SwitchStmt switchStmt( BiConsumer<A,B> command ){
        return switchStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> SwitchStmt switchStmt( Ex.TriConsumer<A,B,C> command ){
        return switchStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> SwitchStmt switchStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return switchStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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
     * i.e. "synchronized(e) { ...}"
     * @param code
     * @return
     */
    public static SynchronizedStmt synchronizedStmt( String code ){
        return synchronizedStmt( new String[]{code});
    }

    /**
     * Builds a SynchronizedStmt from the first SynchronizedStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.synchronizedStmt( (Integer i)-> { synchronized(i) { System.out.println(1}; } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static SynchronizedStmt synchronizedStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(SynchronizedStmt.class).get();
    }

    public static SynchronizedStmt synchronizedStmt( LambdaExpr le ){
        Optional<SynchronizedStmt> ods = le.findFirst(SynchronizedStmt.class);
        if( ods == null ){
            throw new _draftException("No SynchronizedStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static SynchronizedStmt synchronizedStmt( Ex.Command command ){
        return synchronizedStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> SynchronizedStmt synchronizedStmt( Consumer<A> command ){
        return synchronizedStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> SynchronizedStmt synchronizedStmt( BiConsumer<A,B> command ){
        return synchronizedStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> SynchronizedStmt synchronizedStmt( Ex.TriConsumer<A,B,C> command ){
        return synchronizedStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> SynchronizedStmt synchronizedStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return synchronizedStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     * i.e."throw new RuntimeException("SHOOT");"
     *
     * @param code
     * @return
     */
    public static ThrowStmt throwStmt( String code ){
        return throwStmt( new String[]{code});
    }

    /**
     * Builds a DoStmt from the first DoStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> { do{ System.out.println(1}; System.out.println(2); }while(i==1) } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static ThrowStmt throwStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(ThrowStmt.class).get();
    }

    public static ThrowStmt throwStmt( LambdaExpr le ){
        Optional<ThrowStmt> ods = le.findFirst(ThrowStmt.class);
        if( ods == null ){
            throw new _draftException("No ThrowStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ThrowStmt throwStmt( Ex.Command command ){
        return throwStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ThrowStmt throwStmt( Consumer<A> command ){
        return throwStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ThrowStmt throwStmt( BiConsumer<A,B> command ){
        return throwStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ThrowStmt throwStmt( Ex.TriConsumer<A,B,C> command ){
        return throwStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ThrowStmt throwStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return throwStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     * i.e. "try{ clazz.getMethod("fieldName"); }"
     * @param code
     * @return
     */
    public static TryStmt tryStmt( String code ){
        return tryStmt( new String[]{code});
    }

    /**
     * Builds a TryStmt from the first TryStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> { try{ System.out.println(1}; System.out.println(2); }catch(Exception e){ return 0; } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a TryStmt based on the Lambda Expression block
     */
    public static TryStmt tryStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(TryStmt.class).get();
    }

    public static TryStmt tryStmt( LambdaExpr le ){
        Optional<TryStmt> ods = le.findFirst(TryStmt.class);
        if( ods == null ){
            throw new _draftException("No TryStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static TryStmt tryStmt( Ex.Command command ){
        return tryStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> TryStmt tryStmt( Consumer<A> command ){
        return tryStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> TryStmt tryStmt( BiConsumer<A,B> command ){
        return tryStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> TryStmt tryStmt( Ex.TriConsumer<A,B,C> command ){
        return tryStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> TryStmt tryStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return tryStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
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

    /**
     * i.e."while(i< 1) { ...}"
     *
     * @param code
     * @return
     */
    public static WhileStmt whileStmt( String code ){
        return whileStmt( new String[]{code});
    }

    /**
     * Builds a WhileStmt from the first WhileStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> { do{ System.out.println(1}; System.out.println(2); }while(i==1) } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a BlockStmt based on the Lambda Expression block
     */
    public static WhileStmt whileStmt( StackTraceElement ste ){
        LambdaExpr le = Ex.lambdaEx( ste );
        return le.findFirst(WhileStmt.class).get();
    }

    public static WhileStmt whileStmt( LambdaExpr le ){
        Optional<WhileStmt> ods = le.findFirst(WhileStmt.class);
        if( ods == null ){
            throw new _draftException("No WhileStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static WhileStmt whileStmt( Ex.Command command ){
        return whileStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> WhileStmt whileStmt( Consumer<A> command ){
        return whileStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> WhileStmt whileStmt( BiConsumer<A,B> command ){
        return whileStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> WhileStmt whileStmt( Ex.TriConsumer<A,B,C> command ){
        return whileStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> WhileStmt whileStmt( Ex.QuadConsumer<A,B,C,D> command ){
        return whileStmt(Ex.lambdaEx( Thread.currentThread().getStackTrace()[2]));
    }

    /** an empty statement i.e. ";" */
    public static final Class<EmptyStmt> EMPTY = EmptyStmt.class;

}


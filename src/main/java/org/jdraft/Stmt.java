package org.jdraft;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.stmt.*;

import java.util.List;
import java.util.Optional;
import java.util.function.*;

import org.jdraft.text.Text;
import org.jdraft.walk.Walk;

/**
 * Translator for creating JavaParser AST {@link Statement} instances
 * (from Strings, Stack Traces, etc.)
 *
 * Also provides functionality related to manipulating Statements as they occur in code.
 * (Particularly because we often want to look at and manipulate code that is sequentially
 * occurring before or after some Statement:
 * <PRE>
 * void m() {
 *     int i=0;
 *     increment: { i++; }
 *     System.out.println(i);
 * }
 * </PRE>
 * ... and (in the code above) if we are looking at the statement "increment: { i++; }"
 * ... we often want to know what coded happens "before" this statement within the same block/scope (i.e. "int i=0;")
 * ... we also often want to know what happens "after" this statement within the same block (i.e. "System.out.println(i);")
 * However in JavaParser... Statement nodes DO NOT have direct access to thier siblings, but rather you MUST
 * go to the BlockStmt (parent) and then find the node to get the preceding or next Statement.  While this
 * is not hard (from an implementation perspective) it can be tedious, and therefore we added some
 *
 * @author Eric
 */
public enum Stmt {
    ;

    /**
     * Adds one or more statements before this statement
     * @param targetStatement the statement to reference where to add
     * @param stmtsToAddBefore all the statements to be added in-order before the target statement
     * @return the BlockStmt containing the  new Statement block or null if this task is unsuccessful
     */
    public static BlockStmt addStatementsBefore( Statement targetStatement, Statement...stmtsToAddBefore){
        if( Walk.isParent(targetStatement, BlockStmt.class) ){
            BlockStmt parentStmt = (BlockStmt) targetStatement.getParentNode().get();
            int index = getStatementIndex(parentStmt,targetStatement );
            if( index == -1){
                return null;
            }
            for(int i=0;i<stmtsToAddBefore.length;i++){
                parentStmt.addStatement(index + i, stmtsToAddBefore[i]);
            }
            return parentStmt;
        }
        return null;
    }

    /**
     * Finds the index of the Statement statement in the parent block
     * NOTE: uses REFERENCE EQUALITY rather than logical equality for checking as there
     * MAY be a situation where a block has the same (logical statement) multiple times
     * in the same parseBlock
     *
     * @param parentBlock
     * @param statement
     * @return
     */
    public static int getStatementIndex(BlockStmt parentBlock, Statement statement){
        List<Statement> sts = parentBlock.getStatements();
        for(int i=0;i<sts.size(); i++){
            if( sts.get(i) == statement){
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds one or more statements before this statement
     * @param targetStatement the statement to reference where to add
     * @param stmtsToAddAfter all the statements to be added in-order AFTER the target statement
     * @return the BlockStmt containing the  new Statement block or null if this task is unsuccessful
     */
    public static BlockStmt addStatementsAfter( Statement targetStatement, Statement...stmtsToAddAfter){
        if( Walk.isParent(targetStatement, BlockStmt.class) ){
            BlockStmt parentStmt = (BlockStmt) targetStatement.getParentNode().get();
            int index = getStatementIndex(parentStmt,targetStatement );
            if( index == -1){
                return null;
            }
            index++; //I want to add statements AFTER this statement
            for(int i=0;i<stmtsToAddAfter.length;i++){
                parentStmt.addStatement(index + i, stmtsToAddAfter[i]);
            }
            return parentStmt;
        }
        return null;
    }

    /**
     * Gets the previous Statement (in the same scope) that occurred before this one
     * (or null if this is the first Statement in the Block)
     * @param st
     * @return
     */
    public static Statement previous(Statement st ){
        if( Walk.isParent(st, BlockStmt.class) ) {
            BlockStmt parentStmt = (BlockStmt) st.getParentNode().get();
            int index = getStatementIndex(parentStmt, st);
            if( index <=0 ){
                return null;
            }
            return parentStmt.getStatements().get(index - 1);
        }
        return null;
    }

    /**
     * Gets the previous Statement (in the same scope) that occurred before this one
     * (or null if this is the first Statement in the Block)
     * @param st
     * @return
     */
    public static Statement next(Statement st ){
        if( Walk.isParent(st, BlockStmt.class) ) {
            BlockStmt parentStmt = (BlockStmt) st.getParentNode().get();
            int index = getStatementIndex(parentStmt, st);
            if( index < 0 ){
                return null;
            }
            if( parentStmt.getStatements().size() > (index + 1) ){
                return parentStmt.getStatements().get(index + 1);
            }
        }
        return null;
    }

    /**
     * Lambda used for converting an actual Statement into a NOOP Commented statement
     * i.e.<PRE>
     * MethodDeclaration md = Ast.method("void m(){",
     * "    System.out.println(1);",
     * "}");
     *
     * //comment out any statement matches pattern System.out.println(????);
     * $stmt.of("System.out.println($any$);").commentOut(md, Stmt.REPLACE_WITH_EMPTY_STMT_COMMENT);
     *
     * System.out.println( md );
     * //Prints:
     *     void m(){
     *         /*<code>System.out.println(1);</code>* /
     *         ;
     *     }
     * </PRE>
     *
     * NOTE: if we print with the {@link Print#EMPTY_STATEMENT_COMMENT_PRINTER} ONLY the comment
     * will be printed (and not the ";" empty statement) like this:
     *
     * System.out.println( md.toString(Stmt.REPLACE_WITH_EMPTY_STMT_COMMENT) );
     * //Prints:
     * void m(){
     *     {/*<code>System.out.println(1);</code>* /}
     * }
     * </PRE>
     * NOTE: we do not print the empty statement ";" ONLY the comment
     * @see Print#EMPTY_STATEMENT_COMMENT_PRINTER
     */
    public static final Function<Statement,Statement> REPLACE_WITH_EMPTY_STMT_COMMENT_FN = (st)->{
        if( st.getParentNode().isPresent() && ! (st.getParentNode().get() instanceof Statement)){
            return null;
        }
        Statement es = new EmptyStmt(); //create a new empty statement
        es.setComment( new BlockComment("<code>"+st.toString(Print.PRINT_NO_COMMENTS)+"</code>") );
        st.replace( es );
        return es;
    };

    /**
     * Lambda used for converting an actual Statement into a NOOP Commented statement
     * i.e.<PRE>
     * MethodDeclaration md = Ast.method("void m(){",
     * "    System.out.println(1);",
     * "}");
     *
     * //comment out any statement matches pattern System.out.println(????);
     * $stmt.of("System.out.println($any$);").commentOut(md, Stmt.REPLACE_WITH_EMPTY_COMMENT_BLOCK);
     *
     * System.out.println( md );
     * //Prints:
     *     void m(){
     *         { /*<code>System.out.println(1);</code>* /
     *         }
     *     }
     * </PRE>
     *
     * NOTE: if we print with the {@link Print#EMPTY_STATEMENT_COMMENT_PRINTER} ONLY the comment
     * will be printed (and not the ";" empty statement) like this:
     *
     * System.out.println( md.toString(Ast.EMPTY_STATEMENT_COMMENT_PRINTER) );
     * //Prints:
     * void m(){
     *     /*<code>System.out.println(1);</code>* /
     * }
     * </PRE>
     * NOTE: we do not print the empty statement ";" ONLY the comment
     * @see Print#EMPTY_STATEMENT_COMMENT_PRINTER
     */
    public static final Function<Statement, Statement> REPLACE_WITH_EMPTY_COMMENT_BLOCK_FN = (st)->{
        BlockStmt bs = Ast.blockStmt("{/*<code>"+st.toString(Print.PRINT_NO_COMMENTS)+"</code>*" + "/}");
        if( bs != null ) {
            st.replace(bs);
        }
        return bs;
    };

    /**
     * Replace this statement with a comment
     * (a "comment" can be an Empty Statement with a Comment:
     * <PRE>
     *  commentOut( Stmt.of("System.out.println(1);") );
     *  //returns
     * /*<code>System.out.println(1);</code>* /
     * ;
     * </PRE>
     * @param st
     * @return
     */
    public static Statement commentOut(Statement st){
        return commentOut(st, REPLACE_WITH_EMPTY_STMT_COMMENT_FN);
    }

    /**
     * Replace this statement with a comment
     * (a "comment" can be an Empty Statement with a Comment:
     * <PRE>
     * commentOut(Stmt.of("System.out.println(1);"), REPLACE_WITH_EMPTY_STMT_COMMENT_FN);
     * /*<code>System.out.println(1);</code>* /
     * ;
     *
     * or a BlockStmt with interior comment
     * commentOut(Stmt.of("System.out.println(1);"), REPLACE_WITH_EMPTY_COMMENT_BLOCK_FN
     * {/*<code>System.out.println(1);</code>* /}
     * </PRE>
     * @param st
     * @param stmtConsumer
     * @return
     */
    public static Statement commentOut( Statement st, Function<Statement,Statement> stmtConsumer ){
        return stmtConsumer.apply(st);
    }

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
        return from( Expr.lambdaExpr( lambdaStackTrace ) );
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
     * NOTE: IMPORTANT CALLER NOTE : TO ONLY HAVE (1) caller site PER LINE
     * I.E. DONT DO THIS:<PRE>
     *     Stmt.of( ()->assert(1==1) ), Stmt.of( ()->assert(2==2))
     * </PRE>
     * @param c the lambda
     * @return the AST LambdaExpr representation for the runtime Command
     */
    public static  Statement of( Expr.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return from( ste );
    }

    public static <T extends Object> Statement of( Supplier<T> c ){
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
    public static <T extends Object, U extends Object, V extends Object, W extends Object> Statement of( Expr.QuadConsumer<T, U, V, W> c ){
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
        if( (str.startsWith("this") || str.startsWith("super")) && !str.startsWith("this.") ){
            ParseResult<ExplicitConstructorInvocationStmt> pre =
                    Ast.JAVAPARSER.parseExplicitConstructorInvocationStmt(str);
            if( pre.isSuccessful() ){
                return pre.getResult().get();
            }
            throw new _jdraftException("unable to parse ExplicitConstructor :\""+ str+"\""+ System.lineSeparator()+ pre.getProblems());
        }
        if( str.startsWith("{") ){
            return blockStmt(str);
        }
        ParseResult<Statement> pr = Ast.JAVAPARSER.parseStatement(str);
        if( pr.isSuccessful() ){
            return pr.getResult().get();
        }
        throw new _jdraftException("unable to parse Statement \""+str+"\""+System.lineSeparator()+" "+pr.getProblems());
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(AssertStmt.class).get();
    }

    /**
     *
     * @param le
     * @return
     */
    public static AssertStmt assertStmt( LambdaExpr le ){
        Optional<AssertStmt> ods = le.findFirst(AssertStmt.class);
        if( ods == null ){
            throw new _jdraftException("No AssertStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static AssertStmt assertStmt( Expr.Command command ){
        return assertStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> AssertStmt assertStmt( Consumer<A> command ){
        return assertStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> AssertStmt assertStmt( BiConsumer<A,B> command ){
        return assertStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> AssertStmt assertStmt( Expr.TriConsumer<A,B,C> command ){
        return assertStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> AssertStmt assertStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return assertStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }
    
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
                throw new _jdraftException("Unclosed comment");
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
            //System.out.println( comb );
            ParseResult<BlockStmt> pbs = Ast.JAVAPARSER.parseBlock(comb );
            if( pbs.isSuccessful() ){
                return pbs.getResult().get();
            }
            throw new _jdraftException("unable to parse block : "+System.lineSeparator()+
                    Text.indent(comb)+System.lineSeparator()+
                    pbs.getProblems() );
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
        if( combined.contains("super") || combined.contains("this")){

            combined = "UNKNOWN()"+ combined;
            ParseResult<ConstructorDeclaration> cbd = Ast.JAVAPARSER.parseBodyDeclaration(combined);
            if( cbd.isSuccessful() ){
                BlockStmt bs = cbd.getResult().get().getBody();
                bs.remove();
                return bs;
            }
        }
        ParseResult<BlockStmt> pbs = Ast.JAVAPARSER.parseBlock(combined);
        if( pbs.isSuccessful() ){
            return pbs.getResult().get();
        }
        throw new _jdraftException("Unable to parse blockStmt :"+System.lineSeparator()+ Text.indent(combined) +System.lineSeparator()+ pbs.getProblems());
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
        LambdaExpr le = Expr.lambdaExpr( ste );
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static BlockStmt blockStmt(Expr.Command command ){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object> BlockStmt blockStmt(Consumer<A> command ){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object> BlockStmt blockStmt(BiConsumer<A,B> command ){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object, C extends Object> BlockStmt blockStmt(Expr.TriConsumer<A,B,C> command ){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> BlockStmt blockStmt(Expr.QuadConsumer<A,B,C,D> command ){
        LambdaExpr le = Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]);
        Statement st = le.getBody();
        if( st.isBlockStmt() ){
            return st.asBlockStmt();
        }
        return new BlockStmt().addStatement( st );
    }

    /**
     * i.e."break;" or "break outer;"
     * @param code String representing the break of
     * @return the breakStmt
     */
    public static BreakStmt breakStmt(String... code ) {
        return of( code ).asBreakStmt();
    }

    /** 
     * i.e."continue outer;" 
     *
     * @param code
     * @return 
     */
    public static ContinueStmt continueStmt(String... code ) {
        return of( code ).asContinueStmt();
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(DoStmt.class).get();
    }

    public static DoStmt doStmt( LambdaExpr le ){
        Optional<DoStmt> ods = le.findFirst(DoStmt.class);
        if( ods == null ){
            throw new _jdraftException("No DoStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static DoStmt doStmt( Expr.Command command ){
        return doStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> DoStmt doStmt( Consumer<A> command ){
        return doStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> DoStmt doStmt( BiConsumer<A,B> command ){
        return doStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> DoStmt doStmt( Expr.TriConsumer<A,B,C> command ){
        return doStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> DoStmt doStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return doStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    /** 
     * i.e."this(100,2900);" "super('c',203);"
     * 
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt constructorCallStmt(String... code ) {
        String cd = Text.combine(code);
        ParseResult<ExplicitConstructorInvocationStmt> eps = Ast.JAVAPARSER.parseExplicitConstructorInvocationStmt(cd);
        if( eps.isSuccessful() ){
            return eps.getResult().get();
        }
        throw new _jdraftException("unable to parse constructor call \""+cd+"\""+System.lineSeparator()+eps.getProblems());
    }

    /**
     * i.e."super('c',203);" "this(1);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt(String... code ) {
        return constructorCallStmt( code );
    }

    /**
     * i.e."this(100,2900);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt thisCallStmt(String... code ) {
        return constructorCallStmt( code );
    }

    /**
     * i.e."super('c',203);"
     *
     * @param code the java code
     * @return an ExplicitConstructorInvocationStmt based on the code
     */
    public static ExplicitConstructorInvocationStmt superCallStmt(String... code ) {
        return constructorCallStmt( code );
    }

    public static EmptyStmt emptyStmt(){
        return new EmptyStmt();
    }

    public static EmptyStmt emptyStmt(String...code){
        if( Text.combine(code).equals(";")) {
            return new EmptyStmt();
        }
        throw new _jdraftException("Invalid characters to represent EmptyStmt: "+Text.combine(code));
    }

    /** 
     * i.e."s += t;" 
     * 
     * @param code
     * @return 
     */
    public static ExpressionStmt expressionStmt(String... code ) {
        String str = Text.combine( code );
        if(str.endsWith(";") ){
            return new ExpressionStmt( Expr.of(str.substring(0, str.length() -1) ) );
        }
        return of( code ).asExpressionStmt();
    }

    /**
     * i.e."for(int i=0; i<100;i++) {...}"
     * @param code
     * @return
     */
    public static ExpressionStmt expressionStmt(String code ){
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
    public static ExpressionStmt expressionStmt(StackTraceElement ste ){
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(ExpressionStmt.class).get();
    }

    public static ExpressionStmt expressionStmt(LambdaExpr le ){
        Optional<ExpressionStmt> ods = le.findFirst(ExpressionStmt.class);
        if( ods == null ){
            throw new _jdraftException("No ExpressionStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ExpressionStmt expressionStmt(Expr.Command command ){
        return expressionStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ExpressionStmt expressionStmt(Consumer<A> command ){
        return expressionStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ExpressionStmt expressionStmt(BiConsumer<A,B> command ){
        return expressionStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ExpressionStmt expressionStmt(Expr.TriConsumer<A,B,C> command ){
        return expressionStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ExpressionStmt expressionStmt(Expr.QuadConsumer<A,B,C,D> command ){
        return expressionStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(ForStmt.class).get();
    }

    public static ForStmt forStmt( LambdaExpr le ){
        Optional<ForStmt> ods = le.findFirst(ForStmt.class);
        if( ods == null ){
            throw new _jdraftException("No ForStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ForStmt forStmt( Expr.Command command ){
        return forStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ForStmt forStmt( Consumer<A> command ){
        return forStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ForStmt forStmt( BiConsumer<A,B> command ){
        return forStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ForStmt forStmt( Expr.TriConsumer<A,B,C> command ){
        return forStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ForStmt forStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return forStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }


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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(ForEachStmt.class).get();
    }

    public static ForEachStmt forEachStmt( LambdaExpr le ){
        Optional<ForEachStmt> ods = le.findFirst(ForEachStmt.class);
        if( ods == null ){
            throw new _jdraftException("No ForEachStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ForEachStmt forEachStmt( Expr.Command command ){
        return forEachStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ForEachStmt forEachStmt( Consumer<A> command ){
        return forEachStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ForEachStmt forEachStmt( BiConsumer<A,B> command ){
        return forEachStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ForEachStmt forEachStmt( Expr.TriConsumer<A,B,C> command ){
        return forEachStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ForEachStmt forEachStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return forEachStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(IfStmt.class).get();
    }

    public static IfStmt ifStmt( LambdaExpr le ){
        Optional<IfStmt> ods = le.findFirst(IfStmt.class);
        if( ods == null ){
            throw new _jdraftException("No IfStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static IfStmt ifStmt( Expr.Command command ){
        return ifStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> IfStmt ifStmt( Consumer<A> command ){
        return ifStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> IfStmt ifStmt( BiConsumer<A,B> command ){
        return ifStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> IfStmt ifStmt( Expr.TriConsumer<A,B,C> command ){
        return ifStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> IfStmt ifStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return ifStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(LabeledStmt.class).get();
    }

    public static LabeledStmt labeledStmt( LambdaExpr le ){
        Optional<LabeledStmt> ods = le.findFirst(LabeledStmt.class);
        if( ods == null ){
            throw new _jdraftException("No IfStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static LabeledStmt labeledStmt( Expr.Command command ){
        return labeledStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> LabeledStmt labeledStmt( Consumer<A> command ){
        return labeledStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> LabeledStmt labeledStmt( BiConsumer<A,B> command ){
        return labeledStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> LabeledStmt labeledStmt( Expr.TriConsumer<A,B,C> command ){
        return labeledStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> LabeledStmt labeledStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return labeledStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    /**
     * Converts from a String to a LocalClass
     * i.e. "class C{ int a, b; }"
     * @param code the code that represents a local class
     * @return the AST implementation
     */
    public static LocalClassDeclarationStmt localClassDeclarationStmt(String... code ) {
        return of( code ).asLocalClassDeclarationStmt();
    }

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
        LambdaExpr le = Expr.lambdaExpr(ste);
        if( le.getExpressionBody().isPresent() ){
            return new ReturnStmt(le.getExpressionBody().get());
        }
        return le.getBody().findFirst(ReturnStmt.class).get();
    }
    
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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(SwitchStmt.class).get();
    }

    public static SwitchStmt switchStmt( LambdaExpr le ){
        Optional<SwitchStmt> ods = le.findFirst(SwitchStmt.class);
        if( ods == null ){
            throw new _jdraftException("No SwitchStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static SwitchStmt switchStmt( Expr.Command command ){
        return switchStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> SwitchStmt switchStmt( Consumer<A> command ){
        return switchStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> SwitchStmt switchStmt( BiConsumer<A,B> command ){
        return switchStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> SwitchStmt switchStmt( Expr.TriConsumer<A,B,C> command ){
        return switchStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> SwitchStmt switchStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return switchStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(SynchronizedStmt.class).get();
    }

    public static SynchronizedStmt synchronizedStmt( LambdaExpr le ){
        Optional<SynchronizedStmt> ods = le.findFirst(SynchronizedStmt.class);
        if( ods == null ){
            throw new _jdraftException("No SynchronizedStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static SynchronizedStmt synchronizedStmt( Expr.Command command ){
        return synchronizedStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> SynchronizedStmt synchronizedStmt( Consumer<A> command ){
        return synchronizedStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> SynchronizedStmt synchronizedStmt( BiConsumer<A,B> command ){
        return synchronizedStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> SynchronizedStmt synchronizedStmt( Expr.TriConsumer<A,B,C> command ){
        return synchronizedStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> SynchronizedStmt synchronizedStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return synchronizedStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    
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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(ThrowStmt.class).get();
    }

    public static ThrowStmt throwStmt( LambdaExpr le ){
        Optional<ThrowStmt> ods = le.findFirst(ThrowStmt.class);
        if( ods == null ){
            throw new _jdraftException("No ThrowStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static ThrowStmt throwStmt( Expr.Command command ){
        return throwStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> ThrowStmt throwStmt( Consumer<A> command ){
        return throwStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> ThrowStmt throwStmt( BiConsumer<A,B> command ){
        return throwStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> ThrowStmt throwStmt( Expr.TriConsumer<A,B,C> command ){
        return throwStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> ThrowStmt throwStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return throwStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    
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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(TryStmt.class).get();
    }

    public static TryStmt tryStmt( LambdaExpr le ){
        Optional<TryStmt> ods = le.findFirst(TryStmt.class);
        if( ods == null ){
            throw new _jdraftException("No TryStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static TryStmt tryStmt( Expr.Command command ){
        return tryStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> TryStmt tryStmt( Consumer<A> command ){
        return tryStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> TryStmt tryStmt( BiConsumer<A,B> command ){
        return tryStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> TryStmt tryStmt( Expr.TriConsumer<A,B,C> command ){
        return tryStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> TryStmt tryStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return tryStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

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
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(WhileStmt.class).get();
    }

    public static WhileStmt whileStmt( LambdaExpr le ){
        Optional<WhileStmt> ods = le.findFirst(WhileStmt.class);
        if( ods == null ){
            throw new _jdraftException("No WhileStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static WhileStmt whileStmt( Expr.Command command ){
        return whileStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> WhileStmt whileStmt( Consumer<A> command ){
        return whileStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> WhileStmt whileStmt( BiConsumer<A,B> command ){
        return whileStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> WhileStmt whileStmt( Expr.TriConsumer<A,B,C> command ){
        return whileStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> WhileStmt whileStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return whileStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    /**
     * i.e."yield 6;"
     *
     * @param code
     * @return
     */
    public static YieldStmt yieldStmt( String... code ) {

        String str = "switch(unknown){ case UNKNOWN: "+Text.combine(code)+" }";
        SwitchExpr se = Expr.switchExpr(str);
        YieldStmt ys = (YieldStmt)se.getEntry(0).getStatement(0);
        ys.getParentNode().get().remove(ys);
        return ys;
    }

    /**
     * i.e."yield 6;"
     *
     * @param code
     * @return
     */
    public static YieldStmt yieldStmt( String code ){
        return yieldStmt( new String[]{code});
    }

    /**
     * Builds a WhileStmt from the first WhileStmt within the Lambda expression code found based on the
     * location of the
     * StackTraceElement.  i.e.
     * Stmt.doStmt( (Integer i)-> { do{ System.out.println(1}; System.out.println(2); }while(i==1) } );
     *
     * @param ste the stackTraceElement for the caller location of the
     * @return a YieldStmt based on the Lambda Expression block
     */
    public static YieldStmt yieldStmt( StackTraceElement ste ){
        LambdaExpr le = Expr.lambdaExpr( ste );
        return le.findFirst(YieldStmt.class).get();
    }

    public static YieldStmt yieldStmt( LambdaExpr le ){
        Optional<YieldStmt> ods = le.findFirst(YieldStmt.class);
        if( ods == null ){
            throw new _jdraftException("No YieldStmt in lambda "+ le );
        }
        return ods.get();
    }

    public static YieldStmt yieldStmt( Expr.Command command ){
        return yieldStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object> YieldStmt yieldStmt( Consumer<A> command ){
        return yieldStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object> YieldStmt yieldStmt( BiConsumer<A,B> command ){
        return yieldStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object> YieldStmt yieldStmt( Expr.TriConsumer<A,B,C> command ){
        return yieldStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> YieldStmt yieldStmt( Expr.QuadConsumer<A,B,C,D> command ){
        return yieldStmt(Expr.lambdaExpr( Thread.currentThread().getStackTrace()[2]));
    }

    public static class Classes{

        public static final Class<AssertStmt> ASSERT = AssertStmt.class;

        public static final Class<BlockStmt> BLOCK = BlockStmt.class;

        /** i.e. "break;" "break outer;" */
        public static final Class<BreakStmt> BREAK = BreakStmt.class;

        /** i.e. "continue outer;" */
        public static final Class<ContinueStmt> CONTINUE = ContinueStmt.class;

        /** i.e. "do{ System.out.println(1); }while( a < 100 );" */
        public static final Class<DoStmt> DO = DoStmt.class;

        /** i.e. "this(100,2900);", "super('c',123);"*/
        public static final Class<ExplicitConstructorInvocationStmt> CONSTRUCTOR_CALL_STMT
                = ExplicitConstructorInvocationStmt.class;

        /** i.e. "s += t;" */
        public static final Class<ExpressionStmt> EXPRESSION_STMT = ExpressionStmt.class;

        /** i.e. "for(int i=0; i<100;i++) {...}" */
        public static final Class<ForStmt> FOR = ForStmt.class;

        /** i.e. "for(String element:arr){...}" */
        public static final Class<ForEachStmt> FOR_EACH = ForEachStmt.class;

        /** i.e. "if(a==1){...}" */
        public static final Class<IfStmt> IF = IfStmt.class;

        /** i.e. "outer:   start = getValue();" */
        public static final Class<LabeledStmt> LABELED = LabeledStmt.class;

        /** i.e. "class C{ int a, b; }" */
        public static final Class<LocalClassDeclarationStmt> LOCAL_CLASS = LocalClassDeclarationStmt.class;

        /** i.e. "return VALUE;" */
        public static final Class<ReturnStmt> RETURN = ReturnStmt.class;

        /** i.e. "switch(a) { case 1: break; default : doMethod(a); }" */
        public static final Class<SwitchStmt> SWITCH = SwitchStmt.class;

        /** i.e. "synchronized(e) { ...}" */
        public static final Class<SynchronizedStmt> SYNCHRONIZED = SynchronizedStmt.class;

        /** i.e. "throw new RuntimeException("SHOOT");"*/
        public static final Class<ThrowStmt> THROW = ThrowStmt.class;

        /** i.e. "try{ clazz.getMethod("fieldName"); }" */
        public static final Class<TryStmt> TRY = TryStmt.class;

        /** i.e. "while(i< 1) { ... }"*/
        public static final Class<WhileStmt> WHILE = WhileStmt.class;

        /** i.e. "yield 6;" */
        public static final Class<YieldStmt> YIELD = YieldStmt.class;

        /** an empty statement i.e. ";" */
        public static final Class<EmptyStmt> EMPTY = EmptyStmt.class;
    }
}


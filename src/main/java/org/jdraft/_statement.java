package org.jdraft;

import com.github.javaparser.ast.stmt.*;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface _statement<S extends Statement, _S extends _statement> extends _java._node<S, _S>, _java._withComments<S, _S> {

    /**
     * Refine the ast() method to be more strict (only return Statements)
     * @return
     */
    S ast();

    interface _controlFlow<S extends Statement, _S extends _statement> extends _statement<S, _S>{

        /**
         *
         * @param <S>
         * @param <_S>
         * @see _doStmt
         * @see _forStmt
         * @see _forEachStmt
         * @see _whileStmt
         *
         */
        interface _loop <S extends Statement, _S extends _statement> extends _controlFlow<S, _S>{}

        /**
         * @param <S> the Ast Node type
         * @param <_S> the _statement type
         * @see _breakStmt
         * @see _continueStmt
         * @see _returnStmt
         * @see _throwStmt
         * @see _yieldStmt
         */
        interface _signal<S extends Statement, _S extends _statement> extends _controlFlow<S, _S>{}

        /**
         * Connditional
         * @param <S>
         * @param <_S>
         * @see _ifStmt
         * @see _forStmt
         * @see _doStmt
         * @see _forEachStmt
         * @see _whileStmt
         * @see _switchStmt
         * @see _tryStmt
         */
        interface _branching<S extends Statement, _S extends _statement> extends _controlFlow<S, _S>{}
    }

    Class<_assertStmt> ASSERT = _assertStmt.class;
    Class<_blockStmt> BLOCK = _blockStmt.class;          //scope
    Class<_breakStmt> BREAK = _breakStmt.class;          //signal
    Class<_continueStmt> CONTINUE = _continueStmt.class; //signal
    Class<_doStmt> DO = _doStmt.class;                   //branch //loop //scope
    Class<_emptyStmt> EMPTY = _emptyStmt.class;
    Class<_constructorCallStmt> CONSTRUCTOR_CALL = _constructorCallStmt.class;
    Class<_expressionStmt> EXPRESSION_STMT = _expressionStmt.class;
    Class<_forEachStmt> FOR_EACH = _forEachStmt.class; //branching //loop //scope
    Class<_forStmt> FOR = _forStmt.class;              //branching //loop //scope
    Class<_ifStmt> IF = _ifStmt.class;                 //branching //loop //scope
    Class<_labeledStmt> LABELED = _labeledStmt.class;
    Class<_localClassStmt> LOCAL_CLASS = _localClassStmt.class;
    Class<_returnStmt> RETURN = _returnStmt.class;     //signal
    Class<_switchStmt> SWITCH = _switchStmt.class;    //branching
    Class<_synchronizedStmt> SYNCHRONIZED = _synchronizedStmt.class; //scope
    Class<_throwStmt> THROW = _throwStmt.class;     //signal
    Class<_tryStmt> TRY = _tryStmt.class;           //branching
    Class<_whileStmt> WHILE = _whileStmt.class;     //branching //loop
    Class<_yieldStmt> YIELD = _yieldStmt.class;     //signal

    Class<? extends _statement>[] ALL = new Class[]{
            ASSERT, BLOCK, BREAK, CONTINUE, DO, EMPTY, CONSTRUCTOR_CALL, EXPRESSION_STMT, FOR_EACH,
            FOR, IF, LABELED, LOCAL_CLASS, RETURN, SYNCHRONIZED, SWITCH, THROW, TRY, WHILE, YIELD};

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
    static _statement of( Ex.Command c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of(Stmt.from( ste ));
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
    static <T extends Object> _statement of( Consumer<T> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of(Stmt.from( ste ));
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
    static <T extends Object, U extends Object> _statement of( BiConsumer<T, U> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of( Stmt.from( ste ));
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
    static <T extends Object, U extends Object, V extends Object> _statement of( Ex.TriConsumer<T, U, V> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of( Stmt.from( ste ));
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
    static <T extends Object, U extends Object, V extends Object, W extends Object> _statement of( Ex.QuadConsumer<T, U, V, W> c ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return of( Stmt.from(ste) );
    }

    static _statement of( String...stmtCode ){
        return of(Stmt.of(stmtCode));
    }

    static _statement of( Statement astStatement){
        if( astStatement instanceof AssertStmt){
            return new _assertStmt( (AssertStmt)astStatement);
        }
        if( astStatement instanceof BlockStmt){
            return new _blockStmt( (BlockStmt)astStatement);
        }
        if( astStatement instanceof BreakStmt){
            return new _breakStmt( (BreakStmt)astStatement);
        }
        if( astStatement instanceof ContinueStmt){
            return new _continueStmt( (ContinueStmt)astStatement);
        }
        if( astStatement instanceof DoStmt){
            return new _doStmt( (DoStmt)astStatement);
        }
        if( astStatement instanceof EmptyStmt){
            return new _emptyStmt( (EmptyStmt)astStatement);
        }
        if( astStatement instanceof ExplicitConstructorInvocationStmt ){
            return new _constructorCallStmt( (ExplicitConstructorInvocationStmt)astStatement);
        }
        if( astStatement instanceof ExpressionStmt){
            return new _expressionStmt( (ExpressionStmt)astStatement);
        }
        if( astStatement instanceof ForEachStmt){
            return new _forEachStmt((ForEachStmt)astStatement);
        }
        if( astStatement instanceof ForStmt){
            return new _forStmt( (ForStmt)astStatement);
        }
        if( astStatement instanceof IfStmt){
            return new _ifStmt( (IfStmt)astStatement);
        }
        if( astStatement instanceof LabeledStmt){
            return new _labeledStmt( (LabeledStmt)astStatement);
        }
        if( astStatement instanceof LocalClassDeclarationStmt){
            return new _localClassStmt( (LocalClassDeclarationStmt)astStatement);
        }
        if( astStatement instanceof ReturnStmt){
            return new _returnStmt( (ReturnStmt)astStatement);
        }
        if( astStatement instanceof SwitchStmt){
            return new _switchStmt( (SwitchStmt)astStatement);
        }
        if( astStatement instanceof SynchronizedStmt){
            return new _synchronizedStmt( (SynchronizedStmt)astStatement);
        }
        if( astStatement instanceof ThrowStmt){
            return new _throwStmt( (ThrowStmt)astStatement);
        }
        if( astStatement instanceof TryStmt){
            return new _tryStmt( (TryStmt)astStatement);
        }
        if( astStatement instanceof WhileStmt){
            return new _whileStmt( (WhileStmt)astStatement);
        }
        if( astStatement instanceof YieldStmt){
            return new _yieldStmt( (YieldStmt)astStatement);
        }
        throw new _jdraftException("Unsupported Statement "+ astStatement.getClass() + System.lineSeparator() + astStatement);
    }
}

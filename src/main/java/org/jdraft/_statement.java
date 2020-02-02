package org.jdraft;

import com.github.javaparser.ast.stmt.*;

public interface _statement<S extends Statement, _S extends _statement> extends _java._node<S, _S> {

    /**
     * Refine the ast() method to be more strict (only return Statements)
     * @return
     */
    S ast();

    //declaration
    //    localClass

    //control_flow
    //   looping doStmt, forStmt, forEachStmt, whileStmt
    //   terminal result, break, continue, thisOrSuperCall, throw, yield
    //   conditional doStmt, ifStmt, forStmt, forEachStmt, try, while

    //computation

    interface _controlFlow<S extends Statement, _S extends _statement> extends _statement<S, _S>{
        interface _loop <S extends Statement, _S extends _statement> extends _controlFlow<S, _S>{}
        interface _terminal<S extends Statement, _S extends _statement> extends _controlFlow<S, _S>{}
        interface _conditional<S extends Statement, _S extends _statement> extends _controlFlow<S, _S>{}
    }

    Class<_assertStmt> ASSERT = _assertStmt.class;
    Class<_blockStmt> BLOCK = _blockStmt.class;
    Class<_breakStmt> BREAK = _breakStmt.class;
    Class<_continueStmt> CONTINUE = _continueStmt.class;
    Class<_doStmt> DO = _doStmt.class;            //nodeWithBody
    Class<_emptyStmt> EMPTY = _emptyStmt.class;
    Class<_thisOrSuperCallStmt> THIS_OR_SUPER = _thisOrSuperCallStmt.class;
    Class<_expressionStmt> EXPRESSION_STMT = _expressionStmt.class;
    Class<_forEachStmt> FOR_EACH = _forEachStmt.class; //nodeWithBody
    Class<_forStmt> FOR = _forStmt.class;              //nodeWithBody
    Class<_ifStmt> IF = _ifStmt.class;                 //nodeWithBody
    Class<_labeledStmt> LABELED = _labeledStmt.class;
    Class<_localClassStmt> LOCAL_CLASS = _localClassStmt.class;
    Class<_returnStmt> RETURN = _returnStmt.class;
    Class<_synchronizedStmt> SYNCHRONIZED = _synchronizedStmt.class; //nodeWithBody
    Class<_throwStmt> THROW = _throwStmt.class;
    Class<_tryStmt> TRY = _tryStmt.class;           //
    Class<_whileStmt> WHILE = _whileStmt.class;     //looping
    Class<_yieldStmt> YIELD = _yieldStmt.class;

    Class<? extends _statement>[] ALL = new Class[]{
            ASSERT, BLOCK, BREAK, CONTINUE, DO, EMPTY, THIS_OR_SUPER, EXPRESSION_STMT, FOR_EACH,
            FOR, IF, LABELED, LOCAL_CLASS, RETURN, SYNCHRONIZED, THROW, TRY, WHILE, YIELD};

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
            return new _thisOrSuperCallStmt( (ExplicitConstructorInvocationStmt)astStatement);
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

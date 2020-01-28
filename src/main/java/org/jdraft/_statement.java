package org.jdraft;

import com.github.javaparser.ast.stmt.*;

public interface _statement<S extends Statement, _S extends _statement> extends _node<S, _S>{

    /**
     * Refine the ast() method to be more strict (only return Statements)
     * @return
     */
    S ast();

    static _statement of( String...stmtCode ){
        return of(Stmt.of(stmtCode));
    }

    static _statement of( Statement s){
        //if( s == null ){
         //   return new EmptyStmt();
        //}
        if( s instanceof AssertStmt){
            return new _assertStmt( (AssertStmt)s);
        }
        if( s instanceof BlockStmt){
            return new _blockStmt( (BlockStmt)s);
        }
        if( s instanceof BreakStmt){
            return new _breakStmt( (BreakStmt)s);
        }
        if( s instanceof ContinueStmt){
            return new _continueStmt( (ContinueStmt)s);
        }
        if( s instanceof DoStmt){
            return new _doStmt( (DoStmt)s);
        }
        if( s instanceof EmptyStmt){
            return new _emptyStmt( (EmptyStmt)s);
        }
        if( s instanceof ExplicitConstructorInvocationStmt ){
            return new _thisOrSuperCallStmt( (ExplicitConstructorInvocationStmt)s);
        }
        if( s instanceof ExpressionStmt){
            return new _expressionStmt( (ExpressionStmt)s);
        }
        if( s instanceof ForEachStmt){
            return new _forEachStmt((ForEachStmt)s);
        }
        if( s instanceof ForStmt){
            return new _forStmt( (ForStmt)s);
        }
        if( s instanceof IfStmt){
            return new _ifStmt( (IfStmt)s);
        }
        if( s instanceof LabeledStmt){
            return new _labeledStmt( (LabeledStmt)s);
        }
        if( s instanceof LocalClassDeclarationStmt){
            return new _localClassStmt( (LocalClassDeclarationStmt)s);
        }
        if( s instanceof ReturnStmt){
            return new _returnStmt( (ReturnStmt)s);
        }
        if( s instanceof SynchronizedStmt){
            return new _synchronizedStmt( (SynchronizedStmt)s);
        }
        if( s instanceof ThrowStmt){
            return new _throwStmt( (ThrowStmt)s);
        }
        if( s instanceof TryStmt){
            return new _tryStmt( (TryStmt)s);
        }
        if( s instanceof WhileStmt){
            return new _whileStmt( (WhileStmt)s);
        }
        if( s instanceof YieldStmt){
            return new _yieldStmt( (YieldStmt)s);
        }
        throw new _jdraftException("Unsupported Statement "+ s.getClass() + System.lineSeparator() + s);
    }
}

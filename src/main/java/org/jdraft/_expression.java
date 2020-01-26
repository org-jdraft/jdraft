package org.jdraft;

import com.github.javaparser.ast.expr.*;

public interface _expression<E extends Expression, _E extends _expression> extends _node<E, _E>{

    E ast();

    /**
     * Given an AST expression, return the _expression equivalent
     * @param e
     * @return
     */
    static _expression of( Expression e){
        if( e instanceof IntegerLiteralExpr ){
            return new _int( (IntegerLiteralExpr)e );
        }
        if( e instanceof DoubleLiteralExpr){
            return new _double( (DoubleLiteralExpr)e );
        }
        if( e instanceof CharLiteralExpr){
            return new _char( (CharLiteralExpr)e );
        }
        if( e instanceof BooleanLiteralExpr){
            return new _boolean( (BooleanLiteralExpr)e );
        }
        if( e instanceof LongLiteralExpr){
            return new _long( (LongLiteralExpr)e );
        }
        if( e instanceof StringLiteralExpr){
            return new _string( (StringLiteralExpr)e );
        }
        if( e instanceof ArrayAccessExpr){
            return new _arrayAccess( (ArrayAccessExpr)e);
        }
        if( e instanceof NameExpr){
            return new _nameEx( (NameExpr)e );
        }
        throw new RuntimeException("Unsupported "+ e.getClass()+" of "+ e);
    }
}

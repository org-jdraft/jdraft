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
        if( e instanceof AnnotationExpr ){
            return _anno.of( (AnnotationExpr)e);
        }
        if( e instanceof AssignExpr){
            return new _assign((AssignExpr)e);
        }
        if( e instanceof BinaryExpr){
            return new _binaryExpression((BinaryExpr)e);
        }
        if( e instanceof CastExpr){
            return new _cast((CastExpr)e);
        }
        if( e instanceof ClassExpr){
            return new _classExpression((ClassExpr)e);
        }
        if( e instanceof EnclosedExpr){
            return new _enclosedExpression((EnclosedExpr)e);
        }
        if( e instanceof FieldAccessExpr){
            return new _fieldAccess((FieldAccessExpr)e);
        }
        if( e instanceof InstanceOfExpr){
            return new _instanceOf((InstanceOfExpr)e);
        }
        if( e instanceof MethodCallExpr){
            return new _methodCall((MethodCallExpr)e);
        }
        if( e instanceof MethodReferenceExpr){
            return new _methodReference((MethodReferenceExpr)e);
        }
        if( e instanceof ObjectCreationExpr){
            return new _new((ObjectCreationExpr)e);
        }
        if( e instanceof NullLiteralExpr){
            return new _null((NullLiteralExpr)e);
        }
        if( e instanceof SuperExpr){
            return new _super((SuperExpr)e);
        }
        if( e instanceof ConditionalExpr){
            return new _ternary((ConditionalExpr)e);
        }
        if( e instanceof TextBlockLiteralExpr){
            return new _textBlock((TextBlockLiteralExpr)e);
        }
        if( e instanceof ThisExpr){
            return new _this( (ThisExpr)e);
        }
        if( e instanceof TypeExpr){
            return new _typeExpression( (TypeExpr)e);
        }
        if( e instanceof UnaryExpr){
            return new _unary( (UnaryExpr)e);
        }
        if( e instanceof VariableDeclarationExpr){
            return new _variable( (VariableDeclarationExpr)e);
        }
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
            return new _nameExpression( (NameExpr)e );
        }
        throw new RuntimeException("Unsupported "+ e.getClass()+" of "+ e);
    }
}

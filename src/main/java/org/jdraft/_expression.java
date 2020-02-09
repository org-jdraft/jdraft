package org.jdraft;

import com.github.javaparser.ast.expr.*;

public interface _expression<E extends Expression, _E extends _expression> extends _java._astNode<E, _E> {

    E ast();

    // computation
    //   methodCall, binaryExpression, unary, ternary, enclosedExpression, switchExpr

    // declaration
    //   variable, localClass, arrayInitialize, lambda, new

    // reference
    //   anno, arrayAccess, classExpression, fieldAccess, methodReference, nameExpression, super, typeExpression, cast
    //

    // literal
    //  String, double, int, char, null, long
    Class<_anno> ANNO = _anno.class;
    Class<_arrayAccess> ARRAY_ACCESS = _arrayAccess.class;
    Class<_assign> ASSIGN = _assign.class;
    Class<_arrayInitialize> ARRAY_INITIALIZE = _arrayInitialize.class;
    Class<_binaryExpression> BINARY_EXPRESSION = _binaryExpression.class;
    Class<_boolean> BOOLEAN = _boolean.class;
    Class<_cast> CAST = _cast.class;
    Class<_char> CHAR = _char.class;
    Class<_classExpression> CLASS_EXPRESSION = _classExpression.class;
    Class<_enclosedExpression> ENCLOSED_EXPRESSION = _enclosedExpression.class;
    Class<_fieldAccess> FIELD_ACCESS = _fieldAccess.class;
    Class<_int> INT = _int.class;
    Class<_long> LONG = _long.class;
    Class<_lambda> LAMBDA = _lambda.class;
    Class<_methodCall> METHOD_CALL = _methodCall.class;
    Class<_methodReference> METHOD_REFERENCE = _methodReference.class;
    Class<_nameExpression> NAME_EXPRESSION = _nameExpression.class;
    Class<_new> NEW = _new.class;
    Class<_null> NULL = _null.class;
    Class<_super> SUPER = _super.class;
    Class<_string> STRING = _string.class;
    Class<_switchExpression> SWITCH_EXPRESSION = _switchExpression.class;
    Class<_typeExpression> TYPE_EXPRESSION = _typeExpression.class;
    Class<_ternary> TERNARY = _ternary.class;
    Class<_unary> UNARY = _unary.class;
    Class<_variable> VARIABLE = _variable.class;

    Class<? super _expression>[] ALL = new Class[]{ ANNO, ARRAY_ACCESS, ASSIGN, ARRAY_INITIALIZE,
        BINARY_EXPRESSION, BOOLEAN, CAST, CHAR, CLASS_EXPRESSION, ENCLOSED_EXPRESSION, FIELD_ACCESS,
        INT, LONG, LAMBDA, METHOD_CALL, METHOD_REFERENCE, NAME_EXPRESSION, NEW, NULL, SUPER, STRING,
        SWITCH_EXPRESSION, TYPE_EXPRESSION, TERNARY, UNARY, VARIABLE};

    static _expression of(String...code){
        return of( Ex.of(code));
    }

    /**
     * Given an AST expression, return the _expression equivalent
     * @param e
     * @return
     */
    static _expression of( Expression e){

        if( e instanceof LiteralExpr ){
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
        }
        if( e instanceof AnnotationExpr ){
            return _anno.of( (AnnotationExpr)e);
        }
        if( e instanceof ArrayCreationExpr){
            return new _arrayCreate( (ArrayCreationExpr)e);
        }
        if( e instanceof ArrayAccessExpr){
            return new _arrayAccess( (ArrayAccessExpr)e);
        }
        if( e instanceof ArrayInitializerExpr){
            return new _arrayInitialize( (ArrayInitializerExpr)e);
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
        if( e instanceof LambdaExpr){
            return new _lambda((LambdaExpr)e);
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
        if( e instanceof NameExpr){
            return new _nameExpression( (NameExpr)e );
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
        if( e instanceof SwitchExpr){
            return new _switchExpression( (SwitchExpr)e);
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
        throw new _jdraftException("Unsupported Expression Class "+ e.getClass()+" of "+ e);
    }

    /**
     * Marker interface for Literal expressions:
     * @see _int //hex, binary, octal
     * @see _long //hex, binary, octal, postfix (L, l)
     * @see _string
     * @see _double //float postfix double postfix
     * @see _char
     * @see _boolean
     * @see _null
     */
    interface _literal<NE extends Expression, _NE extends _expression & _java._simple>  extends _expression<NE, _NE>,
            _java._simple<NE, _NE> {
        //get me the string representation of the literal value
        String valueAsString();
    }
}

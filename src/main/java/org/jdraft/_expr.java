package org.jdraft;

import com.github.javaparser.ast.expr.*;

import java.util.Objects;

public interface _expr<E extends Expression, _E extends _expr>
        extends _java._node<E, _E>, _java._withComments<E, _E> {

    default boolean is(String... stringRep) {
        try{
            Expression e = Exprs.of(stringRep);
            return Objects.equals( e, ast());
        } catch(Exception e){ }
        return false;
    }

    default boolean is(Expression e){
        return Objects.equals( e, ast());
    }

    default boolean isLiteral(){
        return this instanceof _literal;
    }

    E ast();

    // computation
    //   methodCall, binaryExpression, unary, conditionalExpression, enclosedExpression, switchExpr

    // declaration
    //   variable, localClass, arrayInitialize, lambda, new

    // reference
    //   anno, arrayAccess, classExpression, fieldAccess, methodReference, nameExpression, super, typeExpression, cast
    //
    public static class Classes {
        Class<_annoExpr> ANNO = _annoExpr.class;
        Class<_arrayAccessExpr> ARRAY_ACCESS = _arrayAccessExpr.class;
        Class<_assignExpr> ASSIGN = _assignExpr.class;
        Class<_arrayInitExpr> ARRAY_INITIALIZE = _arrayInitExpr.class;
        Class<_binaryExpr> BINARY_EXPRESSION = _binaryExpr.class;
        Class<_booleanExpr> BOOLEAN = _booleanExpr.class;
        Class<_castExpr> CAST = _castExpr.class;
        Class<_charExpr> CHAR = _charExpr.class;
        Class<_classExpr> CLASS_EXPRESSION = _classExpr.class;
        Class<_ternaryExpr> CONDITIONAL_EXPRESSION = _ternaryExpr.class;
        Class<_parenthesizedExpr> ENCLOSED_EXPRESSION = _parenthesizedExpr.class;
        Class<_fieldAccessExpr> FIELD_ACCESS = _fieldAccessExpr.class;
        Class<_intExpr> INT = _intExpr.class;
        Class<_longExpr> LONG = _longExpr.class;
        Class<_lambdaExpr> LAMBDA = _lambdaExpr.class;
        Class<_methodCallExpr> METHOD_CALL = _methodCallExpr.class;
        Class<_methodRefExpr> METHOD_REFERENCE = _methodRefExpr.class;
        Class<_nameExpr> NAME_EXPRESSION = _nameExpr.class;
        Class<_newExpr> NEW = _newExpr.class;
        Class<_nullExpr> NULL = _nullExpr.class;
        Class<_superExpr> SUPER = _superExpr.class;
        Class<_stringExpr> STRING = _stringExpr.class;
        Class<_switchExpr> SWITCH_EXPRESSION = _switchExpr.class;
        Class<_typeExpr> TYPE_EXPRESSION = _typeExpr.class;
        Class<_unaryExpr> UNARY = _unaryExpr.class;
        Class<_variablesExpr> VARIABLE = _variablesExpr.class;

        Class<? super _expr>[] ALL = new Class[]{ANNO, ARRAY_ACCESS, ASSIGN, ARRAY_INITIALIZE,
                BINARY_EXPRESSION, BOOLEAN, CAST, CHAR, CLASS_EXPRESSION, CONDITIONAL_EXPRESSION, ENCLOSED_EXPRESSION,
                FIELD_ACCESS, INT, LONG, LAMBDA, METHOD_CALL, METHOD_REFERENCE, NAME_EXPRESSION, NEW, NULL, SUPER, STRING,
                SWITCH_EXPRESSION, TYPE_EXPRESSION, UNARY, VARIABLE};

    }

    static _expr of(String...code){
        return of( Exprs.of(code));
    }
    static _intExpr of(int i){
        return _intExpr.of(i);
    }

    static _doubleExpr of(float f){
        return _doubleExpr.of(f);
    }

    static _longExpr of(long l){
        return _longExpr.of(l);
    }

    static _charExpr of(char c){
        return _charExpr.of(c);
    }

    static _doubleExpr of(double d){
        return _doubleExpr.of(d);
    }

    static _booleanExpr of(boolean b){
        return _booleanExpr.of(b);
    }

    /**
     * Given an AST expression, return the _expression equivalent
     * @param e
     * @return
     */
    static <E extends Expression> _expr of(E e){

        if( e instanceof LiteralExpr ){
            if( e instanceof IntegerLiteralExpr ){
                return new _intExpr( (IntegerLiteralExpr)e );
            }
            if( e instanceof DoubleLiteralExpr){
                return new _doubleExpr( (DoubleLiteralExpr)e );
            }
            if( e instanceof CharLiteralExpr){
                return new _charExpr( (CharLiteralExpr)e );
            }
            if( e instanceof BooleanLiteralExpr){
                return new _booleanExpr( (BooleanLiteralExpr)e );
            }
            if( e instanceof LongLiteralExpr){
                return new _longExpr( (LongLiteralExpr)e );
            }
            if( e instanceof StringLiteralExpr){
                return new _stringExpr( (StringLiteralExpr)e );
            }
            if( e instanceof TextBlockLiteralExpr){
                return new _textBlockExpr( (TextBlockLiteralExpr)e );
            }
        }
        if( e instanceof AnnotationExpr ){
            return _annoExpr.of( (AnnotationExpr)e);
        }
        if( e instanceof ArrayCreationExpr){
            return new _arrayCreateExpr( (ArrayCreationExpr)e);
        }
        if( e instanceof ArrayAccessExpr){
            return new _arrayAccessExpr( (ArrayAccessExpr)e);
        }
        if( e instanceof ArrayInitializerExpr){
            return new _arrayInitExpr( (ArrayInitializerExpr)e);
        }
        if( e instanceof AssignExpr){
            return new _assignExpr((AssignExpr)e);
        }
        if( e instanceof BinaryExpr){
            return new _binaryExpr((BinaryExpr)e);
        }
        if( e instanceof CastExpr){
            return new _castExpr((CastExpr)e);
        }
        if( e instanceof ClassExpr){
            return new _classExpr((ClassExpr)e);
        }
        if( e instanceof EnclosedExpr){
            return new _parenthesizedExpr((EnclosedExpr)e);
        }
        if( e instanceof FieldAccessExpr){
            return new _fieldAccessExpr((FieldAccessExpr)e);
        }
        if( e instanceof InstanceOfExpr){
            return new _instanceOfExpr((InstanceOfExpr)e);
        }
        if( e instanceof LambdaExpr){
            return new _lambdaExpr((LambdaExpr)e);
        }
        if( e instanceof MethodCallExpr){
            return new _methodCallExpr((MethodCallExpr)e);
        }
        if( e instanceof MethodReferenceExpr){
            return new _methodRefExpr((MethodReferenceExpr)e);
        }
        if( e instanceof ObjectCreationExpr){
            return new _newExpr((ObjectCreationExpr)e);
        }
        if( e instanceof NameExpr){
            return new _nameExpr( (NameExpr)e );
        }
        if( e instanceof NullLiteralExpr){
            return new _nullExpr((NullLiteralExpr)e);
        }
        if( e instanceof SuperExpr){
            return new _superExpr((SuperExpr)e);
        }
        if( e instanceof ConditionalExpr){
            return new _ternaryExpr((ConditionalExpr)e);
        }
        if( e instanceof SwitchExpr){
            return new _switchExpr( (SwitchExpr)e);
        }
        if( e instanceof ThisExpr){
            return new _thisExpr( (ThisExpr)e);
        }
        if( e instanceof TypeExpr){
            return new _typeExpr( (TypeExpr)e);
        }
        if( e instanceof UnaryExpr){
            return new _unaryExpr( (UnaryExpr)e);
        }
        if( e instanceof VariableDeclarationExpr){
            return new _variablesExpr( (VariableDeclarationExpr)e);
        }
        throw new _jdraftException("Unsupported Expression Class "+ e.getClass()+" of "+ e);
    }

    /**
     * Marker interface for Literal expressions:
     * @see _intExpr //hex, binary, octal
     * @see _longExpr //hex, binary, octal, postfix (L, l)
     * @see _stringExpr
     * @see _doubleExpr //float postfix double postfix
     * @see _charExpr
     * @see _booleanExpr
     * @see _nullExpr
     * @see _textBlockExpr
     */
    interface _literal<NE extends Expression, _NE extends _expr & _java._node>  extends _expr<NE, _NE>,
            _java._node<NE, _NE> {

        //get me the string representation of the literal value
        String valueAsString();
    }
}

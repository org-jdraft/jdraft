package org.jdraft;

import com.github.javaparser.ast.expr.*;
import org.jdraft.text.Stencil;

import java.util.Objects;

public interface _expr<E extends Expression, _E extends _expr>
        extends _java._node<E, _E>, _java._withComments<E, _E> {

    default boolean is(String... stringRep) {
        try{
            Expression e = Expr.of(stringRep);
            Stencil st = Stencil.of(e.toString(Print.PRINT_NO_COMMENTS));
            if(st.isFixedText() ){
                return equals(_expr.of(e));
            } else {
                return st.matches(ast().toString(Print.PRINT_NO_COMMENTS));
            }
            //return Objects.equals( e, ast());
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
    class Classes {
        public static Class<_annoExpr> ANNO_EXPR = _annoExpr.class;
        public static Class<_arrayAccessExpr> ARRAY_ACCESS_EXPR = _arrayAccessExpr.class;
        public static Class<_assignExpr> ASSIGN_EXPR = _assignExpr.class;
        public static Class<_arrayInitExpr> ARRAY_INIT_EXPR = _arrayInitExpr.class;
        public static Class<_binaryExpr> BINARY_EXPR = _binaryExpr.class;
        public static Class<_booleanExpr> BOOLEAN_EXPR = _booleanExpr.class;
        public static Class<_castExpr> CAST_EXPR = _castExpr.class;
        public static Class<_charExpr> CHAR_EXPR = _charExpr.class;
        public static Class<_classExpr> CLASS_EXPR = _classExpr.class;
        public static Class<_ternaryExpr> CONDITIONAL_EXPR = _ternaryExpr.class;
        public static Class<_parenthesizedExpr> PARENTHESIZED_EXPR = _parenthesizedExpr.class;
        public static Class<_fieldAccessExpr> FIELD_ACCESS_EXPR = _fieldAccessExpr.class;
        public static Class<_intExpr> INT_EXPR = _intExpr.class;
        public static Class<_longExpr> LONG_EXPR = _longExpr.class;
        public static Class<_lambdaExpr> LAMBDA_EXPR = _lambdaExpr.class;
        public static Class<_methodCallExpr> METHOD_CALL_EXPR = _methodCallExpr.class;
        public static Class<_methodRefExpr> METHOD_REF_EXPR = _methodRefExpr.class;
        public static Class<_nameExpr> NAME_EXPR = _nameExpr.class;
        public static Class<_newExpr> NEW_EXPR = _newExpr.class;
        public static Class<_nullExpr> NULL_EXPR = _nullExpr.class;
        public static Class<_superExpr> SUPER_EXPR = _superExpr.class;
        public static Class<_stringExpr> STRING_EXPR = _stringExpr.class;
        public static Class<_switchExpr> SWITCH_EXPR = _switchExpr.class;
        public static Class<_textBlockExpr> TEXT_BLOCK_EXPR = _textBlockExpr.class;
        public static Class<_typeExpr> TYPE_EXPR = _typeExpr.class;
        public static Class<_unaryExpr> UNARY_EXPR = _unaryExpr.class;
        public static Class<_variablesExpr> VARIABLES_EXPR = _variablesExpr.class;

        public static Class<? extends _expr>[] ALL = new Class[]{ANNO_EXPR, ARRAY_ACCESS_EXPR, ASSIGN_EXPR, ARRAY_INIT_EXPR,
                BINARY_EXPR, BOOLEAN_EXPR, CAST_EXPR, CHAR_EXPR, CLASS_EXPR, CONDITIONAL_EXPR, PARENTHESIZED_EXPR,
                FIELD_ACCESS_EXPR, INT_EXPR, LONG_EXPR, LAMBDA_EXPR, METHOD_CALL_EXPR, METHOD_REF_EXPR, NAME_EXPR, NEW_EXPR, NULL_EXPR, SUPER_EXPR, STRING_EXPR,
                SWITCH_EXPR, TEXT_BLOCK_EXPR, TYPE_EXPR, UNARY_EXPR, VARIABLES_EXPR};
    }

    static _expr of(String...code){
        return of( Expr.of(code));
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
            return new _newArrayExpr( (ArrayCreationExpr)e);
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

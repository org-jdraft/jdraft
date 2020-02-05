package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;

import java.util.*;
import java.util.function.*;

import org.jdraft.*;
import org.jdraft.text.*;

/**
 * $pattern for any {@link com.github.javaparser.ast.expr.Expression}) implementation
 *
 * NOTE: in the future I might make more or ALL of the expression implementations separate implementations
 * <PRE>
 * (i.e.
 * $arrayAccessEx, $arrayCreationEx, $binaryEx)
 * rather than:
 * $ex<ArrayAccessExpr, _arrayAccess>, $ex<ArrayCreationExpr, _arrayCreation>, $ex<BinaryExpr, _binaryExpression>
 * </PRE>
 * @param <E> Ast Syntax {@link Expression} Type (could be {@link Expression} to mean all expressions)
 * @param <_E> _java._domain {@link _expression} Type (could be {@link _expression} to mean all expressions)
 */
public class $ex<E extends Expression, _E extends _expression>
    implements $field.$part, $pattern<_E, $ex<E, _E>>, $var.$part, $enumConstant.$part, $annotationEntry.$part, Template<E>,
    $body.$part, $method.$part, $constructor.$part {

    /**
     * 
     * @param <T>
     * @param pattern
     * @return 
     */
    public static <T extends Expression, _E extends _expression> $ex<T, _E> of(String...pattern ){
        //so.... if I JUST do a pattern, I want to se the expression class
        // to Expression.class
        Expression expr = Ex.of(pattern);
        return ($ex<T, _E>)new $ex<Expression, _expression>( Expression.class,
                expr.toString(Ast.PRINT_NO_COMMENTS) );
    }

    /**
     * 
     * @param <T>
     * @param pattern
     * @param constraint
     * @return 
     */
    public static <T extends Expression, _E extends _expression> $ex<T, _E> of(String pattern, Predicate<_E> constraint ){
        return new $ex<T, _E>( (T) Ex.of(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param <T>
     * @param protoExpr
     * @return 
     */
    public static <T extends Expression, _E extends _expression> $ex<T, _E> of(T protoExpr ){
        return new $ex<>(protoExpr );
    }

    /**
     * 
     * @param <T>
     * @param protoExpr
     * @param constraint
     * @return 
     */
    public static <T extends Expression, _E extends _expression> $ex<T, _E> of(T protoExpr, Predicate<_E> constraint ){
        return new $ex<T, _E>(protoExpr ).$and(constraint);
    }

    public static $ex.Or or( Expression... _protos ){
        $ex[] arr = new $ex[_protos.length];
        for(int i=0;i<_protos.length;i++){
            arr[i] = $ex.of( _protos[i]);
        }
        return or(arr);
    }

    public static $ex.Or or( $ex...$tps ){
        return new $ex.Or($tps);
    }
    
    /**
     * i.e. "arr[3]"
     * @param pattern
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess> arrayAccessEx(String... pattern ) {
        return new $ex<>( Ex.arrayAccessEx(pattern) );
    }
    
    /**
     * i.e."arr[3]"
     * @param constraint
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess> arrayAccessEx(Predicate<_arrayAccess> constraint) {
        return new $ex<ArrayAccessExpr, _arrayAccess>( Ex.arrayAccessEx("a[0]") )
                .$(Ex.of("a[0]"), "any").$and(constraint);
    }    

    /**
     * i.e."arr[3]"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess> arrayAccessEx(String pattern, Predicate<_arrayAccess> constraint) {
        return new $ex<ArrayAccessExpr, _arrayAccess> ( Ex.arrayAccessEx(pattern) ).$and(constraint);
    }
    
    /**
     * ANY array Access
     * i.e."arr[3]"
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess> arrayAccessEx( ) {
        return new $ex<>( ArrayAccessExpr.class, "$arrayAccessExpr$");
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate> arrayCreationEx(String... pattern ) {
        return new $ex<>( Ex.arrayCreationEx(pattern ) );
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param constraint
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate> arrayCreationEx(Predicate<_arrayCreate> constraint ) {
        return new $ex<ArrayCreationExpr, _arrayCreate> ( Ex.arrayCreationEx("new int[]")).$(Ex.of("new int[]"), "any").$and(constraint);
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate> arrayCreationEx(String pattern, Predicate<_arrayCreate> constraint ) {
        return new $ex<ArrayCreationExpr, _arrayCreate> ( Ex.arrayCreationEx(pattern ) ).$and(constraint);
    }

    /**
     * i.e."new Obj[]", "new int[][]"
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate> arrayCreationEx( ) {
        return new $ex<>( ArrayCreationExpr.class, "$arrayCreationExpr$");
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(String... pattern ) {
        return new $ex<>( Ex.arrayInitializerEx(pattern ) );
    }
    
    /**
     * 
     * @param ints
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(int[] ints ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<ints.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(ints[i]);
        }
        sb.append("}");
        return arrayInitEx(sb.toString());
    }
    
    /**
     * 
     * @param bools
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(boolean[] bools ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<bools.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(bools[i]);
        }
        sb.append("}");
        return arrayInitEx(sb.toString());
    }
    
    /**
     * 
     * @param chars
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(char[] chars ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<chars.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append("'").append(chars[i]).append("'");
        }
        sb.append("}");
        return arrayInitEx(sb.toString());
    }
    
    /**
     * 
     * @param doubles
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(double[] doubles ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<doubles.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(doubles[i]).append("d");
        }
        sb.append("}");
        return arrayInitEx(sb.toString());
    }
    
    /**
     * 
     * @param floats
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(float[] floats ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<floats.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(floats[i]).append("f");
        }
        sb.append("}");
        return arrayInitEx(sb.toString());
    }

    /**
     * i.e."{1,2,3,4,5}"
     * @param constraint
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(Predicate<_arrayInitialize> constraint) {
        return new $ex<ArrayInitializerExpr, _arrayInitialize> ( Ex.arrayInitializerEx("{1}") ).$(Ex.of("{1}"), "any").$and(constraint);
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx(String pattern, Predicate<_arrayInitialize> constraint) {
        return new $ex<ArrayInitializerExpr, _arrayInitialize>( Ex.arrayInitializerEx(pattern ) ).$and(constraint);
    }
    
    /**
     * Any array initializer i.e. "{1,2,3,4,5}"
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize> arrayInitEx() {
        return new $ex( ArrayInitializerExpr.class, "$arrayInitializer$");
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @return 
     */
    public static $ex<AssignExpr, _assign> assignEx(String... pattern ) {
        return new $ex<>( Ex.assignEx(pattern ) );
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param constraint
     * @return 
     */
    public static $ex<AssignExpr, _assign> assignEx(Predicate<_assign> constraint) {
        return new $ex<AssignExpr, _assign>( Ex.assignEx("a=1") ).$(Ex.of("a=1"), "any").$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<AssignExpr, _assign> assignEx(String pattern, Predicate<_assign> constraint) {
        return new $ex<AssignExpr, _assign> ( Ex.assignEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @return 
     */
    public static $ex<AssignExpr,_assign> assignEx( ) {
        return new $ex<>( AssignExpr.class, "$assignExpr$");
    }

    /** 
     * a || b 
     * @param pattern
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression> binaryEx(String... pattern ) {
        return new $ex<>( Ex.binaryEx(pattern ) );
    }
    
    /**
     * a || b 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression> binaryEx(String pattern, Predicate<_binaryExpression> constraint) {
        return new $ex<BinaryExpr, _binaryExpression>( Ex.binaryEx(pattern ) ).$and(constraint);
    }    

    /**
     * a || b 
     * @param constraint
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression> binaryEx(Predicate<_binaryExpression> constraint) {
        return new $ex<BinaryExpr, _binaryExpression>( Ex.binaryEx("a || b" ) ).$(Ex.binaryEx("a || b"), "any").$and(constraint);
    }  
    
    /**
     * a || b    
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression> binaryEx( ) {
        return new $ex<>( BinaryExpr.class, "$binaryExpr$");
    } 
    
    /** 
     *  i.e.true
     * @param b
     * @return  
     */
    public static $ex<BooleanLiteralExpr, _boolean> of(boolean b ){
        return new $ex<>( Ex.of( b ) );
    }

    public static $ex<LiteralExpr, _expression._literal> literalEx() {
        return new $ex(LiteralExpr.class, "$expr$");
    }

    public static $ex<LiteralExpr, _expression._literal> literalEx(Predicate<_expression._literal> constraint) {
        return new $ex(LiteralExpr.class, "$expr$").$and(constraint);
    }

    /**
     * 
     * @param b
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean> booleanLiteralEx(boolean b ) {
        return new $ex( Ex.of( b ) );
    }
    
    /**
     * 
     * @param b
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean> booleanLiteralEx(boolean b, Predicate<_boolean> constraint) {
        return new $ex( Ex.of( b ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * 
     * @param pattern
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean> booleanLiteralEx(String... pattern ) {
        return new $ex( Ex.booleanLiteralEx(pattern ) );
    }

    /** 
     * "true" / "false" 
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean> booleanLiteralEx(Predicate<_boolean>constraint ) {
        return new $ex( Ex.booleanLiteralEx("true") ).$("true", "any").$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean> booleanLiteralEx(String pattern, Predicate<_boolean>constraint ) {
        return new $ex( Ex.booleanLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean> booleanLiteralEx( ) {
        return new $ex( BooleanLiteralExpr.class, "$booleanLiteral$");
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @return 
     */
    public static $ex<CastExpr, _cast> castEx(String... pattern ) {
        return new $ex( Ex.castEx(pattern ) );
    }

    /** 
     * (String)o 
     * @param constraint
     * @return 
     */
    public static $ex<CastExpr, _cast> castEx(Predicate<_cast> constraint ) {
        return new $ex( CastExpr.class, "($cast$)" ).$and(constraint); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any").constraint(constraint);
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<CastExpr, _cast> castEx(String pattern, Predicate<_cast> constraint ) {
        return new $ex( Ex.castEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * (String)o 
     * @return 
     */
    public static $ex<CastExpr, _cast> castEx( ) {
        return new $ex( CastExpr.class, "($cast$)"); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any");
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> of(char c ){
        return new $ex( Ex.charLiteralEx( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> of(char c, Predicate<_char> constraint){
        return new $ex( Ex.charLiteralEx( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> charLiteralEx(char c ) {
        return new $ex( Ex.charLiteralEx( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> charLiteralEx(char c, Predicate<_char> constraint) {
        return new $ex( Ex.charLiteralEx( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> charLiteralEx(String... pattern ) {
        return new $ex( Ex.charLiteralEx(pattern ) );
    }

    /** 
     * 'c' 
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> charLiteralEx(Predicate<_char> constraint) {
        return new $ex( Ex.charLiteralEx('a') ).$("'a'", "any").$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> charLiteralEx(String pattern, Predicate<_char> constraint) {
        return new $ex( Ex.charLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * ANY char literal
     * 'c' 
     * @return 
     */
    public static $ex<CharLiteralExpr, _char> charLiteralEx(  ) {
        return new $ex( CharLiteralExpr.class, "$charLiteral$");
    }
    
    /** 
     * i.e."String.class" 
     * @param pattern
     * @return 
     */
    public static $ex<ClassExpr, _classExpression> classEx(String... pattern ) {
        return new $ex( Ex.classEx(pattern ) );
    }

    /**
     * Map.class
     * @param constraint
     * @return 
     */
    public static $ex<ClassExpr, _classExpression> classEx(Predicate<_classExpression> constraint) {
        return new $ex( Ex.classEx("a.class") )
            .$("a.class", "any").$and(constraint);
    }
    
    /**
     * String.class
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ClassExpr, _classExpression> classEx(String pattern, Predicate<_classExpression> constraint) {
        return new $ex( Ex.classEx(pattern ) ).$and(constraint);
    }

    /**
     * Class expr (i.e. "String.class")
     * @return 
     */
    public static $ex<ClassExpr, _classExpression> classEx() {
        return new $ex( ClassExpr.class, "$classExpr$");
    }
    
    /** 
     * i.e."(a < b) ? a : b" 
     * @param pattern
     * @return 
     */
    public static $ex<ConditionalExpr, _ternary> conditionalEx(String... pattern ) {
        return new $ex( Ex.conditionalEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ConditionalExpr, _ternary> conditionalEx(String pattern, Predicate<_ternary> constraint) {
        return new $ex( Ex.conditionalEx(pattern ) ).$and(constraint);
    }
    
    /**
     * conditional, i.e. "(a==1) ? 1 : 2" 
     * @param constraint
     * @return 
     */
    public static $ex<ConditionalExpr, _ternary> conditionalEx(Predicate<_ternary> constraint) {
        return new $ex( Ex.conditionalEx("(a==1) ? 1 : 2" ) )
                .$(Ex.conditionalEx("(a==1) ? 1 : 2"), "any")
                .$and(constraint);
    }    
    
    /**
     * Any conditional i.e. "(a==1) ? 1 : 2" 
     * @return 
     */
    public static $ex<ConditionalExpr, _ternary> conditionalEx() {
        return new $ex( ConditionalExpr.class, "$conditionalExpr$");
    }    
    
    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> of(double d ){
        return new $ex( Ex.of( d ) );
    }

    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(double d ) {
        return new $ex( Ex.doubleLiteralEx( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(double d, Predicate<_double> constraint) {
        return new $ex( Ex.doubleLiteralEx( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(String pattern ) {
        return new $ex( Ex.doubleLiteralEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(String pattern, Predicate<_double> constraint) {
        return new $ex( Ex.doubleLiteralEx(pattern ) ).$and(constraint);
    }
        
    /**
     * 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> of(float d ){
        return new $ex( Ex.doubleLiteralEx( d ) );
    }

    /**
     * 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(float d ) {
        return new $ex( Ex.of( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(float d, Predicate<_double> constraint) {
        return new $ex( Ex.of( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx(Predicate<_double> constraint) {
        return new $ex( Ex.of( 1.0d ) ).$("1.0d", "any").$and(constraint);
    }
        
    /**
     * 10.1d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> doubleLiteralEx( ) {
        return new $ex( DoubleLiteralExpr.class, "$doubleLiteral$");
    }
    
    /** 
     * i.e."3.14f" 
     * @param pattern
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> floatLiteralEx(String... pattern ) {
        return new $ex( Ex.floatLiteralEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> floatLiteralEx(String pattern, Predicate<_double> constraint ) {
        return new $ex( Ex.floatLiteralEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> floatLiteralEx(Predicate<_double> constraint ) {
        return new $ex( Ex.of(1.0f) ).$(Ex.of(1.0f), "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double> floatLiteralEx( ) {
        return new $ex( DoubleLiteralExpr.class, "$floatLiteral$");
    }
    
    /** 
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression> enclosedEx(String... pattern ) {
        return new $ex( Ex.enclosedEx(pattern ) );
    }

    /**
     * i.e.( 3 + 4 ) 
     * @param constraint
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression> enclosedEx(Predicate<_enclosedExpression>constraint ) {
        return new $ex( Ex.enclosedEx("(a)" ) ).$("(a)", "any").$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression> enclosedEx(String pattern, Predicate<_enclosedExpression>constraint ) {
        return new $ex( Ex.enclosedEx(pattern ) ).$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 )    
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression> enclosedEx( ) {
        return new $ex( EnclosedExpr.class, "$enclosedExpr$");
    }
    
    /** 
     *  i.e."person.NAME"
     * @param pattern
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess> fieldAccessEx(String... pattern ) {
        return new $ex( Ex.fieldAccessEx(pattern ) );
    }

    /**
     * i.e. "System.out"
     * @param constraint
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess> fieldAccessEx(Predicate<_fieldAccess> constraint ) {
        return new $ex( Ex.fieldAccessEx("a.B") )
                .$(Ex.fieldAccessEx("a.B"), "any").$and(constraint);
    }
    
    /**
     * i.e. "System.out"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess> fieldAccessEx(String pattern, Predicate<_fieldAccess> constraint ) {
        return new $ex( Ex.fieldAccessEx(pattern ) ).$and(constraint);
    }
    
    /**
     * i.e. "System.out"
     * 
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess> fieldAccessEx( ) {
        return new $ex( FieldAccessExpr.class, "$fieldAccessExpr$");
    }
    
    /** 
     * v instanceof Serializable 
     * @param pattern
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf> instanceOfEx(String... pattern ) {
        return new $ex( Ex.instanceOfEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf> instanceOfEx(Predicate<_instanceOf> constraint ) {
        return new $ex( Ex.instanceOfEx( "a instanceof b" ) ).$("a instanceof b", "any")
                .$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf> instanceOfEx(String pattern, Predicate<_instanceOf> constraint ) {
        return new $ex( Ex.instanceOfEx(pattern ) ).$and(constraint);
    }

    public static $ex<InstanceOfExpr, _instanceOf> instanceOfEx($typeRef type ){
        return instanceOfEx("$expr$ instanceof $type$", e-> type.matches( e.getType() ) );
    }

    /**
     * 
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf> instanceOfEx() {
        return new $ex( InstanceOfExpr.class, "$instanceOf$" );
    }

    public static $ex<InstanceOfExpr, _instanceOf> instanceOfEx(Class typeClass ){
        return instanceOfEx( $typeRef.of(typeClass) );
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int> of(int i) {
        return new $ex( Ex.intLiteralEx(i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int> of(int i, Predicate<_int> constraint) {
        return new $ex( Ex.intLiteralEx( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int> intLiteralEx(Predicate<_int> constraint) {
        return new $ex( Ex.intLiteralEx( 1 ) ).$("1", "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int> intLiteralEx( ) {
        return new $ex( IntegerLiteralExpr.class, "$intLiteralExpr$");
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $ex<IntegerLiteralExpr,_int> intLiteralEx(int i) {
        return new $ex( Ex.intLiteralEx( i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr,_int> intLiteralEx(int i, Predicate<_int> constraint) {
        return new $ex( Ex.intLiteralEx( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int> intLiteralEx(String... pattern ) {
        return new $ex( IntegerLiteralExpr.class, Text.combine(pattern) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr,_int> intLiteralEx(String pattern, Predicate<_int> constraint ) {
        return new $ex( Ex.intLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @param pattern
     * @return 
     */
    public static $ex<LambdaExpr, _lambda> lambdaEx(String... pattern ) {
        return new $ex( Ex.lambdaEx(pattern ) );
    }

    /** 
     * a-> System.out.println( a )  
     * @param constraint
     * @return 
     */
    public static $ex<LambdaExpr, _lambda> lambdaEx(Predicate<_lambda> constraint) {
        return new $ex( Ex.lambdaEx("a-> true" ) ).$(Ex.lambdaEx("a->true"), "any").$and(constraint);
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<LambdaExpr, _lambda> lambdaEx(String pattern , Predicate<_lambda> constraint) {
        return new $ex( Ex.lambdaEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @return 
     */
    public static $ex<LambdaExpr, _lambda> lambdaEx( ) {
        return new $ex( LambdaExpr.class, "$lambdaExpr$" );
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> of(long l) {
        return new $ex( Ex.longLiteralEx( l ) );
    }

    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> of(long l, Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx( l ) ).$and(constraint);
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> longLiteralEx(long l ) {
        return new $ex( Ex.longLiteralEx( l ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> longLiteralEx(Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx(1L)).$(Ex.longLiteralEx(1L), "any")
                .$and(constraint) ;
    }
    
    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> longLiteralEx(long l, Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx( l ) ).$and(constraint);
    }
   
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> longLiteralEx(String... pattern ) {
        return new $ex( Ex.longLiteralEx(pattern ) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> longLiteralEx(String pattern, Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx(pattern ) ).$and(constraint);
    }

    /**
     * Any long literal
     * @return 
     */
    public static $ex<LongLiteralExpr, _long> longLiteralEx( ) {
        return new $ex( LongLiteralExpr.class, "$longLiteralExpr$");
    }
    
    /** 
     * doMethod(t)
     * @param pattern 
     * @return  
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(String... pattern ) {
        return new $ex( Ex.methodCallEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(String pattern, Predicate<_methodCall> constraint ) {
        return new $ex( Ex.methodCallEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(Predicate<_methodCall> constraint ) {
        return new $ex( Ex.methodCallEx("a()" )).$(Ex.of("a()"), "any").$and(constraint);
    }
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static MethodCallExpr methodCallEx(Ex.Command lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return astLambda.getBody().findFirst(MethodCallExpr.class).get();
    }

    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(Consumer<? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }    
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }  
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(Ex.TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }
    
    /**
     * Builds & returns a MethodCallExpr based on the FIRST method call inside
     * the body of the lambda passed in
     * 
     * (SOURCE PASSING)
     * 
     * @param lambdaWithMethodCallInSource a lambda expression containing the 
     * source with a METHOD CALL that will be converted into a MethodCallExpr
     * Ast node
     * @return the MethodCallExpr Ast Node representing the first method call
     * in the lambda body
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx(Ex.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }
    
    /**
     * ANY method call
     * @return 
     */
    public static $ex<MethodCallExpr, _methodCall> methodCallEx( ) {
        return new $ex( MethodCallExpr.class, "$methodCall$");
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @return 
     */
    public static $ex<MethodReferenceExpr, _methodReference> methodReferenceEx(String... pattern ) {
        return new $ex( Ex.methodReferenceEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<MethodReferenceExpr, _methodReference> methodReferenceEx(Predicate<_methodReference> constraint) {
        return new $ex( Ex.methodReferenceEx("A:b")).$("A:b", "any").$and(constraint);
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<MethodReferenceExpr,_methodReference> methodReferenceEx(String pattern, Predicate<_methodReference>constraint ) {
        return new $ex( Ex.methodReferenceEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<MethodReferenceExpr, _methodReference> methodReferenceEx() {
        return new $ex( MethodReferenceExpr.class, "$methodReference$");
    }
    
    /** 
     *  i.e."null"
     * @return  
     */
    public static $ex<NullLiteralExpr, _null> nullEx(){
        return new $ex( NullLiteralExpr.class, "$nullExpr$" );
    }

    /**
     *
     * @param nle
     * @return
     */
    public static $ex<NullLiteralExpr, _null> nullEx(Predicate<_null> nle){
        return new $ex( NullLiteralExpr.class, "$nullExpr$" ).$and(nle);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<NameExpr, _nameExpression> nameEx(String... pattern ) {
        return new $ex( Ex.nameEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<NameExpr, _nameExpression> nameEx(Predicate<_nameExpression>constraint) {
        return new $ex( Ex.nameEx("name" ) ).$("name", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<NameExpr, _nameExpression> nameEx(String pattern, Predicate<_nameExpression>constraint) {
        return new $ex( Ex.nameEx(pattern ) ).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $ex<NameExpr, _nameExpression> nameEx() {
        return new $ex( NameExpr.class, "$nameExpr$");
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new> objectCreationEx(String... pattern ) {
        return new $ex( Ex.objectCreationEx(pattern ) );
    }
    
    /** 
     * "new Date()"
     * @param constraint
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new> objectCreationEx(Predicate<_new>constraint ) {
        return new $ex( Ex.objectCreationEx( "new a()" ) ).$("new a()", "any").$and(constraint);
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new> objectCreationEx(String pattern, Predicate<_new>constraint ) {
        return new $ex( Ex.objectCreationEx(pattern ) ).$and(constraint);
    }

    /** 
     * "new Date()"
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new> objectCreationEx() {
        return new $ex( ObjectCreationExpr.class, "$objectCreationExpr$");
    }

    /**
     *
     * @param literal
     * @return
     */
    public static $ex<StringLiteralExpr, _string> stringLiteralEx(String literal ) {
        return new $ex( Ex.stringLiteralEx(literal) );
    }
    
    /** 
     * "literal"
     * @param pattern
     * @return 
     */
    public static $ex<StringLiteralExpr, _string> stringLiteralEx(String... pattern ) {
        return new $ex( Ex.stringLiteralEx(pattern ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<StringLiteralExpr, _string> stringLiteralEx(Predicate<_string> constraint) {
        return new $ex( Ex.stringLiteralEx( "\"a\"" ) ).$("\"a\"", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<StringLiteralExpr, _string> stringLiteralEx(String pattern, Predicate<_string> constraint) {
        return new $ex( Ex.stringLiteralEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<StringLiteralExpr, _string> stringLiteralEx( ) {
        return new $ex( StringLiteralExpr.class, "$stringLiteral$");
    }
    
    /**
     * ANY super expression 
     * 
     * @return 
     */
    public static $ex<SuperExpr,_super> superEx(){
        return new $ex(SuperExpr.class, "$superExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<SuperExpr, _super> superEx(String...pattern ){
        return new $ex(Ex.superEx(pattern));
    }
    
    /**
     * 
     * @param se
     * @return 
     */
    public static $ex<SuperExpr, _super> superEx(Predicate<_super> se){
        return superEx().$and(se);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<SuperExpr, _super> superEx(String pattern, Predicate<_super> constraint ){
        return new $ex(Ex.superEx(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param protoSuperExpr
     * @return 
     */
    public static $ex<SuperExpr, _super> superEx(SuperExpr protoSuperExpr){
        return new $ex(protoSuperExpr);
    }
    
    /**
     * 
     * @param superExpr
     * @param constraint
     * @return 
     */
    public static $ex<SuperExpr, _super> superEx(SuperExpr superExpr, Predicate<_super> constraint ){
        return new $ex(superExpr).$and(constraint);
    }

    /**
     * ANY this expression
     * @return 
     */
    public static $ex<ThisExpr, _this> thisEx( ){
        return new $ex(ThisExpr.class, "$thisExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<ThisExpr, _this> thisEx(String... pattern){
        return new $ex(Ex.thisEx(pattern) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr, _this> thisEx(String pattern, Predicate<_this> constraint){
        return new $ex(Ex.thisEx(pattern) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr, _this> thisEx(Predicate<_this> constraint){
        return new $ex(Ex.thisEx() ).$and(constraint);
    }
        
    /**
     * 
     * @param protoThisExpr
     * @return 
     */
    public static $ex<ThisExpr, _this> thisEx(ThisExpr protoThisExpr){
        return new $ex(protoThisExpr);
    }
    
    /**
     * 
     * @param protoThisExpr
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr, _this> thisEx(ThisExpr protoThisExpr, Predicate<_this> constraint){
        return new $ex(protoThisExpr ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression> typeEx(String... pattern ) {
        return new $ex( Ex.typeEx(pattern ) );
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param constraint
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression> typeEx(Predicate<_typeExpression> constraint ) {
        return new $ex( Ex.typeEx( "a" ) ).$("a", "any").$and(constraint);
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression> typeEx(String pattern, Predicate<_typeExpression> constraint ) {
        return new $ex( Ex.typeEx(pattern ) ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression> typeEx( ) {
        return new $ex( TypeExpr.class, "$typeExpr$");
    }
    
    /** 
     * i.e."!true" 
     * @param pattern
     * @return 
     */
    public static $ex<UnaryExpr, _unary> unaryEx(String... pattern ) {
        return new $ex( Ex.unaryEx(pattern ) );
    }
   
    /** 
     *  i.e."!true"
     * @param constraint 
     * @return  
     */
    public static $ex<UnaryExpr, _unary> unaryEx(Predicate<_unary>constraint) {
        return new $ex( Ex.unaryEx( "!true" ) ).$("!true", "any").$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $ex<UnaryExpr, _unary> unaryEx(String pattern, Predicate<_unary>constraint) {
        return new $ex( Ex.unaryEx(pattern ) ).$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @return  
     */
    public static $ex<UnaryExpr, _unary> unaryEx() {
        return new $ex( UnaryExpr.class, "$unaryExpr$");
    }
    
    /** 
     * "int i = 1"
     * @param pattern
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _variable> varLocalEx(String... pattern ) {
        return new $ex( Ex.varLocalEx(pattern ) );
    }

    /** 
     * "int i = 1"
     * @param constraint 
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _variable> varLocalEx(Predicate<_variable> constraint) {
        return new $ex( Ex.varLocalEx( "int i=1") ).$(Ex.of("int i=1"), "any").$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _variable> varLocalEx(String pattern, Predicate<_variable> constraint) {
        return new $ex( Ex.varLocalEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _variable> varLocalEx( ) {
        return new $ex( VariableDeclarationExpr.class, "$varDecl$");
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $ex<E, _E> $isAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $ex<E, _E> $isNotAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $ex<E, _E> $isBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $ex<E, _E> $isNotBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $not(prev);
    }

    /**
     * Matches ANY expression
     * @return 
     */
    public static $ex<Expression, _expression> of(){
        return new $ex( Expression.class, "$expr$");
    }
    
    /**
     * Matches ANY expression that matches the constraint
     * @param constraint
     * @return 
     */
    public static $ex<Expression, _expression> of(Predicate<_expression> constraint ){
        return of().$and(constraint);
    }
    
    /** Class of the Expression */
    public Class<E> expressionClass;
    
    /** The pattern (including potential tokens within $_$) */
    public Stencil exprStencil;
    
    /**
     * Matching Constraint this proto
     * (By default, ALWAYS matches)
     */
    public Predicate<_E> constraint = (e)->true;

    /**
     * 
     * @param astExpressionProto 
     */
    public $ex(E astExpressionProto){
        this.expressionClass = (Class<E>)astExpressionProto.getClass();
        this.exprStencil = Stencil.of(astExpressionProto.toString() );
    }
   
    /**
     * 
     * @param expressionClass
     * @param stencil 
     */
    public $ex(Class<E>expressionClass, String stencil ){
        this.expressionClass = expressionClass;
        this.exprStencil = Stencil.of(stencil);
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified $expr prototype
     */
    @Override
    public $ex<E, _E> $and(Predicate<_E>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public E fill(Object...values){
        String str = exprStencil.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (E) Ex.of( str);
    }

    @Override
    public $ex<E, _E> $(String target, String $paramName) {
        this.exprStencil = this.exprStencil.$(target, $paramName);
        return this;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $ex<E, _E> $(Expression astExpr, String $name){
        this.exprStencil = this.exprStencil.$(astExpr.toString(), $name);
        return this;
    }

    /**
     * 
     * @param translator
     * @param tokens
     * @return 
     */
    @Override
    public $ex<E, _E> hardcode$(Translator translator, Tokens tokens) {
        this.exprStencil = this.exprStencil.hardcode$(translator, tokens);
        return this;
    }

    /**
     * 
     * @param _n
     * @return 
     */
    public E draft(_java._node _n ){
        return (E)draft(_n.tokenize());
    }

    @Override
    public E draft(Translator t, Map<String,Object> tokens ){
        return (E) Ex.of(exprStencil.draft( t, tokens ));
    }

    public boolean match( Node node ){
        if( node instanceof Expression ){
            return matches( (Expression)node);
        }
        return false;
    }

    /**
     * 
     * @param expression
     * @return 
     */
    public boolean matches( String...expression ){
        return select( expression) != null;
    }
    
    /**
     * 
     * @param astExpr
     * @return 
     */
    public boolean matches( Expression astExpr ){
        if( astExpr == null ){
            return this.isMatchAny();
        }
        return select(astExpr) != null;
    }

    /**
     * Does this $expr match ANY
     * @return 
     */
    public boolean isMatchAny(){
        try{
            return  //this.expressionClass == Expression.class 
                this.constraint.test(null) 
                && this.exprStencil.isMatchAny();
        }catch(Exception e){
            return false;
        }
    }
    @Override
    public List<String> list$(){
        return this.exprStencil.list$();
    }

    @Override
    public List<String> list$Normalized(){
        return this.exprStencil.list$Normalized();
    }

    /**
     * Compare (2) Strings that represent numbers to see if they represent the
     * SAME number...
     * <PRE>
     * NOTE: this is useful because sometimes we 
     * use BINARY notation to represent numbers :  "0b101001010"
     * use HEX notation to represent numbers :  "0xDEADBEEF"
     * 
     * use optional postfixes on numbers (L/l for long, D/d for double, F/f for float)
     * so we CAN'T JUST compare the string values.
     * 
     * </PRE>
     * 
     * NOTE: only public to allow for testing (used internally)
     * @param expected
     * @param actual
     * @return 
     */
    public static boolean compareNumberLiterals( String expected, String actual ){
        Number ex = Ex.parseNumber(expected);
        Number act = Ex.parseNumber(actual);
        if( ex.equals( act ) ){
            return true;
        }
        if( ex.getClass() == act.getClass() ){ //botht he same class
            return false;
        }
        if( ex instanceof Long || ex instanceof Integer ){
            Long exl = ex.longValue();
            Long actL = act.longValue();
            return exl.equals(actL);
        }
        return false;
    }


    /**
     *
     * @param _e
     * @return
     */
    @Override
    public Select select( _expression _e){
        return select(_e.ast());
    }

    /**
     * 
     * @param astExpr
     * @return 
     */
    public Select select( Expression astExpr){
        if( astExpr == null ){
            if(this.isMatchAny()){
                return new Select(astExpr, new Tokens());
            }
        }
        if( expressionClass.isAssignableFrom(astExpr.getClass()) 
                && constraint.test( (_E)_expression.of(astExpr) ) ){
            //slight modification..
            if( (astExpr instanceof IntegerLiteralExpr 
                || astExpr instanceof DoubleLiteralExpr 
                || astExpr instanceof LongLiteralExpr)  
                    && exprStencil.isFixedText()) {
                
                //there is an issue here the lowercase and uppercase Expressions 1.23d =/= 1.23D (which they are equivalent
                //need to handle postfixes 1.2f, 2.3d, 1000l
                //need to handle postfixes 1.2F, 2.3D, 1000L
                String st = astExpr.toString(Ast.PRINT_NO_COMMENTS);
                try{
                    if( compareNumberLiterals(exprStencil.getTextForm().getFixedText(), st) ){
                        return new Select(astExpr, new Tokens());
                    }
                }catch(Exception e){
                    //it might not be a number... so comparison will fail
                }
                return null;                
            }
            Tokens ts = exprStencil.parse(astExpr.toString(Ast.PRINT_NO_COMMENTS) );
            if( ts != null ){
                return new Select(astExpr, ts);
            }            
        }
        return null;        
    }

    public Select<E,_E> select(String...expr){
        try{
            return select(Ex.of(expr));
        }catch(Exception e){
            return null;
        }
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param exprMatchFn
     * @return  the first Expression that matches (or null if none found)
     */
    @Override
    public _E firstIn(Node astNode, Predicate<_E> exprMatchFn ){
        Optional<E> f = astNode.findFirst(this.expressionClass, s ->{
            Select sel = select(s);
            return sel != null && exprMatchFn.test( (_E)sel._ex );
        });
        if( f.isPresent()){
            return (_E)_expression.of(f.get());
        }
        return null;
    }

    /**
     *
     * @param astNode
     * @param exprMatchFn
     * @return

    public E firstIn(Node astNode, Predicate<E> exprMatchFn ){
        Optional<E> f = astNode.findFirst(this.expressionClass, s ->{
            Select sel = select(s);
            return sel != null && exprMatchFn.test( (E)sel.astExpression);
            });         
        if( f.isPresent()){
            return f.get();
        }
        return null;
    }
    */
    
    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public Select<E,_E> selectFirstIn(Class clazz ){
        return selectFirstIn( (_type)_java.type(clazz));
    }
    
    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param _j the _java model node
     * @return  the first Expression that matches (or null if none found)
     */
    @Override
    public Select<E,_E> selectFirstIn(_java._domain _j){
        if( _j instanceof _compilationUnit){
            _compilationUnit _c = (_compilationUnit) _j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return selectFirstIn(_t.ast());
        }
        return selectFirstIn( ((_java._node) _j).ast() );
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Expression that matches (or null if none found)
     */
    @Override
    public Select<E,_E> selectFirstIn(Node astNode ){
        Optional<E> f = astNode.findFirst(this.expressionClass, s -> this.matches(s) );
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public Select<E, _E> selectFirstIn(Class clazz, Predicate<Select<E,_E>> selectConstraint ){
        return selectFirstIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    public Select<E, _E> selectFirstIn(_java._domain _j, Predicate<Select<E, _E>> selectConstraint){
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return selectFirstIn(((_compilationUnit) _j).astCompilationUnit(), selectConstraint);
            } else{
                return selectFirstIn(((_type) _j).ast(), selectConstraint);
            }
        }
        return selectFirstIn(((_java._node) _j).ast(), selectConstraint);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    public Select<E, _E> selectFirstIn(Node astNode, Predicate<Select<E, _E>> selectConstraint){
        Optional<E> f = astNode.findFirst(this.expressionClass, s -> {
            Select<E, _E> sel = select(s);
            return sel != null && selectConstraint.test(sel);
        });          
        if( f.isPresent()){
            return select(_expression.of(f.get()));
        }
        return null;
    }

    @Override
    public List<_E> listIn(_java._domain _j){
        if( _j instanceof _compilationUnit){
            if( ((_compilationUnit) _j).isTopLevel()){
                return listIn(((_compilationUnit) _j).astCompilationUnit());
            }
                return listIn(((_type) _j).ast());
        }
        return listIn( ((_java._node) _j).ast() );
    }    

    @Override
    public List<_E> listIn(Node astNode ){
        List<_E> typesList = new ArrayList<>();
        astNode.walk(this.expressionClass, e ->{
            if( this.matches(e) ){
                typesList.add((_E)_expression.of(e));
            }
        } );
        return typesList;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Consumer<_E> expressionActionFn){
        return forEachIn(astNode, e ->true, expressionActionFn);
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<_E>exprMatchFn, Consumer<_E> expressionActionFn){
        astNode.walk(this.expressionClass, e-> {
            //$args tokens = deconstruct( e );
            Select sel = select(e);
            if( sel != null && exprMatchFn.test((_E)sel._ex)) {
                expressionActionFn.accept( (_E)_expression.of(e));
            }
        });
        return astNode;
    }

    @Override
    public List<Select<E, _E>> listSelectedIn(Node astNode ){
        List<Select<E, _E>>sts = new ArrayList<>();
        astNode.walk(this.expressionClass, e-> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }

    @Override
    public List<Select<E, _E>> listSelectedIn(_java._domain _j){
        List<Select<E, _E>>sts = new ArrayList<>();
        Walk.in(_j, this.expressionClass, e -> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }
    
    @Override
    public List<Select<E, _E>> listSelectedIn(Class clazz){
        List<Select<E, _E>>sts = new ArrayList<>();
        Walk.in((_type)_java.type(clazz), this.expressionClass, e -> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @return 
     */
    public List<Select<E, _E>> listSelectedIn(Class clazz, Predicate<Select<E, _E>> selectConstraint ){
        return listSelectedIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select<E, _E>> listSelectedIn(Node astNode , Predicate<Select<E, _E>> selectConstraint){
        List<Select<E, _E>>sts = new ArrayList<>();
        astNode.walk(this.expressionClass, e-> {
            Select s = select( e );
            if( s != null  && selectConstraint.test(s)){
                sts.add( s);
            }
        });
        return sts;
    }

    /**
     * 
     * @param _j
     * @param selectConstraint
     * @return 
     */
    public List<Select<E, _E>> listSelectedIn(_java._domain _j, Predicate<Select<E, _E>> selectConstraint){
        List<Select<E, _E>>sts = new ArrayList<>();
        Walk.in(_j, this.expressionClass, e -> {
            Select s = select( e );
            if( s != null  && selectConstraint.test(s)){
                sts.add( s);
            }
        });
        return sts;
    }

    @Override
    public <N extends Node> N removeIn(N astNode, Predicate<_E> exprMatchFn){
        astNode.walk( this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null && exprMatchFn.test( (_E)sel._ex)){
                sel.ast().removeForced();
            }
        });
        return astNode;
    }

     @Override
    public <N extends Node> N removeIn(N astNode ){
        return removeIn(astNode, e ->true);
    }
    
    /**
     * 
     * @param clazz
     * @param astExprReplace
     * @return 
     */
    public <_CT extends _type> _CT replaceIn( Class clazz, Node astExprReplace ){
        return (_CT)replaceIn((_type)_java.type(clazz), astExprReplace);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param astExprReplace
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, Node astExprReplace ){
        Walk.in(_j, this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel.ast().replace(astExprReplace );
            }
        });
        return _j;
    }

    /**
     * 
     * @param <_J>
     * @param _j
     * @param protoReplaceExpr
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, String protoReplaceExpr ){
        return (_J) replaceIn(_j, $ex.of(protoReplaceExpr) );
    }
    
    /**
     * 
     * @param clazz
     * @param $replaceProto
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $ex $replaceProto){
        return replaceIn((_CT)_java.type(clazz), $replaceProto);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replaceProto
     * @return 
     */
    public <_J extends _java._domain> _J replaceIn(_J _j, $ex $replaceProto){
        Walk.in(_j, this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                Expression replaceNode = (Expression) $replaceProto.draft( sel.tokens.asTokens() );
                sel.ast().replace( replaceNode );
            }
        });
        return _j;
    }

    /**
     * 
     * @param clazz the target class to search through
     * @param selectConsumer the consumer to operate on all selected entities
     * @return the (potentially modified) _type of the clazz
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select<E, _E>> selectConsumer ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Consumer<Select<E, _E>> selectConsumer ){
        Walk.in(_j, this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return _j;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select<E, _E>> selectConsumer ){
        astNode.walk(this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    } 
    
    /**
     * 
     * @param clazz
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Predicate<Select<E, _E>> selectConstraint, Consumer<Select<E, _E>> selectConsumer ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConstraint, selectConsumer );
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java._domain> _J forSelectedIn(_J _j, Predicate<Select<E,_E>> selectConstraint, Consumer<Select<E, _E>> selectConsumer ){
        Walk.in(_j, this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null  && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return _j;
    }

    /**
     * 
     * @param <N>
     * @param astNode
     * @param selectConstraint
     * @param selectConsumer
     * @return 
     */
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<E, _E>> selectConstraint, Consumer<Select<E, _E>> selectConsumer ){
        astNode.walk(this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    }

    @Override
    public String toString() {
        return "$ex{ (" + this.expressionClass.getSimpleName() + ") : \"" + this.exprStencil + "\" }";
    }

    /**
     * prints the member where the expression occurs
     * @param clazz

    public void printParentMemberIn(Class clazz){
        forEachIn(clazz, c-> System.out.println( Ast.parentMemberOf(c) ) );
    }

    public<_J extends _model> void printParentMemberIn(_J _j ){
        forEachIn(_j, c-> System.out.println( Ast.parentMemberOf(c) ) );
    }
    */

    /**
     * An Or entity that can match against any of the $pattern instances provided
     * NOTE: template features (draft/fill) are supressed.
     */
    public static class Or extends $ex{

        final List<$ex>ors = new ArrayList<>();

        public Or($ex...$as){
            super(Expression.class, "$ex$" );
            Arrays.stream($as).forEach($a -> ors.add($a) );
        }

        @Override
        public $ex hardcode$(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.hardcode$(translator, kvs));
            return this;
        }

        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("$ex.Or{");
            sb.append(System.lineSeparator());
            ors.forEach($a -> sb.append( Text.indent($a.toString()) ) );
            sb.append("}");
            return sb.toString();
        }

        /**
         *
         * @param astNode
         * @return
         */
        public $ex.Select select(Expression astNode){
            $ex $a = whichMatch(astNode);
            if( $a != null ){
                return $a.select(astNode);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $anno that matches the AnnotationExpr or null if none of the match
         * @param ae
         * @return
         */
        public $ex whichMatch(Expression ae){
            if( !this.constraint.test( ae ) ){
                return null;
            }
            Optional<$ex> orsel  = this.ors.stream().filter( $p-> $p.match(ae) ).findFirst();
            if( orsel.isPresent() ){
                return orsel.get();
            }
            return null;
        }
    }

    /**
     * A Matched Selection result returned from matching a prototype $expr
     * inside of some (Ast)Node or (_java)_node
     * @param <T> expression type
     */
    public static class Select<T extends Expression, _T extends _expression> implements $pattern.selected,
            selectAst<T> {

        public final _T _ex;
        //public final T astExpression;
        public final $tokens tokens;

        public Select( _T _ex, Tokens tokens){
            this._ex = _ex;
            this.tokens = $tokens.of(tokens);
        }
        public Select( T astExpr, Tokens tokens){
            //this.astExpression = astExpr;
            this._ex = (_T)_expression.of(astExpr);
            this.tokens = $tokens.of(tokens);
        }
                
        public Select( T astExpr, $tokens tokens) {
            //this.astExpression = astExpr;
            this._ex = (_T)_expression.of(astExpr);
            this.tokens = tokens;
        }

        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$expr.Select{"+ System.lineSeparator()+
                Text.indent(_ex.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public T ast() {
            return (T)this._ex.ast();
        }

        public _T domain(){ return this._ex; }
    }
}

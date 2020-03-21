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
public class $ex<E extends Expression, _E extends _expression, $E extends $ex>
    implements $field.$part, $pattern<_E, $E>, $var.$part, $enumConstant.$part, $annotationEntry.$part, Template<_E>,
    $body.$part, $method.$part, $constructor.$part {

    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E, _E, $E> of(){
        return new $ex();
    }
    /**
     * 
     * @param <E>
     * @param pattern
     * @return 
     */
    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E, _E, $E> of(String...pattern ){
        //so.... if I JUST do a pattern, I want to se the expression class
        // to Expression.class
        Expression expr = Ex.of(pattern);
        return ($ex<E, _E, $E>)new $ex<Expression, _expression, $ex>( Expression.class,
                expr.toString(Print.PRINT_NO_COMMENTS) );
    }

    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E,_E,$E> of(Class expressionClass){
        if( Expression.class.isAssignableFrom( expressionClass ) ){
            return $ex.of().$and( e-> expressionClass.isAssignableFrom(e.ast().getClass()));
        }
        else if( _expression.class.isAssignableFrom(expressionClass)){
            return $ex.of().$and( e-> expressionClass.isAssignableFrom(e.getClass()));
        }
        throw new $pattern.$exception("class " + expressionClass + " is not Expression or _expression type");
    }

    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E,_E,$E> of(Class expressionClass, String...ex){
        $ex $e = $ex.of(expressionClass);
        $e.exprStencil = Stencil.of(ex);
        return $e;
    }

    /**
     * 
     * @param <E>
     * @param pattern
     * @param constraint
     * @return 
     */
    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E, _E, $E> of(String pattern, Predicate<_E> constraint ){
        return new $ex<>( (E) Ex.of(pattern)).$and( (Predicate<_expression>) constraint);
    }

    /**
     *
     * @param <E>
     * @param _e
     * @return
     */
    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E, _E, $E> of(_E _e ){
        return ($ex<E, _E, $E>)of(_e.ast() );
    }

    /**
     * 
     * @param <E>
     * @param protoExpr
     * @return 
     */
    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E, _E, $E> of(E protoExpr ){
        return new $ex<>(protoExpr );
    }

    /**
     * 
     * @param <E>
     * @param protoExpr
     * @param constraint
     * @return 
     */
    public static <E extends Expression, _E extends _expression, $E extends $ex> $ex<E, _E, $E>of(E protoExpr, Predicate<_E> constraint ){
        return new $ex<E, _E, $E>(protoExpr ).$and(constraint);
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
    public static $ex<ArrayAccessExpr, _arrayAccess, $ex> arrayAccessEx(String... pattern ) {
        return new $ex<>( Ex.arrayAccessEx(pattern) );
    }
    
    /**
     * i.e."arr[3]"
     * @param constraint
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess, $ex>  arrayAccessEx(Predicate<_arrayAccess> constraint) {
        return new $ex<ArrayAccessExpr, _arrayAccess, $ex> ( Ex.arrayAccessEx("a[0]") )
                .$(Ex.of("a[0]"), "any").$and(constraint);
    }    

    /**
     * i.e."arr[3]"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess, $ex>  arrayAccessEx(String pattern, Predicate<_arrayAccess> constraint) {
        return new $ex<ArrayAccessExpr, _arrayAccess, $ex>  ( Ex.arrayAccessEx(pattern) ).$and(constraint);
    }
    
    /**
     * ANY array Access
     * i.e."arr[3]"
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccess, $ex>  arrayAccessEx( ) {
        return new $ex<>( ArrayAccessExpr.class, "$arrayAccessExpr$");
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate, $ex>  arrayCreationEx(String... pattern ) {
        return new $ex<>( Ex.arrayCreationEx(pattern ) );
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param constraint
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate, $ex>  arrayCreationEx(Predicate<_arrayCreate> constraint ) {
        return new $ex<ArrayCreationExpr, _arrayCreate, $ex>  ( Ex.arrayCreationEx("new int[]")).$(Ex.of("new int[]"), "any").$and(constraint);
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate, $ex>  arrayCreationEx(String pattern, Predicate<_arrayCreate> constraint ) {
        return new $ex<ArrayCreationExpr, _arrayCreate, $ex>  ( Ex.arrayCreationEx(pattern ) ).$and(constraint);
    }

    /**
     * i.e."new Obj[]", "new int[][]"
     * @return 
     */
    public static $ex<ArrayCreationExpr, _arrayCreate, $ex>  arrayCreationEx( ) {
        return new $ex<>( ArrayCreationExpr.class, "$arrayCreationExpr$");
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(String... pattern ) {
        return new $ex<>( Ex.arrayInitializerEx(pattern ) );
    }
    
    /**
     * 
     * @param ints
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(int[] ints ) {
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
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(boolean[] bools ) {
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
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(char[] chars ) {
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
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(double[] doubles ) {
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
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(float[] floats ) {
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
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(Predicate<_arrayInitialize> constraint) {
        return new $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  ( Ex.arrayInitializerEx("{1}") ).$(Ex.of("{1}"), "any").$and(constraint);
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx(String pattern, Predicate<_arrayInitialize> constraint) {
        return new $ex<ArrayInitializerExpr, _arrayInitialize, $ex> ( Ex.arrayInitializerEx(pattern ) ).$and(constraint);
    }
    
    /**
     * Any array initializer i.e. "{1,2,3,4,5}"
     * @return 
     */
    public static $ex<ArrayInitializerExpr, _arrayInitialize, $ex>  arrayInitEx() {
        return new $ex( ArrayInitializerExpr.class, "$arrayInitializer$");
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @return 
     */
    public static $ex<AssignExpr, _assign, $ex>  assignEx(String... pattern ) {
        return new $ex<>( Ex.assignEx(pattern ) );
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param constraint
     * @return 
     */
    public static $ex<AssignExpr, _assign, $ex>  assignEx(Predicate<_assign> constraint) {
        return new $ex<AssignExpr, _assign, $ex> ( Ex.assignEx("a=1") ).$(Ex.of("a=1"), "any").$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<AssignExpr, _assign, $ex>  assignEx(String pattern, Predicate<_assign> constraint) {
        return new $ex<AssignExpr, _assign, $ex>  ( Ex.assignEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @return 
     */
    public static $ex<AssignExpr,_assign, $ex>  assignEx( ) {
        return new $ex<>( AssignExpr.class, "$assignExpr$");
    }

    /** 
     * a || b 
     * @param pattern
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression, $ex>  binaryEx(String... pattern ) {
        return new $ex<>( Ex.binaryEx(pattern ) );
    }
    
    /**
     * a || b 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression, $ex>  binaryEx(String pattern, Predicate<_binaryExpression> constraint) {
        return new $ex<BinaryExpr, _binaryExpression, $ex> ( Ex.binaryEx(pattern ) ).$and(constraint);
    }    

    /**
     * a || b 
     * @param constraint
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression, $ex>  binaryEx(Predicate<_binaryExpression> constraint) {
        return new $ex<BinaryExpr, _binaryExpression, $ex> ( Ex.binaryEx("a || b" ) ).$(Ex.binaryEx("a || b"), "any").$and(constraint);
    }  
    
    /**
     * a || b    
     * @return 
     */
    public static $ex<BinaryExpr, _binaryExpression, $ex>  binaryEx( ) {
        return new $ex<>( BinaryExpr.class, "$binaryExpr$");
    } 
    
    /** 
     *  i.e.true
     * @param
     * @return  

    public static $ex<BooleanLiteralExpr, _boolean, $ex> of(boolean b ){
        return new $ex<>( Ex.of( b ) );
    }
    */

    public static $ex<LiteralExpr, _expression._literal, $ex>  literalEx() {
        return new $ex(LiteralExpr.class, "$expr$");
    }

    public static $ex<LiteralExpr, _expression._literal, $ex>  literalEx(Predicate<_expression._literal> constraint) {
        return new $ex(LiteralExpr.class, "$expr$").$and(constraint);
    }

    /**
     * 
     * @param b
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean, $ex>  booleanLiteralEx(boolean b ) {
        return new $ex( Ex.of( b ) );
    }
    
    /**
     * 
     * @param b
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean, $ex>  booleanLiteralEx(boolean b, Predicate<_boolean> constraint) {
        return new $ex( Ex.of( b ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * 
     * @param pattern
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean, $ex>  booleanLiteralEx(String... pattern ) {
        return new $ex( Ex.booleanLiteralEx(pattern ) );
    }

    /** 
     * "true" / "false" 
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean, $ex>  booleanLiteralEx(Predicate<_boolean>constraint ) {
        return new $ex( Ex.booleanLiteralEx("true") ).$("true", "any").$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean, $ex>  booleanLiteralEx(String pattern, Predicate<_boolean>constraint ) {
        return new $ex( Ex.booleanLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @return 
     */
    public static $ex<BooleanLiteralExpr, _boolean, $ex>  booleanLiteralEx( ) {
        return new $ex( BooleanLiteralExpr.class, "$booleanLiteral$");
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @return 
     */
    public static $ex<CastExpr, _cast, $ex>  castEx(String... pattern ) {
        return new $ex( Ex.castEx(pattern ) );
    }

    /** 
     * (String)o 
     * @param constraint
     * @return 
     */
    public static $ex<CastExpr, _cast, $ex>  castEx(Predicate<_cast> constraint ) {
        return new $ex( CastExpr.class, "($cast$)" ).$and(constraint); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any").constraint(constraint);
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<CastExpr, _cast, $ex>  castEx(String pattern, Predicate<_cast> constraint ) {
        return new $ex( Ex.castEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * (String)o 
     * @return 
     */
    public static $ex<CastExpr, _cast, $ex>  castEx( ) {
        return new $ex( CastExpr.class, "($cast$)"); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any");
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex>  of(char c ){
        return new $ex( Ex.charLiteralEx( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex>  of(char c, Predicate<_char> constraint){
        return new $ex( Ex.charLiteralEx( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex>  charLiteralEx(char c ) {
        return new $ex( Ex.charLiteralEx( c ) );
    }

    /**
     * 'a', 'e', 'i', 'o', 'u'
     * @param ds
     * @return
     */
    public static $ex<CharLiteralExpr, _char, $ex>  charLiteralEx(char... ds ) {
        Set<Character> sd = new HashSet<>();
        for(int i=0;i<ds.length; i++){
            sd.add( ds[i]);
        }
        return new $ex( CharLiteralExpr.class, "$char$" ).$and(d-> sd.contains(d));
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex>  charLiteralEx(char c, Predicate<_char> constraint) {
        return new $ex( Ex.charLiteralEx( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex>  charLiteralEx(String... pattern ) {
        return new $ex( Ex.charLiteralEx(Text.combine(pattern) ) );
    }

    /** 
     * 'c' 
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex> charLiteralEx(Predicate<_char> constraint) {
        return new $ex( Ex.charLiteralEx('a') ).$("'a'", "any").$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @param constraint
     * @return 

    public static $ex<CharLiteralExpr, _char, $ex>  charLiteralEx(String pattern, Predicate<_char> constraint) {
        return new $ex( Ex.charLiteralEx(pattern ) ).$and(constraint);
    }
    */
    
    /** 
     * ANY char literal
     * 'c' 
     * @return 
     */
    public static $ex<CharLiteralExpr, _char, $ex> charLiteralEx(  ) {
        return new $ex( CharLiteralExpr.class, "$charLiteral$");
    }
    
    /** 
     * i.e."String.class" 
     * @param pattern
     * @return 
     */
    public static $ex<ClassExpr, _classExpression, $ex> classEx(String... pattern ) {
        return new $ex( Ex.classEx(pattern ) );
    }

    /**
     * Map.class
     * @param constraint
     * @return 
     */
    public static $ex<ClassExpr, _classExpression, $ex> classEx(Predicate<_classExpression> constraint) {
        return new $ex( Ex.classEx("a.class") )
            .$("a.class", "any").$and(constraint);
    }
    
    /**
     * String.class
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ClassExpr, _classExpression, $ex> classEx(String pattern, Predicate<_classExpression> constraint) {
        return new $ex( Ex.classEx(pattern ) ).$and(constraint);
    }

    /**
     * Class expr (i.e. "String.class")
     * @return 
     */
    public static $ex<ClassExpr, _classExpression, $ex> classEx() {
        return new $ex( ClassExpr.class, "$classExpr$");
    }
    
    /** 
     * i.e."(a < b) ? a : b" 
     * @param pattern
     * @return 
     */
    public static $ex<ConditionalExpr, _conditionalExpression, $ex> conditionalEx(String... pattern ) {
        return new $ex( Ex.conditionalEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ConditionalExpr, _conditionalExpression, $ex> conditionalEx(String pattern, Predicate<_conditionalExpression> constraint) {
        return new $ex( Ex.conditionalEx(pattern ) ).$and(constraint);
    }
    
    /**
     * conditional, i.e. "(a==1) ? 1 : 2" 
     * @param constraint
     * @return 
     */
    public static $ex<ConditionalExpr, _conditionalExpression, $ex> conditionalEx(Predicate<_conditionalExpression> constraint) {
        return new $ex( Ex.conditionalEx("(a==1) ? 1 : 2" ) )
                .$(Ex.conditionalEx("(a==1) ? 1 : 2"), "any")
                .$and(constraint);
    }    
    
    /**
     * Any conditional i.e. "(a==1) ? 1 : 2" 
     * @return 
     */
    public static $ex<ConditionalExpr, _conditionalExpression, $ex> conditionalEx() {
        return new $ex( ConditionalExpr.class, "$conditionalExpr$");
    }    
    
    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> of(double d ){
        return new $ex( Ex.of( d ) );
    }

    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(double d ) {
        return new $ex( Ex.doubleLiteralEx( d ) );
    }

    /**
     * 3.14d
     * @param ds
     * @return
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(double... ds ) {
        Set<Double> sd = new HashSet<>();
        Arrays.stream(ds).forEach(dd->sd.add(dd));
        return new $ex( DoubleLiteralExpr.class, "$any$" ).$and(d-> sd.contains(d));
    }

    /**
     * 3.14f
     * @param fs
     * @return
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(float... fs ) {
        Set<Float> sd = new HashSet<>();
        for(int i=0;i<fs.length; i++){
            sd.add( fs[i]);
        }
        return new $ex( Ex.doubleLiteralEx( ) ).$and(d-> sd.contains(d));
    }


    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(double d, Predicate<_double> constraint) {
        return new $ex( Ex.doubleLiteralEx( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(String pattern ) {
        return new $ex( Ex.doubleLiteralEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(String pattern, Predicate<_double> constraint) {
        return new $ex( Ex.doubleLiteralEx(pattern ) ).$and(constraint);
    }
        
    /**
     * 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> of(float d ){
        return new $ex( Ex.doubleLiteralEx( d ) );
    }

    /**
     * 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(float d ) {
        return new $ex( Ex.of( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(float d, Predicate<_double> constraint) {
        return new $ex( Ex.of( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx(Predicate<_double> constraint) {
        return new $ex( Ex.of( 1.0d ) ).$("1.0d", "any").$and(constraint);
    }
        
    /**
     * 10.1d
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> doubleLiteralEx( ) {
        return new $ex( DoubleLiteralExpr.class, "$doubleLiteral$");
    }
    
    /** 
     * i.e."3.14f" 
     * @param pattern
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> floatLiteralEx(String... pattern ) {
        return new $ex( Ex.floatLiteralEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> floatLiteralEx(String pattern, Predicate<_double> constraint ) {
        return new $ex( Ex.floatLiteralEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> floatLiteralEx(Predicate<_double> constraint ) {
        return new $ex( Ex.of(1.0f) ).$(Ex.of(1.0f), "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<DoubleLiteralExpr, _double, $ex> floatLiteralEx( ) {
        return new $ex( DoubleLiteralExpr.class, "$floatLiteral$");
    }
    
    /** 
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression, $ex> enclosedEx(String... pattern ) {
        return new $ex( Ex.enclosedEx(pattern ) );
    }

    /**
     * i.e.( 3 + 4 ) 
     * @param constraint
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression, $ex> enclosedEx(Predicate<_enclosedExpression>constraint ) {
        return new $ex( Ex.enclosedEx("(a)" ) ).$("(a)", "any").$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression, $ex> enclosedEx(String pattern, Predicate<_enclosedExpression>constraint ) {
        return new $ex( Ex.enclosedEx(pattern ) ).$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 )    
     * @return 
     */
    public static $ex<EnclosedExpr, _enclosedExpression, $ex> enclosedEx( ) {
        return new $ex( EnclosedExpr.class, "$enclosedExpr$");
    }
    
    /** 
     *  i.e."person.NAME"
     * @param pattern
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess, $ex> fieldAccessEx(String... pattern ) {
        return new $ex( Ex.fieldAccessEx(pattern ) );
    }

    /**
     * i.e. "System.out"
     * @param constraint
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess, $ex> fieldAccessEx(Predicate<_fieldAccess> constraint ) {
        return new $ex( Ex.fieldAccessEx("a.B") )
                .$(Ex.fieldAccessEx("a.B"), "any").$and(constraint);
    }
    
    /**
     * i.e. "System.out"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess, $ex> fieldAccessEx(String pattern, Predicate<_fieldAccess> constraint ) {
        return new $ex( Ex.fieldAccessEx(pattern ) ).$and(constraint);
    }
    
    /**
     * i.e. "System.out"
     * 
     * @return 
     */
    public static $ex<FieldAccessExpr, _fieldAccess, $ex> fieldAccessEx( ) {
        return new $ex( FieldAccessExpr.class, "$fieldAccessExpr$");
    }
    
    /** 
     * v instanceof Serializable 
     * @param pattern
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf, $ex> instanceOfEx(String... pattern ) {
        return new $ex( Ex.instanceOfEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf, $ex> instanceOfEx(Predicate<_instanceOf> constraint ) {
        return new $ex( Ex.instanceOfEx( "a instanceof b" ) ).$("a instanceof b", "any")
                .$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf, $ex> instanceOfEx(String pattern, Predicate<_instanceOf> constraint ) {
        return new $ex( Ex.instanceOfEx(pattern ) ).$and(constraint);
    }

    public static $ex<InstanceOfExpr, _instanceOf, $ex> instanceOfEx($typeRef type ){
        return instanceOfEx("$expr$ instanceof $type$", e-> type.matches( e.getTypeRef() ) );
    }

    /**
     * 
     * @return 
     */
    public static $ex<InstanceOfExpr, _instanceOf, $ex> instanceOfEx() {
        return new $ex( InstanceOfExpr.class, "$instanceOf$" );
    }

    public static $ex<InstanceOfExpr, _instanceOf, $ex> instanceOfEx(Class typeClass ){
        return instanceOfEx( $typeRef.of(typeClass) );
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int, $ex> of(int i) {
        return new $ex( Ex.intLiteralEx(i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int, $ex> of(int i, Predicate<_int> constraint) {
        return new $ex( Ex.intLiteralEx( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int, $ex> intLiteralEx(Predicate<_int> constraint) {
        return new $ex( Ex.intLiteralEx( 1 ) ).$("1", "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int, $ex> intLiteralEx( ) {
        return new $ex( IntegerLiteralExpr.class, "$intLiteralExpr$");
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $ex<IntegerLiteralExpr,_int, $ex> intLiteralEx(int i) {
        return new $ex( Ex.intLiteralEx( i ) );
    }

    public static $ex<IntegerLiteralExpr,_int, $ex> intLiteralEx(int... is) {
        Set<Integer> iset = new HashSet<>();
        Arrays.stream(is).forEach(i-> iset.add(i));
        return intLiteralEx().$and(i-> iset.contains(i.getValue()));
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr,_int, $ex> intLiteralEx(int i, Predicate<_int> constraint) {
        return new $ex( Ex.intLiteralEx( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<IntegerLiteralExpr, _int, $ex> intLiteralEx(String... pattern ) {
        return new $ex( IntegerLiteralExpr.class, Text.combine(pattern) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr,_int, $ex> intLiteralEx(String pattern, Predicate<_int> constraint ) {
        return new $ex( Ex.intLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @param pattern
     * @return 
     */
    public static $ex<LambdaExpr, _lambda, $ex> lambdaEx(String... pattern ) {
        return new $ex( Ex.lambdaEx(pattern ) );
    }

    /** 
     * a-> System.out.println( a )  
     * @param constraint
     * @return 
     */
    public static $ex<LambdaExpr, _lambda, $ex> lambdaEx(Predicate<_lambda> constraint) {
        return new $ex( Ex.lambdaEx("a-> true" ) ).$(Ex.lambdaEx("a->true"), "any").$and(constraint);
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<LambdaExpr, _lambda, $ex> lambdaEx(String pattern , Predicate<_lambda> constraint) {
        return new $ex( Ex.lambdaEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @return 
     */
    public static $ex<LambdaExpr, _lambda, $ex> lambdaEx( ) {
        return new $ex( LambdaExpr.class, "$lambdaExpr$" );
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> of(long l) {
        return new $ex( Ex.longLiteralEx( l ) );
    }

    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> of(long l, Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx( l ) ).$and(constraint);
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx(long l ) {
        return new $ex( Ex.longLiteralEx( l ) );
    }

    /**
     * 1L, 0L, -1L, Long.MIN_VALUE, Long.MAX_VALUE
     * @param ls
     * @return
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx(long... ls ) {
        Set<Long> sd = new HashSet<>();
        for(int i=0;i<ls.length; i++){
            sd.add( ls[i]);
        }
        return new $ex( Ex.longLiteralEx( ) ).$and(d-> sd.contains(d));
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx(Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx(1L)).$(Ex.longLiteralEx(1L), "any")
                .$and(constraint) ;
    }
    
    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx(long l, Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx( l ) ).$and(constraint);
    }
   
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx(String... pattern ) {
        return new $ex( Ex.longLiteralEx(pattern ) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx(String pattern, Predicate<_long> constraint ) {
        return new $ex( Ex.longLiteralEx(pattern ) ).$and(constraint);
    }

    /**
     * Any long literal
     * @return 
     */
    public static $ex<LongLiteralExpr, _long, $ex> longLiteralEx( ) {
        return new $ex( LongLiteralExpr.class, "$longLiteralExpr$");
    }
    
    /** 
     * doMethod(t)
     * @param pattern 
     * @return  
     */
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(String... pattern ) {
        return new $ex( Ex.methodCallEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(String pattern, Predicate<_methodCall> constraint ) {
        return new $ex( Ex.methodCallEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(Predicate<_methodCall> constraint ) {
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
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(Consumer<? extends Object> lambdaWithMethodCallInSource ){
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
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
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
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(Ex.TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
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
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx(Ex.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }
    
    /**
     * ANY method call
     * @return 
     */
    public static $ex<MethodCallExpr, _methodCall, $ex> methodCallEx( ) {
        return new $ex( MethodCallExpr.class, "$methodCall$");
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @return 
     */
    public static $ex<MethodReferenceExpr, _methodReference, $ex> methodReferenceEx(String... pattern ) {
        return new $ex( Ex.methodReferenceEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<MethodReferenceExpr, _methodReference, $ex> methodReferenceEx(Predicate<_methodReference> constraint) {
        return new $ex( Ex.methodReferenceEx("A:b")).$("A:b", "any").$and(constraint);
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<MethodReferenceExpr,_methodReference, $ex> methodReferenceEx(String pattern, Predicate<_methodReference>constraint ) {
        return new $ex( Ex.methodReferenceEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<MethodReferenceExpr, _methodReference, $ex> methodReferenceEx() {
        return new $ex( MethodReferenceExpr.class, "$methodReference$");
    }
    
    /** 
     *  i.e."null"
     * @return  
     */
    public static $ex<NullLiteralExpr, _null, $ex> nullEx(){
        return new $ex( NullLiteralExpr.class, "$nullExpr$" );
    }

    /**
     *
     * @param nle
     * @return
     */
    public static $ex<NullLiteralExpr, _null, $ex> nullEx(Predicate<_null> nle){
        return new $ex( NullLiteralExpr.class, "$nullExpr$" ).$and(nle);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<NameExpr, _nameExpression, $ex> nameEx(String... pattern ) {
        return new $ex( Ex.nameEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<NameExpr, _nameExpression, $ex> nameEx(Predicate<_nameExpression>constraint) {
        return new $ex( Ex.nameEx("name" ) ).$("name", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<NameExpr, _nameExpression, $ex> nameEx(String pattern, Predicate<_nameExpression>constraint) {
        return new $ex( Ex.nameEx(pattern ) ).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $ex<NameExpr, _nameExpression, $ex> nameEx() {
        return new $ex( NameExpr.class, "$nameExpr$");
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new, $ex> newEx(String... pattern ) {
        return new $ex( Ex.newEx(pattern ) );
    }
    
    /** 
     * "new Date()"
     * @param constraint
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new, $ex> newEx(Predicate<_new>constraint ) {
        return new $ex( Ex.newEx( "new a()" ) ).$("new a()", "any").$and(constraint);
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new, $ex> newEx(String pattern, Predicate<_new>constraint ) {
        return new $ex( Ex.newEx(pattern ) ).$and(constraint);
    }

    /** 
     * "new Date()"
     * @return 
     */
    public static $ex<ObjectCreationExpr, _new, $ex> newEx() {
        return new $ex( ObjectCreationExpr.class, "$objectCreationExpr$");
    }

    /**
     *
     * @param literal
     * @return
     */
    public static $ex<StringLiteralExpr, _string, $ex> stringLiteralEx(String literal ) {
        return new $ex( Ex.stringLiteralEx(literal) );
    }
    
    /** 
     * "literal"
     * @param pattern
     * @return 

    public static $ex<StringLiteralExpr, _string> stringLiteralEx(String... pattern ) {
        Set<String>lits = new HashSet<>();
        Arrays.stream(patterns)

        return new $ex( StringLiteralExpr.class, "$any$").$and(Ex.stringLiteralEx(pattern ) );
    }
    */

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<StringLiteralExpr, _string, $ex> stringLiteralEx(Predicate<_string> constraint) {
        return new $ex( Ex.stringLiteralEx( "\"a\"" ) ).$("\"a\"", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<StringLiteralExpr, _string, $ex> stringLiteralEx(String pattern, Predicate<_string> constraint) {
        return new $ex( Ex.stringLiteralEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<StringLiteralExpr, _string, $ex> stringLiteralEx( ) {
        return new $ex( StringLiteralExpr.class, "$stringLiteral$");
    }
    
    /**
     * ANY super expression 
     * 
     * @return 
     */
    public static $ex<SuperExpr,_super, $ex> superEx(){
        return new $ex(SuperExpr.class, "$superExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<SuperExpr, _super, $ex> superEx(String...pattern ){
        return new $ex(Ex.superEx(pattern));
    }
    
    /**
     * 
     * @param se
     * @return 
     */
    public static $ex<SuperExpr, _super, $ex> superEx(Predicate<_super> se){
        return superEx().$and(se);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<SuperExpr, _super, $ex> superEx(String pattern, Predicate<_super> constraint ){
        return new $ex(Ex.superEx(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param protoSuperExpr
     * @return 
     */
    public static $ex<SuperExpr, _super, $ex> superEx(SuperExpr protoSuperExpr){
        return new $ex(protoSuperExpr);
    }
    
    /**
     * 
     * @param superExpr
     * @param constraint
     * @return 
     */
    public static $ex<SuperExpr, _super, $ex> superEx(SuperExpr superExpr, Predicate<_super> constraint ){
        return new $ex(superExpr).$and(constraint);
    }

    /**
     * doMethod(t)
     * @param pattern
     * @return
     */
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(String... pattern ) {
        return new $ex( Ex.switchEx(pattern ) );
    }

    /**
     * @param se
     * @return
     */
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(SwitchExpr se) {
        return new $ex( se );
    }

    /**
     * @param se
     * @return
     */
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(_switchExpression se) {
        return new $ex( se.ast() );
    }

    /**
     *
     * @param pattern
     * @param constraint
     * @return
     */
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(String pattern, Predicate<_switchExpression> constraint ) {
        return switchEx( ).$and(constraint);
    }

    /**
     *
     * @param constraint
     * @return
     */
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(Predicate<_switchExpression> constraint ) {
        return new $ex( Ex.switchEx()).$and(constraint);
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
    public static SwitchExpr switchExpression(Ex.Command lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return astLambda.getBody().findFirst(SwitchExpr.class).get();
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
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(Consumer<? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(SwitchExpr.class).get());
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
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(SwitchExpr.class).get());
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
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(Ex.TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(SwitchExpr.class).get());
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
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx(Ex.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(SwitchExpr.class).get());
    }

    /**
     * ANY method call
     * @return
     */
    public static $ex<SwitchExpr, _switchExpression, $ex> switchEx( ) {
        return new $ex( SwitchExpr.class, "$switchEx$");
    }
















    /**
     * ANY this expression
     * @return 
     */
    public static $ex<ThisExpr, _this, $ex> thisEx( ){
        return new $ex(ThisExpr.class, "$thisExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<ThisExpr, _this, $ex> thisEx(String... pattern){
        return new $ex(Ex.thisEx(pattern) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr, _this, $ex> thisEx(String pattern, Predicate<_this> constraint){
        return new $ex(Ex.thisEx(pattern) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr, _this, $ex> thisEx(Predicate<_this> constraint){
        return new $ex(Ex.thisEx() ).$and(constraint);
    }
        
    /**
     * 
     * @param protoThisExpr
     * @return 
     */
    public static $ex<ThisExpr, _this, $ex> thisEx(ThisExpr protoThisExpr){
        return new $ex(protoThisExpr);
    }
    
    /**
     * 
     * @param protoThisExpr
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr, _this, $ex> thisEx(ThisExpr protoThisExpr, Predicate<_this> constraint){
        return new $ex(protoThisExpr ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression, $ex> typeEx(String... pattern ) {
        return new $ex( Ex.typeEx(pattern ) );
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param constraint
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression, $ex> typeEx(Predicate<_typeExpression> constraint ) {
        return new $ex( Ex.typeEx( "a" ) ).$("a", "any").$and(constraint);
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression, $ex> typeEx(String pattern, Predicate<_typeExpression> constraint ) {
        return new $ex( Ex.typeEx(pattern ) ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @return 
     */
    public static $ex<TypeExpr, _typeExpression, $ex> typeEx( ) {
        return new $ex( TypeExpr.class, "$typeExpr$");
    }
    
    /** 
     * i.e."!true" 
     * @param pattern
     * @return 
     */
    public static $ex<UnaryExpr, _unary, $ex> unaryEx(String... pattern ) {
        return new $ex( Ex.unaryEx(pattern ) );
    }
   
    /** 
     *  i.e."!true"
     * @param constraint 
     * @return  
     */
    public static $ex<UnaryExpr, _unary, $ex> unaryEx(Predicate<_unary>constraint) {
        return new $ex( Ex.unaryEx( "!true" ) ).$("!true", "any").$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $ex<UnaryExpr, _unary, $ex> unaryEx(String pattern, Predicate<_unary>constraint) {
        return new $ex( Ex.unaryEx(pattern ) ).$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @return  
     */
    public static $ex<UnaryExpr, _unary, $ex> unaryEx() {
        return new $ex( UnaryExpr.class, "$unaryExpr$");
    }
    
    /** 
     * "int i = 1"
     * @param pattern
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _localVariables, $ex> varLocalEx(String... pattern ) {
        return new $ex( Ex.varLocalEx(pattern ) );
    }

    /** 
     * "int i = 1"
     * @param constraint 
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _localVariables, $ex> varLocalEx(Predicate<_localVariables> constraint) {
        return new $ex( Ex.varLocalEx( "int i=1") ).$(Ex.of("int i=1"), "any").$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _localVariables, $ex> varLocalEx(String pattern, Predicate<_localVariables> constraint) {
        return new $ex( Ex.varLocalEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @return  
     */
    public static $ex<VariableDeclarationExpr, _localVariables, $ex> varLocalEx( ) {
        return new $ex( VariableDeclarationExpr.class, "$varDecl$");
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $ex<E, _E, $E> $isAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $and(prev);
    }

    /**
     * Adds a constraint that the beforeExpression occurs in the same context/block before the target Expression
     * @param patternsOccurringBeforeThisNode
     * @return
     */
    public $ex<E, _E, $E> $isNotAfter($pattern... patternsOccurringBeforeThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findPrevious(e.ast(), patternsOccurringBeforeThisNode) != null;
        return $not(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $ex<E, _E, $E> $isBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $and(prev);
    }

    /**
     *
     * @param patternsOccurringAfterThisNode
     * @return
     */
    public $ex<E, _E, $E> $isNotBefore($pattern... patternsOccurringAfterThisNode ){
        Predicate<_E> prev = e -> $pattern.BodyScope.findNext(e.ast(), patternsOccurringAfterThisNode) != null;
        return $not(prev);
    }

    /**
     * Matches ANY expression
     * @return 
     */
    public static $ex<Expression, _expression, $ex> any(){
        return new $ex( Expression.class, "$expr$");
    }
    
    /**
     * Matches ANY expression that matches the constraint
     * @param constraint
     * @return 

    public static $ex<Expression, _expression, $ex> of(Predicate<_expression> constraint ){
        return any().$and(constraint);
    }
    */
    
    /** Ast Class of the Expression */
    public Class<E> astExpressionClass;

    public Class<_E> domainClass;

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
        this.astExpressionClass = (Class<E>)astExpressionProto.getClass();
        this.exprStencil = Stencil.of(astExpressionProto.toString() );
    }

    private $ex(){
        this.astExpressionClass = (Class<E>)Expression.class;
        this.exprStencil = null;
    }

    private $ex(Class<E> exprClass){
        this(exprClass, null);
    }

    /**
     * 
     * @param astExpressionClass
     * @param stencil 
     */
    public $ex(Class<E> astExpressionClass, String stencil ){
        this.astExpressionClass = astExpressionClass;
        if( stencil == null ){
            this.exprStencil = null;
        } else {
            this.exprStencil = Stencil.of(stencil);
        }
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified $expr prototype
     */
    @Override
    public $E $and(Predicate<_E>constraint ){
        this.constraint = this.constraint.and(constraint);
        return ($E)this;
    }

    /**
     * Useful for omitting a group of classes, or interfaces
     *
     * <PRE>
     *     //we can omit specific classes
     *     $.literal().$not(_string.class, _null.class)
     *
     *     //note: we can use interfaces to omit categories of classes
     *     $ex.of().$not(_literal.class); //all expressions that ARE NOT literal
     *     $ex.of().$not(_withTypeArguments.class); //all expressions that ARE NOT capabile of having typeArguments
     *  </PRE>
     * @param expressionClass
     * @return
     */
    public $E $not( Class...expressionClass ){
        Set<Class> exprSet = new HashSet<>();
        Arrays.stream(expressionClass).forEach(e-> exprSet.add(e));
        Predicate<_E> pe = (_e)-> exprSet.stream().anyMatch( c -> c.isAssignableFrom(_e.getClass()));
        return $and( pe.negate() );
    }
    
    @Override
    public _E fill(Object...values){
        String str = exprStencil.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (_E)_expression.of((E) Ex.of( str));
    }

    @Override
    public $E $(String target, String $paramName) {
        this.exprStencil = this.exprStencil.$(target, $paramName);
        return ($E)this;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $ex<E, _E, $E> $(Expression astExpr, String $name){
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
    public $E $hardcode(Translator translator, Tokens tokens) {
        this.exprStencil = this.exprStencil.$hardcode(translator, tokens);
        return ($E)this;
    }

    /**
     * 
     * @param _n
     * @return 
     */
    public _E draft(_java._multiPart _n ){
        return (_E)draft(_n.tokenize());
    }

    @Override
    public _E draft(Translator t, Map<String,Object> tokens ){
        if( exprStencil == null ){
            System.out.println( "EXPRSTENICL = null");
            Object val = tokens.get(this.getClass().getSimpleName());
            if( val == null ){
                throw new _jdraftException("no stencil or override provided for drafting "+ this);
            }
            if( val instanceof Stencil ){
                return (_E)_expression.of( Ex.of(((Stencil) val).draft( t, tokens )));
            }
            Stencil st = Stencil.of( val.toString() );
            return (_E)_expression.of( Ex.of(st.draft( t, tokens )));
        }
        return (_E)_expression.of( Ex.of(exprStencil.draft( t, tokens )));
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
    public List<String> $list(){
        return this.exprStencil.$list();
    }

    @Override
    public List<String> $listNormalized(){
        return this.exprStencil.$listNormalized();
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
        if( _e == null ){
            if(this.isMatchAny()){
                return new Select(_e, new Tokens());
            }
        }
        if( astExpressionClass.isAssignableFrom(_e.ast().getClass())
                && constraint.test( (_E)_e ) ){

            if( exprStencil == null ){
                return new Select(_e, new Tokens());
            }
            //slight modification..
            if( (_e.ast() instanceof IntegerLiteralExpr
                    || _e.ast() instanceof DoubleLiteralExpr
                    || _e.ast() instanceof LongLiteralExpr)
                    && exprStencil.isFixedText()) {

                //there is an issue here the lowercase and uppercase Expressions 1.23d =/= 1.23D (which they are equivalent
                //need to handle postfixes 1.2f, 2.3d, 1000l
                //need to handle postfixes 1.2F, 2.3D, 1000L
                String st = _e.ast().toString(Print.PRINT_NO_COMMENTS);
                try{
                    if( compareNumberLiterals(exprStencil.getTextForm().getFixedText(), st) ){
                        return new Select(_e, new Tokens());
                    }
                }catch(Exception e){
                    //it might not be a number... so comparison will fail
                }
                return null;
            }
            Tokens ts = exprStencil.parse(_e.ast().toString(Print.PRINT_NO_COMMENTS) );
            if( ts != null ){
                return new Select(_e.ast(), ts);
            }
        }
        return null;
        //return select(_e.ast());
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

        if( astExpressionClass.isAssignableFrom(astExpr.getClass())) {
            return select( (_E)_expression.of(astExpr) );
        }
        /*
                && constraint.test( (_E)_expression.of(astExpr) ) ){

            if( exprStencil == null ){
                return new Select(astExpr, new Tokens());
            }
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
        */
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
        Optional<E> f = astNode.findFirst(this.astExpressionClass, s ->{
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
        if( _j instanceof _codeUnit){
            _codeUnit _c = (_codeUnit) _j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return selectFirstIn(_t.ast());
        }
        return selectFirstIn( ((_java._multiPart) _j).ast() );
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Expression that matches (or null if none found)
     */
    @Override
    public Select<E,_E> selectFirstIn(Node astNode ){
        Optional<E> f = astNode.findFirst(this.astExpressionClass, s -> this.matches(s) );
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
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return selectFirstIn(((_codeUnit) _j).astCompilationUnit(), selectConstraint);
            } else{
                return selectFirstIn(((_type) _j).ast(), selectConstraint);
            }
        }
        return selectFirstIn(((_java._multiPart) _j).ast(), selectConstraint);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    public Select<E, _E> selectFirstIn(Node astNode, Predicate<Select<E, _E>> selectConstraint){
        Optional<E> f = astNode.findFirst(this.astExpressionClass, s -> {
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
        if( _j instanceof _codeUnit){
            if( ((_codeUnit) _j).isTopLevel()){
                return listIn(((_codeUnit) _j).astCompilationUnit());
            }
                return listIn(((_type) _j).ast());
        }
        return listIn( ((_java._multiPart) _j).ast() );
    }    

    @Override
    public List<_E> listIn(Node astNode ){
        List<_E> typesList = new ArrayList<>();
        astNode.walk(this.astExpressionClass, e ->{
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
        //NOTE: changed to postorder because we want to replace the leaf nodes FIRST
        // (i.e. for situations where we have nested/child expressions that match)
        astNode.walk(Node.TreeTraversal.POSTORDER, e->{
            if( this.astExpressionClass.isAssignableFrom(e.getClass())){
                Select sel = select( (E)e);
                if( sel != null && exprMatchFn.test((_E)sel._ex)) {
                    expressionActionFn.accept( (_E)_expression.of( (E)e));
                }
            }
        });
        return astNode;
    }

    @Override
    public List<Select<E, _E>> listSelectedIn(Node astNode ){
        List<Select<E, _E>>sts = new ArrayList<>();
        astNode.walk(this.astExpressionClass, e-> {
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
        Tree.in(_j, this.astExpressionClass, e -> {
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
        Tree.in((_type)_java.type(clazz), this.astExpressionClass, e -> {
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
        astNode.walk(this.astExpressionClass, e-> {
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
        Tree.in(_j, this.astExpressionClass, e -> {
            Select s = select( e );
            if( s != null  && selectConstraint.test(s)){
                sts.add( s);
            }
        });
        return sts;
    }

    @Override
    public <N extends Node> N removeIn(N astNode, Predicate<_E> exprMatchFn){
        astNode.walk( this.astExpressionClass, e-> {
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
        Tree.in(_j, this.astExpressionClass, e-> {
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
        Tree.in(_j, this.astExpressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                Expression replaceNode = (Expression)( (  (_java._multiPart)$replaceProto.draft( sel.tokens.asTokens())).ast());
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
        Tree.in(_j, this.astExpressionClass, e-> {
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
        astNode.walk(this.astExpressionClass, e-> {
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
        Tree.in(_j, this.astExpressionClass, e-> {
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
        astNode.walk(this.astExpressionClass, e-> {
            Select sel = select( e );
            if( sel != null && selectConstraint.test(sel)){
                selectConsumer.accept( sel );
            }
        });
        return astNode;
    }

    @Override
    public String toString() {
        if( this.exprStencil == null ){
            return "$ex{ (" + this.astExpressionClass.getSimpleName() + ") }";
        }
        return "$ex{ (" + this.astExpressionClass.getSimpleName() + ") : \"" + this.exprStencil + "\" }";
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
        public $ex $hardcode(Translator translator, Tokens kvs) {
            ors.forEach( $a -> $a.$hardcode(translator, kvs));
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

        /**
         *
         * @param _e
         * @return
         */
        public $ex.Select select(_expression _e){
            $ex $a = whichMatch(_e);
            if( $a != null ){
                return $a.select(_e);
            }
            return null;
        }

        public boolean isMatchAny(){
            return false;
        }

        /**
         * Return the underlying $expr that matches the Expression or null if none of the match
         * @param ae
         * @return
         */
        public $ex whichMatch(Expression ae){
            return whichMatch(_expression.of(ae));
        }

        public $ex whichMatch( _expression _e){
            if( !this.constraint.test( _e ) ){
                return null;
            }
            Optional<$ex> orsel  = this.ors.stream().filter( $p-> $p.match(_e) ).findFirst();
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
            selectAst<T>, select_java<_T> {

        public final _T _ex;
        public final $tokens tokens;

        public Select( _T _ex, Tokens tokens){
            this._ex = _ex;
            this.tokens = $tokens.of(tokens);
        }
        public Select( T astExpr, Tokens tokens){
            this._ex = (_T)_expression.of(astExpr);
            this.tokens = $tokens.of(tokens);
        }
                
        public Select( T astExpr, $tokens tokens) {
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

        @Override
        public _T _node() {
            return this._ex;
        }
    }
}

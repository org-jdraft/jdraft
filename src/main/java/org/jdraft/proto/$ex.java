package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;

import org.jdraft.*;

import java.util.*;
import java.util.function.*;

/**
 * $proto for some {@link com.github.javaparser.ast.expr.Expression}) implementation
 *
 * @param <T> the underlying Expression TYPE (could be Expression to mean all expressions)
 */
public final class $ex<T extends Expression>
    implements $field.$part, $proto<T, $ex<T>>, $var.$part, Template<T> {

    /**
     * 
     * @param <T>
     * @param pattern
     * @return 
     */
    public static <T extends Expression> $ex<T> of(String...pattern ){
        //so.... if I JUST do a pattern, I want to se the experssion class
        // to Expression.class
        Expression expr = Ex.of(pattern);
        return ($ex<T>)new $ex<Expression>( Expression.class,
                expr.toString(Ast.PRINT_NO_COMMENTS) );
    }
    
    /**
     * 
     * @param <T>
     * @param pattern
     * @param constraint
     * @return 
     */
    public static <T extends Expression> $ex<T> of(String pattern, Predicate<T> constraint ){
        return new $ex<>( (T) Ex.of(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param <T>
     * @param protoExpr
     * @return 
     */
    public static <T extends Expression> $ex<T> of(T protoExpr ){
        return new $ex<>(protoExpr );
    }

    /**
     * 
     * @param <T>
     * @param protoExpr
     * @param constraint
     * @return 
     */
    public static <T extends Expression> $ex<T> of(T protoExpr, Predicate<T> constraint ){
        return new $ex<>(protoExpr ).$and(constraint);
    }
     
    
    /**
     * i.e. "arr[3]"
     * @param pattern
     * @return
     */
    public static $ex<ArrayAccessExpr> arrayAccessEx(String... pattern ) {
        return new $ex<>( Ex.arrayAccessEx(pattern) );
    }
    
    /**
     * i.e."arr[3]"
     * @param constraint
     * @return
     */
    public static $ex<ArrayAccessExpr> arrayAccessEx(Predicate<ArrayAccessExpr> constraint) {
        return new $ex<>( Ex.arrayAccessEx("a[0]") )
                .$(Ex.of("a[0]"), "any").$and(constraint);
    }    

    /**
     * i.e."arr[3]"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $ex<ArrayAccessExpr> arrayAccessEx(String pattern, Predicate<ArrayAccessExpr> constraint) {
        return new $ex<>( Ex.arrayAccessEx(pattern) ).$and(constraint);
    }
    
    /**
     * ANY array Access
     * i.e."arr[3]"
     * @return
     */
    public static $ex<ArrayAccessExpr> arrayAccessEx( ) {
        return new $ex<>( ArrayAccessExpr.class, "$arrayAccessExpr$");
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @return 
     */
    public static $ex<ArrayCreationExpr> arrayCreationEx(String... pattern ) {
        return new $ex<>( Ex.arrayCreationEx(pattern ) );
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param constraint
     * @return 
     */
    public static $ex<ArrayCreationExpr> arrayCreationEx(Predicate<ArrayCreationExpr> constraint ) {
        return new $ex<>( Ex.arrayCreationEx("new int[]")).$(Ex.of("new int[]"), "any").$and(constraint);
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ArrayCreationExpr> arrayCreationEx(String pattern, Predicate<ArrayCreationExpr> constraint ) {
        return new $ex<>( Ex.arrayCreationEx(pattern ) ).$and(constraint);
    }

    /**
     * i.e."new Obj[]", "new int[][]"
     * @return 
     */
    public static $ex<ArrayCreationExpr> arrayCreationEx( ) {
        return new $ex<>( ArrayCreationExpr.class, "$arrayCreationExpr$");
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @return 
     */
    public static $ex<ArrayInitializerExpr> arrayInitEx(String... pattern ) {
        return new $ex<>( Ex.arrayInitializerEx(pattern ) );
    }
    
    /**
     * 
     * @param ints
     * @return 
     */
    public static $ex<ArrayInitializerExpr> arrayInitEx(int[] ints ) {
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
    public static $ex<ArrayInitializerExpr> arrayInitEx(boolean[] bools ) {
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
    public static $ex<ArrayInitializerExpr> arrayInitEx(char[] chars ) {
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
    public static $ex<ArrayInitializerExpr> arrayInitEx(double[] doubles ) {
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
    public static $ex<ArrayInitializerExpr> arrayInitEx(float[] floats ) {
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
    public static $ex<ArrayInitializerExpr> arrayInitEx(Predicate<ArrayInitializerExpr> constraint) {
        return new $ex<>( Ex.arrayInitializerEx("{1}") ).$(Ex.of("{1}"), "any").$and(constraint);
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ArrayInitializerExpr> arrayInitEx(String pattern, Predicate<ArrayInitializerExpr> constraint) {
        return new $ex<>( Ex.arrayInitializerEx(pattern ) ).$and(constraint);
    }
    
    /**
     * Any array initializer i.e. "{1,2,3,4,5}"
     * @return 
     */
    public static $ex<ArrayInitializerExpr> arrayInitEx() {
        return new $ex( ArrayInitializerExpr.class, "$arrayInitializer$");
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @return 
     */
    public static $ex<AssignExpr> assignEx(String... pattern ) {
        return new $ex<>( Ex.assignEx(pattern ) );
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param constraint
     * @return 
     */
    public static $ex<AssignExpr> assignEx(Predicate<AssignExpr> constraint) {
        return new $ex<>( Ex.assignEx("a=1") ).$(Ex.of("a=1"), "any").$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<AssignExpr> assignEx(String pattern, Predicate<AssignExpr> constraint) {
        return new $ex<>( Ex.assignEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @return 
     */
    public static $ex<AssignExpr> assignEx( ) {
        return new $ex<>( AssignExpr.class, "$assignExpr$");
    }

    /** 
     * a || b 
     * @param pattern
     * @return 
     */
    public static $ex<BinaryExpr> binaryEx(String... pattern ) {
        return new $ex<>( Ex.binaryEx(pattern ) );
    }
    
    /**
     * a || b 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<BinaryExpr> binaryEx(String pattern, Predicate<BinaryExpr> constraint) {
        return new $ex<>( Ex.binaryEx(pattern ) ).$and(constraint);
    }    

    /**
     * a || b 
     * @param constraint
     * @return 
     */
    public static $ex<BinaryExpr> binaryEx(Predicate<BinaryExpr> constraint) {
        return new $ex<>( Ex.binaryEx("a || b" ) ).$(Ex.binaryEx("a || b"), "any").$and(constraint);
    }  
    
    /**
     * a || b    
     * @return 
     */
    public static $ex<BinaryExpr> binaryEx( ) {
        return new $ex<>( BinaryExpr.class, "$binaryExpr$");
    } 
    
    /** 
     *  i.e.true
     * @param b
     * @return  
     */
    public static $ex<BooleanLiteralExpr> of(boolean b ){
        return new $ex<>( Ex.of( b ) );
    }

    public static $ex<LiteralExpr> literalEx() {
        return new $ex(LiteralExpr.class, "$expr$");
    }

    public static $ex<LiteralExpr> literalEx(Predicate<LiteralExpr> constraint) {
        return new $ex(LiteralExpr.class, "$expr$").$and(constraint);
    }

    /**
     * 
     * @param b
     * @return 
     */
    public static $ex<BooleanLiteralExpr> booleanLiteralEx(boolean b ) {
        return new $ex( Ex.of( b ) );
    }
    
    /**
     * 
     * @param b
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr> booleanLiteralEx(boolean b, Predicate<BooleanLiteralExpr> constraint) {
        return new $ex( Ex.of( b ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * 
     * @param pattern
     * @return 
     */
    public static $ex<BooleanLiteralExpr> booleanLiteralEx(String... pattern ) {
        return new $ex( Ex.booleanLiteralEx(pattern ) );
    }

    /** 
     * "true" / "false" 
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr> booleanLiteralEx(Predicate<BooleanLiteralExpr>constraint ) {
        return new $ex( Ex.booleanLiteralEx("true") ).$("true", "any").$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<BooleanLiteralExpr> booleanLiteralEx(String pattern, Predicate<BooleanLiteralExpr>constraint ) {
        return new $ex( Ex.booleanLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @return 
     */
    public static $ex<BooleanLiteralExpr> booleanLiteralEx( ) {
        return new $ex( BooleanLiteralExpr.class, "$booleanLiteral$");
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @return 
     */
    public static $ex<CastExpr> castEx(String... pattern ) {
        return new $ex( Ex.castEx(pattern ) );
    }

    /** 
     * (String)o 
     * @param constraint
     * @return 
     */
    public static $ex<CastExpr> castEx(Predicate<CastExpr> constraint ) {
        return new $ex( CastExpr.class, "($cast$)" ).$and(constraint); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any").constraint(constraint);
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<CastExpr> castEx(String pattern, Predicate<CastExpr> constraint ) {
        return new $ex( Ex.castEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * (String)o 
     * @return 
     */
    public static $ex<CastExpr> castEx( ) {
        return new $ex( CastExpr.class, "($cast$)"); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any");
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $ex<CharLiteralExpr> of(char c ){
        return new $ex( Ex.charLiteralEx( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr> of(char c, Predicate<CharLiteralExpr> constraint){
        return new $ex( Ex.charLiteralEx( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $ex<CharLiteralExpr> charLiteralEx(char c ) {
        return new $ex( Ex.charLiteralEx( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr> charLiteralEx(char c, Predicate<CharLiteralExpr> constraint) {
        return new $ex( Ex.charLiteralEx( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @return 
     */
    public static $ex<CharLiteralExpr> charLiteralEx(String... pattern ) {
        return new $ex( Ex.charLiteralEx(pattern ) );
    }

    /** 
     * 'c' 
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr> charLiteralEx(Predicate<CharLiteralExpr> constraint) {
        return new $ex( Ex.charLiteralEx('a') ).$("'a'", "any").$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<CharLiteralExpr> charLiteralEx(String pattern, Predicate<CharLiteralExpr> constraint) {
        return new $ex( Ex.charLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * ANY char literal
     * 'c' 
     * @return 
     */
    public static $ex<CharLiteralExpr> charLiteralEx(  ) {
        return new $ex( CharLiteralExpr.class, "$charLiteral$");
    }
    
    /** 
     * i.e."String.class" 
     * @param pattern
     * @return 
     */
    public static $ex<ClassExpr> classEx(String... pattern ) {
        return new $ex( Ex.classEx(pattern ) );
    }

    /**
     * Map.class
     * @param constraint
     * @return 
     */
    public static $ex<ClassExpr> classEx(Predicate<ClassExpr> constraint) {
        return new $ex( Ex.classEx("a.class") )
            .$("a.class", "any").$and(constraint);
    }
    
    /**
     * String.class
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ClassExpr> classEx(String pattern, Predicate<ConditionalExpr> constraint) {
        return new $ex( Ex.classEx(pattern ) ).$and(constraint);
    }

    /**
     * Class expr (i.e. "String.class")
     * @return 
     */
    public static $ex<ClassExpr> classEx() {
        return new $ex( ClassExpr.class, "$classExpr$");
    }
    
    /** 
     * i.e."(a < b) ? a : b" 
     * @param pattern
     * @return 
     */
    public static $ex<ConditionalExpr> conditionalEx(String... pattern ) {
        return new $ex( Ex.conditionalEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ConditionalExpr> conditionalEx(String pattern, Predicate<ConditionalExpr> constraint) {
        return new $ex( Ex.conditionalEx(pattern ) ).$and(constraint);
    }
    
    /**
     * conditional, i.e. "(a==1) ? 1 : 2" 
     * @param constraint
     * @return 
     */
    public static $ex<ConditionalExpr> conditionalEx(Predicate<ConditionalExpr> constraint) {
        return new $ex( Ex.conditionalEx("(a==1) ? 1 : 2" ) )
                .$(Ex.conditionalEx("(a==1) ? 1 : 2"), "any")
                .$and(constraint);
    }    
    
    /**
     * Any conditional i.e. "(a==1) ? 1 : 2" 
     * @return 
     */
    public static $ex<ConditionalExpr> conditionalEx() {
        return new $ex( ConditionalExpr.class, "$conditionalExpr$");
    }    
    
    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr> of(double d ){
        return new $ex( Ex.of( d ) );
    }

    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(double d ) {
        return new $ex( Ex.doubleLiteralEx( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(double d, Predicate<DoubleLiteralExpr> constraint) {
        return new $ex( Ex.doubleLiteralEx( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(String pattern ) {
        return new $ex( Ex.doubleLiteralEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(String pattern, Predicate<DoubleLiteralExpr> constraint) {
        return new $ex( Ex.doubleLiteralEx(pattern ) ).$and(constraint);
    }
        
    /**
     * 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr> of(float d ){
        return new $ex( Ex.doubleLiteralEx( d ) );
    }

    /**
     * 
     * @param d
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(float d ) {
        return new $ex( Ex.of( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(float d, Predicate<DoubleLiteralExpr> constraint) {
        return new $ex( Ex.of( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx(Predicate<DoubleLiteralExpr> constraint) {
        return new $ex( Ex.of( 1.0d ) ).$("1.0d", "any").$and(constraint);
    }
        
    /**
     * 10.1d
     * @return 
     */
    public static $ex<DoubleLiteralExpr> doubleLiteralEx( ) {
        return new $ex( DoubleLiteralExpr.class, "$doubleLiteral$");
    }
    
    /** 
     * i.e."3.14f" 
     * @param pattern
     * @return 
     */
    public static $ex<DoubleLiteralExpr> floatLiteralEx(String... pattern ) {
        return new $ex( Ex.floatLiteralEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr> floatLiteralEx(String pattern, Predicate<DoubleLiteralExpr> constraint ) {
        return new $ex( Ex.floatLiteralEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<DoubleLiteralExpr> floatLiteralEx(Predicate<DoubleLiteralExpr> constraint ) {
        return new $ex( Ex.of(1.0f) ).$(Ex.of(1.0f), "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<DoubleLiteralExpr> floatLiteralEx( ) {
        return new $ex( DoubleLiteralExpr.class, "$floatLiteral$");
    }
    
    /** 
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @return 
     */
    public static $ex<EnclosedExpr> enclosedEx(String... pattern ) {
        return new $ex( Ex.enclosedEx(pattern ) );
    }

    /**
     * i.e.( 3 + 4 ) 
     * @param constraint
     * @return 
     */
    public static $ex<EnclosedExpr> enclosedEx(Predicate<EnclosedExpr>constraint ) {
        return new $ex( Ex.enclosedEx("(a)" ) ).$("(a)", "any").$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<EnclosedExpr> enclosedEx(String pattern, Predicate<EnclosedExpr>constraint ) {
        return new $ex( Ex.enclosedEx(pattern ) ).$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 )    
     * @return 
     */
    public static $ex<EnclosedExpr> enclosedEx( ) {
        return new $ex( EnclosedExpr.class, "$enclosedExpr$");
    }
    
    /** 
     *  i.e."person.NAME"
     * @param pattern
     * @return 
     */
    public static $ex<FieldAccessExpr> fieldAccessEx(String... pattern ) {
        return new $ex( Ex.fieldAccessEx(pattern ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<FieldAccessExpr> fieldAccessEx(Predicate<FieldAccessExpr> constraint ) {
        return new $ex( Ex.fieldAccessEx("a.B") )
                .$(Ex.fieldAccessEx("a.B"), "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<FieldAccessExpr> fieldAccessEx(String pattern, Predicate<FieldAccessExpr> constraint ) {
        return new $ex( Ex.fieldAccessEx(pattern ) ).$and(constraint);
    }
    
    /**
     * System.out
     * 
     * @return 
     */
    public static $ex<FieldAccessExpr> fieldAccessEx( ) {
        return new $ex( FieldAccessExpr.class, "$fieldAccessExpr$");
    }
    
    /** 
     * v instanceof Serializable 
     * @param pattern
     * @return 
     */
    public static $ex<InstanceOfExpr> instanceOfEx(String... pattern ) {
        return new $ex( Ex.instanceOfEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<InstanceOfExpr> instanceOfEx(Predicate<InstanceOfExpr> constraint ) {
        return new $ex( Ex.instanceOfEx( "a instanceof b" ) ).$("a instanceof b", "any")
                .$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<InstanceOfExpr> instanceOfEx(String pattern, Predicate<InstanceOfExpr> constraint ) {
        return new $ex( Ex.instanceOfEx(pattern ) ).$and(constraint);
    }

    public static $ex<InstanceOfExpr> instanceOfEx($typeRef type ){
        return instanceOfEx("$expr$ instanceof $type$", e-> type.matches( e.getType() ) );
    }

    /**
     * 
     * @return 
     */
    public static $ex<InstanceOfExpr> instanceOfEx() {
        return new $ex( InstanceOfExpr.class, "$instanceOf$" );
    }

    public static $ex<InstanceOfExpr> instanceOfEx(Class typeClass ){
        return instanceOfEx( $typeRef.of(typeClass) );
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $ex<IntegerLiteralExpr> of(int i) {
        return new $ex( Ex.intLiteralEx(i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr> of(int i, Predicate<IntegerLiteralExpr> constraint) {
        return new $ex( Ex.intLiteralEx( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr> intLiteralEx(Predicate<IntegerLiteralExpr> constraint) {
        return new $ex( Ex.intLiteralEx( 1 ) ).$("1", "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<IntegerLiteralExpr> intLiteralEx( ) {
        return new $ex( IntegerLiteralExpr.class, "$intLiteralExpr$");
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $ex<IntegerLiteralExpr> intLiteralEx(int i) {
        return new $ex( Ex.intLiteralEx( i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr> intLiteralEx(int i, Predicate<IntegerLiteralExpr> constraint) {
        return new $ex( Ex.intLiteralEx( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<IntegerLiteralExpr> intLiteralEx(String... pattern ) {
        return new $ex( IntegerLiteralExpr.class, Text.combine(pattern) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<IntegerLiteralExpr> intLiteralEx(String pattern, Predicate<IntegerLiteralExpr> constraint ) {
        return new $ex( Ex.intLiteralEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @param pattern
     * @return 
     */
    public static $ex<LambdaExpr> lambdaEx(String... pattern ) {
        return new $ex( Ex.lambdaEx(pattern ) );
    }

    /** 
     * a-> System.out.println( a )  
     * @param constraint
     * @return 
     */
    public static $ex<LambdaExpr> lambdaEx(Predicate<LambdaExpr> constraint) {
        return new $ex( Ex.lambdaEx("a-> true" ) ).$(Ex.lambdaEx("a->true"), "any").$and(constraint);
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<LambdaExpr> lambdaEx(String pattern , Predicate<LambdaExpr> constraint) {
        return new $ex( Ex.lambdaEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @return 
     */
    public static $ex<LambdaExpr> lambdaEx( ) {
        return new $ex( LambdaExpr.class, "$lambdaExpr$" );
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $ex<LongLiteralExpr> of(long l) {
        return new $ex( Ex.longLiteralEx( l ) );
    }

    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr> of(long l, Predicate<LongLiteralExpr> constraint ) {
        return new $ex( Ex.longLiteralEx( l ) ).$and(constraint);
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $ex<LongLiteralExpr> longLiteralEx(long l ) {
        return new $ex( Ex.longLiteralEx( l ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr> longLiteralEx(Predicate<LongLiteralExpr> constraint ) {
        return new $ex( Ex.longLiteralEx(1L)).$(Ex.longLiteralEx(1L), "any")
                .$and(constraint) ;
    }
    
    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr> longLiteralEx(long l, Predicate<LongLiteralExpr> constraint ) {
        return new $ex( Ex.longLiteralEx( l ) ).$and(constraint);
    }
   
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<LongLiteralExpr> longLiteralEx(String... pattern ) {
        return new $ex( Ex.longLiteralEx(pattern ) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<LongLiteralExpr> longLiteralEx(String pattern, Predicate<LongLiteralExpr> constraint ) {
        return new $ex( Ex.longLiteralEx(pattern ) ).$and(constraint);
    }

    /**
     * Any long literal
     * @return 
     */
    public static $ex<LongLiteralExpr> longLiteralEx( ) {
        return new $ex( LongLiteralExpr.class, "$longLiteralExpr$");
    }
    
    /** 
     * doMethod(t)
     * @param pattern 
     * @return  
     */
    public static $ex<MethodCallExpr> methodCallEx(String... pattern ) {
        return new $ex( Ex.methodCallEx(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<MethodCallExpr> methodCallEx(String pattern, Predicate<MethodCallExpr> constraint ) {
        return new $ex( Ex.methodCallEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<MethodCallExpr> methodCallEx(Predicate<MethodCallExpr> constraint ) {
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
    public static $ex<MethodCallExpr> methodCallEx(Consumer<? extends Object> lambdaWithMethodCallInSource ){
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
    public static $ex<MethodCallExpr> methodCallEx(BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
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
    public static $ex<MethodCallExpr> methodCallEx(Ex.TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
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
    public static $ex<MethodCallExpr> methodCallEx(Ex.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Ex.lambdaEx(ste);
        return $ex.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }
    
    /**
     * ANY method call
     * @return 
     */
    public static $ex<MethodCallExpr> methodCallEx( ) {
        return new $ex( MethodCallExpr.class, "$methodCall$");
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @return 
     */
    public static $ex<MethodReferenceExpr> methodReferenceEx(String... pattern ) {
        return new $ex( Ex.methodReferenceEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<MethodReferenceExpr> methodReferenceEx(Predicate<MethodReferenceExpr> constraint) {
        return new $ex( Ex.methodReferenceEx("A:b")).$("A:b", "any").$and(constraint);
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<MethodReferenceExpr> methodReferenceEx(String pattern, Predicate<MethodReferenceExpr>constraint ) {
        return new $ex( Ex.methodReferenceEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<MethodReferenceExpr> methodReferenceEx() {
        return new $ex( MethodReferenceExpr.class, "$methodReference$");
    }
    
    /** 
     *  i.e."null"
     * @return  
     */
    public static $ex<NullLiteralExpr> nullEx(){
        return new $ex( NullLiteralExpr.class, "$nullExpr$" );
    }

    /**
     *
     * @param nle
     * @return
     */
    public static $ex<NullLiteralExpr> nullEx(Predicate<NullLiteralExpr> nle){
        return new $ex( NullLiteralExpr.class, "$nullExpr$" ).$and(nle);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<NameExpr> nameEx(String... pattern ) {
        return new $ex( Ex.nameEx(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<NameExpr> nameEx(Predicate<NameExpr>constraint) {
        return new $ex( Ex.nameEx("name" ) ).$("name", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<NameExpr> nameEx(String pattern, Predicate<NameExpr>constraint) {
        return new $ex( Ex.nameEx(pattern ) ).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $ex<NameExpr> nameEx() {
        return new $ex( NameExpr.class, "$nameExpr$");
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @return 
     */
    public static $ex<ObjectCreationExpr> objectCreationEx(String... pattern ) {
        return new $ex( Ex.objectCreationEx(pattern ) );
    }
    
    /** 
     * "new Date()"
     * @param constraint
     * @return 
     */
    public static $ex<ObjectCreationExpr> objectCreationEx(Predicate<ObjectCreationExpr>constraint ) {
        return new $ex( Ex.objectCreationEx( "new a()" ) ).$("new a()", "any").$and(constraint);
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ObjectCreationExpr> objectCreationEx(String pattern, Predicate<ObjectCreationExpr>constraint ) {
        return new $ex( Ex.objectCreationEx(pattern ) ).$and(constraint);
    }

    /** 
     * "new Date()"
     * @return 
     */
    public static $ex<ObjectCreationExpr> objectCreationEx() {
        return new $ex( ObjectCreationExpr.class, "$objectCreationExpr$");
    }

    /**
     *
     * @param literal
     * @return
     */
    public static $ex<StringLiteralExpr> stringLiteralEx(String literal ) {
        return new $ex( Ex.stringLiteralEx(literal) );
    }
    
    /** 
     * "literal"
     * @param pattern
     * @return 
     */
    public static $ex<StringLiteralExpr> stringLiteralEx(String... pattern ) {
        return new $ex( Ex.stringLiteralEx(pattern ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<StringLiteralExpr> stringLiteralEx(Predicate<StringLiteralExpr> constraint) {
        return new $ex( Ex.stringLiteralEx( "\"a\"" ) ).$("\"a\"", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<StringLiteralExpr> stringLiteralEx(String pattern, Predicate<StringLiteralExpr> constraint) {
        return new $ex( Ex.stringLiteralEx(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $ex<StringLiteralExpr> stringLiteralEx( ) {
        return new $ex( StringLiteralExpr.class, "$stringLiteral$");
    }
    
    /**
     * ANY super expression 
     * 
     * @return 
     */
    public static $ex<SuperExpr> superEx(){
        return new $ex(SuperExpr.class, "$superExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<SuperExpr> superEx(String...pattern ){
        return new $ex(Ex.superEx(pattern));
    }
    
    /**
     * 
     * @param se
     * @return 
     */
    public static $ex<SuperExpr> superEx(Predicate<SuperExpr> se){
        return superEx().$and(se);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<SuperExpr> superEx(String pattern, Predicate<SuperExpr> constraint ){
        return new $ex(Ex.superEx(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param protoSuperExpr
     * @return 
     */
    public static $ex<SuperExpr> superEx(SuperExpr protoSuperExpr){
        return new $ex(protoSuperExpr);
    }
    
    /**
     * 
     * @param superExpr
     * @param constraint
     * @return 
     */
    public static $ex<SuperExpr> superEx(SuperExpr superExpr, Predicate<SuperExpr> constraint ){
        return new $ex(superExpr).$and(constraint);
    }

    /**
     * ANY this expression
     * @return 
     */
    public static $ex<ThisExpr> thisEx( ){
        return new $ex(ThisExpr.class, "$thisExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $ex<ThisExpr> thisEx(String... pattern){
        return new $ex(Ex.thisEx(pattern) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr> thisEx(String pattern, Predicate<ThisExpr> constraint){
        return new $ex(Ex.thisEx(pattern) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr> thisEx(Predicate<ThisExpr> constraint){
        return new $ex(Ex.thisEx() ).$and(constraint);
    }
        
    /**
     * 
     * @param protoThisExpr
     * @return 
     */
    public static $ex<ThisExpr> thisEx(ThisExpr protoThisExpr){
        return new $ex(protoThisExpr);
    }
    
    /**
     * 
     * @param protoThisExpr
     * @param constraint
     * @return 
     */
    public static $ex<ThisExpr> thisEx(ThisExpr protoThisExpr, Predicate<ThisExpr> constraint){
        return new $ex(protoThisExpr ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @return 
     */
    public static $ex<TypeExpr> typeEx(String... pattern ) {
        return new $ex( Ex.typeEx(pattern ) );
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param constraint
     * @return 
     */
    public static $ex<TypeExpr> typeEx(Predicate<TypeExpr> constraint ) {
        return new $ex( Ex.typeEx( "a" ) ).$("a", "any").$and(constraint);
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $ex<TypeExpr> typeEx(String pattern, Predicate<TypeExpr> constraint ) {
        return new $ex( Ex.typeEx(pattern ) ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @return 
     */
    public static $ex<TypeExpr> typeEx( ) {
        return new $ex( TypeExpr.class, "$typeExpr$");
    }
    
    /** 
     * i.e."!true" 
     * @param pattern
     * @return 
     */
    public static $ex<UnaryExpr> unaryEx(String... pattern ) {
        return new $ex( Ex.unaryEx(pattern ) );
    }
   
    /** 
     *  i.e."!true"
     * @param constraint 
     * @return  
     */
    public static $ex<UnaryExpr> unaryEx(Predicate<UnaryExpr>constraint) {
        return new $ex( Ex.unaryEx( "!true" ) ).$("!true", "any").$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $ex<UnaryExpr> unaryEx(String pattern, Predicate<UnaryExpr>constraint) {
        return new $ex( Ex.unaryEx(pattern ) ).$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @return  
     */
    public static $ex<UnaryExpr> unaryEx() {
        return new $ex( UnaryExpr.class, "$unaryExpr$");
    }
    
    /** 
     * "int i = 1"
     * @param pattern
     * @return  
     */
    public static $ex<VariableDeclarationExpr> varLocalEx(String... pattern ) {
        return new $ex( Ex.varLocalEx(pattern ) );
    }

    /** 
     * "int i = 1"
     * @param constraint 
     * @return  
     */
    public static $ex<VariableDeclarationExpr> varLocalEx(Predicate<VariableDeclarationExpr> constraint) {
        return new $ex( Ex.varLocalEx( "int i=1") ).$(Ex.of("int i=1"), "any").$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $ex<VariableDeclarationExpr> varLocalEx(String pattern, Predicate<VariableDeclarationExpr> constraint) {
        return new $ex( Ex.varLocalEx(pattern ) ).$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @return  
     */
    public static $ex<VariableDeclarationExpr> varLocalEx( ) {
        return new $ex( VariableDeclarationExpr.class, "$varDecl$");
    }

    /*
    public static $expr<Expression> any(){
        return of();
    }
     */
    
    /**
     * Matches ANY expression
     * @return 
     */
    public static $ex<Expression> of(){
        return new $ex( Expression.class, "$expr$");
    }
    
    /**
     * Matches ANY expression that matches the constraint
     * @param constraint
     * @return 
     */
    public static $ex<Expression> of(Predicate<Expression> constraint ){
        return of().$and(constraint);
    }
    
    /** Class of the Expression */
    public Class<T> expressionClass;
    
    /** The pattern (including potential tokens within $_$) */
    public Stencil exprStencil;
    
    /**
     * Matching Constraint this proto
     * (By default, ALWAYS matches)
     */
    public Predicate<T> constraint = (t)->true;

    /**
     * 
     * @param astExpressionProto 
     */
    public $ex(T astExpressionProto){
        this.expressionClass = (Class<T>)astExpressionProto.getClass();
        this.exprStencil = Stencil.of(astExpressionProto.toString() );
    }
   
    /**
     * 
     * @param expressionClass
     * @param stencil 
     */
    public $ex(Class<T>expressionClass, String stencil ){
        this.expressionClass = expressionClass;
        this.exprStencil = Stencil.of(stencil);
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified $expr prototype
     */
    @Override
    public $ex<T> $and(Predicate<T>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public T fill(Object...values){
        String str = exprStencil.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (T) Ex.of( str);
    }

    @Override
    public $ex<T> $(String target, String $name ) {
        this.exprStencil = this.exprStencil.$(target, $name);
        return this;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $ex<T> $(Expression astExpr, String $name){
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
    public $ex<T> hardcode$(Translator translator, Tokens tokens) {
        this.exprStencil = this.exprStencil.hardcode$(translator, tokens);
        return this;
    }

    /**
     * 
     * @param _n
     * @return 
     */
    public T draft(_node _n ){
        return (T)draft(_n.tokenize());
    }

    @Override
    public T draft(Translator t, Map<String,Object> tokens ){
        return (T) Ex.of(exprStencil.draft( t, tokens ));
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
     * @param astExpr
     * @return 
     */
    @Override
    public Select select( Expression astExpr){
        if( expressionClass.isAssignableFrom(astExpr.getClass()) 
                && constraint.test( (T)astExpr)){
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

    public Select<T> select(String...expr){
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
    public T firstIn(Node astNode, Predicate<T> exprMatchFn ){
        Optional<T> f = astNode.findFirst(this.expressionClass, s ->{
            Select sel = select(s);
            return sel != null && exprMatchFn.test( (T)sel.astExpression);
            });         
        if( f.isPresent()){
            return f.get();
        }
        return null;
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    @Override
    public Select<T> selectFirstIn( Class clazz ){
        return selectFirstIn( (_type)_java.type(clazz));
    }
    
    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param _j the _java model node
     * @return  the first Expression that matches (or null if none found)
     */
    @Override
    public Select<T> selectFirstIn( _java _j){
        if( _j instanceof _code ){
            _code _c = (_code) _j;
            if( _c.isTopLevel() ){
                return selectFirstIn(_c.astCompilationUnit());
            }
            _type _t = (_type) _j; //only possible
            return selectFirstIn(_t.ast());
        }
        return selectFirstIn( ((_node) _j).ast() );
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @return  the first Expression that matches (or null if none found)
     */
    @Override
    public Select<T> selectFirstIn( Node astNode ){
        Optional<T> f = astNode.findFirst(this.expressionClass, s -> this.matches(s) );         
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
    public Select<T> selectFirstIn( Class clazz, Predicate<Select<T>> selectConstraint ){
        return selectFirstIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param _j the _java node
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    public Select<T> selectFirstIn( _java _j, Predicate<Select<T>> selectConstraint){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return selectFirstIn(((_code) _j).astCompilationUnit(), selectConstraint);
            } else{
                return selectFirstIn(((_type) _j).ast(), selectConstraint);
            }
        }
        return selectFirstIn(((_node) _j).ast(), selectConstraint);
    }

    /**
     * Returns the first Expression that matches the pattern and constraint
     * @param astNode the node to look through
     * @param selectConstraint
     * @return  the first Expression that matches (or null if none found)
     */
    public Select<T> selectFirstIn( Node astNode,Predicate<Select<T>> selectConstraint){
        Optional<T> f = astNode.findFirst(this.expressionClass, s -> {
            Select<T> sel = select(s);
            return sel != null && selectConstraint.test(sel);
        });          
        if( f.isPresent()){
            return select(f.get());
        }
        return null;
    }

    @Override
    public List<T> listIn(_java _j){
        if( _j instanceof _code ){
            if( ((_code) _j).isTopLevel()){
                return listIn(((_code) _j).astCompilationUnit());
            }
                return listIn(((_type) _j).ast());
        }
        return listIn( ((_node) _j).ast() );
    }    

    @Override
    public List<T> listIn(Node astNode ){
        List<T> typesList = new ArrayList<>();
        astNode.walk(this.expressionClass, t->{
            if( this.matches(t) ){
                typesList.add(t);
            }
        } );
        return typesList;
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Consumer<T> expressionActionFn){
        return forEachIn(astNode, t->true, expressionActionFn);
    }
    
    @Override
    public <N extends Node> N forEachIn(N astNode, Predicate<T>exprMatchFn, Consumer<T> expressionActionFn){
        astNode.walk(this.expressionClass, e-> {
            //$args tokens = deconstruct( e );
            Select sel = select(e);
            if( sel != null && exprMatchFn.test((T)sel.astExpression)) {
                expressionActionFn.accept( e);
            }
        });
        return astNode;
    }

    @Override
    public List<Select<T>> listSelectedIn(Node astNode ){
        List<Select<T>>sts = new ArrayList<>();
        astNode.walk(this.expressionClass, e-> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }

    @Override
    public List<Select<T>> listSelectedIn(_java _j){
        List<Select<T>>sts = new ArrayList<>();
        _walk.in(_j, this.expressionClass, e -> {
            Select s = select( e );
            if( s != null ){
                sts.add( s);
            }
        });
        return sts;
    }
    
    @Override
    public List<Select<T>> listSelectedIn(Class clazz){
        List<Select<T>>sts = new ArrayList<>();
        _walk.in((_type)_java.type(clazz), this.expressionClass, e -> {
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
    public List<Select<T>> listSelectedIn(Class clazz, Predicate<Select<T>> selectConstraint ){
        return listSelectedIn((_type)_java.type(clazz), selectConstraint);
    }
    
    /**
     * 
     * @param astNode
     * @param selectConstraint
     * @return 
     */
    public List<Select<T>> listSelectedIn(Node astNode , Predicate<Select<T>> selectConstraint){
        List<Select<T>>sts = new ArrayList<>();
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
    public List<Select<T>> listSelectedIn(_java _j, Predicate<Select<T>> selectConstraint){
        List<Select<T>>sts = new ArrayList<>();
        _walk.in(_j, this.expressionClass, e -> {
            Select s = select( e );
            if( s != null  && selectConstraint.test(s)){
                sts.add( s);
            }
        });
        return sts;
    }

    @Override
    public <N extends Node> N removeIn(N astNode, Predicate<T> exprMatchFn){
        astNode.walk( this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null && exprMatchFn.test( (T) e)){
                sel.astExpression.removeForced();
            }
        });
        return astNode;
    }

     @Override
    public <N extends Node> N removeIn(N astNode ){
        return removeIn(astNode, t->true);
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
    public <_J extends _java> _J replaceIn(_J _j, Node astExprReplace ){
        _walk.in(_j, this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                sel.astExpression.replace(astExprReplace );
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
    public <_J extends _java> _J replaceIn(_J _j, String protoReplaceExpr ){
        return replaceIn(_j, $ex.of(protoReplaceExpr) );
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
    public <_J extends _java> _J replaceIn(_J _j, $ex $replaceProto){
        _walk.in(_j, this.expressionClass, e-> {
            Select sel = select( e );
            if( sel != null ){
                Expression replaceNode = (Expression) $replaceProto.draft( sel.tokens.asTokens() );
                sel.astExpression.replace( replaceNode );
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
    public <_CT extends _type> _CT forSelectedIn(Class clazz, Consumer<Select<T>> selectConsumer ){
        return (_CT)forSelectedIn((_type)_java.type(clazz), selectConsumer);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param selectConsumer
     * @return 
     */
    public <_J extends _java> _J forSelectedIn(_J _j, Consumer<Select<T>> selectConsumer ){
        _walk.in(_j, this.expressionClass, e-> {
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
    public <N extends Node> N forSelectedIn(N astNode, Consumer<Select<T>> selectConsumer ){
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
    public <_CT extends _type> _CT forSelectedIn( Class clazz, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectConsumer ){
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
    public <_J extends _java> _J forSelectedIn(_J _j, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectConsumer ){
        _walk.in(_j, this.expressionClass, e-> {
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
    public <N extends Node> N forSelectedIn(N astNode, Predicate<Select<T>> selectConstraint, Consumer<Select<T>> selectConsumer ){
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
        return "(" + this.expressionClass.getSimpleName() + ") : \"" + this.exprStencil + "\"";
    }
       
    /**
     * A Matched Selection result returned from matching a prototype $expr
     * inside of some (Ast)Node or (_java)_node
     * @param <T> expression type
     */
    public static class Select<T extends Expression> implements $proto.selected,
            selectAst<T> {
        
        public final T astExpression;
        public final $tokens tokens;

        public Select( T astExpr, Tokens tokens){
            this.astExpression = astExpr;
            this.tokens = $tokens.of(tokens);
        }
                
        public Select( T astExpr, $tokens tokens){
            this.astExpression = astExpr;
            this.tokens = tokens;
        }
        
        @Override
        public $tokens tokens(){
            return tokens;
        }
        
        @Override
        public String toString(){
            return "$expr.Select{"+ System.lineSeparator()+
                Text.indent(astExpression.toString() )+ System.lineSeparator()+
                Text.indent("$tokens : " + tokens) + System.lineSeparator()+
                "}";
        }

        @Override
        public T ast() {
            return this.astExpression;
        }
    }
}
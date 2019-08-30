package org.jdraft.proto;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.*;

import org.jdraft.*;

import java.util.*;
import java.util.function.*;

/**
 *  prototype for an  {@link com.github.javaparser.ast.expr.Expression})
 *
 * @param <T> the underlying Expression TYPE
 */
public final class $expr <T extends Expression>
    implements $field.$part, $proto<T, $expr<T>>, $var.$part, Template<T> {
    //implements Template<T>{ //}, $exprProto<T> {

    /**
     * 
     * @param <T>
     * @param pattern
     * @return 
     */
    public static <T extends Expression> $expr<T> of( String...pattern ){
        //so.... if I JUST do a pattern, I want to se the experssion class
        // to Expression.class
        Expression expr = Expr.of(pattern);
        return ($expr<T>)new $expr<Expression>( Expression.class, 
                expr.toString(Ast.PRINT_NO_COMMENTS) );
    }
    
    /**
     * 
     * @param <T>
     * @param pattern
     * @param constraint
     * @return 
     */
    public static <T extends Expression> $expr<T> of( String pattern, Predicate<T> constraint ){
        return new $expr<>( (T)Expr.of(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param <T>
     * @param protoExpr
     * @return 
     */
    public static <T extends Expression> $expr<T> of(T protoExpr ){
        return new $expr<>(protoExpr );
    }

    /**
     * 
     * @param <T>
     * @param protoExpr
     * @param constraint
     * @return 
     */
    public static <T extends Expression> $expr<T> of(T protoExpr, Predicate<T> constraint ){
        return new $expr<>(protoExpr ).$and(constraint);
    }
     
    
    /**
     * i.e. "arr[3]"
     * @param pattern
     * @return
     */
    public static $expr<ArrayAccessExpr> arrayAccess(String... pattern ) {
        return new $expr<>( Expr.arrayAccess(pattern) );
    }
    
    /**
     * i.e."arr[3]"
     * @param constraint
     * @return
     */
    public static $expr<ArrayAccessExpr> arrayAccess(Predicate<ArrayAccessExpr> constraint) {
        return new $expr<>( Expr.arrayAccess("a[0]") )
                .$(Expr.of("a[0]"), "any").$and(constraint);
    }    

    /**
     * i.e."arr[3]"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $expr<ArrayAccessExpr> arrayAccess(String pattern, Predicate<ArrayAccessExpr> constraint) {
        return new $expr<>( Expr.arrayAccess(pattern) ).$and(constraint);
    }
    
    /**
     * ANY array Access
     * i.e."arr[3]"
     * @return
     */
    public static $expr<ArrayAccessExpr> arrayAccess( ) {
        return new $expr<>( ArrayAccessExpr.class, "$arrayAccessExpr$");
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @return 
     */
    public static $expr<ArrayCreationExpr> arrayCreation( String... pattern ) {
        return new $expr<>( Expr.arrayCreation(pattern ) );
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param constraint
     * @return 
     */
    public static $expr<ArrayCreationExpr> arrayCreation(Predicate<ArrayCreationExpr> constraint ) {
        return new $expr<>( Expr.arrayCreation("new int[]")).$(Expr.of("new int[]"), "any").$and(constraint);
    }
    
    /**
     * i.e."new Obj[]", "new int[][]"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<ArrayCreationExpr> arrayCreation( String pattern, Predicate<ArrayCreationExpr> constraint ) {
        return new $expr<>( Expr.arrayCreation(pattern ) ).$and(constraint);
    }

    /**
     * i.e."new Obj[]", "new int[][]"
     * @return 
     */
    public static $expr<ArrayCreationExpr> arrayCreation( ) {
        return new $expr<>( ArrayCreationExpr.class, "$arrayCreationExpr$");
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( String... pattern ) {
        return new $expr<>( Expr.arrayInitializer(pattern ) );
    }
    
    /**
     * 
     * @param ints
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( int[] ints ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<ints.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(ints[i]);
        }
        sb.append("}");
        return arrayInitializer(sb.toString());        
    }
    
    /**
     * 
     * @param bools
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( boolean[] bools ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<bools.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(bools[i]);
        }
        sb.append("}");
        return arrayInitializer(sb.toString());        
    }
    
    /**
     * 
     * @param chars
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( char[] chars ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<chars.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append("'").append(chars[i]).append("'");
        }
        sb.append("}");
        return arrayInitializer(sb.toString());        
    }
    
    /**
     * 
     * @param doubles
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( double[] doubles ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<doubles.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(doubles[i]).append("d");
        }
        sb.append("}");
        return arrayInitializer(sb.toString());        
    }
    
    /**
     * 
     * @param floats
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( float[] floats ) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(int i=0;i<floats.length; i++){
            if( i > 0){
                sb.append(",");
            }
            sb.append(floats[i]).append("f");
        }
        sb.append("}");
        return arrayInitializer(sb.toString());        
    }

    /**
     * i.e."{1,2,3,4,5}"
     * @param constraint
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( Predicate<ArrayInitializerExpr> constraint) {
        return new $expr<>( Expr.arrayInitializer("{1}") ).$(Expr.of("{1}"), "any").$and(constraint);
    }
    
    /**
     * i.e."{1,2,3,4,5}"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer( String pattern, Predicate<ArrayInitializerExpr> constraint) {
        return new $expr<>( Expr.arrayInitializer(pattern ) ).$and(constraint);
    }
    
    /**
     * Any array initializer i.e. "{1,2,3,4,5}"
     * @return 
     */
    public static $expr<ArrayInitializerExpr> arrayInitializer() {
        return new $expr( ArrayInitializerExpr.class, "$arrayInitializer$");
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @return 
     */
    public static $expr<AssignExpr> assign( String... pattern ) {
        return new $expr<>( Expr.assign(pattern ) );
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param constraint
     * @return 
     */
    public static $expr<AssignExpr> assign(Predicate<AssignExpr> constraint) {
        return new $expr<>( Expr.assign("a=1") ).$(Expr.of("a=1"), "any").$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<AssignExpr> assign( String pattern, Predicate<AssignExpr> constraint) {
        return new $expr<>( Expr.assign(pattern ) ).$and(constraint);
    }
    
    /** 
     * i.e."a = 1", "a = 4" 
     * @return 
     */
    public static $expr<AssignExpr> assign( ) {
        return new $expr<>( AssignExpr.class, "$assignExpr$");
    }

    /** 
     * a || b 
     * @param pattern
     * @return 
     */
    public static $expr<BinaryExpr> binary( String... pattern ) {
        return new $expr<>( Expr.binary(pattern ) );
    }
    
    /**
     * a || b 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<BinaryExpr> binary( String pattern, Predicate<BinaryExpr> constraint) {
        return new $expr<>( Expr.binary(pattern ) ).$and(constraint);
    }    

    /**
     * a || b 
     * @param constraint
     * @return 
     */
    public static $expr<BinaryExpr> binary(Predicate<BinaryExpr> constraint) {
        return new $expr<>( Expr.binary("a || b" ) ).$(Expr.binary("a || b"), "any").$and(constraint);
    }  
    
    /**
     * a || b    
     * @return 
     */
    public static $expr<BinaryExpr> binary( ) {
        return new $expr<>( BinaryExpr.class, "$binaryExpr$");
    } 
    
    /** 
     *  i.e.true
     * @param b
     * @return  
     */
    public static $expr<BooleanLiteralExpr> of( boolean b ){
        return new $expr<>( Expr.of( b ) );
    }

    public static $expr<LiteralExpr> literal() {
        return new $expr(LiteralExpr.class, "$expr$");
    }

    public static $expr<LiteralExpr> literal( Predicate<LiteralExpr> constraint) {
        return new $expr(LiteralExpr.class, "$expr$").$and(constraint);
    }

    /**
     * 
     * @param b
     * @return 
     */
    public static $expr<BooleanLiteralExpr> booleanLiteral( boolean b ) {
        return new $expr( Expr.of( b ) );
    }
    
    /**
     * 
     * @param b
     * @param constraint
     * @return 
     */
    public static $expr<BooleanLiteralExpr> booleanLiteral( boolean b, Predicate<BooleanLiteralExpr> constraint) {
        return new $expr( Expr.of( b ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * 
     * @param pattern
     * @return 
     */
    public static $expr<BooleanLiteralExpr> booleanLiteral( String... pattern ) {
        return new $expr( Expr.booleanLiteral(pattern ) );
    }

    /** 
     * "true" / "false" 
     * @param constraint
     * @return 
     */
    public static $expr<BooleanLiteralExpr> booleanLiteral( Predicate<BooleanLiteralExpr>constraint ) {
        return new $expr( Expr.booleanLiteral("true") ).$("true", "any").$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<BooleanLiteralExpr> booleanLiteral( String pattern, Predicate<BooleanLiteralExpr>constraint ) {
        return new $expr( Expr.booleanLiteral(pattern ) ).$and(constraint);
    }
    
    /** 
     * "true" / "false" 
     * @return 
     */
    public static $expr<BooleanLiteralExpr> booleanLiteral( ) {
        return new $expr( BooleanLiteralExpr.class, "$booleanLiteral$");
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @return 
     */
    public static $expr<CastExpr> cast( String... pattern ) {
        return new $expr( Expr.cast(pattern ) );
    }

    /** 
     * (String)o 
     * @param constraint
     * @return 
     */
    public static $expr<CastExpr> cast( Predicate<CastExpr> constraint ) {
        return new $expr( CastExpr.class, "($cast$)" ).$and(constraint); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any").constraint(constraint);
    }
    
    /** 
     * (String)o 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<CastExpr> cast( String pattern, Predicate<CastExpr> constraint ) {
        return new $expr( Expr.cast(pattern ) ).$and(constraint);
    }
    
    /** 
     * (String)o 
     * @return 
     */
    public static $expr<CastExpr> cast( ) {
        return new $expr( CastExpr.class, "($cast$)"); //Expr.cast("(String)o")).$(Expr.of("(String)o"),"any");
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $expr<CharLiteralExpr> of( char c ){
        return new $expr( Expr.charLiteral( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $expr<CharLiteralExpr> of( char c, Predicate<CharLiteralExpr> constraint){
        return new $expr( Expr.charLiteral( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param c
     * @return 
     */
    public static $expr<CharLiteralExpr> charLiteral( char c ) {
        return new $expr( Expr.charLiteral( c ) );
    }

    /** 
     * 'c' 
     * @param c
     * @param constraint
     * @return 
     */
    public static $expr<CharLiteralExpr> charLiteral( char c, Predicate<CharLiteralExpr> constraint) {
        return new $expr( Expr.charLiteral( c ) ).$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @return 
     */
    public static $expr<CharLiteralExpr> charLiteral( String... pattern ) {
        return new $expr( Expr.charLiteral(pattern ) );
    }

    /** 
     * 'c' 
     * @param constraint
     * @return 
     */
    public static $expr<CharLiteralExpr> charLiteral( Predicate<CharLiteralExpr> constraint) {
        return new $expr( Expr.charLiteral('a') ).$("'a'", "any").$and(constraint);
    }
    
    /** 
     * 'c' 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<CharLiteralExpr> charLiteral( String pattern, Predicate<CharLiteralExpr> constraint) {
        return new $expr( Expr.charLiteral(pattern ) ).$and(constraint);
    }
    
    /** 
     * ANY char literal
     * 'c' 
     * @return 
     */
    public static $expr<CharLiteralExpr> charLiteral(  ) {
        return new $expr( CharLiteralExpr.class, "$charLiteral$");
    }
    
    /** 
     * i.e."String.class" 
     * @param pattern
     * @return 
     */
    public static $expr<ClassExpr> classExpr( String... pattern ) {
        return new $expr( Expr.classExpr(pattern ) );
    }

    /**
     * Map.class
     * @param constraint
     * @return 
     */
    public static $expr<ClassExpr> classExpr( Predicate<ClassExpr> constraint) {
        return new $expr( Expr.classExpr("a.class") )
            .$("a.class", "any").$and(constraint);
    }
    
    /**
     * String.class
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<ClassExpr> classExpr( String pattern, Predicate<ConditionalExpr> constraint) {
        return new $expr( Expr.classExpr(pattern ) ).$and(constraint);
    }

    /**
     * Class expr (i.e. "String.class")
     * @return 
     */
    public static $expr<ClassExpr> classExpr() {
        return new $expr( ClassExpr.class, "$classExpr$");
    }
    
    /** 
     * i.e."(a < b) ? a : b" 
     * @param pattern
     * @return 
     */
    public static $expr<ConditionalExpr> conditional( String... pattern ) {
        return new $expr( Expr.conditional(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<ConditionalExpr> conditional( String pattern, Predicate<ConditionalExpr> constraint) {
        return new $expr( Expr.conditional(pattern ) ).$and(constraint);
    }
    
    /**
     * conditional, i.e. "(a==1) ? 1 : 2" 
     * @param constraint
     * @return 
     */
    public static $expr<ConditionalExpr> conditional(Predicate<ConditionalExpr> constraint) {
        return new $expr( Expr.conditional("(a==1) ? 1 : 2" ) )
                .$(Expr.conditional("(a==1) ? 1 : 2"), "any")
                .$and(constraint);
    }    
    
    /**
     * Any conditional i.e. "(a==1) ? 1 : 2" 
     * @return 
     */
    public static $expr<ConditionalExpr> conditional() {
        return new $expr( ConditionalExpr.class, "$conditionalExpr$");
    }    
    
    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $expr<DoubleLiteralExpr> of( double d ){
        return new $expr( Expr.of( d ) );
    }

    /** 
     * 3.14d 
     * @param d
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( double d ) {
        return new $expr( Expr.doubleLiteral( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( double d, Predicate<DoubleLiteralExpr> constraint) {
        return new $expr( Expr.doubleLiteral( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( String pattern ) {
        return new $expr( Expr.doubleLiteral(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( String pattern, Predicate<DoubleLiteralExpr> constraint) {
        return new $expr( Expr.doubleLiteral(pattern ) ).$and(constraint);
    }
        
    /**
     * 
     * @param d
     * @return 
     */
    public static $expr<DoubleLiteralExpr> of( float d ){
        return new $expr( Expr.doubleLiteral( d ) );
    }

    /**
     * 
     * @param d
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( float d ) {
        return new $expr( Expr.of( d ) );
    }

    /**
     * 
     * @param d
     * @param constraint
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( float d, Predicate<DoubleLiteralExpr> constraint) {
        return new $expr( Expr.of( d ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( Predicate<DoubleLiteralExpr> constraint) {
        return new $expr( Expr.of( 1.0d ) ).$("1.0d", "any").$and(constraint);
    }
        
    /**
     * 10.1d
     * @return 
     */
    public static $expr<DoubleLiteralExpr> doubleLiteral( ) {
        return new $expr( DoubleLiteralExpr.class, "$doubleLiteral$");
    }
    
    /** 
     * i.e."3.14f" 
     * @param pattern
     * @return 
     */
    public static $expr<DoubleLiteralExpr> floatLiteral( String... pattern ) {
        return new $expr( Expr.floatLiteral(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<DoubleLiteralExpr> floatLiteral( String pattern, Predicate<DoubleLiteralExpr> constraint ) {
        return new $expr( Expr.floatLiteral(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<DoubleLiteralExpr> floatLiteral(Predicate<DoubleLiteralExpr> constraint ) {
        return new $expr( Expr.of(1.0f) ).$(Expr.of(1.0f), "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $expr<DoubleLiteralExpr> floatLiteral( ) {
        return new $expr( DoubleLiteralExpr.class, "$floatLiteral$");
    }
    
    /** 
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @return 
     */
    public static $expr<EnclosedExpr> enclosedExpr( String... pattern ) {
        return new $expr( Expr.enclosedExpr(pattern ) );
    }

    /**
     * i.e.( 3 + 4 ) 
     * @param constraint
     * @return 
     */
    public static $expr<EnclosedExpr> enclosedExpr(Predicate<EnclosedExpr>constraint ) {
        return new $expr( Expr.enclosedExpr("(a)" ) ).$("(a)", "any").$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 ) 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<EnclosedExpr> enclosedExpr( String pattern, Predicate<EnclosedExpr>constraint ) {
        return new $expr( Expr.enclosedExpr(pattern ) ).$and(constraint);
    }
    
    /**
     * i.e.( 3 + 4 )    
     * @return 
     */
    public static $expr<EnclosedExpr> enclosedExpr( ) {
        return new $expr( EnclosedExpr.class, "$enclosedExpr$");
    }
    
    /** 
     *  i.e."person.NAME"
     * @param pattern
     * @return 
     */
    public static $expr<FieldAccessExpr> fieldAccess(String... pattern ) {
        return new $expr( Expr.fieldAccess(pattern ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<FieldAccessExpr> fieldAccess(Predicate<FieldAccessExpr> constraint ) {
        return new $expr( Expr.fieldAccess("a.B") )
                .$(Expr.fieldAccess("a.B"), "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<FieldAccessExpr> fieldAccess(String pattern, Predicate<FieldAccessExpr> constraint ) {
        return new $expr( Expr.fieldAccess(pattern ) ).$and(constraint);
    }
    
    /**
     * System.out
     * 
     * @return 
     */
    public static $expr<FieldAccessExpr> fieldAccess( ) {
        return new $expr( FieldAccessExpr.class, "$fieldAccessExpr$");
    }
    
    /** 
     * v instanceof Serializable 
     * @param pattern
     * @return 
     */
    public static $expr<InstanceOfExpr> instanceOf(String... pattern ) {
        return new $expr( Expr.instanceOf(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<InstanceOfExpr> instanceOf(Predicate<InstanceOfExpr> constraint ) {
        return new $expr( Expr.instanceOf( "a instanceof b" ) ).$("a instanceof b", "any")
                .$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<InstanceOfExpr> instanceOf(String pattern, Predicate<InstanceOfExpr> constraint ) {
        return new $expr( Expr.instanceOf(pattern ) ).$and(constraint);
    }

    public static $expr<InstanceOfExpr> instanceOf( $typeRef type ){
        return instanceOf("$expr$ instanceof $type$", e-> type.matches( e.getType() ) );
    }

    /**
     * 
     * @return 
     */
    public static $expr<InstanceOfExpr> instanceOf() {
        return new $expr( InstanceOfExpr.class, "$instanceOf$" );
    }

    public static $expr<InstanceOfExpr> instanceOf( Class typeClass ){
        return instanceOf( $typeRef.of(typeClass) );
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $expr<IntegerLiteralExpr> of(int i) {
        return new $expr( Expr.intLiteral(i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $expr<IntegerLiteralExpr> of(int i, Predicate<IntegerLiteralExpr> constraint) {
        return new $expr( Expr.intLiteral( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<IntegerLiteralExpr> intLiteral(Predicate<IntegerLiteralExpr> constraint) {
        return new $expr( Expr.intLiteral( 1 ) ).$("1", "any").$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $expr<IntegerLiteralExpr> intLiteral( ) {
        return new $expr( IntegerLiteralExpr.class, "$intLiteralExpr$");
    }
    
    /**
     * 
     * @param i
     * @return 
     */
    public static $expr<IntegerLiteralExpr> intLiteral(int i) {
        return new $expr( Expr.intLiteral( i ) );
    }

    /**
     * 
     * @param i
     * @param constraint
     * @return 
     */
    public static $expr<IntegerLiteralExpr> intLiteral(int i, Predicate<IntegerLiteralExpr> constraint) {
        return new $expr( Expr.intLiteral( i ) ).$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $expr<IntegerLiteralExpr> intLiteral(String... pattern ) {
        return new $expr( IntegerLiteralExpr.class, Text.combine(pattern) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<IntegerLiteralExpr> intLiteral(String pattern, Predicate<IntegerLiteralExpr> constraint ) {
        return new $expr( Expr.intLiteral(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @param pattern
     * @return 
     */
    public static $expr<LambdaExpr> lambda(String... pattern ) {
        return new $expr( Expr.lambda(pattern ) );
    }

    /** 
     * a-> System.out.println( a )  
     * @param constraint
     * @return 
     */
    public static $expr<LambdaExpr> lambda(Predicate<LambdaExpr> constraint) {
        return new $expr( Expr.lambda("a-> true" ) ).$(Expr.lambda("a->true"), "any").$and(constraint);
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<LambdaExpr> lambda(String pattern , Predicate<LambdaExpr> constraint) {
        return new $expr( Expr.lambda(pattern ) ).$and(constraint);
    }
    
    /** 
     * a-> System.out.println( a )  
     * @return 
     */
    public static $expr<LambdaExpr> lambda( ) {
        return new $expr( LambdaExpr.class, "$lambdaExpr$" );
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $expr<LongLiteralExpr> of(long l) {
        return new $expr( Expr.longLiteral( l ) );
    }

    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $expr<LongLiteralExpr> of(long l, Predicate<LongLiteralExpr> constraint ) {
        return new $expr( Expr.longLiteral( l ) ).$and(constraint);
    }
    
    /**
     * 
     * @param l
     * @return 
     */
    public static $expr<LongLiteralExpr> longLiteral( long l ) {
        return new $expr( Expr.longLiteral( l ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<LongLiteralExpr> longLiteral( Predicate<LongLiteralExpr> constraint ) {
        return new $expr( Expr.longLiteral(1L)).$(Expr.longLiteral(1L), "any")
                .$and(constraint) ;
    }
    
    /**
     * 
     * @param l
     * @param constraint
     * @return 
     */
    public static $expr<LongLiteralExpr> longLiteral( long l, Predicate<LongLiteralExpr> constraint ) {
        return new $expr( Expr.longLiteral( l ) ).$and(constraint);
    }
   
    /**
     * 
     * @param pattern
     * @return 
     */
    public static $expr<LongLiteralExpr> longLiteral( String... pattern ) {
        return new $expr( Expr.longLiteral(pattern ) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<LongLiteralExpr> longLiteral( String pattern, Predicate<LongLiteralExpr> constraint ) {
        return new $expr( Expr.longLiteral(pattern ) ).$and(constraint);
    }

    /**
     * Any long literal
     * @return 
     */
    public static $expr<LongLiteralExpr> longLiteral( ) {
        return new $expr( LongLiteralExpr.class, "$longLiteralExpr$");
    }
    
    /** 
     * doMethod(t)
     * @param pattern 
     * @return  
     */
    public static $expr<MethodCallExpr> methodCall( String... pattern ) {
        return new $expr( Expr.methodCall(pattern ) );
    }

    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<MethodCallExpr> methodCall( String pattern, Predicate<MethodCallExpr> constraint ) {
        return new $expr( Expr.methodCall(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<MethodCallExpr> methodCall( Predicate<MethodCallExpr> constraint ) {
        return new $expr( Expr.methodCall("a()" )).$(Expr.of("a()"), "any").$and(constraint);
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
    public static MethodCallExpr methodCall( Expr.Command lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambda(ste);
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
    public static $expr<MethodCallExpr> methodCall( Consumer<? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambda(ste);
        return $expr.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
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
    public static $expr<MethodCallExpr> methodCall( BiConsumer<? extends Object,? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambda(ste);
        return $expr.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
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
    public static $expr<MethodCallExpr> methodCall( Expr.TriConsumer<? extends Object,? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambda(ste);
        return $expr.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
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
    public static $expr<MethodCallExpr> methodCall( Expr.QuadConsumer<? extends Object,? extends Object, ? extends Object, ? extends Object> lambdaWithMethodCallInSource ){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        LambdaExpr astLambda = Expr.lambda(ste);
        return $expr.of(astLambda.getBody().findFirst(MethodCallExpr.class).get());
    }
    
    /**
     * ANY method call
     * @return 
     */
    public static $expr<MethodCallExpr> methodCall( ) {
        return new $expr( MethodCallExpr.class, "$methodCall$");
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @return 
     */
    public static $expr<MethodReferenceExpr> methodReference( String... pattern ) {
        return new $expr( Expr.methodReference(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<MethodReferenceExpr> methodReference( Predicate<MethodReferenceExpr> constraint) {
        return new $expr( Expr.methodReference("A:b")).$("A:b", "any").$and(constraint);
    }
    
    /** 
     * i.e."String:toString" 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<MethodReferenceExpr> methodReference( String pattern, Predicate<MethodReferenceExpr>constraint ) {
        return new $expr( Expr.methodReference(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $expr<MethodReferenceExpr> methodReference() {
        return new $expr( MethodReferenceExpr.class, "$methodReference$");
    }
    
    /** 
     *  i.e."null"
     * @return  
     */
    public static $expr<NullLiteralExpr> nullExpr(){
        return new $expr( NullLiteralExpr.class, "$nullExpr$" );
    }

    /**
     *
     * @param nle
     * @return
     */
    public static $expr<NullLiteralExpr> nullExpr(Predicate<NullLiteralExpr> nle){
        return new $expr( NullLiteralExpr.class, "$nullExpr$" ).$and(nle);
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $expr<NameExpr> name( String... pattern ) {
        return new $expr( Expr.name(pattern ) );
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<NameExpr> name( Predicate<NameExpr>constraint) {
        return new $expr( Expr.name("name" ) ).$("name", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<NameExpr> name( String pattern, Predicate<NameExpr>constraint) {
        return new $expr( Expr.name(pattern ) ).$and(constraint);
    }

    /**
     * 
     * @return 
     */
    public static $expr<NameExpr> name() {
        return new $expr( NameExpr.class, "$nameExpr$");
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @return 
     */
    public static $expr<ObjectCreationExpr> objectCreation(String... pattern ) {
        return new $expr( Expr.objectCreation(pattern ) );
    }
    
    /** 
     * "new Date()"
     * @param constraint
     * @return 
     */
    public static $expr<ObjectCreationExpr> objectCreation(Predicate<ObjectCreationExpr>constraint ) {
        return new $expr( Expr.objectCreation( "new a()" ) ).$("new a()", "any").$and(constraint);
    }
    
    /** 
     * "new Date()"
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<ObjectCreationExpr> objectCreation(String pattern, Predicate<ObjectCreationExpr>constraint ) {
        return new $expr( Expr.objectCreation(pattern ) ).$and(constraint);
    }

    /** 
     * "new Date()"
     * @return 
     */
    public static $expr<ObjectCreationExpr> objectCreation() {
        return new $expr( ObjectCreationExpr.class, "$objectCreationExpr$");
    }

    /**
     *
     * @param literal
     * @return
     */
    public static $expr<StringLiteralExpr> stringLiteral( String literal ) {
        return new $expr( Expr.stringLiteral(literal) );
    }
    
    /** 
     * "literal"
     * @param pattern
     * @return 
     */
    public static $expr<StringLiteralExpr> stringLiteral( String... pattern ) {
        return new $expr( Expr.stringLiteral(pattern ) );
    }

    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<StringLiteralExpr> stringLiteral(Predicate<StringLiteralExpr> constraint) {
        return new $expr( Expr.stringLiteral( "\"a\"" ) ).$("\"a\"", "any").$and(constraint);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<StringLiteralExpr> stringLiteral( String pattern, Predicate<StringLiteralExpr> constraint) {
        return new $expr( Expr.stringLiteral(pattern ) ).$and(constraint);
    }
    
    /**
     * 
     * @return 
     */
    public static $expr<StringLiteralExpr> stringLiteral( ) {
        return new $expr( StringLiteralExpr.class, "$stringLiteral$");
    }
    
    /**
     * ANY super expression 
     * 
     * @return 
     */
    public static $expr<SuperExpr> superExpr(){
        return new $expr(SuperExpr.class, "$superExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $expr<SuperExpr> superExpr(String...pattern ){
        return new $expr(Expr.superExpr(pattern));
    }
    
    /**
     * 
     * @param se
     * @return 
     */
    public static $expr<SuperExpr> superExpr(Predicate<SuperExpr> se){
        return superExpr().$and(se);
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<SuperExpr> superExpr(String pattern, Predicate<SuperExpr> constraint ){
        return new $expr(Expr.superExpr(pattern)).$and(constraint);
    }
    
    /**
     * 
     * @param protoSuperExpr
     * @return 
     */
    public static $expr<SuperExpr> superExpr(SuperExpr protoSuperExpr){
        return new $expr(protoSuperExpr);
    }
    
    /**
     * 
     * @param superExpr
     * @param constraint
     * @return 
     */
    public static $expr<SuperExpr> superExpr(SuperExpr superExpr, Predicate<SuperExpr> constraint ){
        return new $expr(superExpr).$and(constraint);
    }

    /**
     * ANY this expression
     * @return 
     */
    public static $expr<ThisExpr> thisExpr( ){
        return new $expr(ThisExpr.class, "$thisExpr$");
    }

    /**
     * 
     * @param pattern
     * @return 
     */
    public static $expr<ThisExpr> thisExpr(String... pattern){
        return new $expr(Expr.thisExpr(pattern) );
    }
    
    /**
     * 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<ThisExpr> thisExpr(String pattern, Predicate<ThisExpr> constraint){
        return new $expr(Expr.thisExpr(pattern) ).$and(constraint);
    }
    
    /**
     * 
     * @param constraint
     * @return 
     */
    public static $expr<ThisExpr> thisExpr(Predicate<ThisExpr> constraint){
        return new $expr(Expr.thisExpr() ).$and(constraint);
    }
        
    /**
     * 
     * @param protoThisExpr
     * @return 
     */
    public static $expr<ThisExpr> thisExpr(ThisExpr protoThisExpr){
        return new $expr(protoThisExpr);
    }
    
    /**
     * 
     * @param protoThisExpr
     * @param constraint
     * @return 
     */
    public static $expr<ThisExpr> thisExpr(ThisExpr protoThisExpr, Predicate<ThisExpr> constraint){
        return new $expr(protoThisExpr ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @return 
     */
    public static $expr<TypeExpr> typeExpr(String... pattern ) {
        return new $expr( Expr.typeExpr(pattern ) );
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param constraint
     * @return 
     */
    public static $expr<TypeExpr> typeExpr(Predicate<TypeExpr> constraint ) {
        return new $expr( Expr.typeExpr( "a" ) ).$("a", "any").$and(constraint);
    }
    
    /** 
     * i.e."World" in World::greet 
     * @param pattern
     * @param constraint
     * @return 
     */
    public static $expr<TypeExpr> typeExpr(String pattern, Predicate<TypeExpr> constraint ) {
        return new $expr( Expr.typeExpr(pattern ) ).$and(constraint);
    }

    /** 
     * i.e."World" in World::greet 
     * @return 
     */
    public static $expr<TypeExpr> typeExpr( ) {
        return new $expr( TypeExpr.class, "$typeExpr$");
    }
    
    /** 
     * i.e."!true" 
     * @param pattern
     * @return 
     */
    public static $expr<UnaryExpr> unary( String... pattern ) {
        return new $expr( Expr.unary(pattern ) );
    }
   
    /** 
     *  i.e."!true"
     * @param constraint 
     * @return  
     */
    public static $expr<UnaryExpr> unary( Predicate<UnaryExpr>constraint) {
        return new $expr( Expr.unary( "!true" ) ).$("!true", "any").$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $expr<UnaryExpr> unary( String pattern, Predicate<UnaryExpr>constraint) {
        return new $expr( Expr.unary(pattern ) ).$and(constraint);
    }
    
    /** 
     *  i.e."!true"
     * @return  
     */
    public static $expr<UnaryExpr> unary() {
        return new $expr( UnaryExpr.class, "$unaryExpr$");
    }
    
    /** 
     * "int i = 1"
     * @param pattern
     * @return  
     */
    public static $expr<VariableDeclarationExpr> varLocal(String... pattern ) {
        return new $expr( Expr.varLocal(pattern ) );
    }

    /** 
     * "int i = 1"
     * @param constraint 
     * @return  
     */
    public static $expr<VariableDeclarationExpr> varLocal(Predicate<VariableDeclarationExpr> constraint) {
        return new $expr( Expr.varLocal( "int i=1") ).$(Expr.of("int i=1"), "any").$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @param pattern 
     * @param constraint 
     * @return  
     */
    public static $expr<VariableDeclarationExpr> varLocal(String pattern, Predicate<VariableDeclarationExpr> constraint) {
        return new $expr( Expr.varLocal(pattern ) ).$and(constraint);
    }
    
    /** 
     * "int i = 1"
     * @return  
     */
    public static $expr<VariableDeclarationExpr> varLocal( ) {
        return new $expr( VariableDeclarationExpr.class, "$varDecl$");
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
    public static $expr<Expression> of(){
        return new $expr( Expression.class, "$expr$");
    }
    
    /**
     * Matches ANY expression that matches the constraint
     * @param constraint
     * @return 
     */
    public static $expr<Expression> of( Predicate<Expression> constraint ){
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
    public $expr(T astExpressionProto){
        this.expressionClass = (Class<T>)astExpressionProto.getClass();
        this.exprStencil = Stencil.of(astExpressionProto.toString() );
    }
   
    /**
     * 
     * @param expressionClass
     * @param stencil 
     */
    public $expr (Class<T>expressionClass, String stencil ){
        this.expressionClass = expressionClass;
        this.exprStencil = Stencil.of(stencil);
    }

    /**
     * ADDS an additional matching constraint to the prototype
     * @param constraint a constraint to be added
     * @return the modified $expr prototype
     */
    @Override
    public $expr<T> $and(Predicate<T>constraint ){
        this.constraint = this.constraint.and(constraint);
        return this;
    }
    
    @Override
    public T fill(Object...values){
        String str = exprStencil.fill(Translator.DEFAULT_TRANSLATOR, values);
        return (T)Expr.of( str);
    }

    @Override
    public $expr<T> $(String target, String $name ) {
        this.exprStencil = this.exprStencil.$(target, $name);
        return this;
    }

    /**
     * 
     * @param astExpr
     * @param $name
     * @return 
     */
    public $expr<T> $( Expression astExpr, String $name){
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
    public $expr<T> hardcode$( Translator translator, Tokens tokens) {
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
        return (T)Expr.of(exprStencil.draft( t, tokens ));
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
        Number ex = Expr.parseNumber(expected);
        Number act = Expr.parseNumber(actual);
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
            return select(Expr.of(expr));
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
        return replaceIn(_j, $expr.of(protoReplaceExpr) );
    }
    
    /**
     * 
     * @param clazz
     * @param $replaceProto
     * @return 
     */
    public <_CT extends _type> _CT replaceIn(Class clazz, $expr $replaceProto){
        return (_CT)replaceIn((_type)_java.type(clazz), $replaceProto);
    }
    
    /**
     * 
     * @param <_J>
     * @param _j
     * @param $replaceProto
     * @return 
     */
    public <_J extends _java> _J replaceIn(_J _j, $expr $replaceProto){
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

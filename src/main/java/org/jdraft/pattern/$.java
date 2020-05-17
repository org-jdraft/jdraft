package org.jdraft.pattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.nodeTypes.NodeWithBlockStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithOptionalBlockStmt;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.*;

import org.jdraft.*;
import org.jdraft._annoExprs;
import org.jdraft._parameters;
import org.jdraft._typeParameters;
import org.jdraft.bot.$expr;
import org.jdraft.bot.$refactorBot;
import org.jdraft.bot.$stmt;
import org.jdraft.macro._remove;
import org.jdraft.macro.macro;

/**
 * $ is a short circuit API to construct "Body and control flow" types of $prototypes.
 * this includes:
 * <UL>
 *     <LI>$body</LI>
 *     <LI>$stmt</LI>
 *     <LI>$expr</LI>
 *     <LI>$comment</LI>
 *     <LI>$catchClause</LI>
 *     <LI>$case</LI>
 * </UL>
 *
 * ...this also includes duplicates "hybrid" (used in code bodies and as member properties)
 * <UL>
 *     <LI>$typeRef</LI>
 *     <LI>$id/name</LI>
 *     <LI>$var</LI>
 * </UL>
 *
 * This abstraction is a shortcut to unify all of the $prototypes in a single
 * API that is easy / convenient to use (since modern IDES will autocomplete, this
 * will help simplify getting up to speed/ using the protyotype API)
 * 
 * @author Eric
 */
public final class $ {

    public static Node.TreeTraversal PARENTS = Node.TreeTraversal.PARENTS;
    public static Node.TreeTraversal PRE_ORDER = Node.TreeTraversal.PREORDER;
    public static Node.TreeTraversal POST_ORDER = Node.TreeTraversal.POSTORDER;
    public static Node.TreeTraversal BREADTH_FIRST = Node.TreeTraversal.BREADTHFIRST;
    public static Node.TreeTraversal DIRECT_CHILDREN = Node.TreeTraversal.DIRECT_CHILDREN;

    public static Class<_expr._literal> LITERAL = _expr._literal.class;

    public static $modifiers PUBLIC = $modifiers.of("public");
    public static $modifiers PRIVATE = $modifiers.of("private");
    public static $modifiers PROTECTED = $modifiers.of("protected");

    public static $modifiers ABSTRACT = $modifiers.of("abstract");
    public static $modifiers STATIC = $modifiers.of("static");
    public static $modifiers FINAL = $modifiers.of("final");

    public static $modifiers NOT_ABSTRACT = $modifiers.not("abstract");
    public static $modifiers NOT_STATIC = $modifiers.not("static");
    public static $modifiers NOT_FINAL = $modifiers.not("final");

    public static $modifiers SYNCHRONIZED = $modifiers.of("synchronized");
    public static $modifiers TRANSIENT = $modifiers.of("transient");
    public static $modifiers VOLATILE = $modifiers.of("volatile");
    public static $modifiers NATIVE = $modifiers.of("native");
    public static $modifiers STRICT_FP = $modifiers.of("strictfp");

    /** the VOID type */
    public static $typeRef VOID = $typeRef.VOID;

    public static final UnaryExpr.Operator PLUS = UnaryExpr.Operator.PLUS; //+(val)
    public static final UnaryExpr.Operator MINUS= UnaryExpr.Operator.MINUS; //-(val)
    public static final UnaryExpr.Operator PREFIX_INCREMENT= UnaryExpr.Operator.PREFIX_INCREMENT; //++(val)
    public static final UnaryExpr.Operator PREFIX_DECREMENT= UnaryExpr.Operator.PREFIX_DECREMENT; //--(val)
    public static final UnaryExpr.Operator LOGICAL_COMPLIMENT = UnaryExpr.Operator.LOGICAL_COMPLEMENT; //!(val)
    public static final UnaryExpr.Operator BITWISE_COMPLIMENT = UnaryExpr.Operator.BITWISE_COMPLEMENT; //~(val)
    public static final UnaryExpr.Operator POSTFIX_INCREMENT= UnaryExpr.Operator.POSTFIX_INCREMENT; //(val)++
    public static final UnaryExpr.Operator POSTFIX_DECREMENT= UnaryExpr.Operator.POSTFIX_DECREMENT; //(val)--

    public static final BinaryExpr.Operator OR = BinaryExpr.Operator.OR; // a || b
    public static final BinaryExpr.Operator AND = BinaryExpr.Operator.AND; // a && b
    public static final BinaryExpr.Operator BINARY_OR = BinaryExpr.Operator.BINARY_OR; // a | b
    public static final BinaryExpr.Operator BINARY_AND = BinaryExpr.Operator.BINARY_AND; // a & b
    public static final BinaryExpr.Operator XOR = BinaryExpr.Operator.XOR; // a ^ b
    public static final BinaryExpr.Operator EQUALS = BinaryExpr.Operator.EQUALS; // a == b
    public static final BinaryExpr.Operator NOT_EQUALS = BinaryExpr.Operator.NOT_EQUALS; // a != b
    public static final BinaryExpr.Operator LESS = BinaryExpr.Operator.LESS; // a < b
    public static final BinaryExpr.Operator LESS_EQUALS = BinaryExpr.Operator.LESS_EQUALS; // a <= b
    public static final BinaryExpr.Operator GREATER = BinaryExpr.Operator.GREATER; // a > b
    public static final BinaryExpr.Operator GREATER_EQUALS = BinaryExpr.Operator.GREATER_EQUALS; // a >= b

    public static final BinaryExpr.Operator ADD = BinaryExpr.Operator.PLUS; // a + b
    public static final BinaryExpr.Operator SUBTRACT = BinaryExpr.Operator.MINUS; // a - b
    public static final BinaryExpr.Operator MULTIPLY = BinaryExpr.Operator.MULTIPLY; // a * b
    public static final BinaryExpr.Operator DIVIDE = BinaryExpr.Operator.DIVIDE; // a / b
    public static final BinaryExpr.Operator REMAINDER = BinaryExpr.Operator.REMAINDER; // a % b

    public static final BinaryExpr.Operator SHIFT_LEFT = BinaryExpr.Operator.LEFT_SHIFT; // a < b
    public static final BinaryExpr.Operator SHIFT_RIGHT = BinaryExpr.Operator.SIGNED_RIGHT_SHIFT; // a >> b
    public static final BinaryExpr.Operator SHIFT_RIGHT_UNSIGNED = BinaryExpr.Operator.UNSIGNED_RIGHT_SHIFT; // a >>> b

    /**
     *
     * @param targetPattern
     * @param refactorPattern
     * @return
     */
    public static $refactorBot refactor(String targetPattern, String refactorPattern){
        targetPattern = targetPattern.trim();
        if( targetPattern.endsWith(";") ){
            return $stmt.refactor(targetPattern, refactorPattern);
        }
        else{
            return $expr.refactor(targetPattern, refactorPattern);
        }
    }

    public static $refactorBot refactor($expr $targetPattern, $expr $refactorPattern){
        return $expr.refactor($targetPattern, $refactorPattern);
    }

    public static $refactorBot refactor($expr $targetPattern, String refactorPattern){
        return $expr.refactor($targetPattern, $expr.of(refactorPattern));
    }

    public static $refactorBot refactor($stmt $targetPattern, $stmt $refactorPattern){
        return $stmt.refactor($targetPattern, $refactorPattern);
    }

    public static $refactorBot refactor($stmt $targetPattern, String refactorPattern){
        return $stmt.refactor($targetPattern, $stmt.of(refactorPattern));
    }

    public static $or or( $pattern...patterns){
        return $or.of(patterns);
    }

    public static $body body(){
        return $body.of();
    }

    public static $body body( Predicate<_body> constraint){
        return $body.of().$and(constraint);
    }

    public static $body body(String...body){
        return $body.of(body);
    }

    public static $body body( _body _bd ){
        return $body.of(_bd);
    }

    public static $body body( _body _bd, Predicate<_body> constraint){
        return $body.of(_bd);
    }

    public static $body body( NodeWithBlockStmt astNodeWithBlock ){
        return $body.of(_body.of(astNodeWithBlock));
    }

    public static $body body( NodeWithOptionalBlockStmt astNodeWithBlock ){
        return $body.of(_body.of(astNodeWithBlock));
    }

    public static $body body(Exprs.Command commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Consumer commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(BiConsumer commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Exprs.TriConsumer commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Exprs.QuadConsumer commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object> $body body(Function<A,B> commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object> $body body(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body body(Exprs.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object>  $body body(Exprs.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body ( le );
    }

    public static $comment comment(){
        return $comment.of();
    }

    public static $comment comment( Predicate<Comment> constraint ){
        return $comment.of(constraint);
    }

    public static $comment comment( String commentPattern ){
        return $comment.of(commentPattern);
    }

    /**
     * Java Comments
     *
     * @see LineComment
     * @see BlockComment
     * @see JavadocComment
     *
     * @param commentPattern
     * @param commentPredicate
     * @return
     */
    public static $comment comment( String commentPattern, Predicate<Comment> commentPredicate){
        return $comment.of(commentPattern, commentPredicate);
    }

    /**
     * //Line Comment
     *
     * @see LineComment
     * @return
     */
    public static $comment<LineComment> lineComment(){
        return $comment.lineComment();
    }

    /**
     *  //Line Comment
     *
     * @see LineComment
     * @param contentPattern
     * @return
     */
    public static $comment<LineComment> lineComment( String contentPattern){
        return $comment.lineComment(contentPattern);
    }

    /**
     * //Line Comment
     * @see LineComment
     * @param constraint
     * @return
     */
    public static $comment<LineComment> lineComment( Predicate<LineComment> constraint ){
        return $comment.lineComment(constraint);
    }

    /**
     *  //Line Comment
     * @see LineComment
     * @param commentPattern
     * @param commentPredicate
     * @return
     */
    public static $comment<LineComment> lineComment( String commentPattern, Predicate<LineComment> commentPredicate){
        return $comment.lineComment(commentPattern, commentPredicate);
    }


    /**
     * /* block comment * /
     * @see BlockComment
     * @return
     */
    public static $comment<BlockComment> blockComment(){
        return $comment.blockComment();
    }

    /**
     *
     * @see BlockComment
     * @param contentPattern
     * @return
     */
    public static $comment<LineComment> blockComment( String contentPattern){
        return $comment.lineComment(contentPattern);
    }

    /**
     * @see BlockComment
     * @param constraint
     * @return
     */
    public static $comment<LineComment> blockComment( Predicate<LineComment> constraint ){
        return $comment.lineComment(constraint);
    }

    /**
     *
     * @see BlockComment
     * @param commentPattern
     * @param commentPredicate
     * @return
     */
    public static $comment<BlockComment> blockComment( String commentPattern, Predicate<BlockComment> commentPredicate){
        return $comment.blockComment(commentPattern, commentPredicate);
    }

    /**
     * Pattern match ANY node that has equal text
     * @param nodeText
     * @return
     */
    public static $token token(String nodeText ){
        return $token.of(nodeText);
    }

    public static $ex ex(){
        return $ex.any();
    }

    public static $ex literal(){
        return $ex.any().$and(e-> e.ast().isLiteralExpr());
    }

    public static $ex literal( String literalValue){
        return $ex.any().$and(e -> e.ast().isLiteralExpr() && e.is(literalValue));
    }

    public static $ex literal( String... literalValues){
        Set<String> vs = new HashSet<>();
        Arrays.stream(literalValues).forEach(s -> vs.add(s));
        return $ex.any().$and(e -> e.ast().isLiteralExpr() && vs.contains(e.toString()));
    }

    public static $ex ex(Predicate<_expr> constraint){
        return $ex.any().$and(constraint);
    }

    public static $ex ex(String... pattern){
        return $ex.of(pattern);
    }

    public static <T extends Expression, _E extends _expr, $E extends $ex> $ex<T, _E, $E> of(T protoExpr ){
        return $ex.of(protoExpr);
    }

    /*
    public static <T extends Expression, _E extends _expression> $ex<T, _E> of(T protoExpr, Predicate<_E> constraint){
        return ($ex<T, _E>)$ex.of(protoExpr).$and((_E)constraint);
    }
     */

    public static $ex<StringLiteralExpr, _stringExpr, $ex> of(String stringLiteral ){
        return $ex.stringLiteralEx(stringLiteral);
    }

    public static $ex<CharLiteralExpr, _charExpr, $ex> of(char c){
        return $ex.of(c);
    }

    public static $ex<IntegerLiteralExpr, _intExpr, $ex> of(int i){
        return $ex.of(i);
    }

    /**
     * Note: this can be finniky
     * @param f
     * @return
     */
    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> of(float f){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _jdraftException("unable to resolve calling class "+ ste.getClassName());
        }
        $ex.Select s = $ex.of("$.of($val$)").selectFirstIn(
                clazz, se-> {
                    try{
                        if( se.ast().getBegin().get().line == ste.getLineNumber() ){
                            Float.parseFloat( se.get("val").toString() );
                            return true;
                        }
                        return false;
                    } catch(Exception ex ){
                        return false;
                    }
                });
        return $ex.doubleLiteralEx( s.get("val").toString() );
    }

    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> of(double d){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _jdraftException("unable to resolve calling class "+ ste.getClassName());
        }
        $ex.Select s = $ex.of("$.of($val$)").selectFirstIn(
                clazz, se-> {
                    try{
                        if( se.ast().getBegin().get().line == ste.getLineNumber() ){
                            Float.parseFloat( se.get("val").toString() );
                            return true;
                        }
                        return false;
                    } catch(Exception ex ){
                        return false;
                    }
                });
        return $ex.doubleLiteralEx( s.get("val").toString() );
    }


    public static $ex<LongLiteralExpr, _longExpr, $ex> of(long l){
        return $ex.of(l);
    }

    public static $boolean of(boolean b){
        return $boolean.of(b);
    }
    /*
    public static $ex<BooleanLiteralExpr, _boolean, $ex> of(boolean b){
        return $ex.of(b);
    }
     */

    /**
     * index Expression
     * @return
     */
    public static $ex<ArrayAccessExpr, _arrayAccessExpr, $ex> arrayAccess(){
        return $ex.arrayAccessEx();
    }

    public static $ex<ArrayAccessExpr, _arrayAccessExpr, $ex> arrayAccess(String ae){
        return $ex.arrayAccessEx(ae);
    }

    public static $ex<ArrayAccessExpr, _arrayAccessExpr, $ex> arrayAccess(ArrayAccessExpr aae){
        return $ex.of(aae);
    }

    public static $ex<ArrayAccessExpr, _arrayAccessExpr, $ex> arrayAccess(Predicate<_arrayAccessExpr> aae){
        return $ex.arrayAccessEx(aae);
    }

    public static $ex<ArrayCreationExpr, _arrayCreateExpr, $ex> arrayCreation(){
        return $ex.arrayCreationEx();
    }

    public static $ex<ArrayCreationExpr, _arrayCreateExpr, $ex> arrayCreation(String ac){
        return $ex.arrayCreationEx(ac);
    }

    public static $ex<ArrayCreationExpr, _arrayCreateExpr, $ex> arrayCreation(ArrayCreationExpr ace){
        return $ex.of(ace);
    }

    public static $ex<ArrayCreationExpr, _arrayCreateExpr, $ex> arrayCreation(Predicate<_arrayCreateExpr> ace){
        return $ex.arrayCreationEx(ace);
    }

    /**
     * new int[][]{{1, 1}, {2, 2}};
     * @return
     */
    public static $ex<ArrayInitializerExpr, _arrayInitExpr, $ex> arrayInit(){
        return $ex.arrayInitEx();
    }

    public static $ex<ArrayInitializerExpr, _arrayInitExpr, $ex> arrayInit(String ae){
        return $ex.arrayInitEx(ae);
    }

    public static $ex<ArrayInitializerExpr, _arrayInitExpr, $ex> arrayInit(ArrayInitializerExpr aei){
        return $ex.of(aei);
    }

    public static $ex<ArrayInitializerExpr, _arrayInitExpr, $ex> arrayInit(Predicate<_arrayInitExpr> aei){
        return $ex.arrayInitEx(aei);
    }

    public static $ex<AssignExpr, _assignExpr, $ex> assign(){
        return $ex.assignEx();
    }

    public static $ex<AssignExpr, _assignExpr, $ex> assign(String a){
        return $ex.assignEx(a);
    }

    public static $ex<AssignExpr, _assignExpr, $ex> assign(AssignExpr ae){
        return $ex.of(ae);
    }

    public static $ex<AssignExpr, _assignExpr, $ex> assign(Predicate<_assignExpr> ae){
        return $ex.assignEx(ae);
    }

    public static $ex<BinaryExpr, _binaryExpr, $ex> binaryExpr(){
        return $ex.binaryEx();
    }

    public static $ex<BinaryExpr, _binaryExpr, $ex> binaryExpr(String be){
        return $ex.binaryEx(be);
    }

    public static $ex<BinaryExpr, _binaryExpr, $ex> binaryExpr(BinaryExpr be){
        return $ex.of(be);
    }

    public static $ex<BinaryExpr, _binaryExpr, $ex> binaryExpr(BinaryExpr.Operator bo){
        return $ex.binaryEx(be-> be.getOperator() == bo);
    }

    public static $ex<BinaryExpr, _binaryExpr, $ex> binaryExpr(BinaryExpr.Operator... ops){
        return $ex.binaryEx(b-> Arrays.stream(ops).anyMatch(o->b.getOperator() == o));
    }

    public static $ex<BinaryExpr, _binaryExpr, $ex> binaryExpr(Predicate<_binaryExpr> be){
        return $ex.binaryEx(be);
    }

    public static $ex<BooleanLiteralExpr, _booleanExpr, $ex> booleanLiteral(){
        return $ex.booleanLiteralEx();
    }

    public static $ex<BooleanLiteralExpr, _booleanExpr, $ex> booleanLiteral(boolean bool){
        return $ex.booleanLiteralEx(bool);
    }

    public static $ex<BooleanLiteralExpr, _booleanExpr, $ex> booleanLiteral(Predicate<_booleanExpr> bl){
        return $ex.booleanLiteralEx(bl);
    }

    public static $ex<CastExpr, _castExpr, $ex> cast(Class castClazz){
        return $ex.castEx("($type$)$expr$").$and(c-> Types.equal(c.ast().getType(), Types.of(castClazz) ) );
    }

    public static $ex<CastExpr, _castExpr, $ex> cast(){
        return $ex.castEx();
    }

    public static $ex<CastExpr, _castExpr, $ex> cast(String ce){
        return $ex.castEx(ce);
    }

    public static $ex<CastExpr, _castExpr, $ex> cast(CastExpr ce){
        return $ex.of(ce);
    }

    public static $ex<CastExpr, _castExpr, $ex> cast(Predicate<_castExpr> ce){
        return $ex.castEx(ce);
    }

    public static $ex<CharLiteralExpr, _charExpr, $ex> charLiteral(){
        return $ex.charLiteralEx();
    }

    public static $ex<CharLiteralExpr, _charExpr, $ex> charLiteral(char c){
        return $ex.charLiteralEx(c);
    }

    public static $ex<CharLiteralExpr, _charExpr, $ex> charLiteral(char... cs){
        return $ex.charLiteralEx(cs);
    }

    public static $ex<CharLiteralExpr, _charExpr, $ex> charLiteral(Predicate<_charExpr> pc){
        return $ex.charLiteralEx(pc);
    }

    public static $ex<ClassExpr, _classExpr, $ex> classExpr(){
        return $ex.classEx();
    }

    public static $ex<ClassExpr, _classExpr, $ex> classExpr(String ce){
        return $ex.classEx(ce);
    }

    public static $ex<ClassExpr, _classExpr, $ex> classExpr(ClassExpr ce){
        return $ex.of(ce);
    }

    public static $ex<ClassExpr, _classExpr, $ex> classExpr(Predicate<_classExpr> ce){
        return $ex.classEx(ce);
    }

    public static $ex<ConditionalExpr, _conditionalExpr, $ex> conditionalExpr(){
        return $ex.conditionalEx();
    }

    public static $ex<ConditionalExpr, _conditionalExpr, $ex> conditionalExpr(String ce){
        return $ex.conditionalEx(ce);
    }

    public static $ex<ConditionalExpr, _conditionalExpr, $ex> conditionalExpr(ConditionalExpr ce){
        return $ex.of(ce);
    }

    public static $ex<ConditionalExpr, _conditionalExpr, $ex> conditionalExpr(Predicate<_conditionalExpr> ce){
        return $ex.conditionalEx(ce);
    }



    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> doubleLiteral(){
        return $ex.doubleLiteralEx();
    }

    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> doubleLiteral(Predicate<_doubleExpr> dl){
        return $ex.doubleLiteralEx(dl);
    }

    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> doubleLiteral(String pattern){
        return $ex.doubleLiteralEx(pattern);
    }

    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> doubleLiteral(double dbl){
        return $ex.doubleLiteralEx(dbl);
    }

    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> doubleLiteral(double...doubles){
        return $ex.doubleLiteralEx(doubles);
    }

    public static $ex<DoubleLiteralExpr, _doubleExpr, $ex> doubleLiteral(float...floats){
        return $ex.doubleLiteralEx(floats);
    }

    public static $ex<EnclosedExpr, _enclosedEx, $ex> enclosedExpr(){
        return $ex.enclosedEx();
    }

    public static $ex<EnclosedExpr, _enclosedEx, $ex> enclosedExpr(String ee){
        return $ex.enclosedEx(ee);
    }

    public static $ex<EnclosedExpr, _enclosedEx, $ex> enclosedExpr(EnclosedExpr ee){
        return $ex.of(ee);
    }

    public static $ex<EnclosedExpr, _enclosedEx, $ex> enclosedExpr(Predicate<_enclosedEx> ee){
        return $ex.enclosedEx(ee);
    }

    public static $ex<FieldAccessExpr, _fieldAccessExpr, $ex> fieldAccessExpr(){
        return $ex.fieldAccessEx();
    }

    public static $ex<FieldAccessExpr, _fieldAccessExpr, $ex> fieldAccessExpr(String fae){
        return $ex.fieldAccessEx(fae);
    }

    public static $ex<FieldAccessExpr, _fieldAccessExpr, $ex> fieldAccessExpr(FieldAccessExpr fae){
        return $ex.of(fae);
    }

    public static $ex<FieldAccessExpr, _fieldAccessExpr, $ex> fieldAccessExpr(Predicate<_fieldAccessExpr> fae){
        return $ex.fieldAccessEx(fae);
    }

    public static $ex<InstanceOfExpr, _instanceOfExpr, $ex> instanceOf(){
        return $ex.instanceOfEx();
    }

    public static $ex<InstanceOfExpr, _instanceOfExpr, $ex> instanceOf(String io){
        return $ex.instanceOfEx(io);
    }

    public static $ex<InstanceOfExpr, _instanceOfExpr, $ex> instanceOf(Class instanceOfClass){
        return $ex.instanceOfEx(instanceOfClass);
    }

    public static $ex<InstanceOfExpr, _instanceOfExpr, $ex> instanceOf(InstanceOfExpr io){
        return $ex.of(io);
    }

    public static $ex<InstanceOfExpr, _instanceOfExpr, $ex> instanceOf(Predicate<_instanceOfExpr> io){
        return $ex.instanceOfEx(io);
    }

    public static $ex<IntegerLiteralExpr, _intExpr, $ex> intLiteral(){
        return $ex.intLiteralEx();
    }

    public static $ex<IntegerLiteralExpr, _intExpr, $ex> intLiteral(int...ints){
        return $ex.intLiteralEx(ints);
    }

    public static $ex<IntegerLiteralExpr, _intExpr, $ex> intLiteral(String pattern){
        return $ex.intLiteralEx(pattern);
    }

    public static $ex<IntegerLiteralExpr, _intExpr, $ex> intLiteral(Predicate<_intExpr> il){
        return $ex.intLiteralEx(il);
    }

    public static $ex<IntegerLiteralExpr, _intExpr, $ex> intLiteral(int val){
        return $ex.intLiteralEx(val);
    }

    public static $ex<LambdaExpr, _lambdaExpr, $ex> lambda(){
        return $ex.lambdaEx();
    }

    public static $ex<LambdaExpr, _lambdaExpr, $ex> lambda(String lm){
        return $ex.lambdaEx(lm);
    }

    public static $ex<LambdaExpr, _lambdaExpr, $ex> lambda(LambdaExpr le){
        return $ex.of(le);
    }

    public static $ex<LambdaExpr, _lambdaExpr, $ex> lambda(Predicate<_lambdaExpr> le){
        return $ex.lambdaEx(le);
    }

    public static $ex<LongLiteralExpr, _longExpr, $ex> longLiteral(){
        return $ex.longLiteralEx();
    }

    public static $ex<LongLiteralExpr, _longExpr, $ex> longLiteral(long lit){
        return $ex.longLiteralEx(lit);
    }

    public static $ex<LongLiteralExpr, _longExpr, $ex> longLiteral(Predicate<_longExpr> lle){
        return $ex.longLiteralEx(lle);
    }

    public static $ex<MethodCallExpr, _methodCallExpr, $ex> methodCall(){
        return $ex.methodCallEx();
    }

    public static $ex<MethodCallExpr, _methodCallExpr, $ex> methodCall(String mc){
        return $ex.methodCallEx(mc);
    }

    public static $ex<MethodCallExpr, _methodCallExpr, $ex> methodCall(MethodCallExpr mce){
        return $ex.of(mce);
    }

    public static $ex<MethodCallExpr, _methodCallExpr, $ex> methodCall(Predicate<_methodCallExpr> mce){
        return $ex.methodCallEx(mce);
    }

    public static $ex<MethodReferenceExpr, _methodRefExpr, $ex> methodReference(){
        return $ex.methodReferenceEx();
    }

    public static $ex<MethodReferenceExpr, _methodRefExpr, $ex> methodReference(String mr){
        return $ex.methodReferenceEx(mr);
    }

    public static $ex<MethodReferenceExpr, _methodRefExpr, $ex> methodReference(MethodReferenceExpr mre){
        return $ex.of(mre);
    }

    public static $ex<MethodReferenceExpr, _methodRefExpr, $ex> methodReference(Predicate<_methodRefExpr> mre){
        return $ex.methodReferenceEx(mre);
    }

    public static $ex<NameExpr, _nameExpr, $ex> nameExpr(){
        return $ex.nameEx();
    }

    public static $ex<NameExpr, _nameExpr, $ex> nameExpr(String name){
        return $ex.nameEx(name);
    }

    public static $ex<NameExpr, _nameExpr, $ex> nameExpr(NameExpr ne){
        return $ex.of(ne);
    }

    public static $ex<NameExpr, _nameExpr, $ex> nameExpr(Predicate<_nameExpr> ne){
        return $ex.nameEx(ne);
    }

    public static $ex<NullLiteralExpr, _nullExpr, $ex> nullExpr(){
        return $ex.nullEx();
    }

    public static $ex<NullLiteralExpr, _nullExpr, $ex> nullExpr(NullLiteralExpr nle){
        return $ex.of(nle);
    }

    public static $ex<NullLiteralExpr, _nullExpr, $ex> nullExpr(Predicate<_nullExpr> nle){
        return $ex.nullEx(nle);
    }

    public static $ex<ObjectCreationExpr, _newExpr, $ex> newExpr(){
        return $ex.newEx();
    }

    public static $ex<ObjectCreationExpr, _newExpr, $ex> newExpr(String oc){
        return $ex.newEx(oc);
    }

    public static $ex<ObjectCreationExpr, _newExpr, $ex> newExpr(ObjectCreationExpr oce){
        return $ex.of(oce);
    }

    public static $ex<ObjectCreationExpr, _newExpr, $ex> newExpr(Predicate<_newExpr> oce){
        return $ex.newEx(oce);
    }

    public static $ex<StringLiteralExpr, _stringExpr, $ex> stringLiteral(){
        return $ex.stringLiteralEx();
    }

    public static $ex<StringLiteralExpr, _stringExpr, $ex> stringLiteral(String lit){
        return $ex.stringLiteralEx(lit);
    }

    /*
    public static $ex<StringLiteralExpr, _string> stringLiteral(String... lits){
        return $ex.stringLiteralEx(lits);
    }

     */

    /**
     * "Yes", "No", "Maybe So"
     * @param ss
     * @return
     */
    public static $ex<StringLiteralExpr, _stringExpr, $ex> stringLiteralEx(String... ss ) {
        Set<String> sd = new HashSet<>();
        for(int i=0;i<ss.length; i++){
            sd.add( ss[i]);
        }
        return new $ex( Exprs.stringLiteralEx( ) ).$and(d-> sd.contains(d));
    }

    public static $ex<StringLiteralExpr, _stringExpr, $ex> stringLiteral(StringLiteralExpr sl){
        return $ex.of(sl);
    }

    public static $ex<StringLiteralExpr, _stringExpr, $ex> stringLiteral(Predicate<_stringExpr> sl){
        return $ex.stringLiteralEx(sl);
    }

    public static $ex<SuperExpr, _superExpr, $ex> superExpr(){
        return $ex.superEx();
    }

    public static $ex<SuperExpr, _superExpr, $ex> superExpr(String se){
        return $ex.superEx(se);
    }

    public static $ex<SuperExpr, _superExpr, $ex> superExpr(SuperExpr se){
        return $ex.superEx(se);
    }

    public static $ex<SuperExpr, _superExpr, $ex> superExpr(Predicate<_superExpr> se){
        return $ex.superEx(se);
    }

    public static $ex<SwitchExpr, _switchExpr, $ex> switchExpr(){
        return $ex.switchEx();
    }

    public static $ex<SwitchExpr, _switchExpr, $ex> switchExpr(String se){
        return $ex.switchEx(se);
    }

    public static $ex<SwitchExpr, _switchExpr, $ex> switchExpr(SwitchExpr se){
        return $ex.switchEx(se);
    }

    public static $ex<SwitchExpr, _switchExpr, $ex> switchExpr(_switchExpr se){
        return $ex.switchEx(se );
    }

    public static $ex<SwitchExpr, _switchExpr, $ex> switchExpr(Predicate<_switchExpr> se){
        return $ex.switchEx(se);
    }


    public static $ex<ThisExpr, _thisExpr, $ex> thisExpr(){
        return $ex.thisEx();
    }

    public static $ex<ThisExpr, _thisExpr, $ex> thisExpr(String se){
        return $ex.thisEx(se);
    }

    public static $ex<ThisExpr, _thisExpr, $ex> thisExpr(ThisExpr te){
        return $ex.thisEx(te);
    }

    public static $ex<ThisExpr, _thisExpr, $ex> thisExpr(Predicate<_thisExpr> te){
        return $ex.thisEx(te);
    }

    public static $ex<UnaryExpr, _unaryExpr, $ex> unary(){
        return $ex.unaryEx();
    }

    public static $ex<UnaryExpr, _unaryExpr, $ex> unary(String ue){
        return $ex.unaryEx(ue);
    }

    public static $ex<UnaryExpr, _unaryExpr, $ex> unary(UnaryExpr ue){
        return $ex.of(ue);
    }

    //a unary with any of these operators
    public static $ex<UnaryExpr, _unaryExpr, $ex> unary(UnaryExpr.Operator... ops){
        return $ex.unaryEx(u-> Arrays.stream(ops).anyMatch(o->u.getOperator() == o));
    }

    public static $ex<UnaryExpr, _unaryExpr, $ex> unary(UnaryExpr.Operator op){
        return $ex.unaryEx(u-> u.getOperator() == op);
    }

    public static $ex<UnaryExpr, _unaryExpr, $ex> unary(Predicate<_unaryExpr> ue){
        return $ex.unaryEx();
    }

    /**
     * A Declaration site for local variable(s) within a code body
     * int i
     * @return
     */
    public static $ex<VariableDeclarationExpr, _variablesExpr, $ex> localVariables(){
        return $ex.varLocalEx();
    }

    public static $ex<VariableDeclarationExpr, _variablesExpr, $ex> localVariables(String ve){
        return $ex.varLocalEx(ve);
    }

    public static $ex<VariableDeclarationExpr, _variablesExpr, $ex> localVariables(VariableDeclarationExpr vde){
        return $ex.of(vde);
    }

    public static $ex<VariableDeclarationExpr, _variablesExpr, $ex> localVariables(Predicate<_variablesExpr> vde){
        return $ex.varLocalEx(vde);
    }

    public static $switchEntry switchCase(){
        return $switchEntry.of();
    }

    public static $switchEntry switchCase(String... sc){
        return $switchEntry.of(sc);
    }

    public static $switchEntry switchCase(SwitchEntry se){
        return $switchEntry.of(se);
    }

    public static $switchEntry switchCase(Predicate<_switchEntry> se){
        return $switchEntry.of().$and(se);
    }

    public static $catch catchClause(){
        return $catch.of();
    }

    public static $catch catchClause(String cc){
        return $catch.of(cc);
    }

    public static $catch catchClause(CatchClause cc ){
        return $catch.of(cc);
    }

    public static $catch catchClause(Predicate<_catch> cc ){
        return $catch.of().$and(cc);
    }

    /*** STATEMENTS */
    public static org.jdraft.pattern.$stmt stmt(){
        return org.jdraft.pattern.$stmt.of();
    }

    public static <S extends Statement, _S extends _stmt> org.jdraft.pattern.$stmt stmt(S stmt ){
        return org.jdraft.pattern.$stmt.of(stmt);
    }

    public static org.jdraft.pattern.$stmt stmt(String...pattern){
        return org.jdraft.pattern.$stmt.of(pattern);
    }

    public static org.jdraft.pattern.$stmt assertStmt(){
        return org.jdraft.pattern.$stmt.assertStmt();
    }

    public static org.jdraft.pattern.$stmt assertStmt(Predicate<_assertStmt> matchFn){
        return assertStmt().$and(matchFn);
    }

    public static org.jdraft.pattern.$stmt assertStmt(String...as){
        return org.jdraft.pattern.$stmt.assertStmt(as);
    }

    public static org.jdraft.pattern.$stmt assertStmt(AssertStmt as){
        return org.jdraft.pattern.$stmt.of(as);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt assertStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt assertStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt assertStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt assertStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt assertStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt blockStmt(){
        return org.jdraft.pattern.$stmt.blockStmt();
    }

    public static org.jdraft.pattern.$stmt blockStmt(String... bs){
        return org.jdraft.pattern.$stmt.blockStmt(bs);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt blockStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt blockStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt blockStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt blockStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt blockStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt blockStmt(BlockStmt bs){
        return org.jdraft.pattern.$stmt.of(bs);
    }

    public static org.jdraft.pattern.$stmt breakStmt(){
        return org.jdraft.pattern.$stmt.breakStmt();
    }

    public static org.jdraft.pattern.$stmt breakStmt(Predicate<_breakStmt> matchFn){
        return org.jdraft.pattern.$stmt.breakStmt(matchFn);
    }

    public static org.jdraft.pattern.$stmt breakStmt(String...breakStmt){
        return org.jdraft.pattern.$stmt.breakStmt(breakStmt);
    }

    public static org.jdraft.pattern.$stmt breakStmt(BreakStmt bs){
        return org.jdraft.pattern.$stmt.of(bs);
    }

    public static org.jdraft.pattern.$stmt constructorCallStmt(){
        return org.jdraft.pattern.$stmt.constructorCallStmt();
    }

    public static org.jdraft.pattern.$stmt constructorCallStmt(String... se){
        return org.jdraft.pattern.$stmt.constructorCallStmt(se);
    }

    public static org.jdraft.pattern.$stmt constructorCallStmt(ExplicitConstructorInvocationStmt te){
        return org.jdraft.pattern.$stmt.constructorCallStmt(te);
    }

    public static org.jdraft.pattern.$stmt constructorCallStmt(Predicate<_constructorCallStmt> te){
        return org.jdraft.pattern.$stmt.constructorCallStmt().$and(te);
    }

    /**
     * i.e. "this(2);"
     * @return
     */
    public static org.jdraft.pattern.$stmt thisCallStmt(){
        return org.jdraft.pattern.$stmt.thisCallStmt();
    }

    /**
     * i.e. "this(2);"
     * @param ctor
     * @return
     */
    public static org.jdraft.pattern.$stmt thisCallStmt(String...ctor){
        return org.jdraft.pattern.$stmt.thisCallStmt(ctor);
    }

    /**
     * i.e. "this(2);"
     * @param cs
     * @return
     */
    public static org.jdraft.pattern.$stmt thisCallStmt(ExplicitConstructorInvocationStmt cs){
        return org.jdraft.pattern.$stmt.of(cs);
    }

    /**
     * i.e. "super(2);"
     * @return
     */
    public static org.jdraft.pattern.$stmt superCallStmt(){
        return org.jdraft.pattern.$stmt.superCallStmt();
    }

    /**
     * i.e. "super(2);"
     * @param ctor
     * @return
     */
    public static org.jdraft.pattern.$stmt superCallStmt(String...ctor){
        return org.jdraft.pattern.$stmt.superCallStmt(ctor);
    }

    /**
     * i.e. "super(2);"
     * @param cs
     * @return
     */
    public static org.jdraft.pattern.$stmt superCallStmt(ExplicitConstructorInvocationStmt cs){
        return org.jdraft.pattern.$stmt.of(cs);
    }

    public static org.jdraft.pattern.$stmt continueStmt(){
        return org.jdraft.pattern.$stmt.continueStmt();
    }

    public static org.jdraft.pattern.$stmt continueStmt(String...continueStmt){
        return org.jdraft.pattern.$stmt.continueStmt(continueStmt);
    }

    public static org.jdraft.pattern.$stmt continueStmt(ContinueStmt cs){
        return org.jdraft.pattern.$stmt.of(cs);
    }

    public static $doStmt doStmt(){
        return $doStmt.of();
    }

    public static $doStmt doStmt(String...doStmt){
        return $doStmt.of(doStmt);
    }


    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $doStmt doStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $doStmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object> $doStmt doStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $doStmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object> $doStmt doStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $doStmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $doStmt doStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $doStmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $doStmt doStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $doStmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static $doStmt doStmt(DoStmt ds){
        return $doStmt.of(ds);
    }

    public static org.jdraft.pattern.$stmt emptyStmt(){
        return org.jdraft.pattern.$stmt.emptyStmt();
    }

    public static org.jdraft.pattern.$stmt expressionStmt(){
        return org.jdraft.pattern.$stmt.expressionStmt();
    }

    public static org.jdraft.pattern.$stmt expressionStmt(String ex){
        return org.jdraft.pattern.$stmt.expressionStmt(ex);
    }

    public static org.jdraft.pattern.$stmt expressionStmt(ExpressionStmt es){
        return org.jdraft.pattern.$stmt.expressionStmt(es);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt expressionStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt expressionStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt expressionStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt expressionStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt expressionStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt forStmt(){
        return org.jdraft.pattern.$stmt.forStmt();
    }

    public static org.jdraft.pattern.$stmt forStmt(String...fs){
        return org.jdraft.pattern.$stmt.forStmt(fs);
    }

    public static org.jdraft.pattern.$stmt forStmt(ForStmt fs){
        return org.jdraft.pattern.$stmt.of(fs);
    }
    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt forStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt forStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt forStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt forStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt forStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt forEachStmt(){
        return org.jdraft.pattern.$stmt.forEachStmt();
    }

    public static org.jdraft.pattern.$stmt forEachStmt(String forEachStmt){
        return org.jdraft.pattern.$stmt.forEachStmt(forEachStmt);
    }

    public static org.jdraft.pattern.$stmt forEachStmt(ForEachStmt fes){
        return org.jdraft.pattern.$stmt.forEachStmt(fes);
    }

    /**
     * Create a prototype forEachStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt forEachStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt forEachStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt forEachStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt forEachStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt forEachStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt ifStmt(){
        return org.jdraft.pattern.$stmt.ifStmt();
    }

    public static org.jdraft.pattern.$stmt ifStmt(String is){
        return org.jdraft.pattern.$stmt.ifStmt( new String[]{is});
    }

    public static org.jdraft.pattern.$stmt ifStmt(String...is){
        return org.jdraft.pattern.$stmt.ifStmt(is);
    }

    /**
     * Create a prototype IfStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt ifStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt ifStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt ifStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt ifStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt ifStmt(Function<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object> org.jdraft.pattern.$stmt ifStmt(BiFunction<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt ifStmt(Exprs.TriFunction<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt ifStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt ifStmt(IfStmt is){
        return org.jdraft.pattern.$stmt.ifStmt(is);
    }

    public static org.jdraft.pattern.$stmt labeledStmt(){
        return org.jdraft.pattern.$stmt.labeledStmt();
    }

    public static org.jdraft.pattern.$stmt labeledStmt(String ls){
        return org.jdraft.pattern.$stmt.labeledStmt(ls);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt labeledStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt labeledStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt labeledStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt labeledStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt labeledStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt labeledStmt(LabeledStmt ls){
        return org.jdraft.pattern.$stmt.labeledStmt(ls);
    }

    public static org.jdraft.pattern.$stmt localClassStmt(){
        return org.jdraft.pattern.$stmt.localClassStmt();
    }

    public static org.jdraft.pattern.$stmt localClassStmt(String... str){
        return org.jdraft.pattern.$stmt.localClassStmt(str);
    }

    public static org.jdraft.pattern.$stmt localClassStmt(LocalClassDeclarationStmt lcds){
        return org.jdraft.pattern.$stmt.localClassStmt(lcds);
    }

    /**
     *
     * @return
     */
    public static org.jdraft.pattern.$stmt returnStmt() {
        return org.jdraft.pattern.$stmt.returnStmt();
    }

    /**
     * Builds and returns a ReturnStmt
     * @param s
     * @return
     */
    public static org.jdraft.pattern.$stmt returnStmt(Supplier<? extends Object> s){
        return org.jdraft.pattern.$stmt.returnStmt( Stmts.returnStmt(Thread.currentThread().getStackTrace()[2]) );
    }

    /**
     * Builds and returns a ReturnStmt
     * @param s
     * @return
     */
    public static org.jdraft.pattern.$stmt returnStmt(Function<? extends Object, ? extends Object> s){
        return org.jdraft.pattern.$stmt.returnStmt( Stmts.returnStmt(Thread.currentThread().getStackTrace()[2]) );
    }

    /**
     * i.e."return VALUE;"
     * @param pattern
     * @return
     */
    public static org.jdraft.pattern.$stmt returnStmt(String... pattern ) {
        return org.jdraft.pattern.$stmt.returnStmt(pattern);
    }

    /**
     * i.e."return VALUE;"
     * @param pattern
     * @param constraint
     * @return
     */
    public static org.jdraft.pattern.$stmt returnStmt(String pattern, Predicate<ReturnStmt> constraint ) {
        return org.jdraft.pattern.$stmt.returnStmt( pattern, constraint);
    }

    public static org.jdraft.pattern.$stmt switchStmt(){
        return org.jdraft.pattern.$stmt.switchStmt();
    }

    public static org.jdraft.pattern.$stmt switchStmt(Predicate<_switchStmt> matchFn){
        return org.jdraft.pattern.$stmt.switchStmt().$and(matchFn);
    }

    public static org.jdraft.pattern.$stmt switchStmt(String ... ss){
        return org.jdraft.pattern.$stmt.switchStmt(ss);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt switchStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt switchStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt switchStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt switchStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt switchStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt switchStmt(SwitchStmt ss){
        return org.jdraft.pattern.$stmt.switchStmt(ss);
    }

    public static org.jdraft.pattern.$stmt synchronizedStmt() { return org.jdraft.pattern.$stmt.synchronizedStmt(); }

    public static org.jdraft.pattern.$stmt synchronizedStmt(String ... ss){
        return org.jdraft.pattern.$stmt.synchronizedStmt(ss);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt synchronizedStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt synchronizedStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt synchronizedStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt synchronizedStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt synchronizedStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static org.jdraft.pattern.$stmt synchronizedStmt(SynchronizedStmt ss){
        return (org.jdraft.pattern.$stmt) org.jdraft.pattern.$stmt.of(ss);
    }

    public static org.jdraft.pattern.$stmt throwStmt(){
        return org.jdraft.pattern.$stmt.throwStmt();
    }

    public static org.jdraft.pattern.$stmt throwStmt(String... ts){
        return org.jdraft.pattern.$stmt.throwStmt(ts);
    }

    public static org.jdraft.pattern.$stmt throwStmt(ThrowStmt ts){
        return org.jdraft.pattern.$stmt.throwStmt(ts);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt throwStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt throwStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt throwStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt throwStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt throwStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(ThrowStmt.class).get() );
    }


    public static org.jdraft.pattern.$stmt tryStmt(){
        return org.jdraft.pattern.$stmt.tryStmt();
    }

    public static org.jdraft.pattern.$stmt tryStmt(Predicate<_tryStmt> matchFn){
        return org.jdraft.pattern.$stmt.tryStmt().$and(matchFn);
    }

    public static org.jdraft.pattern.$stmt tryStmt(String...tryStmt){
        return org.jdraft.pattern.$stmt.tryStmt(tryStmt);
    }

    public static org.jdraft.pattern.$stmt tryStmt(TryStmt ts){
        return org.jdraft.pattern.$stmt.tryStmt(ts);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt tryStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt tryStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt tryStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt tryStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt tryStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(TryStmt.class).get() );
    }


    public static org.jdraft.pattern.$stmt whileStmt(){
        return org.jdraft.pattern.$stmt.whileStmt();
    }

    public static org.jdraft.pattern.$stmt whileStmt(String...whileStmt){
        return org.jdraft.pattern.$stmt.whileStmt(whileStmt);
    }

    public static org.jdraft.pattern.$stmt whileStmt(WhileStmt ws){
        return org.jdraft.pattern.$stmt.whileStmt(ws);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static org.jdraft.pattern.$stmt whileStmt(Exprs.Command ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object> org.jdraft.pattern.$stmt whileStmt(Consumer<A> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object> org.jdraft.pattern.$stmt whileStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt whileStmt(Exprs.TriConsumer<A,B,C> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> org.jdraft.pattern.$stmt whileStmt(Exprs.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Exprs.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return org.jdraft.pattern.$stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    /** cant construct one of these */
    private $(){}

    /** Functionality */
    public static boolean hasAncestor( Node node, Predicate<Node> ancestorMatchFn){
        return Tree.hasAncestor(node, ancestorMatchFn );
    }

    /** True if the node has an ancestor */
    public static boolean hasAncestor( Node node, $pattern $p ){
        return Tree.hasAncestor(node, a -> $p.match(a) );
    }

    public static boolean hasAncestor(_java._domain _j, $pattern $p ){
        return Tree.hasAncestor( _j, Node.class, a-> $p.match(a) );
    }

    /** True if the node has an ancestor */
    public static boolean hasDescendant( Node node, $pattern $p ){
        return Tree.hasDescendant(node, a -> $p.match(a) );
    }

    public static boolean hasDescendant(_java._domain _j, $pattern $p ){
        return Tree.hasDescendant( _j, Node.class, a-> $p.match(a) );
    }

    /**
     *
     * @return
     */
    public static $node of() { return $node.of(); }

    /**
     * @param proto the node text or pattern
     * @param nodeClasses
     * @param <N>
     * @return
     */
    public static <N extends Node> $node of(String proto, Class<N>...nodeClasses){
        return $node.of(proto, nodeClasses);
    }

    public static $node of(Class nodeClass){
        return $node.of(new Class[]{nodeClass});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2){
        return $node.of( nodeClass, nodeClass2);
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2, Class<? extends Node> nodeClass3){
        return $node.of( nodeClass, nodeClass2, nodeClass3);
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2, Class<? extends Node> nodeClass3, Class<? extends Node> nodeClass4){
        return $node.of( nodeClass, nodeClass2, nodeClass3, nodeClass4);
    }

    public static $id id (){
        return $id.of();
    }

    public static $id id( String pattern ){
        return $id.of(pattern);
    }

    public static $name id( Predicate<String> idMatchFn){
        return $name.of(idMatchFn);
    }

    public static $name name (){
        return $name.of();
    }

    public static $name name( String pattern ){
        return $name.of(pattern);
    }

    public static $name name( Predicate<String> idMatchFn){
        return $name.of(idMatchFn);
    }

    /**
     *
     * @param nodeClasses
     * @param <N>
     * @return
     */
    public static <N extends Node> $node of(Class<N>...nodeClasses){
        return $node.of(nodeClasses);
    }

    /**
     *
     * @param $ps
     * @return
     */
    public static $node of( $pattern... $ps ){
        return $node.of( $ps );
    }

    public static $annoRef anno(){
        return $annoRef.of();
    }
    
    public static $annoRef anno($id name, $annoRef.$memberValue...memberValues ){
        return $annoRef.of(name, memberValues);
    }
    
    public static $annoRef anno(Predicate<_annoExpr> constraint){
        return $annoRef.of().$and(constraint);
    }
    
    public static $annoRef anno(String pattern){
        return $annoRef.of(pattern);
    }
    
    public static $annoRef anno(String pattern, Predicate<_annoExpr> constraint){
        return $annoRef.of(pattern).$and(constraint);
    }
    
    public static $annoRef anno(Class<? extends Annotation> clazz ){
        return $annoRef.of(clazz);
    }
    
    public static $annoRef anno(Class<? extends Annotation> clazz, Predicate<_annoExpr> constraint){
        return $annoRef.of(clazz).$and(constraint);
    }
    
    public static $annoRefs annos(){
        return $annoRefs.of();
    }
    
    public static $annoRefs annos(Predicate<_annoExprs> constraint ){
        return $annoRefs.of().$and(constraint);
    }
    
    public static $annoRefs annos(_annoExprs _anns ){
        return $annoRefs.of(_anns);
    }
    
    public static $annoRefs annos(_annoExprs _anns, Predicate<_annoExprs> constraint){
        return $annoRefs.of(_anns).$and(constraint);
    }
    
    public static $annoRefs annos($annoRef... annos){
        return $annoRefs.of(annos);
    }

    /**
     * @see JavadocComment
     * @return
     */
    public static $comment<JavadocComment> javadoc(){
        return $comment.javadocComment();
    }

    /**
     * @see JavadocComment
     * @param contentPattern
     * @return
     */
    public static $comment<JavadocComment> javadoc( String contentPattern){
        return $comment.javadocComment(contentPattern);
    }

    /**
     *
     * @see JavadocComment
     * @param constraint
     * @return
     */
    public static $comment<JavadocComment> javadoc( Predicate<JavadocComment> constraint ){
        return $comment.javadocComment(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @return
     */
    public static $constructor constructor(){
        return $constructor.of();
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param constraint
     * @return
     */
    public static $constructor constructor( Predicate<_constructor> constraint){
        return $constructor.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param _proto
     * @return
     */
    public static $constructor constructor( _constructor _proto ){
        return $constructor.of(_proto);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param _proto
     * @param constraint
     * @return
     */
    public static $constructor constructor( _constructor _proto, Predicate<_constructor> constraint ){
        return $constructor.of(_proto).$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param pattern
     * @return
     */
    public static $constructor constructor( String pattern){
        return $constructor.of(pattern);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param pattern
     * @param constraint
     * @return
     */
    public static $constructor constructor( String pattern, Predicate<_constructor> constraint){
        return $constructor.of(pattern).$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param pattern
     * @return
     */
    public static $constructor constructor( String...pattern){
        return $constructor.of(pattern);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param part
     * @return
     */
    public static $constructor constructor($constructor.$part part){
        return $constructor.of(part);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param parts
     * @return
     */
    public static $constructor constructor($constructor.$part...parts){
        return $constructor.of(parts);
    }

    /**
     * @see com.github.javaparser.ast.body.ConstructorDeclaration
     * @see _constructor
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static $constructor constructor(Object anonymousObjectContainingMethod){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        return $constructor.of( ste, anonymousObjectContainingMethod);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @return
     */
    public static $method method(){
        return $method.of();
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param methodPrototype
     * @return
     */
    public static $method method(String methodPrototype){
        return $method.of(methodPrototype);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param ms
     * @return
     */
    public static $method method(String...ms){
        return $method.of(ms);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param anonymousObjectContainingMethod
     * @return
     */
    public static $method method(Object anonymousObjectContainingMethod){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        ObjectCreationExpr oce = Exprs.newEx(ste);
        NodeList<BodyDeclaration<?>> bds = oce.getAnonymousClassBody().get();
        //removeIn all things that aren't METHODS or METHODS WITH @_remove
        bds.removeIf(b -> b.isAnnotationPresent(_remove.class) || (!(b instanceof MethodDeclaration)));
        //there should be only (1) method left, if > 1 take the first method
        MethodDeclaration md = (MethodDeclaration) bds.get(0);
        _method _m =  macro.to(anonymousObjectContainingMethod.getClass(), md);

        return $method.of( _m);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param constraint
     * @return
     */
    public static $method method(Predicate<_method> constraint){
        return $method.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param ms
     * @return
     */
    public static $method method(_method ms){
        return $method.of(ms);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param ms
     * @param _methodMatchFn
     * @return
     */
    public static $method method(_method ms, Predicate<_method> _methodMatchFn){
        return $method.of(ms).$and(_methodMatchFn);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param part
     * @return
     */
    public static $method method($method.$part part){
        return $method.of(part);
    }

    /**
     * @see com.github.javaparser.ast.body.MethodDeclaration
     * @see _method
     * @param parts
     * @return
     */
    public static $method method($method.$part...parts){
        return $method.of(parts);
    }

    public static $field field( $field.$part...parts ){
        return $field.of(parts);
    }

    public static $field field(){
        return $field.of();
    }

    public static $field field(String fc){
        return $field.of(fc);
    }
    
    public static $field field(Predicate<_field> constraint){
        return $field.of().$and(constraint);
    }

    public static $field field(_field f){
        return $field.of(f);
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @return
     */
    public static $import importDecl(){
        return $import.of();
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @param pattern
     * @return
     */
    public static $import importDecl(String pattern){
        return $import.of(pattern);
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @param constraint
     * @return
     */
    public static $import importDecl(Predicate<_import> constraint){
        return $import.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.ImportDeclaration
     * @see _import
     * @param imp
     * @return
     */
    public static $import importDecl(_import imp){
        return $import.of(imp);
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @return
     */
    public static $modifiers modifiers(){
        return $modifiers.of();
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param $mods
     * @return
     */
    public static $modifiers modifiers( $modifiers... $mods ){
        return $modifiers.of( $mods );
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param ms
     * @return
     */
    public static $modifiers modifiers(String...ms){
        return $modifiers.of(ms);
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param constraint
     * @return
     */
    public static $modifiers modifiers(Predicate<_modifiers> constraint){
        return $modifiers.of().$and(constraint);
    }

    /**
     * @see com.github.javaparser.ast.Modifier
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     * @see _modifiers
     * @param ms
     * @return
     */
    public static $modifiers modifiers(_modifiers ms){
        return $modifiers.of(ms);
    }

    public static $package packageDecl( ){
        return $package.of();
    }

    public static $package packageDecl(Predicate<PackageDeclaration> packageNameMatchFn ){
        return $package.of(packageNameMatchFn);
    }

    public static $package packageDecl(String pattern ){
        return $package.of(pattern);
    }

    public static $package packageDecl(String pattern, Predicate<PackageDeclaration> packageFn){
        return $package.of(pattern, packageFn);
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock( ){
        return $initBlock.of().setStatic();
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock(Predicate<_initBlock> packageNameMatchFn ){
        return $initBlock.of( packageNameMatchFn).setStatic();
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock(String pattern ){
        return $initBlock.of(pattern).setStatic();
    }

    /**
     * Static initializer block of a type
     * @return a prototype representing any static initializer block
     */
    public static $initBlock staticBlock(String... pattern ){
        return $initBlock.of(pattern).setStatic();
    }

    public static $initBlock initBlock( ){
        return $initBlock.of();
    }

    public static $initBlock initBlock(Predicate<_initBlock> packageNameMatchFn ){
        return $initBlock.of( packageNameMatchFn);
    }

    public static $initBlock initBlock(String... pattern ){
        return $initBlock.of(pattern);
    }

    public static $parameters parameters(){
        return $parameters.of();
    }

    public static $parameters parameters(String... parameters){
        return $parameters.of(parameters);
    }
    
    public static $parameters parameters(Predicate<_parameters> constraint){
        return $parameters.of().$and(constraint);
    }
    
    public static $parameter parameter(){
        return $parameter.of();
    }

    public static $parameter parameter(String p){
        return $parameter.of(p);
    }

    public static $parameter parameter( _parameter p){
        return $parameter.of(p);
    }

    public static $parameter parameter(Predicate<_parameter> constraint){
        return $parameter.of().$and(constraint);
    }
    

    public static $throws throwsDecl(){
        return $throws.of();
    }

    public static $throws throwsDecl(String...th){
        return $throws.of(th);
    }

    public static $throws throwsDecl(_throws ts){
        return $throws.of(ts);
    }

    public static $throws throwsDecl(Predicate<_throws> ts){
        return $throws.of().$and( ts);
    }
    
    public static $typeParameter typeParameter(){
        return $typeParameter.of();
    }

    public static $typeParameter typeParameter(String tp){
        return $typeParameter.of(tp);
    }

    public static $typeParameter typeParameter(_typeParameter tp){
        return $typeParameter.of(tp);
    }

    public static $typeParameter typeParameter(Predicate<_typeParameter> tp){
        return $typeParameter.of().$and(tp);
    }
    
    public static $typeParameters typeParameters(){
        return $typeParameters.of();
    }

    public static $typeParameters typeParameters(String...tps){
        return $typeParameters.of(tps);
    }

    public static $typeParameters typeParameters(_typeParameters tps){
        return $typeParameters.of(tps);
    }

    public static $typeParameters typeParameters(Predicate<_typeParameters> tps){
        return $typeParameters.of().$and(tps);
    }
    
    public static $typeRef typeRef(){
        return $typeRef.of();
    }

    public static $typeRef typeRef(String tr){
        return $typeRef.of(tr);
    }

    public static $typeRef typeRef( Class clazz){
        return $typeRef.of(clazz);
    }

    public static $typeRef typeRef(Type tr){
        return $typeRef.of(tr);
    }

    public static $typeRef typeRef(Predicate<_typeRef> tr){
        return $typeRef.of().$and(tr);
    }

    public static $var variable(){
        return $var.of();
    }

    public static $var variable(String var){
        return $var.of(var);
    }

    public static $var variable(VariableDeclarator var){
        return $var.of(var);
    }
    
    public static $var variable(Predicate<VariableDeclarator> vd){
        return $var.of().$and(vd);
    }
}

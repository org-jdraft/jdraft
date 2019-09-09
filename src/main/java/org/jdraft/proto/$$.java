package org.jdraft.proto;

import com.github.javaparser.ast.Node;
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
import org.jdraft.*;

import java.util.Arrays;
import java.util.function.*;

/**
 * $$ is a short circuit API to construct "Body and control flow" types of $prototypes.
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
 */
public final class $$ {

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

    private $$(){}

    /** Functionality */
    public static boolean hasAncestor(Node node, Predicate<Node> ancestorMatchFn){
        return Ast.hasAncestor(node, ancestorMatchFn );
    }

    /** True if the node has an ancestor */
    public static boolean hasAncestor( Node node, $proto $p ){
        return Ast.hasAncestor(node, a -> $p.match(a) );
    }

    public static boolean hasAncestor( _java _j, $proto $p ){
        return _java.hasAncestor( _j, Node.class, a-> $p.match(a) );
    }

    /** True if the node has an ancestor */
    public static boolean hasDescendant( Node node, $proto $p ){
        return Ast.hasDescendant(node, a -> $p.match(a) );
    }

    public static boolean hasDescendant( _java _j, $proto $p ){
        return _java.hasDescendant( _j, Node.class, a-> $p.match(a) );
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

    public static $node of(Class<? extends Node> nodeClass){
        return $node.of(new Class[]{nodeClass});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2){
        return $node.of(new Class[]{nodeClass, nodeClass2});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2, Class<? extends Node> nodeClass3){
        return $node.of(new Class[]{nodeClass, nodeClass2, nodeClass3});
    }

    public static $node of(Class<? extends Node> nodeClass, Class<? extends Node> nodeClass2, Class<? extends Node> nodeClass3, Class<? extends Node> nodeClass4){
        return $node.of(new Class[]{nodeClass, nodeClass2, nodeClass3, nodeClass4});
    }

    public static $id id (){
        return $id.of();
    }

    public static $id id( String pattern ){
        return $id.of(pattern);
    }

    public static $id id( Predicate<String> idMatchFn){
        return $id.of(idMatchFn);
    }

    public static $id name (){
        return $id.of();
    }

    public static $id name( String pattern ){
        return $id.of(pattern);
    }

    public static $id name( Predicate<String> idMatchFn){
        return $id.of(idMatchFn);
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
    public static $node of( $proto ... $ps ){
        return $node.of( $ps );
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

    public static $body body(Ex.Command commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Consumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(BiConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Ex.TriConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Ex.QuadConsumer commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object> $body body(Function<A,B> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object> $body body(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body body(Ex.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object>  $body body(Ex.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
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

    public static $ex expr(){
        return $ex.of();
    }

    public static $ex literal(){
        return $ex.of().$and(e-> e.isLiteralExpr());
    }

    public static $ex expr(Predicate<Expression> constraint){
        return $ex.of().$and(constraint);
    }

    public static $ex expr(String... pattern){
        return $ex.of(pattern);
    }

    public static <T extends Expression> $ex<T> of(T protoExpr ){
        return $ex.of(protoExpr);
    }

    public static <T extends Expression> $ex<T> of(T protoExpr, Predicate<T> constraint){
        return $ex.of(protoExpr).$and(constraint);
    }

    public static $ex<StringLiteralExpr> of(String stringLiteral ){
        return $ex.stringLiteralEx(stringLiteral);
    }

    public static $ex<CharLiteralExpr> of(char c){
        return $ex.of(c);
    }

    public static $ex<IntegerLiteralExpr> of(int i){
        return $ex.of(i);
    }

    /**
     * Note: this can be finniky
     * @param f
     * @return
     */
    public static $ex<DoubleLiteralExpr> of(float f){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _draftException("unable to resolve calling class "+ ste.getClassName());
        }
        $ex.Select s = $ex.of("$$.of($val$)").selectFirstIn(
                clazz, se-> {
                    try{
                        if( se.astExpression.getBegin().get().line == ste.getLineNumber() ){
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

    public static $ex<DoubleLiteralExpr> of(double d){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _draftException("unable to resolve calling class "+ ste.getClassName());
        }
        $ex.Select s = $ex.of("$$.of($val$)").selectFirstIn(
                clazz, se-> {
                    try{
                        if( se.astExpression.getBegin().get().line == ste.getLineNumber() ){
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


    public static $ex<LongLiteralExpr> of(long l){
        return $ex.of(l);
    }

    public static $ex<BooleanLiteralExpr> of(boolean b){
        return $ex.of(b);
    }

    /**
     * index Expression
     * @return
     */
    public static $ex<ArrayAccessExpr> arrayAccess(){
        return $ex.arrayAccessEx();
    }

    public static $ex<ArrayAccessExpr> arrayAccess(String ae){
        return $ex.arrayAccessEx(ae);
    }

    public static $ex<ArrayAccessExpr> arrayAccess(ArrayAccessExpr aae){
        return $ex.of(aae);
    }

    public static $ex<ArrayAccessExpr> arrayAccess(Predicate<ArrayAccessExpr> aae){
        return $ex.arrayAccessEx(aae);
    }

    public static $ex<ArrayCreationExpr> arrayCreation(){
        return $ex.arrayCreationEx();
    }

    public static $ex<ArrayCreationExpr> arrayCreation(String ac){
        return $ex.arrayCreationEx(ac);
    }

    public static $ex<ArrayCreationExpr> arrayCreation(ArrayCreationExpr ace){
        return $ex.of(ace);
    }

    public static $ex<ArrayCreationExpr> arrayCreation(Predicate<ArrayCreationExpr> ace){
        return $ex.arrayCreationEx(ace);
    }

    /**
     * new int[][]{{1, 1}, {2, 2}};
     * @return
     */
    public static $ex<ArrayInitializerExpr> arrayInit(){
        return $ex.arrayInitEx();
    }

    public static $ex<ArrayInitializerExpr> arrayInit(String ae){
        return $ex.arrayInitEx(ae);
    }

    public static $ex<ArrayInitializerExpr> arrayInit(ArrayInitializerExpr aei){
        return $ex.of(aei);
    }

    public static $ex<ArrayInitializerExpr> arrayInit(Predicate<ArrayInitializerExpr> aei){
        return $ex.arrayInitEx(aei);
    }

    public static $ex<AssignExpr> assign(){
        return $ex.assignEx();
    }

    public static $ex<AssignExpr> assign(String a){
        return $ex.assignEx(a);
    }

    public static $ex<AssignExpr> assign(AssignExpr ae){
        return $ex.of(ae);
    }

    public static $ex<AssignExpr> assign(Predicate<AssignExpr> ae){
        return $ex.assignEx(ae);
    }

    public static $ex<BinaryExpr> binaryExpr(){
        return $ex.binaryEx();
    }

    public static $ex<BinaryExpr> binaryExpr(String be){
        return $ex.binaryEx(be);
    }

    public static $ex<BinaryExpr> binaryExpr(BinaryExpr be){
        return $ex.of(be);
    }

    public static $ex<BinaryExpr> binaryExpr(BinaryExpr.Operator bo){
        return $ex.binaryEx(be-> be.getOperator() == bo);
    }

    public static $ex<BinaryExpr> binaryExpr(BinaryExpr.Operator... ops){
        return $ex.binaryEx(b-> Arrays.stream(ops).anyMatch(o->b.getOperator() == o));
    }

    public static $ex<BinaryExpr> binaryExpr(Predicate<BinaryExpr> be){
        return $ex.binaryEx(be);
    }

    public static $ex<BooleanLiteralExpr> booleanLiteral(){
        return $ex.booleanLiteralEx();
    }

    public static $ex<BooleanLiteralExpr> booleanLiteral(boolean bool){
        return $ex.booleanLiteralEx(bool);
    }

    public static $ex<BooleanLiteralExpr> booleanLiteral(Predicate<BooleanLiteralExpr> bl){
        return $ex.booleanLiteralEx(bl);
    }

    public static $ex<CastExpr> cast(Class castClazz){
        return $ex.castEx("($type$)$expr$").$and(c-> Ast.typesEqual(c.getType(), Ast.typeRef(castClazz) ) );
    }

    public static $ex<CastExpr> cast(){
        return $ex.castEx();
    }

    public static $ex<CastExpr> cast(String ce){
        return $ex.castEx(ce);
    }

    public static $ex<CastExpr> cast(CastExpr ce){
        return $ex.of(ce);
    }

    public static $ex<CastExpr> cast(Predicate<CastExpr> ce){
        return $ex.castEx(ce);
    }

    public static $ex<CharLiteralExpr> charLiteral(){
        return $ex.charLiteralEx();
    }

    public static $ex<CharLiteralExpr> charLiteral(char c){
        return $ex.charLiteralEx(c);
    }

    public static $ex<CharLiteralExpr> charLiteral(Predicate<CharLiteralExpr> pc){
        return $ex.charLiteralEx(pc);
    }

    public static $ex<ClassExpr> classExpr(){
        return $ex.classEx();
    }

    public static $ex<ClassExpr> classExpr(String ce){
        return $ex.classEx(ce);
    }

    public static $ex<ClassExpr> classExpr(ClassExpr ce){
        return $ex.of(ce);
    }

    public static $ex<ClassExpr> classExpr(Predicate<ClassExpr> ce){
        return $ex.classEx(ce);
    }

    public static $ex<ConditionalExpr> conditionalExpr(){
        return $ex.conditionalEx();
    }

    public static $ex<ConditionalExpr> conditionalExpr(String ce){
        return $ex.conditionalEx(ce);
    }

    public static $ex<ConditionalExpr> conditionalExpr(ConditionalExpr ce){
        return $ex.of(ce);
    }

    public static $ex<ConditionalExpr> conditionalExpr(Predicate<ConditionalExpr> ce){
        return $ex.conditionalEx(ce);
    }

    public static $ex<DoubleLiteralExpr> doubleLiteral(){
        return $ex.doubleLiteralEx();
    }

    public static $ex<DoubleLiteralExpr> doubleLiteral(Predicate<DoubleLiteralExpr> dl){
        return $ex.doubleLiteralEx(dl);
    }

    public static $ex<DoubleLiteralExpr> doubleLiteral(String pattern){
        return $ex.doubleLiteralEx(pattern);
    }

    public static $ex<DoubleLiteralExpr> doubleLiteral(double dbl){
        return $ex.doubleLiteralEx(dbl);
    }

    public static $ex<EnclosedExpr> enclosedExpr(){
        return $ex.enclosedEx();
    }

    public static $ex<EnclosedExpr> enclosedExpr(String ee){
        return $ex.enclosedEx(ee);
    }

    public static $ex<EnclosedExpr> enclosedExpr(EnclosedExpr ee){
        return $ex.of(ee);
    }

    public static $ex<EnclosedExpr> enclosedExpr(Predicate<EnclosedExpr> ee){
        return $ex.enclosedEx(ee);
    }

    public static $ex<FieldAccessExpr> fieldAccessExpr(){
        return $ex.fieldAccessEx();
    }

    public static $ex<FieldAccessExpr> fieldAccessExpr(String fae){
        return $ex.fieldAccessEx(fae);
    }

    public static $ex<FieldAccessExpr> fieldAccessExpr(FieldAccessExpr fae){
        return $ex.of(fae);
    }

    public static $ex<FieldAccessExpr> fieldAccessExpr(Predicate<FieldAccessExpr> fae){
        return $ex.fieldAccessEx(fae);
    }

    public static $ex<InstanceOfExpr> instanceOf(){
        return $ex.instanceOfEx();
    }

    public static $ex<InstanceOfExpr> instanceOf(String io){
        return $ex.instanceOfEx(io);
    }

    public static $ex<InstanceOfExpr> instanceOf(Class instanceOfClass){
        return $ex.instanceOfEx(instanceOfClass);
    }

    public static $ex<InstanceOfExpr> instanceOf(InstanceOfExpr io){
        return $ex.of(io);
    }

    public static $ex<InstanceOfExpr> instanceOf(Predicate<InstanceOfExpr> io){
        return $ex.instanceOfEx(io);
    }

    public static $ex<IntegerLiteralExpr> intLiteral(){
        return $ex.intLiteralEx();
    }

    public static $ex<IntegerLiteralExpr> intLiteral(String pattern){
        return $ex.intLiteralEx(pattern);
    }

    public static $ex<IntegerLiteralExpr> intLiteral(Predicate<IntegerLiteralExpr> il){
        return $ex.intLiteralEx(il);
    }

    public static $ex<IntegerLiteralExpr> intLiteral(int val){
        return $ex.intLiteralEx(val);
    }

    public static $ex<LambdaExpr> lambda(){
        return $ex.lambdaEx();
    }

    public static $ex<LambdaExpr> lambda(String lm){
        return $ex.lambdaEx(lm);
    }

    public static $ex<LambdaExpr> lambda(LambdaExpr le){
        return $ex.of(le);
    }

    public static $ex<LambdaExpr> lambda(Predicate<LambdaExpr> le){
        return $ex.lambdaEx(le);
    }

    public static $ex<LongLiteralExpr> longLiteral(){
        return $ex.longLiteralEx();
    }

    public static $ex<LongLiteralExpr> longLiteral(long lit){
        return $ex.longLiteralEx(lit);
    }

    public static $ex<LongLiteralExpr> longLiteral(Predicate<LongLiteralExpr> lle){
        return $ex.longLiteralEx(lle);
    }

    public static $ex<MethodCallExpr> methodCall(){
        return $ex.methodCallEx();
    }

    public static $ex<MethodCallExpr> methodCall(String mc){
        return $ex.methodCallEx(mc);
    }

    public static $ex<MethodCallExpr> methodCall(MethodCallExpr mce){
        return $ex.of(mce);
    }

    public static $ex<MethodCallExpr> methodCall(Predicate<MethodCallExpr> mce){
        return $ex.methodCallEx(mce);
    }

    public static $ex<MethodReferenceExpr> methodReference(){
        return $ex.methodReferenceEx();
    }

    public static $ex<MethodReferenceExpr> methodReference(String mr){
        return $ex.methodReferenceEx(mr);
    }

    public static $ex<MethodReferenceExpr> methodReference(MethodReferenceExpr mre){
        return $ex.of(mre);
    }

    public static $ex<MethodReferenceExpr> methodReference(Predicate<MethodReferenceExpr> mre){
        return $ex.methodReferenceEx(mre);
    }

    public static $ex<NameExpr> nameExpr(){
        return $ex.nameEx();
    }

    public static $ex<NameExpr> nameExpr(String name){
        return $ex.nameEx(name);
    }

    public static $ex<NameExpr> nameExpr(NameExpr ne){
        return $ex.of(ne);
    }

    public static $ex<NameExpr> nameExpr(Predicate<NameExpr> ne){
        return $ex.nameEx(ne);
    }

    public static $ex<NullLiteralExpr> nullExpr(){
        return $ex.nullEx();
    }

    public static $ex<NullLiteralExpr> nullExpr(NullLiteralExpr nle){
        return $ex.of(nle);
    }

    public static $ex<NullLiteralExpr> nullExpr(Predicate<NullLiteralExpr> nle){
        return $ex.nullEx(nle);
    }

    public static $ex<ObjectCreationExpr> objectCreation(){
        return $ex.objectCreationEx();
    }

    public static $ex<ObjectCreationExpr> objectCreation(String oc){
        return $ex.objectCreationEx(oc);
    }

    public static $ex<ObjectCreationExpr> objectCreation(ObjectCreationExpr oce){
        return $ex.of(oce);
    }

    public static $ex<ObjectCreationExpr> objectCreation(Predicate<ObjectCreationExpr> oce){
        return $ex.objectCreationEx(oce);
    }

    public static $ex<StringLiteralExpr> stringLiteral(){
        return $ex.stringLiteralEx();
    }

    public static $ex<StringLiteralExpr> stringLiteral(String lit){
        return $ex.stringLiteralEx(lit);
    }

    public static $ex<StringLiteralExpr> stringLiteral(StringLiteralExpr sl){
        return $ex.of(sl);
    }

    public static $ex<StringLiteralExpr> stringLiteral(Predicate<StringLiteralExpr> sl){
        return $ex.stringLiteralEx(sl);
    }

    public static $ex<SuperExpr> superExpr(){
        return $ex.superEx();
    }

    public static $ex<SuperExpr> superExpr(String se){
        return $ex.superEx(se);
    }

    public static $ex<SuperExpr> superExpr(SuperExpr se){
        return $ex.superEx(se);
    }

    public static $ex<SuperExpr> superExpr(Predicate<SuperExpr> se){
        return $ex.superEx(se);
    }

    public static $ex<ThisExpr> thisExpr(){
        return $ex.thisEx();
    }

    public static $ex<ThisExpr> thisExpr(String se){
        return $ex.thisEx(se);
    }

    public static $ex<ThisExpr> thisExpr(ThisExpr te){
        return $ex.thisEx(te);
    }

    public static $ex<ThisExpr> thisExpr(Predicate<ThisExpr> te){
        return $ex.thisEx(te);
    }

    public static $ex<UnaryExpr> unary(){
        return $ex.unaryEx();
    }

    public static $ex<UnaryExpr> unary(String ue){
        return $ex.unaryEx(ue);
    }

    public static $ex<UnaryExpr> unary(UnaryExpr ue){
        return $ex.of(ue);
    }

    //a unary with any of these operators
    public static $ex<UnaryExpr> unary(UnaryExpr.Operator... ops){
        return $ex.unaryEx(u-> Arrays.stream(ops).anyMatch(o->u.getOperator() == o));
    }

    public static $ex<UnaryExpr> unary(UnaryExpr.Operator op){
        return $ex.unaryEx(u-> u.getOperator() == op);
    }

    public static $ex<UnaryExpr> unary(Predicate<UnaryExpr> ue){
        return $ex.unaryEx();
    }

    public static $ex<VariableDeclarationExpr> varLocal(){
        return $ex.varLocalEx();
    }

    public static $ex<VariableDeclarationExpr> varLocal(String ve){
        return $ex.varLocalEx(ve);
    }

    public static $ex<VariableDeclarationExpr> varLocal(VariableDeclarationExpr vde){
        return $ex.of(vde);
    }

    public static $ex<VariableDeclarationExpr> varLocal(Predicate<VariableDeclarationExpr> vde){
        return $ex.varLocalEx(vde);
    }

    public static $case switchCase(){
        return $case.of();
    }

    public static $case switchCase(String... sc){
        return $case.of(sc);
    }

    public static $case switchCase( SwitchEntry se){
        return $case.of(se);
    }

    public static $case switchCase(Predicate<SwitchEntry> se){
        return $case.of().$and(se);
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

    public static $catch catchClause(Predicate<CatchClause> cc ){
        return $catch.of().$and(cc);
    }

    /*** STATEMENTS */
    public static $stmt stmt(){
        return $stmt.of();
    }

    public static <S extends Statement>  $stmt<S> stmt(S stmt ){
        return $stmt.of(stmt);
    }

    public static $stmt stmt(String...pattern){
        return $stmt.of(pattern);
    }

    public static $stmt<AssertStmt> assertStmt(){
        return $stmt.assertStmt();
    }

    public static $stmt<AssertStmt> assertStmt(String...as){
        return $stmt.assertStmt(as);
    }

    public static $stmt<AssertStmt> assertStmt( AssertStmt as){
        return $stmt.of(as);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<AssertStmt> assertStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object> $stmt<AssertStmt> assertStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<AssertStmt> assertStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<AssertStmt> assertStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<AssertStmt> assertStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static $stmt<BlockStmt> blockStmt(){
        return $stmt.blockStmt();
    }

    public static $stmt<BlockStmt> blockStmt(String... bs){
        return $stmt.blockStmt(bs);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<BlockStmt> blockStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object> $stmt<BlockStmt> blockStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<BlockStmt> blockStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<BlockStmt> blockStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<BlockStmt> blockStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static $stmt<BlockStmt> blockStmt( BlockStmt bs){
        return $stmt.of(bs);
    }

    public static $stmt<BreakStmt> breakStmt(){
        return $stmt.breakStmt();
    }

    public static $stmt<BreakStmt> breakStmt(String...breakStmt){
        return $stmt.breakStmt(breakStmt);
    }

    public static $stmt<BreakStmt> breakStmt(BreakStmt bs){
        return $stmt.of(bs);
    }

    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt(){
        return $stmt.ctorInvocationStmt();
    }

    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt(String...ctor){
        return $stmt.ctorInvocationStmt(ctor);
    }

    public static $stmt<ExplicitConstructorInvocationStmt> constructorInvocationStmt( ExplicitConstructorInvocationStmt cs){
        return $stmt.of(cs);
    }

    public static $stmt<ContinueStmt> continueStmt(){
        return $stmt.continueStmt();
    }

    public static $stmt<ContinueStmt> continueStmt(String...continueStmt){
        return $stmt.continueStmt(continueStmt);
    }

    public static $stmt<ContinueStmt> continueStmt(ContinueStmt cs){
        return $stmt.of(cs);
    }

    public static $stmt<DoStmt> doStmt(){
        return $stmt.doStmt();
    }

    public static $stmt<DoStmt> doStmt(String...doStmt){
        return $stmt.doStmt(doStmt);
    }


    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<DoStmt> doStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object> $stmt<DoStmt> doStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<DoStmt> doStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<DoStmt> doStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<DoStmt> doStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static $stmt<DoStmt> doStmt(DoStmt ds){
        return $stmt.doStmt(ds);
    }

    public static $stmt<EmptyStmt> emptyStmt(){
        return $stmt.emptyStmt();
    }

    public static $stmt<ExpressionStmt> expressionStmt(){
        return $stmt.expressionStmt();
    }

    public static $stmt<ExpressionStmt> expressionStmt(String ex){
        return $stmt.expressionStmt(ex);
    }

    public static $stmt<ExpressionStmt> expressionStmt( ExpressionStmt es){
        return $stmt.expressionStmt(es);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<ExpressionStmt> expressionStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object> $stmt<ExpressionStmt> expressionStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ExpressionStmt> expressionStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ExpressionStmt> expressionStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ExpressionStmt> expressionStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static $stmt<ForStmt> forStmt(){
        return $stmt.forStmt();
    }

    public static $stmt<ForStmt> forStmt(String...fs){
        return $stmt.forStmt(fs);
    }

    public static $stmt<ForStmt> forStmt(ForStmt fs){
        return $stmt.of(fs);
    }
    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<ForStmt> forStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object> $stmt<ForStmt> forStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ForStmt> forStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForStmt> forStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForStmt> forStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static $stmt<ForEachStmt> forEachStmt(){
        return $stmt.forEachStmt();
    }

    public static $stmt<ForEachStmt> forEachStmt(String forEachStmt){
        return $stmt.forEachStmt(forEachStmt);
    }

    public static $stmt<ForEachStmt> forEachStmt(ForEachStmt fes){
        return $stmt.forEachStmt(fes);
    }

    /**
     * Create a prototype AssertStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<ForEachStmt> forEachStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object> $stmt<ForEachStmt> forEachStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ForEachStmt> forEachStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForEachStmt> forEachStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForEachStmt> forEachStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static $stmt<IfStmt> ifStmt(){
        return $stmt.ifStmt();
    }

    public static $stmt<IfStmt> ifStmt(String is){
        return $stmt.ifStmt( new String[]{is});
    }

    public static $stmt<IfStmt> ifStmt(String...is){
        return $stmt.ifStmt(is);
    }

    /**
     * Create a prototype IfStmt from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<IfStmt> ifStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object> $stmt<IfStmt> ifStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<IfStmt> ifStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<IfStmt> ifStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object> $stmt<IfStmt> ifStmt(Function<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object> $stmt<IfStmt> ifStmt(BiFunction<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<IfStmt> ifStmt(Ex.TriFunction<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<IfStmt> ifStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static $stmt<IfStmt> ifStmt( IfStmt is){
        return $stmt.ifStmt(is);
    }

    public static $stmt<LabeledStmt> labeledStmt(){
        return $stmt.labeledStmt();
    }

    public static $stmt<LabeledStmt> labeledStmt(String ls){
        return $stmt.labeledStmt(ls);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<LabeledStmt> labeledStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object> $stmt<LabeledStmt> labeledStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<LabeledStmt> labeledStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<LabeledStmt> labeledStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<LabeledStmt> labeledStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static $stmt<LabeledStmt> labeledStmt(LabeledStmt ls){
        return $stmt.labeledStmt(ls);
    }

    public static $stmt<LocalClassDeclarationStmt> localClassStmt(){
        return $stmt.localClassStmt();
    }

    public static $stmt<LocalClassDeclarationStmt> localClassStmt(String... str){
        return $stmt.localClassStmt(str);
    }

    public static $stmt<LocalClassDeclarationStmt> localClassStmt( LocalClassDeclarationStmt lcds){
        return $stmt.localClassStmt(lcds);
    }

    /**
     *
     * @return
     */
    public static $stmt<ReturnStmt> returnStmt() {
        return $stmt.returnStmt();
    }

    /**
     * i.e."return VALUE;"
     * @param pattern
     * @return
     */
    public static $stmt<ReturnStmt> returnStmt( String... pattern ) {
        return $stmt.returnStmt(pattern);
    }

    /**
     * i.e."return VALUE;"
     * @param pattern
     * @param constraint
     * @return
     */
    public static $stmt<ReturnStmt> returnStmt( String pattern, Predicate<ReturnStmt> constraint ) {
        return $stmt.returnStmt( pattern, constraint);
    }

    public static $stmt<SwitchStmt> switchStmt(){
        return $stmt.switchStmt();
    }

    public static $stmt<SwitchStmt> switchStmt(String ... ss){
        return $stmt.switchStmt(ss);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<SwitchStmt> switchStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object> $stmt<SwitchStmt> switchStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<SwitchStmt> switchStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SwitchStmt> switchStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SwitchStmt> switchStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static $stmt<SwitchStmt> switchStmt(SwitchStmt ss){
        return $stmt.switchStmt(ss);
    }

    public static $stmt<SynchronizedStmt> synchronizedStmt() { return $stmt.synchronizedStmt(); }

    public static $stmt<SynchronizedStmt> synchronizedStmt(String ... ss){
        return $stmt.synchronizedStmt(ss);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<SynchronizedStmt> synchronizedStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object> $stmt<SynchronizedStmt> synchronizedStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<SynchronizedStmt> synchronizedStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SynchronizedStmt> synchronizedStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SynchronizedStmt> synchronizedStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static $stmt<SynchronizedStmt> synchronizedStmt(SynchronizedStmt ss){
        return ($stmt<SynchronizedStmt>)$stmt.of(ss);
    }

    public static $stmt<ThrowStmt> throwStmt(){
        return $stmt.throwStmt();
    }

    public static $stmt<ThrowStmt> throwStmt(String... ts){
        return $stmt.throwStmt(ts);
    }

    public static $stmt<ThrowStmt> throwStmt(ThrowStmt ts){
        return $stmt.throwStmt(ts);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<ThrowStmt> throwStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object> $stmt<ThrowStmt> throwStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ThrowStmt> throwStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ThrowStmt> throwStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ThrowStmt> throwStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }


    public static $stmt<TryStmt> tryStmt(){
        return $stmt.tryStmt();
    }

    public static $stmt<TryStmt> tryStmt(String...tryStmt){
        return $stmt.tryStmt(tryStmt);
    }

    public static $stmt<TryStmt> tryStmt(TryStmt ts){
        return $stmt.tryStmt(ts);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<TryStmt> tryStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object> $stmt<TryStmt> tryStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<TryStmt> tryStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<TryStmt> tryStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<TryStmt> tryStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }


    public static $stmt<WhileStmt> whileStmt(){
        return $stmt.whileStmt();
    }

    public static $stmt<WhileStmt> whileStmt(String...whileStmt){
        return $stmt.whileStmt(whileStmt);
    }

    public static $stmt<WhileStmt> whileStmt(WhileStmt ws){
        return $stmt.whileStmt(ws);
    }

    /**
     * Create a prototype Labeled from the first assert statement that appears in the
     * Lambda
     * @param ec
     * @return
     */
    public static $stmt<WhileStmt> whileStmt(Ex.Command ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object> $stmt<WhileStmt> whileStmt(Consumer<A> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<WhileStmt> whileStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<WhileStmt> whileStmt(Ex.TriConsumer<A,B,C> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<WhileStmt> whileStmt(Ex.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Ex.lambdaEx(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
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

    public static $var var(){
        return $var.of();
    }

    public static $var var(String var){
        return $var.of(var);
    }

    public static $var var(VariableDeclarator var){
        return $var.of(var);
    }

    public static $var var(Predicate<VariableDeclarator> vd){
        return $var.of().$and(vd);
    }

}
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

    public static $body body(Expr.Command commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Consumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(BiConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Expr.TriConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static $body body(Expr.QuadConsumer commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object> $body body(Function<A,B> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object> $body body(BiFunction<A,B,C> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $body body(Expr.TriFunction<A,B,C,D> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return new $body( le );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object, E extends Object>  $body body(Expr.QuadFunction<A,B,C,D,E> commandLambda ){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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

    public static $expr expr(){
        return $expr.of();
    }

    public static $expr literal(){
        return $expr.of().$and(e-> e.isLiteralExpr());
    }

    public static $expr expr(Predicate<Expression> constraint){
        return $expr.of().$and(constraint);
    }

    public static $expr expr(String... pattern){
        return $expr.of(pattern);
    }

    public static <T extends Expression> $expr<T> of(T protoExpr ){
        return $expr.of(protoExpr);
    }

    public static <T extends Expression> $expr<T> of(T protoExpr, Predicate<T> constraint){
        return $expr.of(protoExpr).$and(constraint);
    }

    public static $expr<StringLiteralExpr> of(String stringLiteral ){
        return $expr.stringLiteral(stringLiteral);
    }

    public static $expr<CharLiteralExpr> of(char c){
        return $expr.of(c);
    }

    public static $expr<IntegerLiteralExpr> of(int i){
        return $expr.of(i);
    }

    /**
     * Note: this can be finniky
     * @param f
     * @return
     */
    public static $expr<DoubleLiteralExpr> of(float f){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _draftException("unable to resolve calling class "+ ste.getClassName());
        }
        $expr.Select s = $expr.of("$$.of($val$)").selectFirstIn(
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
        return $expr.doubleLiteral( s.get("val").toString() );
    }

    public static $expr<DoubleLiteralExpr> of(double d){
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        Class clazz = null;
        try{
            clazz = Class.forName(ste.getClassName() );
        }catch(Exception e){
            throw new _draftException("unable to resolve calling class "+ ste.getClassName());
        }
        $expr.Select s = $expr.of("$$.of($val$)").selectFirstIn(
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
        return $expr.doubleLiteral( s.get("val").toString() );
    }


    public static $expr<LongLiteralExpr> of(long l){
        return $expr.of(l);
    }

    public static $expr<BooleanLiteralExpr> of(boolean b){
        return $expr.of(b);
    }

    /**
     * index Expression
     * @return
     */
    public static $expr<ArrayAccessExpr> arrayAccess(){
        return $expr.arrayAccess();
    }

    public static $expr<ArrayAccessExpr> arrayAccess(String ae){
        return $expr.arrayAccess(ae);
    }

    public static $expr<ArrayAccessExpr> arrayAccess(ArrayAccessExpr aae){
        return $expr.of(aae);
    }

    public static $expr<ArrayAccessExpr> arrayAccess(Predicate<ArrayAccessExpr> aae){
        return $expr.arrayAccess(aae);
    }

    public static $expr<ArrayCreationExpr> arrayCreation(){
        return $expr.arrayCreation();
    }

    public static $expr<ArrayCreationExpr> arrayCreation(String ac){
        return $expr.arrayCreation(ac);
    }

    public static $expr<ArrayCreationExpr> arrayCreation(ArrayCreationExpr ace){
        return $expr.of(ace);
    }

    public static $expr<ArrayCreationExpr> arrayCreation(Predicate<ArrayCreationExpr> ace){
        return $expr.arrayCreation(ace);
    }

    /**
     * new int[][]{{1, 1}, {2, 2}};
     * @return
     */
    public static $expr<ArrayInitializerExpr> arrayInit(){
        return $expr.arrayInitializer();
    }

    public static $expr<ArrayInitializerExpr> arrayInit(String ae){
        return $expr.arrayInitializer(ae);
    }

    public static $expr<ArrayInitializerExpr> arrayInit( ArrayInitializerExpr aei){
        return $expr.of(aei);
    }

    public static $expr<ArrayInitializerExpr> arrayInit(Predicate<ArrayInitializerExpr> aei){
        return $expr.arrayInitializer(aei);
    }

    public static $expr<AssignExpr> assign(){
        return $expr.assign();
    }

    public static $expr<AssignExpr> assign(String a){
        return $expr.assign(a);
    }

    public static $expr<AssignExpr> assign(AssignExpr ae){
        return $expr.of(ae);
    }

    public static $expr<AssignExpr> assign(Predicate<AssignExpr> ae){
        return $expr.assign(ae);
    }

    public static $expr<BinaryExpr> binaryExpr(){
        return $expr.binary();
    }

    public static $expr<BinaryExpr> binaryExpr(String be){
        return $expr.binary(be);
    }

    public static $expr<BinaryExpr> binaryExpr(BinaryExpr be){
        return $expr.of(be);
    }

    public static $expr<BinaryExpr> binaryExpr( BinaryExpr.Operator bo){
        return $expr.binary( be-> be.getOperator() == bo);
    }

    public static $expr<BinaryExpr> binaryExpr( BinaryExpr.Operator... ops){
        return $expr.binary(b-> Arrays.stream(ops).anyMatch(o->b.getOperator() == o));
    }

    public static $expr<BinaryExpr> binaryExpr(Predicate<BinaryExpr> be){
        return $expr.binary(be);
    }

    public static $expr<BooleanLiteralExpr> booleanLiteral(){
        return $expr.booleanLiteral();
    }

    public static $expr<BooleanLiteralExpr> booleanLiteral(boolean bool){
        return $expr.booleanLiteral(bool);
    }

    public static $expr<BooleanLiteralExpr> booleanLiteral(Predicate<BooleanLiteralExpr> bl){
        return $expr.booleanLiteral(bl);
    }

    public static $expr<CastExpr> cast(Class castClazz){
        return $expr.cast("($type$)$expr$").$and(c-> Ast.typesEqual(c.getType(), Ast.typeRef(castClazz) ) );
    }

    public static $expr<CastExpr> cast(){
        return $expr.cast();
    }

    public static $expr<CastExpr> cast(String ce){
        return $expr.cast(ce);
    }

    public static $expr<CastExpr> cast(CastExpr ce){
        return $expr.of(ce);
    }

    public static $expr<CastExpr> cast(Predicate<CastExpr> ce){
        return $expr.cast(ce);
    }

    public static $expr<CharLiteralExpr> charLiteral(){
        return $expr.charLiteral();
    }

    public static $expr<CharLiteralExpr> charLiteral(char c){
        return $expr.charLiteral(c);
    }

    public static $expr<CharLiteralExpr> charLiteral(Predicate<CharLiteralExpr> pc){
        return $expr.charLiteral(pc);
    }

    public static $expr<ClassExpr> classExpr(){
        return $expr.classExpr();
    }

    public static $expr<ClassExpr> classExpr(String ce){
        return $expr.classExpr(ce);
    }

    public static $expr<ClassExpr> classExpr( ClassExpr ce){
        return $expr.of(ce);
    }

    public static $expr<ClassExpr> classExpr(Predicate<ClassExpr> ce){
        return $expr.classExpr(ce);
    }

    public static $expr<ConditionalExpr> conditionalExpr(){
        return $expr.conditional();
    }

    public static $expr<ConditionalExpr> conditionalExpr(String ce){
        return $expr.conditional(ce);
    }

    public static $expr<ConditionalExpr> conditionalExpr(ConditionalExpr ce){
        return $expr.of(ce);
    }

    public static $expr<ConditionalExpr> conditionalExpr(Predicate<ConditionalExpr> ce){
        return $expr.conditional(ce);
    }

    public static $expr<DoubleLiteralExpr> doubleLiteral(){
        return $expr.doubleLiteral();
    }

    public static $expr<DoubleLiteralExpr> doubleLiteral(Predicate<DoubleLiteralExpr> dl){
        return $expr.doubleLiteral(dl);
    }

    public static $expr<DoubleLiteralExpr> doubleLiteral(String pattern){
        return $expr.doubleLiteral(pattern);
    }

    public static $expr<DoubleLiteralExpr> doubleLiteral(double dbl){
        return $expr.doubleLiteral(dbl);
    }

    public static $expr<EnclosedExpr> enclosedExpr(){
        return $expr.enclosedExpr();
    }

    public static $expr<EnclosedExpr> enclosedExpr(String ee){
        return $expr.enclosedExpr(ee);
    }

    public static $expr<EnclosedExpr> enclosedExpr( EnclosedExpr ee){
        return $expr.of(ee);
    }

    public static $expr<EnclosedExpr> enclosedExpr(Predicate<EnclosedExpr> ee){
        return $expr.enclosedExpr(ee);
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(){
        return $expr.fieldAccess();
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(String fae){
        return $expr.fieldAccess(fae);
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(FieldAccessExpr fae){
        return $expr.of(fae);
    }

    public static $expr<FieldAccessExpr> fieldAccessExpr(Predicate<FieldAccessExpr> fae){
        return $expr.fieldAccess(fae);
    }

    public static $expr<InstanceOfExpr> instanceOf(){
        return $expr.instanceOf();
    }

    public static $expr<InstanceOfExpr> instanceOf(String io){
        return $expr.instanceOf(io);
    }

    public static $expr<InstanceOfExpr> instanceOf(Class instanceOfClass){
        return $expr.instanceOf(instanceOfClass);
    }

    public static $expr<InstanceOfExpr> instanceOf(InstanceOfExpr io){
        return $expr.of(io);
    }

    public static $expr<InstanceOfExpr> instanceOf(Predicate<InstanceOfExpr> io){
        return $expr.instanceOf(io);
    }

    public static $expr<IntegerLiteralExpr> intLiteral(){
        return $expr.intLiteral();
    }

    public static $expr<IntegerLiteralExpr> intLiteral(String pattern){
        return $expr.intLiteral(pattern);
    }

    public static $expr<IntegerLiteralExpr> intLiteral(Predicate<IntegerLiteralExpr> il){
        return $expr.intLiteral(il);
    }

    public static $expr<IntegerLiteralExpr> intLiteral(int val){
        return $expr.intLiteral(val);
    }

    public static $expr<LambdaExpr> lambda(){
        return $expr.lambda();
    }

    public static $expr<LambdaExpr> lambda(String lm){
        return $expr.lambda(lm);
    }

    public static $expr<LambdaExpr> lambda( LambdaExpr le){
        return $expr.of(le);
    }

    public static $expr<LambdaExpr> lambda(Predicate<LambdaExpr> le){
        return $expr.lambda(le);
    }

    public static $expr<LongLiteralExpr> longLiteral(){
        return $expr.longLiteral();
    }

    public static $expr<LongLiteralExpr> longLiteral(long lit){
        return $expr.longLiteral(lit);
    }

    public static $expr<LongLiteralExpr> longLiteral(Predicate<LongLiteralExpr> lle){
        return $expr.longLiteral(lle);
    }

    public static $expr<MethodCallExpr> methodCall(){
        return $expr.methodCall();
    }

    public static $expr<MethodCallExpr> methodCall(String mc){
        return $expr.methodCall(mc);
    }

    public static $expr<MethodCallExpr> methodCall(MethodCallExpr mce){
        return $expr.of(mce);
    }

    public static $expr<MethodCallExpr> methodCall(Predicate<MethodCallExpr> mce){
        return $expr.methodCall(mce);
    }

    public static $expr<MethodReferenceExpr> methodReference(){
        return $expr.methodReference();
    }

    public static $expr<MethodReferenceExpr> methodReference(String mr){
        return $expr.methodReference(mr);
    }

    public static $expr<MethodReferenceExpr> methodReference( MethodReferenceExpr mre){
        return $expr.of(mre);
    }

    public static $expr<MethodReferenceExpr> methodReference(Predicate<MethodReferenceExpr> mre){
        return $expr.methodReference(mre);
    }

    public static $expr<NameExpr> nameExpr(){
        return $expr.name();
    }

    public static $expr<NameExpr> nameExpr(String name){
        return $expr.name(name);
    }

    public static $expr<NameExpr> nameExpr( NameExpr ne){
        return $expr.of(ne);
    }

    public static $expr<NameExpr> nameExpr(Predicate<NameExpr> ne){
        return $expr.name(ne);
    }

    public static $expr<NullLiteralExpr> nullExpr(){
        return $expr.nullExpr();
    }

    public static $expr<NullLiteralExpr> nullExpr(NullLiteralExpr nle){
        return $expr.of(nle);
    }

    public static $expr<NullLiteralExpr> nullExpr(Predicate<NullLiteralExpr> nle){
        return $expr.nullExpr(nle);
    }

    public static $expr<ObjectCreationExpr> objectCreation(){
        return $expr.objectCreation();
    }

    public static $expr<ObjectCreationExpr> objectCreation(String oc){
        return $expr.objectCreation(oc);
    }

    public static $expr<ObjectCreationExpr> objectCreation( ObjectCreationExpr oce){
        return $expr.of(oce);
    }

    public static $expr<ObjectCreationExpr> objectCreation(Predicate<ObjectCreationExpr> oce){
        return $expr.objectCreation(oce);
    }

    public static $expr<StringLiteralExpr> stringLiteral(){
        return $expr.stringLiteral();
    }

    public static $expr<StringLiteralExpr> stringLiteral(String lit){
        return $expr.stringLiteral(lit);
    }

    public static $expr<StringLiteralExpr> stringLiteral(StringLiteralExpr sl){
        return $expr.of(sl);
    }

    public static $expr<StringLiteralExpr> stringLiteral(Predicate<StringLiteralExpr> sl){
        return $expr.stringLiteral(sl);
    }

    public static $expr<SuperExpr> superExpr(){
        return $expr.superExpr();
    }

    public static $expr<SuperExpr> superExpr(String se){
        return $expr.superExpr(se);
    }

    public static $expr<SuperExpr> superExpr(SuperExpr se){
        return $expr.superExpr(se);
    }

    public static $expr<SuperExpr> superExpr(Predicate<SuperExpr> se){
        return $expr.superExpr(se);
    }

    public static $expr<ThisExpr> thisExpr(){
        return $expr.thisExpr();
    }

    public static $expr<ThisExpr> thisExpr(String se){
        return $expr.thisExpr(se);
    }

    public static $expr<ThisExpr> thisExpr( ThisExpr te){
        return $expr.thisExpr(te);
    }

    public static $expr<ThisExpr> thisExpr(Predicate<ThisExpr> te){
        return $expr.thisExpr(te);
    }

    public static $expr<UnaryExpr> unary(){
        return $expr.unary();
    }

    public static $expr<UnaryExpr> unary(String ue){
        return $expr.unary(ue);
    }

    public static $expr<UnaryExpr> unary( UnaryExpr ue){
        return $expr.of(ue);
    }

    //a unary with any of these operators
    public static $expr<UnaryExpr> unary( UnaryExpr.Operator... ops){
        return $expr.unary(u-> Arrays.stream(ops).anyMatch( o->u.getOperator() == o));
    }

    public static $expr<UnaryExpr> unary( UnaryExpr.Operator op){
        return $expr.unary(u-> u.getOperator() == op);
    }

    public static $expr<UnaryExpr> unary(Predicate<UnaryExpr> ue){
        return $expr.unary();
    }

    public static $expr<VariableDeclarationExpr> varLocal(){
        return $expr.varLocal();
    }

    public static $expr<VariableDeclarationExpr> varLocal(String ve){
        return $expr.varLocal(ve);
    }

    public static $expr<VariableDeclarationExpr> varLocal(VariableDeclarationExpr vde){
        return $expr.of(vde);
    }

    public static $expr<VariableDeclarationExpr> varLocal(Predicate<VariableDeclarationExpr> vde){
        return $expr.varLocal(vde);
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
    public static $stmt<AssertStmt> assertStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object> $stmt<AssertStmt> assertStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<AssertStmt> assertStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<AssertStmt> assertStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(AssertStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<AssertStmt> assertStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<BlockStmt> blockStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object> $stmt<BlockStmt> blockStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<BlockStmt> blockStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<BlockStmt> blockStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(BlockStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<BlockStmt> blockStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<DoStmt> doStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object> $stmt<DoStmt> doStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<DoStmt> doStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<DoStmt> doStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(DoStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<DoStmt> doStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<ExpressionStmt> expressionStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object> $stmt<ExpressionStmt> expressionStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ExpressionStmt> expressionStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ExpressionStmt> expressionStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ExpressionStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ExpressionStmt> expressionStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<ForStmt> forStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object> $stmt<ForStmt> forStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ForStmt> forStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForStmt> forStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForStmt> forStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<ForEachStmt> forEachStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object> $stmt<ForEachStmt> forEachStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ForEachStmt> forEachStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForEachStmt> forEachStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ForEachStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ForEachStmt> forEachStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<IfStmt> ifStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object> $stmt<IfStmt> ifStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<IfStmt> ifStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<IfStmt> ifStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object> $stmt<IfStmt> ifStmt(Function<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object> $stmt<IfStmt> ifStmt(BiFunction<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<IfStmt> ifStmt(Expr.TriFunction<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(IfStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<IfStmt> ifStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<LabeledStmt> labeledStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object> $stmt<LabeledStmt> labeledStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<LabeledStmt> labeledStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<LabeledStmt> labeledStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(LabeledStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<LabeledStmt> labeledStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<SwitchStmt> switchStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object> $stmt<SwitchStmt> switchStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<SwitchStmt> switchStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SwitchStmt> switchStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SwitchStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SwitchStmt> switchStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<SynchronizedStmt> synchronizedStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object> $stmt<SynchronizedStmt> synchronizedStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<SynchronizedStmt> synchronizedStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SynchronizedStmt> synchronizedStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(SynchronizedStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<SynchronizedStmt> synchronizedStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<ThrowStmt> throwStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object> $stmt<ThrowStmt> throwStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<ThrowStmt> throwStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ThrowStmt> throwStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(ThrowStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<ThrowStmt> throwStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<TryStmt> tryStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object> $stmt<TryStmt> tryStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<TryStmt> tryStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<TryStmt> tryStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(TryStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<TryStmt> tryStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
    public static $stmt<WhileStmt> whileStmt(Expr.Command ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object> $stmt<WhileStmt> whileStmt(Consumer<A> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object>$stmt<WhileStmt> whileStmt(BiConsumer<A,B> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<WhileStmt> whileStmt(Expr.TriConsumer<A,B,C> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
        return $stmt.of( le.findFirst(WhileStmt.class).get() );
    }

    public static <A extends Object, B extends Object, C extends Object, D extends Object> $stmt<WhileStmt> whileStmt(Expr.QuadConsumer<A,B,C,D> ec){
        LambdaExpr le = Expr.lambda(Thread.currentThread().getStackTrace()[2]);
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
